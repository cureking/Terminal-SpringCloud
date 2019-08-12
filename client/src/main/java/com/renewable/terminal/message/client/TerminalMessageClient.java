package com.renewable.terminal.message.client;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description：
 * @Author: jarry
 */
@FeignClient(name = "message", fallback = TerminalMessageClient.TerminalMessageClientFallback.class)
public interface TerminalMessageClient {

	@PostMapping("/message/terminal/upload.do")
	ServerResponse uploadTerminal(@RequestBody Terminal terminal);

	@Component
	class TerminalMessageClientFallback implements TerminalMessageClient{

		@Override
		public ServerResponse uploadTerminal(Terminal terminal) {
			return ServerResponse.createByErrorMessage("Busy service about Message/TerminalMessageClient/uploadTerminal().");
		}
	}
}
