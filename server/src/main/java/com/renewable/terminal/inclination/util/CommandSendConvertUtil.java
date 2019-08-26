package com.renewable.terminal.inclination.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public class CommandSendConvertUtil {


	public static byte[] command2Origin(int sensorTypeCode, int command, int address) {
		return list2OriginData(objece2ListData(sensorTypeCode, command, address));
	}

	public static byte[] command2OriginWithDataArea(int sensorTypeCode, int command, int data, int address) {
		return list2OriginData(objece2ListDataWithDataArea(sensorTypeCode, command, data, address));
	}

	private static List<Integer> objece2ListData(int sensorTypeCode, int address, int command) {

		//数据先以Integer类型保存，之后转为Byte
		List<Integer> originArray = Lists.newArrayList();

		int data_0 = sensorTypeCode;

		// data_1是命令长度，针对固定的命令，这个长度是固定的。一般为4或者5，可以动态，但是感觉现在没那个必要
		// 命令长度
		int data_1 = 4;
		// 目标address
		int data_2 = address;
		// 命令标识
		int data_3 = command;
		// 校验位，初始为0
		int data_4 = 0;

		originArray.add(data_0);
		originArray.add(data_1);
		originArray.add(data_2);
		originArray.add(data_3);

		//设置数据长度（+1是因为数据校验位还没有添加，需要计算）（-1是因为标示符不参与计算）
		originArray.set(1, originArray.size() + 1 - 1);

		//计算校验和     校验和是不计算标示符（不计算第一位）与自身的（不计算最后一位）
		for (int j = 1; j < originArray.size(); j++) {
			data_4 += originArray.get(j);
		}
		originArray.add(data_4);
		return originArray;
	}

	private static List<Integer> objece2ListDataWithDataArea(int sensorTypeCode, int address, int command, int data) {
		List<Integer> originArray = Lists.newArrayList();

		int data_0 = sensorTypeCode;

		// 命令长度
		int data_1 = 4;
		// 目标address
		int data_2 = address;
		// 命令标识
		int data_3 = command;
		// 数据域
		int data_4 = data;
		// 校验位，初始为0
		int data_5 = 0;

		originArray.add(data_0);
		originArray.add(data_1);
		originArray.add(data_2);
		originArray.add(data_3);
		originArray.add(data_4);

		//设置数据长度（+1是因为数据校验位还没有添加，需要计算）（-1是因为标示符不参与计算）
		originArray.set(1, originArray.size() + 1 - 1);

		//计算校验和     校验和是不计算标示符（不计算第一位）与自身的（不计算最后一位）
		for (int j = 1; j < originArray.size(); j++) {
			data_5 += originArray.get(j);
		}
		originArray.add(data_5);
		return originArray;
	}

	/**
	 * List<Integer>转为对应2字节Byte[]类型 （容量扩大一倍）
	 *
	 * @param list
	 * @return
	 */
	private static byte[] list2OriginData(List<Integer> list) {
		byte[] originData = new byte[list.size()];
		int i = 0;
		for (Integer integer : list) {
			//            originData[i] = int2byte(integer);        很想骂人，serialPort是会自动转的，不需要我们处理。（那你倒是标明啊。要么就别用那么具有误会性的参数列表啊）
			originData[i] = int2byteNoChange(integer);
			i++;
		}
		//        Byte[] originData =list.toArray(new Byte[list.size()]);       //原生方法无法使用
		return originData;
	}

	private static byte int2byteNoChange(int data) {
		String hex = Integer.toString(data);
		return Byte.parseByte(hex);
	}
}
