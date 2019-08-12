package com.renewable.terminal.audio.service;

import com.renewable.terminal.terminal.common.ServerResponse;

/**
 * @Description：
 * @Author: jarry
 */
public interface IAudioService {
	/**
	 * 启动声音传感器定时任务组
	 *
	 * @return
	 */
	ServerResponse startupAudioSensorListTasks();
}
