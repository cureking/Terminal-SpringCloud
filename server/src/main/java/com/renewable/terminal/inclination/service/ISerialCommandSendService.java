package com.renewable.terminal.inclination.service;

import com.renewable.terminal.terminal.common.ServerResponse;

/**
 * 这里只负责发送serialcommand，而对于数据库，这里也只读取数据。对数据库的保存是由对应的接收serialcommand的服务负责（这样可以有效提高数据一致性，并降低系统复杂度）
 *
 * @Description： 集中各类对硬件的操作函数
 * @Author: jarry
 */
public interface ISerialCommandSendService {

	// 发送数据

	/**
	 * 对所有（即FF）传感器发送（不行，因为串口存在“撞车”现象，唉）
	 * 那就只能一个个问，并且要确保不能产生“撞车”（每次循环停顿50ms)
	 * 健康问题，直接通过数据接收来判断吧
	 *
	 * @return
	 */
	ServerResponse respConfirm(Integer address);

	/**
	 * 读取所有终端的数据，但是这里只能发数据，不确保目标存在（之后可以根据返回异常来就该状态（每次循环停顿50ms)
	 *
	 * @return
	 */
	ServerResponse readAll();

	/**
	 * 根据配置id，获取对应配置，来进行数据组装及发送
	 *
	 * @param inclinationConfigAddress 也就是inclinationConfigId，InclinationConfig的primaryKey
	 * @return
	 */
	ServerResponse readAllWithAddress(Integer inclinationConfigAddress);

	/**
	 * 根据配置id，修改对应硬件的相关数据。
	 * 一，数据库的数据只根据硬件返回值进行修改
	 * 二，进行修改前，可以考虑先发送一个确认，判断是否在线（并同步更新数据库）
	 *
	 * @param inclinationConfigAddress
	 * @param type
	 * @return
	 */
	ServerResponse setZero(Integer inclinationConfigAddress, int type);

	ServerResponse setAddress(int inclinationConfigAddress, int targetAddress);

	ServerResponse listRealPort();

	ServerResponse setAddressWithoutDb(Integer address, String portName, Integer baudrate, String inclinationType, Integer targetAddress);
}
