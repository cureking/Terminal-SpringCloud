package com.renewable.terminal.inclination.controller;

import com.renewable.terminal.inclination.service.ISerialCommandSendService;
import com.renewable.terminal.terminal.client.TerminalClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/test/terminal/")
@RefreshScope
public class TestController {

	@Autowired
	private TerminalClient terminalClient;

	@Autowired
	private ISerialCommandSendService iSerialCommandSendService;

	@Value("${inclination.duration}")
	private String duration;

	@GetMapping("refresh.do")
	@ResponseBody
	public ServerResponse terminalRefresh() {
		return terminalClient.refreshTerminal();
	}

	@PostMapping("update.do")
	@ResponseBody
	public ServerResponse terminalUpdate(@RequestBody Terminal terminal) {
		return terminalClient.updateTerminal(terminal);
	}

	@GetMapping("msg.do")
	@ResponseBody
	public ServerResponse msg() {
		return ServerResponse.createBySuccessMessage("msg test pass:" + duration);
	}

	/**
	 * just for test
	 *
	 * @param inclinationConfigId
	 * @return
	 */
	@GetMapping("serial.do")
	@ResponseBody
	public ServerResponse serial(@RequestParam("inclinationConfigId") Integer inclinationConfigId) {
		return iSerialCommandSendService.readAllWithAddress(inclinationConfigId);
	}

	@GetMapping("read_all.do")
	@ResponseBody
	public ServerResponse readAll() {
		return iSerialCommandSendService.readAll();
	}


}
