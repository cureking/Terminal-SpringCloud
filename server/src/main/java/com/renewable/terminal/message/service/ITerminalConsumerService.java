package com.renewable.terminal.message.service;

import com.rabbitmq.client.Channel;
import org.springframework.messaging.handler.annotation.Headers;

import java.io.IOException;
import java.util.Map;

/**
 * @Description：
 * @Author: jarry
 */
public interface ITerminalConsumerService {

	void messageOnTerminal(String terminalStr, @Headers Map<String, Object> headers, Channel channel) throws IOException;

}
