package com.renewable.terminal.terminal.controller;


import com.renewable.terminal.message.client.TerminalMessageClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import com.renewable.terminal.terminal.service.ITerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
@RestController
@RequestMapping("/terminal")
public class TerminalController {

	@Autowired
	private ITerminalService iTerminalService;

	@GetMapping("get_terminal.do")
	@ResponseBody
	public ServerResponse getTerminal(){
		return iTerminalService.getTerminal();
	}

	@PostMapping("update.do")
	@ResponseBody
	public ServerResponse updateTerminal(@RequestBody Terminal terminal){
		boolean result = iTerminalService.updateById(terminal);
		// 简单刷新一下，从而完成数据的上传
		iTerminalService.refresh();
		if (!result){
			return ServerResponse.createByErrorMessage("fail !");
		}
		return ServerResponse.createBySuccess(terminal);
	}
	@PostMapping("update_from_center.do")
	@ResponseBody
	public ServerResponse updateTerminalFromCenter(@RequestBody Terminal terminal){
		boolean result = iTerminalService.updateById(terminal);
		if (!result){
			return ServerResponse.createByErrorMessage("fail !");
		}
		return ServerResponse.createBySuccessMessage("success");
	}

	@GetMapping("refresh.do")
	@ResponseBody
	public ServerResponse refreshTerminal(){
		return iTerminalService.refresh();
	}

}
