package com.renewable.terminal.message.service;

import com.renewable.terminal.inclination.entity.InclinationWarning;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface IInclinationWarningProducerService {

	// 由于现在就只有倾斜有报警，故不作报警服务的拆分
	void sendInclinationWarningList(String inclinationWarningListStr);

	void sendInclinationWarningList(List<InclinationWarning> inclinationWarningList);
}
