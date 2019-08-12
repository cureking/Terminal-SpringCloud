package com.renewable.terminal.message.service;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.inclination.entity.InclinationWarning;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface IInclinationProducerService {

	void sendInclinationTotalList(String inclinationTotalListStr);

	void sendInclinationTotalList(List<InclinationTotal> inclinationTotalList);

	void sendInclinationInitList(String inclinationTotalListStr);

	void sendInclinationInitList(List<InclinationInit> inclinationInitList);

	void sendInclinationConfigList(String inclinationTotalListStr);

	void sendInclinationConfigList(List<InclinationConfig> inclinationConfigList);

}
