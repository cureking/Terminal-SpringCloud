package com.renewable.terminal.audio.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
public class openWinExeUtil {
	public static void openWinExe(String command) throws InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;

		try {
			process = runtime.exec(command);
		} catch (Exception e) {
			log.error("error:{}", e.toString());
		}

		Thread.sleep(8000);
		process.destroy();
	}

	public static void openWinExe(String command, File outDir, Long destroyTime) throws InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;

		try {
			process = runtime.exec(command, null, outDir);
		} catch (Exception e) {
			log.error("error:{}", e.toString());
		}
		if (destroyTime == null){
			return;
		}
		Thread.sleep(destroyTime);
		process.destroy();
	}
}
