package com.renewable.terminal.message.service.impl;

import com.renewable.terminal.message.service.IVibrationProducerService;
import com.renewable.terminal.terminal.util.JsonUtil;
import com.renewable.terminal.vibration.entity.VibrationArea;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;
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
@Service
@Slf4j
public class IVibrationProducerServiceImpl implements IVibrationProducerService {

	@Autowired
	private AmqpTemplate amqpTemplate;

	// 从声音模块开始，简化消息机制（不需要使用复杂的路由规则）
	// 发送传感器数据，全部采用List
	// vibrationArea 相关配置
	private static final String VIBRATION_AREA_EXCHANGE = "exchange-vibration-area-data";
	private static final String VIBRATION_AREA_QUEUE = "queue-vibration-area-data";
	// vibrationDeviceConfig 相关配置
	private static final String CONFIG_VIBRATION_DEVICE_TERMINAL2CENTCONTROL_EXCHANGE = "exchange-config-vibration-device-terminal2centcontrol";
	private static final String CONFIG_VIBRATION_DEVICE_TERMINAL2CENTCONTROL_QUEUE = "queue-config-vibration-device-terminal2centcontrol";

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(VIBRATION_AREA_QUEUE),
			exchange = @Exchange(VIBRATION_AREA_EXCHANGE)
	))
	@Override
	public void sendVibrationAreaList(String vibrationAreaListStr) {
		log.info("VibrationProducerService/sendVibrationAreaList has sended: {}", vibrationAreaListStr);

		amqpTemplate.convertAndSend(VIBRATION_AREA_QUEUE, vibrationAreaListStr);
	}

	@Override
	public void sendVibrationAreaList(List<VibrationArea> vibrationAreaList) {
		this.sendVibrationAreaList(JsonUtil.obj2String(vibrationAreaList));
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(CONFIG_VIBRATION_DEVICE_TERMINAL2CENTCONTROL_QUEUE),
			exchange = @Exchange(CONFIG_VIBRATION_DEVICE_TERMINAL2CENTCONTROL_EXCHANGE)
	))
	@Override
	public void sendVibrationDeviceConfigList(String vibrationDeviceConfigListStr) {
		log.info("VibrationProducerService/sendVibrationDeviceConfigList has sended: {}", vibrationDeviceConfigListStr);

		amqpTemplate.convertAndSend(CONFIG_VIBRATION_DEVICE_TERMINAL2CENTCONTROL_QUEUE, vibrationDeviceConfigListStr);
	}

	@Override
	public void sendVibrationDeviceConfigList(List<VibrationDevConfig> vibrationDeviceConfigList) {
		this.sendVibrationDeviceConfigList(JsonUtil.obj2String(vibrationDeviceConfigList));
	}
}
