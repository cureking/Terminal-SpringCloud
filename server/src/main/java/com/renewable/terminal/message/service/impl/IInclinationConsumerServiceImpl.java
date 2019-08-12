package com.renewable.terminal.message.service.impl;

import com.rabbitmq.client.Channel;
import com.renewable.terminal.inclination.client.InclinationClient;
import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.message.common.RedisTemplateUtil;
import com.renewable.terminal.message.service.IInclinationConsumerService;
import com.renewable.terminal.terminal.common.ServerResponse;
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
@Slf4j
@Service
public class IInclinationConsumerServiceImpl implements IInclinationConsumerService {

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	@Autowired
	private InclinationClient inclinationClient;


	//IP USERNAME PASSWORD等都是自动注入的

	//目前业务规模还很小，没必要设置太复杂的命名规则与路由规则。不过，可以先保留topic的路由策略，便于日后扩展。
	// InclinationConfig 相关配置
	private static final String CONFIG_INCLINATION_CENTCONTROL2TERMINAL_EXCHANGE = "exchange-config-inclination-centcontrol2terminal";
	private static final String CONFIG_INCLINATION_CENTCONTROL2TERMINAL_QUEUE = "queue-config-inclination-centcontrol2terminal";

	@RabbitListener(bindings = @QueueBinding(     // 要设置到底监听哪个QUEUE    还可以进行EXCHANGE,QUEUE,BINGDING
			value = @Queue(value = CONFIG_INCLINATION_CENTCONTROL2TERMINAL_QUEUE),
			exchange = @Exchange(value = CONFIG_INCLINATION_CENTCONTROL2TERMINAL_EXCHANGE)
	)
	)
	@Override
	public void messageOnInclinationConfig(String inclinationConfigStr,
												   @Headers Map<String, Object> headers, Channel channel) throws IOException {

		log.info("InclinationConsumer/messageOnInclinationConfig has received: {}", inclinationConfigStr);

		// 1.接收数据，并反序列化出对象
		InclinationConfig inclinationConfig = JsonUtil.string2Obj(inclinationConfigStr, InclinationConfig.class);

		if (inclinationConfig == null || inclinationConfig.getTerminalId() == null) {
			log.warn("InclinationConsumer/messageOnInclinationConfig has ack inclinationConfig. inclinationConfig:{}.",inclinationConfig);
			Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
			channel.basicAck(deliveryTag, false);
			return;
		}

		if (!redisTemplateUtil.get(GLOBAL_TERMINAL_ID).equals(inclinationConfig.getTerminalId())) {
			log.info("refuse target inclinationConfig with terminalId({}).current_terminalId({})", inclinationConfig.getTerminalId(), redisTemplateUtil.get(GLOBAL_TERMINAL_ID));
			return;
		}

		ServerResponse response = inclinationClient.updateConfigFromCenter(inclinationConfig);

		if (response.isSuccess()) {
			//由于配置中写的是手动签收，所以这里需要通过Headers来进行签收
			log.info("the inclinationConfig from centcontrol has consumed . the inclinationConfig is {}", inclinationConfig.toString());
			Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
			channel.basicAck(deliveryTag, false);
		}
	}
}
