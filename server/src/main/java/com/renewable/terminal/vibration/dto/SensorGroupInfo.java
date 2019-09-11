package com.renewable.terminal.vibration.dto;

import lombok.Data;

/**
 * @Description：
 * @Author: jarry
 */
@Data
public class SensorGroupInfo {

	Integer groupId;
	Integer sensorType;
	Double installMode;
	Double installAngle;
	String terminalId;
}
