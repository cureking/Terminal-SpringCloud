package com.renewable.terminal.message.controller;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.message.service.IInclinationProducerService;
import com.renewable.terminal.message.service.IVibrationProducerService;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.entity.VibrationArea;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/message/vibration/")
public class VibrationController {

	@Autowired
	private IVibrationProducerService iVibrationProducerService;

	@PostMapping("upload_area_list.do")
	@ResponseBody
	public ServerResponse uploadAreaList(@RequestBody List<VibrationArea> vibrationAreaList){
		iVibrationProducerService.sendVibrationAreaList(vibrationAreaList);
		return ServerResponse.createBySuccess("vibrationAreaList has sended to mq. vibrationAreaList:",vibrationAreaList);
	}

	@PostMapping("upload_area_list_str.do")
	@ResponseBody
	public ServerResponse uploadAreaListStr(String vibrationAreaListStr){
		iVibrationProducerService.sendVibrationAreaList(vibrationAreaListStr);
		return ServerResponse.createBySuccess("vibrationAreaList has sended to mq. vibrationAreaListStr:",vibrationAreaListStr);
	}

	@PostMapping("upload_config_list.do")
	@ResponseBody
	public ServerResponse uploadConfigList(@RequestBody List<VibrationDevConfig> vibrationDevConfigList){
		iVibrationProducerService.sendVibrationDeviceConfigList(vibrationDevConfigList);
		return ServerResponse.createBySuccess("vibrationDevConfigList has sended to mq. vibrationDevConfigList:",vibrationDevConfigList);
	}

	@PostMapping("upload_config_list_str.do")
	@ResponseBody
	public ServerResponse uploadConfigListStr(String vibrationDevConfigListStr){
		iVibrationProducerService.sendVibrationDeviceConfigList(vibrationDevConfigListStr);
		return ServerResponse.createBySuccess("vibrationDevConfigList has sended to mq. vibrationDevConfigListStr:",vibrationDevConfigListStr);
	}
}
