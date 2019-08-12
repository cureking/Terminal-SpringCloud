package com.renewable.terminal.message.service.impl;

import com.renewable.terminal.message.service.ITerminalProducerService;
import com.renewable.terminal.terminal.entity.Terminal;
import com.renewable.terminal.terminal.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Service
public class ITerminalProducerServiceImpl implements ITerminalProducerService {

	@Autowired
	private AmqpTemplate amqpTemplate;

	private static final String TERMINAL_CONFIG_TERMINAL2CENTCONTROL_EXCHANGE = "exchange-terminal-config-terminal2centcontrol";
	private static final String TERMINAL_CONFIG_TERMINAL2CENTCONTROL_QUEUE = "queue-terminal-config-terminal2centcontrol";


	/**
	 * 发送终端信息至中控室（日后如果需要，可以在这里设置多个消息队列，甚至多个消息队列源，乃至多个消息队列类型)
	 * @param terminalStr
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = TERMINAL_CONFIG_TERMINAL2CENTCONTROL_QUEUE),
			exchange = @Exchange(value = TERMINAL_CONFIG_TERMINAL2CENTCONTROL_EXCHANGE)
	))
	@Override
	public void sendTerminal(String terminalStr){

		log.info("TerminalProducer/sendTerminal has sended: {}", terminalStr);

		amqpTemplate.convertAndSend(TERMINAL_CONFIG_TERMINAL2CENTCONTROL_QUEUE, terminalStr);
	}

	@Override
	public void sendTerminal(Terminal terminal) {
		this.sendTerminal(JsonUtil.obj2String(terminal));
	}
}
