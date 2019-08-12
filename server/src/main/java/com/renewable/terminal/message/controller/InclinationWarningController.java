package com.renewable.terminal.message.controller;

import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.inclination.entity.InclinationWarning;
import com.renewable.terminal.message.service.IInclinationProducerService;
import com.renewable.terminal.message.service.IInclinationWarningProducerService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/message/inclination_warning/")
public class InclinationWarningController {

	@Autowired
	private IInclinationWarningProducerService iInclinationWarningProducerService;



	@PostMapping("upload_warning_list.do")
	@ResponseBody
	public ServerResponse uploadInclinationWarningList(@RequestBody List<InclinationWarning> inclinationWarningList){
		iInclinationWarningProducerService.sendInclinationWarningList(inclinationWarningList);
		return ServerResponse.createBySuccess("inclinationWarningList has sended to mq. inclinationWarningList:",inclinationWarningList);
	}

	@PostMapping("upload_warning_list_str.do")
	@ResponseBody
	public ServerResponse uploadInclinationWarningListStr(String inclinationWarningListStr){
		iInclinationWarningProducerService.sendInclinationWarningList(inclinationWarningListStr);
		return ServerResponse.createBySuccess("inclinationWarningList has sended to mq. inclinationWarningListStr:",inclinationWarningListStr);
	}
}
