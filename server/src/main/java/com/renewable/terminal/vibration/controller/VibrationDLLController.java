package com.renewable.terminal.vibration.controller;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.service.IVibrationDLLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/dll/")
public class VibrationDLLController {

	@Autowired
	private IVibrationDLLService iVibrationDLLService;

	@GetMapping("test.do")
	@ResponseBody
	public ServerResponse test() {
		return iVibrationDLLService.readAdContinueData();
	}
}
