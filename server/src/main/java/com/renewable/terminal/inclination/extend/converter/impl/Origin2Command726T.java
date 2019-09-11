package com.renewable.terminal.inclination.extend.converter.impl;

import com.renewable.terminal.inclination.constant.InclinationConfigConstant.InclinationSensorCommandEnum;
import com.renewable.terminal.inclination.entity.InclinationOrigin;
import com.renewable.terminal.inclination.extend.converter.Origin2Command;
import com.renewable.terminal.inclination.extend.util.CommandReceiveConvertUtil;
import com.renewable.terminal.inclination.service.ISerialCommandReceiveService;
import com.renewable.terminal.terminal.common.ServerResponse;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Description：
 * @Author: jarry
 */
@Component("Origin2Command726T")
@Slf4j
public class Origin2Command726T implements Origin2Command {

	@Autowired
	private ISerialCommandReceiveService iSerialCommandReceiveService;

	interface dataArea {

		int dataAreaStart = 4;

		int[][] dataFormateArray = {{3, 4}, {3, 4}, {3, 2}};
	}


	@Override
	public ServerResponse origin2Object(SerialPort serialPort, byte[] originBuffer) {

		log.info("System has choose the sensor about Inclination726T,data:{}", Arrays.toString(originBuffer));

		int command = originBuffer[3] & 0xFF;
		if (command == InclinationSensorCommandEnum.READALLR.getCode()) {
			//应答制下原始数据响应，同时也是自动输出模式下数据响应
			//todo 暂时不做明确相应，没有时间，且优先级不够。

			// 2.对接收到的检测参数进行处理
			// 调用对应的方法，对应方法会自行传输数据（后面的部分硬件响应需要提交到WebSocket）
			this.origin2ReadResponse(originBuffer);

			// 3.返回成功响应
			return ServerResponse.createBySuccess("acquire the readData:");


			//TODO 其他命令的响应处理页放在后面，不要急，重要程度不高的。
		} else if (command == InclinationSensorCommandEnum.SETZEROR.getCode()) {
			//call the relative service. e.g:Sensor_registry.
			log.info("this origin dataArray is SETZEROR");
			return ServerResponse.createByErrorMessage("this origin dataArray is SETZEROR");
		} else if (command == InclinationSensorCommandEnum.SETSPEEDR.getCode()) {
			log.info("this origin dataArray is SETSPEEDR");
			return ServerResponse.createByErrorMessage("this origin dataArray is SETSPEEDR");
		} else if (command == InclinationSensorCommandEnum.SETMODR.getCode()) {
			log.info("this origin dataArray is SETMODR");
			return ServerResponse.createByErrorMessage("this origin dataArray is SETMODR");
		} else if (command == InclinationSensorCommandEnum.SETADDRR.getCode()) {
			log.info("this origin dataArray is SETADDRR");
			return ServerResponse.createByErrorMessage("this origin dataArray is SETADDRR");
		} else if (command == InclinationSensorCommandEnum.GETZEROR.getCode()) {
			log.info("this origin dataArray is GETZEROR");
			return ServerResponse.createByErrorMessage("this origin dataArray is GETZEROR");
		} else { //2.无法解析数据
			log.error("can't exact this data package:", Arrays.toString(originBuffer));
		}
		return ServerResponse.createByErrorMessage("can't exact this data package:" + Arrays.toString(originBuffer));
	}

	// 是时候，试试切面了
	private void origin2ReadResponse(byte[] originBuffer) {
		// 0.获取地址符 address
		int address = originBuffer[2] & 0xFF;

		// 1.获取数据域数据
//		double[] dataArray = CommandReceiveConvertUtil.originData2Object(dataArea.dataAreaStart,
//				dataArea.dataAreaSingleDataLength,
//				dataArea.dataAreaSingleDataCount,
//				dataArea.integerLength,
//				dataArea.decimalLength,
//				originBuffer);
		double[] dataArray = CommandReceiveConvertUtil.originData2Object(originBuffer, dataArea.dataAreaStart, dataArea.dataFormateArray);

		// 2.组装Entity数据对象中原始数据部分
		InclinationOrigin inclinationOrigin = new InclinationOrigin();
		inclinationOrigin.setAngleX(dataArray[0]);
		inclinationOrigin.setAngleY(dataArray[1]);
		inclinationOrigin.setTemperature(dataArray[2]);

		// 3.调用对应方法，从而完成其余数据的填充，以及上传
		iSerialCommandReceiveService.receiveDataWithOrigin(address, inclinationOrigin);

		// 4.返回成功响应

	}


	private ServerResponse origin2SetZeroResponse(byte[] originBuffer) {
		return null;
	}


	private ServerResponse origin2GetZeroResponse(byte[] originBuffer) {
		return null;
	}


	private ServerResponse origin2SetAddressResponse(byte[] originBuffer) {
		return null;
	}


	private ServerResponse origin2ModelResponse(byte[] originBuffer) {
		return null;
	}


	private ServerResponse origin2SetSpeedResponse(byte[] originBuffer) {
		return null;
	}
}
