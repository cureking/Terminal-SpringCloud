package com.renewable.terminal.vibration.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.dao.VibrationAreaMapper;
import com.renewable.terminal.vibration.entity.VibrationArea;
import com.renewable.terminal.vibration.service.IVibrationAreaService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-08-09
 */
@Service
public class VibrationAreaServiceImpl extends ServiceImpl<VibrationAreaMapper, VibrationArea> implements IVibrationAreaService {

	@Override
	public ServerResponse listByTimeWithCount(Integer devId, Integer passagewayCode, Integer countRequest, String startTime, String endTime) {
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
		Wrapper<VibrationArea> vibrationAreaWrapper = new QueryWrapper<VibrationArea>().between("create_time", startTime, endTime).last("LIMIT 1");
		VibrationArea vibrationAreaStart = this.getOne(vibrationAreaWrapper);
		if (vibrationAreaStart == null) {
			return ServerResponse.createByErrorMessage("vibrationAreaStart is null With the startTime: " + startTime + "and endTime:" + endTime + " !");
		}

		// 2.y 获得指定日期内第一个数据的ID
		long idStart = vibrationAreaStart.getId();
		List<Long> idList = Lists.newArrayList();

		// 2.z 组装所求的ID集合
		for (long i = 0; i < countRequest; i++) {
			idList.add(idStart + countpace * i);
		}

		// 3.请求指定ID集合的数据
		List<VibrationArea> vibrationAreaList = (List<VibrationArea>) this.listByIds(idList);
		if (CollectionUtils.isEmpty(vibrationAreaList)) {
			return ServerResponse.createByErrorMessage("vibrationAreaList is null or vibrationAreaList's size is zero !");
		}

		// 4.返回成功响应
		return ServerResponse.createBySuccess(vibrationAreaList);
	}
}
