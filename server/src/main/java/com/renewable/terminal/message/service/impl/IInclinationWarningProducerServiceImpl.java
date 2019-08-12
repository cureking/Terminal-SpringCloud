package com.renewable.terminal.message.service.impl;

import com.renewable.terminal.inclination.entity.InclinationWarning;
import com.renewable.terminal.message.service.IInclinationWarningProducerService;
import com.renewable.terminal.terminal.util.JsonUtil;
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
public class IInclinationWarningProducerServiceImpl implements IInclinationWarningProducerService {

	@Autowired
	private AmqpTemplate amqpTemplate;

	// inclinationWarning 相关配置
	private static final String WARNING_TERMINAL2CENTCONTROL_EXCHANGE = "exchange-warning-terminal2centcontrol";
	private static final String WARNING_TERMINAL2CENTCONTROL_QUEUE = "queue-warning-inclination-terminal2centcontrol";

	// 报警模块，由于现在只有倾斜有，故此暂不拆分
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(WARNING_TERMINAL2CENTCONTROL_QUEUE),
			exchange = @Exchange(WARNING_TERMINAL2CENTCONTROL_EXCHANGE)
	))
	@Override
	public void sendInclinationWarningList(String inclinationWarningListStr) {

		log.info("WarningProducer/sendInclinationWarningList has sended: {}", inclinationWarningListStr);

		amqpTemplate.convertAndSend(WARNING_TERMINAL2CENTCONTROL_QUEUE, inclinationWarningListStr);
	}

	@Override
	public void sendInclinationWarningList(List<InclinationWarning> inclinationWarningList) {
		this.sendInclinationWarningList(JsonUtil.obj2String(inclinationWarningList));
	}
}
