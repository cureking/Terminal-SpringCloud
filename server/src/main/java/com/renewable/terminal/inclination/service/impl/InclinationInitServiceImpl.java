package com.renewable.terminal.inclination.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.renewable.terminal.inclination.annotation.InclinationInitWarning;
import com.renewable.terminal.inclination.common.RedisTemplateUtil;
import com.renewable.terminal.inclination.constant.InclinationConfigConstant;
import com.renewable.terminal.inclination.dao.InclinationInitMapper;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationOrigin;
import com.renewable.terminal.inclination.service.IInclinationInitService;
import com.renewable.terminal.inclination.util.CheckDataUtil;
import com.renewable.terminal.message.client.InclinationMessageClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.util.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.renewable.terminal.inclination.constant.InclinationInitConstant.ORIGIN_CLEAN_INIT;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
@Service
public class InclinationInitServiceImpl extends ServiceImpl<InclinationInitMapper, InclinationInit> implements IInclinationInitService {

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
		Wrapper<InclinationInit> inclinationInitDurationWrapper = new QueryWrapper<InclinationInit>().between("create_time", startTime, endTime);
		Integer countTotal = this.count(inclinationInitDurationWrapper);
		if (countTotal == null) {
			return ServerResponse.createByErrorMessage("countTotal is null !");
		}

		int countpace = countTotal / countRequest;

		// 2.x 获取在指定日期内的第一个数据
		Wrapper<InclinationInit> inclinationInitWrapper = new QueryWrapper<InclinationInit>().between("create_time", startTime, endTime).last("LIMIT 1");
		InclinationInit inclinationInitStart = this.getOne(inclinationInitWrapper);
		if (inclinationInitStart == null) {
			return ServerResponse.createByErrorMessage("inclinationInitStart is null With the startTime: " + startTime + "and endTime:" + endTime + " !");
		}

		// 2.y 获得指定日期内第一个数据的ID
		long idStart = inclinationInitStart.getId();
		List<Long> idList = Lists.newArrayList();

		// 2.z 组装所求的ID集合
		for (long i = 0; i < countRequest; i++) {
			idList.add(idStart + countpace * i);
		}

		// 3.请求指定ID集合的数据
		List<InclinationInit> inclinationInitList = (List<InclinationInit>) this.listByIds(idList);
		if (inclinationInitList == null || CollectionUtils.isEmpty(inclinationInitList)) {
			return ServerResponse.createByErrorMessage("inclinationInitList is null or inclinationOriginList's size is zero !");
		}

		// 4.返回成功响应
		return ServerResponse.createBySuccess(inclinationInitList);
	}

	@Override
	public ServerResponse originDataSubmit(InclinationOrigin inclinationOrigin) {
		if (inclinationOrigin == null || inclinationOrigin.getAngleInitTotal() == null) {
			return ServerResponse.createByErrorMessage("inclinationOrigin or inclinationOrigin.getAngleInitTotal is null !");
		}
		String inclinationOriginPreStr = redisTemplateUtil.get(ORIGIN_CLEAN_INIT);

		if (!StringUtils.isEmpty(inclinationOriginPreStr)) {
			InclinationOrigin inclinationOriginPre = JsonUtil.string2Obj(inclinationOriginPreStr, InclinationOrigin.class);

			// 比较规则
			inclinationOrigin = (inclinationOriginPre.getAngleInitTotal() > inclinationOrigin.getAngleInitTotal()) ? inclinationOriginPre : inclinationOrigin;
		}

		String inclinationOriginStr = JsonUtil.obj2String(inclinationOrigin);
		redisTemplateUtil.set(ORIGIN_CLEAN_INIT, inclinationOriginStr);
		return ServerResponse.createBySuccess();
	}

	@Override
	@InclinationInitWarning
	public ServerResponse cleanDataPersistence() {
		String inclinationOriginPreStr = redisTemplateUtil.get(ORIGIN_CLEAN_INIT);
		// 获取缓存数据后，需要清空缓存，避免影响下一轮
		redisTemplateUtil.delete(ORIGIN_CLEAN_INIT);
		// redisTemplateUtil.getAndSet(ORIGIN_CLEAN_INIT,null);

		InclinationOrigin inclinationOriginPre = JsonUtil.string2Obj(inclinationOriginPreStr, InclinationOrigin.class);
		if (inclinationOriginPre == null) {
			return ServerResponse.createByErrorMessage("inclinationOriginPre is null !");
		}

		ServerResponse inclinationInitResponse = this.inclinationOrigin2InclinationInitPersistence(inclinationOriginPre);
		if (inclinationInitResponse.isFail()) {
			return inclinationInitResponse;
		}

		InclinationInit inclinationInit = (InclinationInit) inclinationInitResponse.getData();

		// 3. 保存数据库
		boolean result = this.save(inclinationInit);
		if (!result) {
			return ServerResponse.createByErrorMessage("inclinationInit save fail !");
		}
		// 4.传送至中控室
		List<InclinationInit> inclinationInitList = Arrays.asList(inclinationInit);
		inclinationMessageClient.uploadInitList(inclinationInitList);


		return ServerResponse.createBySuccess("inclinationInit save success.", inclinationInit);
	}

	private ServerResponse<InclinationInit> inclinationOrigin2InclinationInitPersistence(InclinationOrigin inclinationOrigin) {
		// 1.数据校验
		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationOrigin, "id");
		if (checkResponse.isFail()) {
			return checkResponse;
		}

		InclinationInit inclinationInit = new InclinationInit();
		BeanUtils.copyProperties(inclinationOrigin, inclinationInit);

		inclinationInit.setId(null);
		inclinationInit.setOriginId(inclinationOrigin.getId());
		inclinationInit.setStatus(InclinationConfigConstant.InclinationStateEnum.Cleaned.getCode());

		return ServerResponse.createBySuccess(inclinationInit);
	}

	@Override
	public ServerResponse<List<InclinationInit>> getLastByCount(Integer address, Integer count) {
		Wrapper<InclinationInit> inclinationInitWrapper = new QueryWrapper<InclinationInit>().eq("inclination_id", address).orderByDesc("id").last("limit " + count.toString());
		List<InclinationInit> inclinationInitList = this.list(inclinationInitWrapper);
		if (CollectionUtils.isEmpty(inclinationInitList)) {
			return ServerResponse.createByError();
		}
		return ServerResponse.createBySuccess(inclinationInitList);
	}
}
