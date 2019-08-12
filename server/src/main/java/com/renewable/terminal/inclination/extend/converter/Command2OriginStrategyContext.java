package com.renewable.terminal.inclination.extend.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description：
 * @Author: jarry
 */
@Component
@Slf4j
public class Command2OriginStrategyContext {

	/**
	 * 使用一个Map存储所有实现了Command2Origin接口的实现
	 * key存储beanName，value存储实现了Command2Origin接口的bean
	 */
	private final String COMMAND2ORIGIN_BEANNAME_PREFIX = "Command2Origin%s";
	private final Map<String, Command2Origin> command2OriginStrategyMap = new ConcurrentHashMap<>();

	@Autowired
	public Command2OriginStrategyContext(Map<String, Command2Origin> command2OriginStrategyMap) {
		this.command2OriginStrategyMap.clear();
		command2OriginStrategyMap.forEach((k, v) -> this.command2OriginStrategyMap.put(k, v));
	}

	/**
	 * @param inclinationType 接收到的是类型参数，如526T
	 * @return
	 */
	public Command2Origin getCommand2Origin(String inclinationType) {
		// 组装bean名
		String command2OriginBeanName = String.format(COMMAND2ORIGIN_BEANNAME_PREFIX, inclinationType);
		// 利用策略模式，返回对应的bean（好吧，这已经不叫策略模式了。不过后续会改变的）
		return command2OriginStrategyMap.get(command2OriginBeanName);
	}

	public byte[] command2ReadAllOrigin(String inclinationType, int address) {
		// 组装bean名
		String command2OriginBeanName = String.format(COMMAND2ORIGIN_BEANNAME_PREFIX, inclinationType);
		// 利用策略模式，调用对应方法
		return command2OriginStrategyMap.get(command2OriginBeanName).command2ReadAllOrigin(address);
	}

	public byte[] command2SetZeroOrigin(String inclinationType, int address, int type) {
		// 组装bean名
		String command2OriginBeanName = String.format(COMMAND2ORIGIN_BEANNAME_PREFIX, inclinationType);
		// 利用策略模式，调用对应方法
		return command2OriginStrategyMap.get(command2OriginBeanName).command2SetZeroOrigin(address, type);
	}

	public byte[] command2SetSpeedOrigin(String inclinationType, int address, int level) {
		// 组装bean名
		String command2OriginBeanName = String.format(COMMAND2ORIGIN_BEANNAME_PREFIX, inclinationType);
		// 利用策略模式，调用对应方法
		return command2OriginStrategyMap.get(command2OriginBeanName).command2SetSpeedOrigin(address, level);
	}

	public byte[] command2SetModelOrigin(String inclinationType, int address, int type) {
		// 组装bean名
		String command2OriginBeanName = String.format(COMMAND2ORIGIN_BEANNAME_PREFIX, inclinationType);
		// 利用策略模式，调用对应方法
		return command2OriginStrategyMap.get(command2OriginBeanName).command2SetModelOrigin(address, type);
	}

	public byte[] command2SetAddressOrigin(String inclinationType, int address, int targetAddress) {
		// 组装bean名
		String command2OriginBeanName = String.format(COMMAND2ORIGIN_BEANNAME_PREFIX, inclinationType);
		// 利用策略模式，调用对应方法
		return command2OriginStrategyMap.get(command2OriginBeanName).command2SetAddressOrigin(address, targetAddress);
	}

	public byte[] command2GetZeroOrigin(String inclinationType, int address) {
		// 组装bean名
		String command2OriginBeanName = String.format(COMMAND2ORIGIN_BEANNAME_PREFIX, inclinationType);
		// 利用策略模式，调用对应方法
		return command2OriginStrategyMap.get(command2OriginBeanName).command2GetZeroOrigin(address);
	}

	public byte[] command2ResponseOrigin(String inclinationType, int address) {
		// 组装bean名
		String command2OriginBeanName = String.format(COMMAND2ORIGIN_BEANNAME_PREFIX, inclinationType);
		// 利用策略模式，调用对应方法
		return command2OriginStrategyMap.get(command2OriginBeanName).command2ResponseOrigin(address);
	}
}
