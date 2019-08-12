package com.renewable.terminal.schedule.task;

import com.renewable.terminal.audio.client.AudioClient;
import com.renewable.terminal.inclination.client.InclinationClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskTimer {

	@Autowired
	private InclinationClient inclinationClient;

	@Autowired
	private AudioClient audioClient;

	// Inclination Part:

	/**
	 * 用于实现轮询获取传感器监测数据
	 */
	@Async
	@Scheduled(cron = "*/2 * * * * *")  // 每五秒钟
	public void requireSerialData() {
		log.info("require origin data task start");

		ServerResponse response = inclinationClient.readAll();
		log.info("Status:{},Msg:{}.", response.getStatus(), response.getMsg());

		log.info("require origin data task end");
	}

	/**
	 * 用于实现轮询获取清洗InclinationInit数据
	 */
	@Async
	@Scheduled(cron = "0 */1 * * * *")	// 每一分钟
	public void cleanData2InitPersistence() {
		log.info("clean origin to inclinationInit task start");

		ServerResponse response = inclinationClient.cleanData2InitPersistence();
		log.info("Status:{},Msg:{}.", response.getStatus(), response.getMsg());

		log.info("clean origin to inclinationInit task end");

	}

	/**
	 * 用于实现轮询获取清洗InclinationITotal数据
	 */
	@Async
	@Scheduled(cron = "0 */1 * * * *")	// 每一分钟
	public void cleanData2TotalPersistence() {
		log.info("clean origin to inclinationTotal task start");

		ServerResponse response = inclinationClient.cleanData2TotalPersistence();
		log.info("Status:{},Msg:{}.", response.getStatus(), response.getMsg());

		log.info("clean origin to inclinationTotal task end");

	}


	// Audio Part:
	/**
	 * 用于实现轮询获取清洗InclinationITotal数据
	 */
	@Async
	@Scheduled(cron = "0 */5 * * * *")	// 每五分钟
	public void audioListenerTask() {
		log.info("audioListenerTask task start");

		ServerResponse response = audioClient.audioListenerTask();
		log.info("Status:{},Msg:{}.", response.getStatus(), response.getMsg());

		log.info("audioListenerTask task end");

	}
}