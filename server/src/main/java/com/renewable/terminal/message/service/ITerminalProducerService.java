package com.renewable.terminal.message.service;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;

/**
 * @Description：
 * @Author: jarry
 */
public interface ITerminalProducerService {

	void sendTerminal(String terminalStr);

	void sendTerminal(Terminal terminal);
}
