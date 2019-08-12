package com.renewable.terminal.message.service.impl;

import com.rabbitmq.client.Channel;
import com.renewable.terminal.message.common.RedisTemplateUtil;
import com.renewable.terminal.message.service.IVibrationConsumerService;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.util.JsonUtil;
import com.renewable.terminal.vibration.client.VibrationClient;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
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
public class IVibrationConsumerServiceImpl implements IVibrationConsumerService {

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	@Autowired
	private VibrationClient vibrationClient;

	// InclinationConfig 相关配置
	private static final String CONFIG_VIBRATION_DEVICE_CENTCONTROL2TERMINAL_EXCHANGE = "exchange-config-vibration-device-centcontrol2terminal";
	private static final String CONFIG_VIBRATION_DEVICE_CENTCONTROL2TERMINAL_QUEUE = "queue-config-vibration-device-centcontrol2terminal";

	@RabbitListener(bindings = @QueueBinding(     // 要设置到底监听哪个QUEUE    还可以进行EXCHANGE,QUEUE,BINGDING
			value = @Queue(value = CONFIG_VIBRATION_DEVICE_CENTCONTROL2TERMINAL_QUEUE),
			exchange = @Exchange(value = CONFIG_VIBRATION_DEVICE_CENTCONTROL2TERMINAL_EXCHANGE)
	)
	)
	@Override
	public void messageOnVibrationDevConfig(String vibrationDevConfigStr, Map<String, Object> headers, Channel channel) throws IOException {

		log.info("VibrationConsumer/messageOnVibrationDevConfig has received: {}", vibrationDevConfigStr);

		// 1.接收数据，并反序列化出对象
		VibrationDevConfig vibrationDevConfig = JsonUtil.string2Obj(vibrationDevConfigStr, VibrationDevConfig.class);

		if (vibrationDevConfig == null || vibrationDevConfig.getTerminalId() == null) {
			log.warn("VibrationConsumer/messageOnVibrationDevConfig has ack inclinationConfig. inclinationConfig:{}.",vibrationDevConfig);
			Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
			channel.basicAck(deliveryTag, false);
			return;
		}

		if (!redisTemplateUtil.get(GLOBAL_TERMINAL_ID).equals(vibrationDevConfig.getTerminalId())) {
			log.info("refuse target inclinationConfig with terminalId({}).current_terminalId({})", vibrationDevConfig.getTerminalId(), redisTemplateUtil.get(GLOBAL_TERMINAL_ID));
			return;
		}

		ServerResponse response = vibrationClient.updateDevConfigFromCenter(vibrationDevConfig);

		if (response.isSuccess()) {
			//由于配置中写的是手动签收，所以这里需要通过Headers来进行签收
			log.info("the vibrationDevConfig from centcontrol has consumed . the vibrationDevConfig is {}", vibrationDevConfig.toString());
			Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
			channel.basicAck(deliveryTag, false);
		}
	}
}
