package com.renewable.terminal.inclination.extend;

import com.renewable.terminal.inclination.exception.serial.*;
import com.renewable.terminal.inclination.util.SerialPortUtil;
import com.renewable.terminal.terminal.common.ServerResponse;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 想了想，还是先写一个简单的吧。如果之后有必要再池化，毕竟串口这种半双工的单通道通信池化还是有些麻烦的
 * 做，还是不做，这是个问题
 * 先只考虑正常运作模式，之后再考虑调试等情况
 *
 * @Description：
 * @Author: jarry
 */
@Component
@Slf4j
public class SerialSender {

	@Autowired
	private SerialListener serialListener;

//	@Autowired
//	private SerialPool serialPool;

	// 资源仓库，减少重复对象的生成与销毁所带来的资源消耗
	private static Map<String, SerialPort> serialPortConcurrentHashMap = new ConcurrentHashMap<>();

	private static ArrayList<String> portList = SerialPortUtil.findPort();

	// 建立线程池	// TODO 不知道这里直接new，可不可以注入（感觉有点模糊了）
	private static SerialPool serialPool = new SerialPool(10, 100);

	public ServerResponse sendData(String portName, int baudrate, byte[] originData) {
		// 1.判断对应的SerialPort对象，在serialPortConcurrentHashMap中是否存在
		String serialPortName = portName + "_" + baudrate;

		// 2.如果不存在，直接创建对应SerialPort对象（单独建立方法，并且需要添加SerialListener），并置入serialPortConcurrentHashMap中
		if (!serialPortConcurrentHashMap.containsKey(serialPortName)) {
			ServerResponse serialPortGeneratorResponse = this.serialPortGenerator(portName, baudrate);
			if (serialPortGeneratorResponse.isFail()) {
				return serialPortGeneratorResponse;
			}
			SerialPort serialPort = (SerialPort) serialPortGeneratorResponse.getData();
			serialPortConcurrentHashMap.put(serialPortName, serialPort);
			// 增加监听
			ServerResponse addListenerResponse = this.addSerialListener(serialPort);
			if (addListenerResponse.isFail()) {
				return addListenerResponse;
			}
		}

		// 3.如果存在（走到这个位置，肯定存在了），直接调用task生成方法生成对应task，并置入serialPool中
		SerialPort serialPort = serialPortConcurrentHashMap.get(serialPortName);
		Runnable runnableTask = new runnableTaskGenerator(serialPort, originData);
		serialPool.submit(runnableTask);

		// 4.返回成功响应
		return ServerResponse.createBySuccess();
	}

	private ServerResponse<SerialPort> serialPortGenerator(String portName, int baudrate) {

		SerialPort serialPort = null;

		try {
			serialPort = SerialPortUtil.openPort(portName, baudrate);
		} catch (SerialPortParameterFailure serialPortParameterFailure) {
			return ServerResponse.createByErrorMessage(serialPortParameterFailure.toString());
		} catch (NotASerialPort notASerialPort) {
			return ServerResponse.createByErrorMessage(notASerialPort.toString());
		} catch (NoSuchPort noSuchPort) {
			return ServerResponse.createByErrorMessage(noSuchPort.toString());
		} catch (PortInUse portInUse) {
			return ServerResponse.createByErrorMessage(portInUse.toString());
		}

		return ServerResponse.createBySuccess(serialPort);
	}

	private ServerResponse addSerialListener(SerialPort serialPort) {
		try {
			SerialPortUtil.addListener(serialPort, serialListener);
		} catch (TooManyListeners tooManyListeners) {
			return ServerResponse.createByErrorMessage(tooManyListeners.toString());
		}
		return ServerResponse.createBySuccess();
	}

	private class runnableTaskGenerator implements Runnable {

		private SerialPort serialPort;
		private byte[] originData;

		private runnableTaskGenerator(SerialPort serialPort, byte[] originData) {
			this.serialPort = serialPort;
			this.originData = originData;
		}

		@Override
		public void run() {
			try {
				log.info("start sending data:{}.", Arrays.toString(originData));
				SerialPortUtil.sendToPort(serialPort, originData);
				log.info("start sended data:{}.", Arrays.toString(originData));
			} catch (SendDataToSerialPortFailure sendDataToSerialPortFailure) {
				sendDataToSerialPortFailure.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure serialPortOutputStreamCloseFailure) {
				serialPortOutputStreamCloseFailure.printStackTrace();
			}
		}
	}

}
