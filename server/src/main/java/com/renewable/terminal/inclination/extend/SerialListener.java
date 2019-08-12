package com.renewable.terminal.inclination.extend;

import com.renewable.terminal.inclination.constant.InclinationConfigConstant;
import com.renewable.terminal.inclination.extend.converter.Origin2CommandStrategyContext;
import com.renewable.terminal.inclination.util.SerialPortUtil;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Description：
 * @Author: jarry
 */
@Component
@Slf4j
public class SerialListener implements SerialPortEventListener {

	@Autowired
	private Origin2CommandStrategyContext origin2CommandStrategyContext;

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		SerialPort serialPort = (SerialPort) serialPortEvent.getSource();

		switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.BI: // 10 通讯中断
				System.out.println("通讯中断");
				break;

			case SerialPortEvent.OE: // 7 溢位（溢出）错误

			case SerialPortEvent.FE: // 9 帧错误

			case SerialPortEvent.PE: // 8 奇偶校验错误

			case SerialPortEvent.CD: // 6 载波检测

			case SerialPortEvent.CTS: // 3 清除待发送数据

			case SerialPortEvent.DSR: // 4 待发送数据准备好了

			case SerialPortEvent.RI: // 5 振铃指示

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
				break;

			case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据

				// 1.获取数据
				byte[] data = null;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					log.error(e.toString());
				}
				data = SerialPortUtil.readFromPort(serialPort);

				// 2.调用数据处理方法
				this.originDataReceive(serialPort, data);

			default:

		}
	}

	/**
	 * 利用策略模式，将接收到的数据发往对应的协议解析
	 *
	 * @param serialPort
	 * @param originData
	 */
	private void originDataReceive(SerialPort serialPort, byte[] originData) {
		log.info("serialListener has listened the data:{}.", Arrays.toString(originData));
		if (originData[0] == InclinationConfigConstant.SensorEnum.Inclination1.getCode()) {
			// 然后发现数据长度为5或者6的返回值就惨了（硬件不能做到如软件这样的支撑，必须是物联网设计所要考虑的）
			// 以下代码集成到
//			if (originData.length == 5 || originData.length == 6){
//				// 我们应该庆幸，这个时候的三个倾斜传感器对这几个响应都是一致的。不过日后如果扩展出不一致的传感器，那么这个设计从硬件底层就死掉了（因为没有标识，只能靠一些不合理的人工设置）。
//
//			}

			// 硬件这边没有做任何标识，只能通过数组长度来判断
			log.info("length of the data array:" + originData.length);
			// 利用策略模式，可以有效降低耦合度
			origin2CommandStrategyContext.origin2Object(InclinationConfigConstant.InclinationSerialTypeEnum.codeOf(originData.length).getValue(), serialPort, originData);

		} else {
			log.error("无此传感器标识符，请添加配置，或检查硬件设备", originData);
		}

	}

	/**
	 * 用于实现数据校验，从而判断数据是否接收完毕
	 *
	 * @param originData
	 * @return
	 */
	private boolean dataArrayCheck(byte[] originData) {


		return true;
	}

}
