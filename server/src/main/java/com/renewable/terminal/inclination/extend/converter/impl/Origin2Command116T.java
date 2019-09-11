package com.renewable.terminal.inclination.extend.converter.impl;

import com.renewable.terminal.inclination.constant.InclinationConfigConstant;
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
@Component("Origin2Command116T")
@Slf4j
public class Origin2Command116T implements Origin2Command {

	@Autowired
	private ISerialCommandReceiveService iSerialCommandReceiveService;

	@Override
	public ServerResponse origin2Object(SerialPort serialPort, byte[] originBuffer) {
		log.info("System has choose the sensor about Inclination116T,data:{}", Arrays.toString(originBuffer));

		int functionCode = originBuffer[1] & 0xFF;

		int address = originBuffer[0] & 0xFF;

		// 功能码:03/06，06说明是设置响应。06说明是原始数据返回
		int identifyCode = originBuffer[1] & 0xFF;
		if (identifyCode == 03){
			// 原始数据处理
			log.info("this origin dataArray is READALL");
			this.origin2ReadResponse(originBuffer);

		}else if (identifyCode == 06){
			// 获取寄存器地址
			// 这里直接采用3位，不管2位（基本都是00，本传感器不用管）
			int registerAddress = originBuffer[3] & 0xFF;
			//TODO 根据寄存器地址，判断是何种返回
		}

		return ServerResponse.createByErrorMessage("can't exact this data package:" + Arrays.toString(originBuffer));
	}

	public void origin2ReadResponse(byte[] originBuffer) {
		// 0.获取地址符 address
		int address = originBuffer[0] & 0xFF;

		// 1.获取数据域数据


		double dataX = calElement(subBytes(originBuffer,3,2));
		double dataY = calElement(subBytes(originBuffer,7,2));
		// 2.组装Entity数据对象中原始数据部分
		InclinationOrigin inclinationOrigin = new InclinationOrigin();
		inclinationOrigin.setAngleX(dataX);
		inclinationOrigin.setAngleY(dataY);
		inclinationOrigin.setTemperature(null);

		// 3.调用对应方法，从而完成其余数据的填充，以及上传
		iSerialCommandReceiveService.receiveDataWithOrigin(address, inclinationOrigin);

		// 4.返回成功响应

	}

//	subBytes
	private static double calElement(byte[] elementArray){
		int var1 = (elementArray[1] << 8) + elementArray[0];
		double var2 = (var1 - 9000)/100.00;
		System.out.println("calElement: pre: "+elementArray[1]+" element<<8:"+(elementArray[1] << 8)+" after:"+elementArray[0]+" result: var1:"+var1+" var2:"+var2);
		return var2;
	}

	private static byte[] subBytes(byte[] source, int begin, int count) {
		byte[] bs = new byte[count];
		try {
			System.arraycopy(source, begin, bs, 0, count);
		} catch (Exception e) {
			System.out.println("Origin2Command116T/subBytes执行异常：" + e.toString());
		}

		return bs;
	}

	// test
	public static void main(String[] args) {

		byte[] receiveMsg = {Integer.valueOf("01",16).byteValue(),
				Integer.valueOf("03",16).byteValue(),
				Integer.valueOf("08",16).byteValue(),
				Integer.valueOf("50",16).byteValue(),
				Integer.valueOf("46",16).byteValue(),
				Integer.valueOf("00",16).byteValue(),
				Integer.valueOf("00",16).byteValue(),
				Integer.valueOf("23",16).byteValue(),
				Integer.valueOf("20",16).byteValue(),
				Integer.valueOf("00",16).byteValue(),
				Integer.valueOf("00",16).byteValue(),
				Integer.valueOf("BD",16).byteValue(),
				Integer.valueOf("61",16).byteValue(),
		};

		double dataX = calElement(subBytes(receiveMsg,3,2));
		double dataY = calElement(subBytes(receiveMsg,7,2));
		System.out.println("dataX:"+dataX);
		System.out.println("dataY:"+dataY);
	}

}
