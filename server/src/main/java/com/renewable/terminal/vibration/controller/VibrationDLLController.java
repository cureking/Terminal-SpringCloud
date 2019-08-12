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

	/**
	 * 根据数据库中的配置列表，执行所有读取
	 * 注意，该方法的执行间隔不可小于配置中设置的读取长度所需时间
	 * @return
	 */
	@GetMapping("read_ad_continue.do")
	@ResponseBody
	public ServerResponse readAdContinueData() {
		return iVibrationDLLService.readAdContinueData();
	}
}
