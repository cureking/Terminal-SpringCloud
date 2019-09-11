package com.renewable.terminal.inclination.extend.converter;

import gnu.io.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description：
 * @Author: jarry
 */
@Component
public class Origin2CommandStrategyContext {

	/**
	 * 使用一个Map存储所有实现了Command2Origin接口的实现
	 * key存储beanName，value存储实现了Command2Origin接口的bean
	 */
	private final String ORIGIN2COMMAND_BEANNAME_PREFIX = "Origin2Command%s";
	private final Map<String, Origin2Command> origin2CommandStrategyMap = new ConcurrentHashMap<>();

	@Autowired
	public Origin2CommandStrategyContext(Map<String, Origin2Command> origin2CommandStrategyMap) {
		this.origin2CommandStrategyMap.clear();
		origin2CommandStrategyMap.forEach((k, v) -> this.origin2CommandStrategyMap.put(k, v));
	}

	/**
	 * @param inclinationType 接收到的是类型参数，如526T
	 * @return
	 */
	public void origin2Object(String inclinationType, SerialPort serialPort, byte[] originBuffer) {
		// 数据校验
		// 组装bean名
		String command2OriginBeanName = String.format(ORIGIN2COMMAND_BEANNAME_PREFIX, inclinationType);

		// 利用策略模式，返回对应的bean
		if (!origin2CommandStrategyMap.containsKey(command2OriginBeanName)){
//			log.warn
			return;
		}
		origin2CommandStrategyMap.get(command2OriginBeanName).origin2Object(serialPort, originBuffer);
	}

}
