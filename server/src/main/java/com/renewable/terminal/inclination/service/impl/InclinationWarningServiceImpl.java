package com.renewable.terminal.inclination.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.renewable.terminal.inclination.dao.InclinationWarningMapper;
import com.renewable.terminal.inclination.entity.InclinationWarning;
import com.renewable.terminal.inclination.service.IInclinationWarningService;
import com.renewable.terminal.message.client.InclinationWarningMessageClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-07-25
 */
@Service
public class InclinationWarningServiceImpl extends ServiceImpl<InclinationWarningMapper, InclinationWarning> implements IInclinationWarningService {

	@Autowired
	private InclinationWarningMessageClient inclinationWarningMessageClient;

	@Override
	public ServerResponse saveAndUpload(InclinationWarning inclinationWarning) {
		if (inclinationWarning == null){
			return ServerResponse.createByErrorMessage("inclinationWarning is null !");
		}

		boolean result = this.save(inclinationWarning);
		if (!result){
			return ServerResponse.createByErrorMessage("inclinationWarning save fail !");
		}

		// 4.上传中控室
		List<InclinationWarning> inclinationWarningList = Arrays.asList(inclinationWarning);
		inclinationWarningMessageClient.uploadInclinationWarningList(inclinationWarningList);

		return ServerResponse.createBySuccessMessage("inclinationWarning has sended to mq.");
	}

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
		Wrapper<InclinationWarning> inclinationWarningWrapper = new QueryWrapper<InclinationWarning>().between("create_time", startTime, endTime).last("LIMIT 1");
		InclinationWarning inclinationWarningStart = this.getOne(inclinationWarningWrapper);
		if (inclinationWarningStart == null) {
			return ServerResponse.createByErrorMessage("inclinationWarningStart is null With the startTime: " + startTime + "and endTime:" + endTime + " !");
		}

		// 2.y 获得指定日期内第一个数据的ID
		long idStart = inclinationWarningStart.getId();
		List<Long> idList = Lists.newArrayList();

		// 2.z 组装所求的ID集合
		for (long i = 0; i < countRequest; i++) {
			idList.add(idStart + countpace * i);
		}

		// 3.请求指定ID集合的数据
		List<InclinationWarning> inclinationWarningList = (List<InclinationWarning>) this.listByIds(idList);
		if (inclinationWarningList == null || CollectionUtils.isEmpty(inclinationWarningList)) {
			return ServerResponse.createByErrorMessage("inclinationWarningList is null or inclinationWarningList's size is zero !");
		}

		// 4.返回成功响应
		return ServerResponse.createBySuccess(inclinationWarningList);
	}

}
