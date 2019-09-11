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

	// 建立线程池
	private static SerialPool serialPool = new SerialPool(10, 100);

	public ServerResponse sendData(String portName, int baudrate, int parity, byte[] originData) {
		// 1.判断对应的SerialPort对象，在serialPortConcurrentHashMap中是否存在
		String serialPortName = portName + "_" + baudrate;

		// 2.如果不存在，直接创建对应SerialPort对象（单独建立方法，并且需要添加SerialListener），并置入serialPortConcurrentHashMap中
		if (!serialPortConcurrentHashMap.containsKey(serialPortName)) {
			ServerResponse serialPortGeneratorResponse = this.serialPortGenerator(portName, baudrate, parity);
			if (serialPortGeneratorResponse.isFail()) {
				log.warn("serialPortGeneratorResponse:{} !", serialPortGeneratorResponse.getMsg());
				return serialPortGeneratorResponse;
			}
			SerialPort serialPort = (SerialPort) serialPortGeneratorResponse.getData();
			serialPortConcurrentHashMap.put(serialPortName, serialPort);
			// 增加监听
			ServerResponse addListenerResponse = this.addSerialListener(serialPort);
			if (addListenerResponse.isFail()) {
				log.warn("addListenerResponse:{} !", addListenerResponse.getMsg());
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



	/**
	 * 为了尝试解决 由于频繁访问对应串口，而导致的硬件“拒绝访问”的问题，而提出重新初始化硬件的串口列表，serialPort
	 * 重新刷新serialPortMap
	 * @return
	 */
	public ServerResponse clearSerialPortMap(){
		log.warn("start refresh serialPortMap");
		for (Map.Entry<String, SerialPort> entry : serialPortConcurrentHashMap.entrySet()) {
			log.warn("start refresh serialPort with portName:{}",entry.getKey());
			SerialPort serialPort = entry.getValue();
			try{
				serialPort.removeEventListener();
			}catch (Exception e){
				log.warn("serialPort removeEventListener fail ! Exception:{} .", e.toString());
			}

			try{
				SerialPortUtil.closePort(serialPort);
			}catch (Exception e){
				log.warn("serialPort closePort fail ! Exception:{} .", e.toString());
			}

			// 我无法确定这个方式是否绝对有效，而尝试这个异常又太耗时，这里添加一个赋值null操作，争取还可以通过GC，销毁对应对象
			serialPort = null;
			log.warn("end refresh serialPort with portName:{}",entry.getKey());
		}

		serialPortConcurrentHashMap.clear();

		log.warn("end refresh serialPortMap");
		return ServerResponse.createBySuccessMessage("clearSerialPortMap");
	}

	private ServerResponse<SerialPort> serialPortGenerator(String portName, int baudrate, int parity) {

		SerialPort serialPort = null;

		try {
			serialPort = SerialPortUtil.openPort(portName, baudrate);
			// 设置相关参数（目前是为了设置parity，后续也许就要设置dataBits什么的了）
			SerialPortUtil.setSerialPortParams(serialPort,baudrate,null,null,parity);
		} catch (SerialPortParameterFailure serialPortParameterFailure) {
			log.warn("SerialPortParameterFailure:{} !", serialPortParameterFailure.toString());
			return ServerResponse.createByErrorMessage(serialPortParameterFailure.toString());
		} catch (NotASerialPort notASerialPort) {
			log.warn("NotASerialPort:{} !", notASerialPort.toString());
			return ServerResponse.createByErrorMessage(notASerialPort.toString());
		} catch (NoSuchPort noSuchPort) {
			log.warn("NoSuchPort:{} !", noSuchPort.toString());
			return ServerResponse.createByErrorMessage(noSuchPort.toString());
		} catch (PortInUse portInUse) {
			log.warn("PortInUse:{} !", portInUse.toString());
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
				log.info("start sending {} with data:{}.", serialPort.getName()+"_"+serialPort.getBaudRate(),Arrays.toString(originData));
				SerialPortUtil.sendToPort(serialPort, originData);
				log.info("start sended {} with data:{}.", serialPort.getName()+"_"+serialPort.getBaudRate(),Arrays.toString(originData));
			} catch (SendDataToSerialPortFailure sendDataToSerialPortFailure) {
				log.error("SerialSender/runnableTaskGenerator/run: catch SendDataToSerialPortFailure:{}", sendDataToSerialPortFailure.toString());
			} catch (SerialPortOutputStreamCloseFailure serialPortOutputStreamCloseFailure) {
				log.error("SerialSender/runnableTaskGenerator/run: catch SerialPortOutputStreamCloseFailure:{}", serialPortOutputStreamCloseFailure.toString());
			}
		}
	}

}
