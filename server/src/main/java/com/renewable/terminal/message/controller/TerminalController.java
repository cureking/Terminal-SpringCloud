package com.renewable.terminal.message.controller;

import com.renewable.terminal.message.service.ITerminalProducerService;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/message/terminal/")
public class TerminalController {

	@Autowired
	private ITerminalProducerService iTerminalProducerService;

	@PostMapping("upload.do")
	@ResponseBody
	public ServerResponse upload(@RequestBody Terminal terminal){
		iTerminalProducerService.sendTerminal(terminal);
		return ServerResponse.createBySuccess("terminal has sended to mq. terminal",terminal);
	}
}
