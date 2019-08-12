package com.renewable.terminal.message.service;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.vibration.entity.VibrationArea;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface IVibrationProducerService {

	void sendVibrationAreaList(String vibrationAreaListStr);

	void sendVibrationAreaList(List<VibrationArea> vibrationAreaList);

	void sendVibrationDeviceConfigList(String vibrationDeviceConfigListStr);

	void sendVibrationDeviceConfigList(List<VibrationDevConfig> vibrationDeviceConfigList);

}
