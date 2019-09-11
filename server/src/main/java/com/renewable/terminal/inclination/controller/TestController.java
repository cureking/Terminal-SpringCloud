package com.renewable.terminal.inclination.controller;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.service.IInclinationConfigService;
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

	@Autowired
	private IInclinationConfigService iInclinationConfigService;

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

	@PostMapping("config.do")
	@ResponseBody
	public ServerResponse config(@RequestBody InclinationConfig inclinationConfig) {
		System.out.println(inclinationConfig);
		return iInclinationConfigService.calXAndY(inclinationConfig);
	}

	@PostMapping("config_s.do")
	@ResponseBody
	public ServerResponse configS(@RequestBody InclinationConfig inclinationConfig) {
		return iInclinationConfigService.calInitWithMatlab(inclinationConfig);
	}

	@GetMapping("index.do")
	@ResponseBody
	public String index() {
		return "index";
	}

}
