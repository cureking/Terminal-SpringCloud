package com.renewable.terminal.audio.controller;

import com.renewable.terminal.audio.service.IAudioService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/audio/")
public class AudioController {

	@Autowired
	private IAudioService iAudioService;

	@GetMapping("listener_task.do")
	public ServerResponse audioListenerTask(){
		return iAudioService.startupAudioSensorListTasks();
	}
}
