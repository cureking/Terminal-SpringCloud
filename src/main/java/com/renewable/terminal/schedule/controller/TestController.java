package com.renewable.terminal.schedule.controller;

import com.renewable.terminal.audio.client.AudioClient;
import com.renewable.terminal.inclination.client.InclinationClient;
import com.renewable.terminal.terminal.common.ServerResponse;
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
@RequestMapping("/test/")
public class TestController {

	@Autowired
	private InclinationClient inclinationClient;

	@Autowired
	private AudioClient audioClient;

	@GetMapping("test.do")
	@ResponseBody
	public ServerResponse test(){
		return inclinationClient.readAll();
	}

	@GetMapping("test2.do")
	@ResponseBody
	public ServerResponse test2(){
		return audioClient.audioListenerTask();
	}

}
