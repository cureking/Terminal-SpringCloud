package com.renewable.terminal.inclination.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.renewable.terminal.inclination.annotation.InclinationTotalWarning;
import com.renewable.terminal.inclination.common.RedisTemplateUtil;
import com.renewable.terminal.inclination.constant.InclinationConfigConstant;
import com.renewable.terminal.inclination.dao.InclinationTotalMapper;
import com.renewable.terminal.inclination.entity.InclinationOrigin;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.inclination.service.IInclinationTotalService;
import com.renewable.terminal.inclination.util.CheckDataUtil;
import com.renewable.terminal.message.client.InclinationMessageClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

import static com.renewable.terminal.inclination.constant.InclinationTotalConstant.ORIGIN_CLEAN_TOTAL;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
@Service
public class InclinationTotalServiceImpl extends ServiceImpl<InclinationTotalMapper, InclinationTotal> implements IInclinationTotalService {

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	@Autowired
	private InclinationMessageClient inclinationMessageClient;

	@Override
	public ServerResponse listByTimeWithCount(Integer countRequest, String startTime, String endTime) {

		// 1.数据校验
		if (countRequest == null) {
			return ServerResponse.createByErrorMessage("the countRequest is null !");
		}

		// 2.获得总数，并计算ID步长
		Integer countTotal = this.count();
		if (countTotal == null) {
			return ServerResponse.createByErrorMessage("countTotal is null !");
		}

		int countpace = countTotal / countRequest;

		// 2.x 获取在指定日期内的第一个数据
		Wrapper<InclinationTotal> inclinationTotalWrapper = new QueryWrapper<InclinationTotal>().between("create_time", startTime, endTime).last("LIMIT 1");
		InclinationTotal inclinationTotalStart = this.getOne(inclinationTotalWrapper);
		if (inclinationTotalStart == null) {
			return ServerResponse.createByErrorMessage("inclinationTotalStart is null With the startTime: " + startTime + "and endTime:" + endTime + " !");
		}

		// 2.y 获得指定日期内第一个数据的ID
		long idStart = inclinationTotalStart.getId();
		List<Long> idList = Lists.newArrayList();

		// 2.z 组装所求的ID集合
		for (long i = 0; i < countRequest; i++) {
			idList.add(idStart + countpace * i);
		}

		// 3.请求指定ID集合的数据
		List<InclinationTotal> inclinationTotalList = (List<InclinationTotal>) this.listByIds(idList);
		if (inclinationTotalList == null || CollectionUtils.isEmpty(inclinationTotalList)) {
			return ServerResponse.createByErrorMessage("inclinationTotalList is null or inclinationTotalList's size is zero !");
		}

		// 4.返回成功响应
		return ServerResponse.createBySuccess(inclinationTotalList);
	}

	@Override
	public ServerResponse originDataSubmit(InclinationOrigin inclinationOrigin) {
//		ORIGIN_CLEAN_TOTAL
		if (inclinationOrigin == null || inclinationOrigin.getAngleTotal() == null) {
			return ServerResponse.createByErrorMessage("inclinationOrigin or inclinationOrigin.getAngleTotal is null !");
		}
		String inclinationOriginPreStr = redisTemplateUtil.get(ORIGIN_CLEAN_TOTAL);

		if (!StringUtils.isEmpty(inclinationOriginPreStr)) {
			InclinationOrigin inclinationOriginPre = JsonUtil.string2Obj(inclinationOriginPreStr, InclinationOrigin.class);

			// 比较规则
			inclinationOrigin = (inclinationOriginPre.getAngleTotal() > inclinationOrigin.getAngleTotal()) ? inclinationOriginPre : inclinationOrigin;
		}

		String inclinationOriginStr = JsonUtil.obj2String(inclinationOrigin);
		redisTemplateUtil.set(ORIGIN_CLEAN_TOTAL, inclinationOriginStr);
		return ServerResponse.createBySuccess();
	}

	@Override
	@InclinationTotalWarning
	public ServerResponse cleanDataPersistence() {
		String inclinationOriginPreStr = redisTemplateUtil.get(ORIGIN_CLEAN_TOTAL);
		// 获取缓存数据后，需要清空缓存，避免影响下一轮
		redisTemplateUtil.delete(ORIGIN_CLEAN_TOTAL);
		InclinationOrigin inclinationOriginPre = JsonUtil.string2Obj(inclinationOriginPreStr, InclinationOrigin.class);
		if (inclinationOriginPre == null) {
			return ServerResponse.createByErrorMessage("inclinationOriginPre is null !");
		}

		ServerResponse inclinationTotalResponse = this.inclinationOrigin2InclinationTotalPersistence(inclinationOriginPre);
		if (inclinationTotalResponse.isFail()) {
			return inclinationTotalResponse;
		}

		InclinationTotal inclinationTotal = (InclinationTotal) inclinationTotalResponse.getData();

		// 3.持久化到数据库
		boolean result = this.save(inclinationTotal);
		if (!result) {
			return ServerResponse.createByErrorMessage("inclinationTotal save fail !");
		}
		// 4.传送至中控室
		List<InclinationTotal> inclinationTotalList = Arrays.asList(inclinationTotal);
		inclinationMessageClient.uploadTotalList(inclinationTotalList);

		return ServerResponse.createBySuccess("inclinationTotal save success.", inclinationTotal);
	}

	private ServerResponse<InclinationTotal> inclinationOrigin2InclinationTotalPersistence(InclinationOrigin inclinationOrigin) {
		// 1.数据校验
		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationOrigin, "id");
		if (checkResponse.isFail()) {
			return checkResponse;
		}

		InclinationTotal inclinationTotal = new InclinationTotal();
		BeanUtils.copyProperties(inclinationOrigin, inclinationTotal);

		inclinationTotal.setId(null);
		inclinationTotal.setOriginId(inclinationOrigin.getId());
		inclinationTotal.setStatus(InclinationConfigConstant.InclinationStateEnum.Cleaned.getCode());

		return ServerResponse.createBySuccess(inclinationTotal);
	}

	@Override
	public ServerResponse<List<InclinationTotal>> getLastByCount(Integer address, Integer count){
		Wrapper<InclinationTotal> inclinationTotalWrapper = new QueryWrapper<InclinationTotal>().eq("inclination_id", address).orderByDesc("id").last("limit "+count.toString());
		List<InclinationTotal> inclinationTotalList = this.list(inclinationTotalWrapper);
		if (CollectionUtils.isEmpty(inclinationTotalList)){
			return ServerResponse.createByError();
		}
		return ServerResponse.createBySuccess(inclinationTotalList);
	}
}
