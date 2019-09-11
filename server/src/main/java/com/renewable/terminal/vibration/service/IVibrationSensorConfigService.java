package com.renewable.terminal.vibration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renewable.terminal.vibration.dto.SensorGroupInfo;
import com.renewable.terminal.vibration.entity.VibrationSensorConfig;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jarry
 * @since 2019-09-10
 */
public interface IVibrationSensorConfigService extends IService<VibrationSensorConfig> {

	/**
	 * 获取分组id数据，目前只有一个采集卡，故不传参数（采集卡id）
	 * @return
	 */
	int[] listGroupId();

	/**
	 * 获取分组配置
	 * @return Map<passageCode,Integer[groupId,groupNumber]>
	 */
	Map<Integer, Integer[]> mapGroupInfo();

	/**
	 * 通过group获取分组配置信息	后续需要建立分组实体类
	 * @param groupId
	 * @return Integer sensorType, Integer sampling, Double mode, Double theta
	 */
	SensorGroupInfo mapGroupInfoByGroupId(int groupId);
}
