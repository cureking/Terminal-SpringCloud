package com.renewable.terminal.message.service.impl;

import com.rabbitmq.client.Channel;
import com.renewable.terminal.message.common.RedisTemplateUtil;
import com.renewable.terminal.message.service.ITerminalConsumerService;
import com.renewable.terminal.terminal.client.TerminalClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import com.renewable.terminal.terminal.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.renewable.terminal.terminal.common.TerminalConstant.GLOBAL_TERMINAL_ID;

/**
 * @Description：
 * @Author: jarry
 */
@Service
@Slf4j
public class ITerminalConsumerServiceImpl implements ITerminalConsumerService {

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;
	@Autowired
	private TerminalClient terminalClient;


	private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE = "exchange-terminal-config-centcontrol2terminal";
	private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_QUEUE = "queue-terminal-config-centcontrol2terminal";

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = TERMINAL_CONFIG_CENTCONTROL2TERMINAL_QUEUE),
			exchange = @Exchange(value = TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE)
	))
	@Override
	public void messageOnTerminal(String terminalStr,
								  @Headers Map<String, Object> headers, Channel channel) throws IOException {

		log.info("TerminalConsumer/messageOnTerminal has received: {}", terminalStr);

		Terminal terminal = JsonUtil.string2Obj(terminalStr, Terminal.class);

		if (terminal == null){
			log.warn("TerminalConsumer/messageOnTerminal has ack terminal. terminal:{}.",terminal);
			Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
			channel.basicAck(deliveryTag, false);
			return;
		}

		if (!redisTemplateUtil.get(GLOBAL_TERMINAL_ID).equals(terminal.getId())){
			log.info("refuse target terminal with id({}) and mac({}) configure to this terminal with id({}).", terminal.getId(), terminal.getMac(), redisTemplateUtil.get(GLOBAL_TERMINAL_ID));
			return;
		}

		// 2.业务逻辑
		ServerResponse response = terminalClient.updateTerminalFromCenter(terminal);

		// 3.确认
		if (response.isSuccess()) {
			// 4.日志记录
			log.info("the terminal from centcontrol has consumed . the terminal is {}", terminal.toString());
			Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
			channel.basicAck(deliveryTag, false);

		}

		// 4.终端服务失败，那么只能将消息返回队列
		if (response.isSuccess()) {
			// 4.日志记录
			log.info("the terminal from centcontrol has consumed . the terminal is {}", terminal.toString());
			Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
			channel.basicNack(deliveryTag, false, true);
		}
	}
}
