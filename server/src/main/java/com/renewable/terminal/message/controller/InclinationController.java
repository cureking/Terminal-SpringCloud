package com.renewable.terminal.message.controller;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.message.service.IInclinationProducerService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/message/inclination/")
public class InclinationController {

	@Autowired
	private IInclinationProducerService iInclinationProducerService;

	@PostMapping("upload_init_list.do")
	@ResponseBody
	public ServerResponse uploadInitList(@RequestBody List<InclinationInit> inclinationInitList){
		iInclinationProducerService.sendInclinationInitList(inclinationInitList);
		return ServerResponse.createBySuccess("inclinationInitList has sended to mq. inclinationInitList:",inclinationInitList);
	}

	@PostMapping("upload_init_list_str.do")
	@ResponseBody
	public ServerResponse uploadInitListStr(String inclinationInitListStr){
		iInclinationProducerService.sendInclinationInitList(inclinationInitListStr);
		return ServerResponse.createBySuccess("inclinationInitList has sended to mq. inclinationInitListStr:",inclinationInitListStr);
	}

	@PostMapping("upload_total_list.do")
	@ResponseBody
	public ServerResponse uploadTotalList(@RequestBody List<InclinationTotal> inclinationTotalList){
		iInclinationProducerService.sendInclinationTotalList(inclinationTotalList);
		return ServerResponse.createBySuccess("inclinationTotalList has sended to mq. inclinationTotalList:",inclinationTotalList);
	}

	@PostMapping("upload_total_list_str.do")
	@ResponseBody
	public ServerResponse uploadTotalListStr(String inclinationTotalListStr){
		iInclinationProducerService.sendInclinationTotalList(inclinationTotalListStr);
		return ServerResponse.createBySuccess("inclinationTotalList has sended to mq. inclinationTotalListStr:",inclinationTotalListStr);
	}

	@PostMapping("upload_config_list.do")
	@ResponseBody
	public ServerResponse uploadConfigList(@RequestBody List<InclinationConfig> inclinationConfigList){
		iInclinationProducerService.sendInclinationConfigList(inclinationConfigList);
		return ServerResponse.createBySuccess("inclinationConfigList has sended to mq. inclinationConfigList:",inclinationConfigList);
	}

	@PostMapping("upload_config_list_str.do")
	@ResponseBody
	public ServerResponse uploadConfigListStr(String inclinationConfigListStr){
		iInclinationProducerService.sendInclinationConfigList(inclinationConfigListStr);
		return ServerResponse.createBySuccess("inclinationConfigList has sended to mq. inclinationConfigListStr:",inclinationConfigListStr);
	}
}
