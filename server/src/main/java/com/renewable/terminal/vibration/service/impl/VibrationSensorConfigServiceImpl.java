package com.renewable.terminal.vibration.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renewable.terminal.vibration.dao.VibrationSensorConfigMapper;
import com.renewable.terminal.vibration.dto.SensorGroupInfo;
import com.renewable.terminal.vibration.entity.VibrationOrigin;
import com.renewable.terminal.vibration.entity.VibrationSensorConfig;
import com.renewable.terminal.vibration.service.IVibrationSensorConfigService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.renewable.terminal.vibration.constant.DLLConstant.DEVIDE_GROUP_ARRAY_SIZE;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-09-10
 */
@Service
public class VibrationSensorConfigServiceImpl extends ServiceImpl<VibrationSensorConfigMapper, VibrationSensorConfig> implements IVibrationSensorConfigService {

	@Override
	public int[] listGroupId() {

		Wrapper<VibrationSensorConfig> vibrationSensorConfigWrapper = new QueryWrapper<VibrationSensorConfig>();
		List<VibrationSensorConfig> vibrationSensorConfigList = this.list(vibrationSensorConfigWrapper);

		int[] groupIdArray = ArrayUtils.EMPTY_INT_ARRAY;
		for (VibrationSensorConfig vibrationSensorConfig : vibrationSensorConfigList) {
			int groupId = vibrationSensorConfig.getGroupId();
			if (!ArrayUtils.contains(groupIdArray,groupId)){
				groupIdArray = ArrayUtils.add(groupIdArray,groupId);
			}
		}

		return groupIdArray;
	}

	@Override
	public Map<Integer, Integer[]> mapGroupInfo() {
		Wrapper<VibrationSensorConfig> vibrationSensorConfigWrapper = new QueryWrapper<VibrationSensorConfig>()
//				.select("group_id", "group_number", "passageway_code")
				;
		List<VibrationSensorConfig> vibrationSensorConfigList = this.list(vibrationSensorConfigWrapper);

		Map<Integer, Integer[]> groupInfoMap = new HashMap<>();
		for (VibrationSensorConfig vibrationSensorConfig : vibrationSensorConfigList) {
			Integer[] groupMapValue = new Integer[]{vibrationSensorConfig.getGroupId(), vibrationSensorConfig.getGroupNumber()};
			groupInfoMap.put(vibrationSensorConfig.getPassagewayCode(), groupMapValue);
		}

		return groupInfoMap;
	}

	@Override
	public SensorGroupInfo mapGroupInfoByGroupId(int groupId) {

		Wrapper<VibrationSensorConfig> vibrationSensorConfigWrapper = new QueryWrapper<VibrationSensorConfig>()
//				.select("group_id", "sensor_type", "install_mode", "install_angle")
				.eq("group_id", groupId).last("limit 1");
		VibrationSensorConfig vibrationSensorConfig = this.getOne(vibrationSensorConfigWrapper);
		SensorGroupInfo sensorGroupInfo = new SensorGroupInfo();
		BeanUtils.copyProperties(vibrationSensorConfig, sensorGroupInfo);

		return sensorGroupInfo;
	}
}
