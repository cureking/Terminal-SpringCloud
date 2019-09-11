package com.renewable.terminal.vibration.extend;

import com.renewable.terminal.vibration.entity.VibrationOrigin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Description： 通过线程池实现数据的处理
 * @Author: jarry
 */
@Component
@Slf4j
public class VibrationDataDealPool {


	// 参照享元模式，建立多个可以重复利用对象。 单例容器
	private static List<VibrationOrigin> vibrationOriginList;

	// 建立线程池	// 这里可以选择直接new，也可以直接注入（注入的VibrationPool，默认是10.100
	private static VibrationPool vibrationPool = new VibrationPool(10, 100);

	static {

	}


	public void submitVibrationDataArray(float[] vibrationArray) {

		Date currentDate = new Date();
		Runnable runnableTask = new runnableTaskGenerator(vibrationArray, currentDate);
		vibrationPool.submit(runnableTask);
	}

	private class runnableTaskGenerator implements Runnable {

		private float[] vibrationArray;
		private Date currentDate;

		private runnableTaskGenerator(float[] vibrationArray, Date currentDate) {
			this.vibrationArray = vibrationArray;
			this.currentDate = currentDate;
		}

		@Override
		public void run() {
			// 此处如果采用多线程处理数据函数，请注意spring的bean是单例的

			// 1.
//			try {
//				log.info("start sending data:{}.", Arrays.toString(originData));
//				SerialPortUtil.sendToPort(serialPort, originData);
//				log.info("start sended data:{}.", Arrays.toString(originData));
//			} catch (SendDataToSerialPortFailure sendDataToSerialPortFailure) {
//				sendDataToSerialPortFailure.printStackTrace();
//			} catch (SerialPortOutputStreamCloseFailure serialPortOutputStreamCloseFailure) {
//				serialPortOutputStreamCloseFailure.printStackTrace();
//			}
		}
	}

}
