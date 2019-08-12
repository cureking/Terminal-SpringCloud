package com.renewable.terminal.audio.extend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.File;

import static com.renewable.terminal.audio.constant.AudioConstant.AUDIO_FILE_DURATION;
import static com.renewable.terminal.audio.constant.AudioConstant.AUDIO_ORIGIN_FILE_NAME;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
@Component
public class AudioRecorder {
	private static final long serialVersionUID = 1L;

	private static AudioFormat audioFormat;
	private static TargetDataLine targetDataLine;

	Thread audioThread = new AudioRecorder.CaptureThread();

	private static class InnerClass {
		private static AudioRecorder staticInnerClassSingleton = new AudioRecorder();
	}

	public static AudioRecorder getInstatce() {
		return InnerClass.staticInnerClassSingleton;
	}

	@Bean
	public static AudioRecorder AudioRecorder() {
		return new AudioRecorder();
	}

	public void startCapture() {
		this.captureAudio();
	}

	public void stopCapture() throws InterruptedException {
		if (targetDataLine == null) {
			return;
		}
		targetDataLine.stop();
		// 也许用了fulsh会好一些
		targetDataLine.flush();
		targetDataLine.close();
	}

	/**
	 * 开始声音抓取（新建了一个DataLine）
	 */
	private void captureAudio() {
		try {
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

			// 新建CaptureThread()，这里是多线程的任务（也许1分钟时长应该设置在这里）
			audioThread = new AudioRecorder.CaptureThread();
			audioThread.start();

		} catch (Exception e) {
			e.printStackTrace();
			// 0表示正常退出当前虚拟机（当然服务器是不可以这样的，关了虚拟机，服务器还咋办。。。）
			// System.exit(0);
			log.error(e.toString());
		}
	}

	/**
	 * 对过去声音的格式进行设置（设定频率，比特位等）
	 *
	 * @return
	 */
	private AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 2;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	/**
	 * 新开一个线程，用于抓取声音数据，并在其中设置了数据保存的格式与路径
	 */
	class CaptureThread extends Thread {
		@Override
		public void run() {
			AudioFileFormat.Type fileType = null;
			File audioFile = null;

			// 这里我们先简单一些。首先由于是本地文件操作，故不建立文件服务器，其次，这里建立简单目录
			fileType = AudioFileFormat.Type.WAVE;

			// 以小时为每分钟文件的标志
			String fileName = String.valueOf(System.currentTimeMillis() / AUDIO_FILE_DURATION);
//			String fileName = AUDIO_ORIGIN_FILE_NAME;
			String fileExtension = ".wav";

//			audioFile = new File("junk.wav");
			String path = "audio";
			File fileDir = new File(path);
			if (!fileDir.exists()) {
				fileDir.setWritable(true);
				fileDir.mkdirs();
			}
			audioFile = new File(path, fileName + fileExtension);

			try {
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}


	public static void main(String args[]) throws InterruptedException {
		AudioRecorder audioRecorder2 = new AudioRecorder();
		audioRecorder2.startCapture();
		Thread.sleep(5000);
		audioRecorder2.stopCapture();
		Thread.sleep(5000);
		audioRecorder2.startCapture();
		Thread.sleep(5000);
		audioRecorder2.stopCapture();

//		openWinExe();


	}// end main

	public static void openWinExe() throws InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;

		try {
//			String command = "audio\\tool\\wsrun.exe";
			String command = "C:\\Program Files (x86)\\Netease\\CloudMusic\\cloudmusic.exe";
			process = runtime.exec(command);
		} catch (Exception e) {
			log.error("error:{}", e.toString());
		}

		Thread.sleep(8000);
		process.destroy();
	}
}
