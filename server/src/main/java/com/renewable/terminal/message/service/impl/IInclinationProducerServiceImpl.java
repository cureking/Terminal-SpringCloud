package com.renewable.terminal.message.service.impl;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.inclination.entity.InclinationWarning;
import com.renewable.terminal.message.service.IInclinationProducerService;
import com.renewable.terminal.message.service.ITerminalProducerService;
import com.renewable.terminal.terminal.util.JsonUtil;
import jdk.nashorn.internal.ir.Terminal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Service
public class IInclinationProducerServiceImpl implements IInclinationProducerService {

	@Autowired
	private AmqpTemplate amqpTemplate;

	// 从声音模块开始，简化消息机制（不需要使用复杂的路由规则）
	// 发送传感器数据，全部采用List
	// inclinationTotal 相关配置
	private static final String INCLINATION_TOTAL_EXCHANGE = "exchange-inclination-total-data";
	private static final String INCLINATION_TOTAL_QUEUE = "queue-inclination-total-data";
	// inclinationInit 相关配置
	private static final String INCLINATION_INIT_EXCHANGE = "exchange-inclination-init-data";
	private static final String INCLINATION_INIT_QUEUE = "queue-inclination-init-data";
	// inclinationConfig 相关配置
	private static final String CONFIG_INCLINATION_TERMINAL2CENTCONTROL_EXCHANGE = "exchange-config-inclination-terminal2centcontrol";
	private static final String CONFIG_INCLINATION_TERMINAL2CENTCONTROL_QUEUE = "queue-config-inclination-terminal2centcontrol";


	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(INCLINATION_TOTAL_QUEUE),
			exchange = @Exchange(INCLINATION_TOTAL_EXCHANGE)
	))
	@Override
	public void sendInclinationTotalList(String inclinationTotalListStr) {

		log.info("InclinationProducer/sendInclinationTotalList has sended: {}", inclinationTotalListStr);

		amqpTemplate.convertAndSend(INCLINATION_TOTAL_QUEUE, inclinationTotalListStr);
	}

	@Override
	public void sendInclinationTotalList(List<InclinationTotal> inclinationTotalList) {
		this.sendInclinationTotalList(JsonUtil.obj2String(inclinationTotalList));
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(INCLINATION_INIT_QUEUE),
			exchange = @Exchange(INCLINATION_INIT_EXCHANGE)
	))
	@Override
	public void sendInclinationInitList(String inclinationTotalListStr) {

		log.info("InclinationProducer/sendInclinationInitList has sended: {}", inclinationTotalListStr);

		amqpTemplate.convertAndSend(INCLINATION_INIT_QUEUE, inclinationTotalListStr);
	}

	@Override
	public void sendInclinationInitList(List<InclinationInit> inclinationInitList) {
		this.sendInclinationInitList(JsonUtil.obj2String(inclinationInitList));
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(CONFIG_INCLINATION_TERMINAL2CENTCONTROL_QUEUE),
			exchange = @Exchange(CONFIG_INCLINATION_TERMINAL2CENTCONTROL_EXCHANGE)
	))
	@Override
	public void sendInclinationConfigList(String inclinationConfigListStr) {

		log.info("InitializationProducer/sendInclinationConfigList has sended: {}", inclinationConfigListStr);

		amqpTemplate.convertAndSend(CONFIG_INCLINATION_TERMINAL2CENTCONTROL_QUEUE, inclinationConfigListStr);
	}

	@Override
	public void sendInclinationConfigList(List<InclinationConfig> inclinationConfigList) {
		this.sendInclinationConfigList(JsonUtil.obj2String(inclinationConfigList));
	}

}
