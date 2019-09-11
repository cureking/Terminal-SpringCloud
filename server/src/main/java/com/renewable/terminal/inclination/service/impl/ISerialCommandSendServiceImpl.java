package com.renewable.terminal.inclination.service.impl;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.extend.SerialSender;
import com.renewable.terminal.inclination.extend.converter.Command2OriginStrategyContext;
import com.renewable.terminal.inclination.service.IInclinationConfigService;
import com.renewable.terminal.inclination.service.ISerialCommandSendService;
import com.renewable.terminal.inclination.util.CheckDataUtil;
import com.renewable.terminal.inclination.util.SerialPortUtil;
import com.renewable.terminal.terminal.common.ServerResponse;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Service
@Slf4j
public class ISerialCommandSendServiceImpl implements ISerialCommandSendService {

	@Autowired
	private IInclinationConfigService iInclinationConfigService;

	@Autowired
	private Command2OriginStrategyContext command2OriginStrategyContext;

	@Autowired
	private SerialSender serialSender;

	@Override
	public ServerResponse respConfirm(Integer address) {

		return null;
	}

	@Override
	public ServerResponse readAll() {
		// 1.校验数据
		List<InclinationConfig> inclinationConfigList = iInclinationConfigService.list();
//		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationConfigList, "inclinationType", "address", "port", "baudrate");
//		if (checkResponse.isFail()){
//			return checkResponse;
//		}
		// 不需要校验，毕竟不能因为一个配置不行，就让所有配置不工作啊
		if (CollectionUtils.isEmpty(inclinationConfigList)) {
			return ServerResponse.createByErrorMessage("the inclinationConfigList is null !");
		}

		// 2.调用相关服务处理
		for (InclinationConfig inclinationConfig : inclinationConfigList) {
			ServerResponse readResponse = this.readAllWithAddress(inclinationConfig);
			if (readResponse.isFail()) {
				log.warn("the inclinationConfig with id = {} can't work ! readResponse:{}.", inclinationConfig.getAddress(), readResponse.getMsg());
				continue;
			}
		}
		return ServerResponse.createBySuccessMessage("inclination readAll command has all sended to hardware .");
	}

	@Override
	public ServerResponse readAllWithAddress(Integer inclinationConfigAddress) {
		// 1.校验数据
		InclinationConfig inclinationConfig = iInclinationConfigService.getById(inclinationConfigAddress);
		// 调用复用方法
		return this.readAllWithAddress(inclinationConfig);
	}

	private ServerResponse readAllWithAddress(InclinationConfig inclinationConfig) {
		// 1.校验数据
		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationConfig, "inclinationType", "address", "port", "baudrate");
		if (checkResponse.isFail()) {
			return checkResponse;
		}
		// 2.获得校验后的数据
		// 3.组装目标命令origin
		// 3.1 获取组装命令数组所需要的配置
		Integer address = inclinationConfig.getAddress();
		// 3.2 依据策略模式，获得对应的册率的对应结果
		String inclinationType = inclinationConfig.getInclinationType();

		byte[] originData = command2OriginStrategyContext.command2ReadAllOrigin(inclinationType, address);

		// 4.调用命令origin发送命令
		String portName = inclinationConfig.getPort();
		int baudrate = inclinationConfig.getBaudrate();

		int parity = SerialPort.PARITY_NONE;
		// 先在这里硬编码，后面有空整理一下（因为有些新想法）
		if (inclinationType.equals("116T")){
			parity = SerialPort.PARITY_EVEN;
		}

		ServerResponse serialSendResponse = serialSender.sendData(portName, baudrate, parity, originData);
		if (serialSendResponse.isFail()) {
			return serialSendResponse;
		}

		// 5.返回成功响应
		return ServerResponse.createBySuccess("inclination readAll command has sended to hardware .");
	}

	@Override
	public ServerResponse setZero(Integer inclinationConfigAddress, int type) {
		// 1.校验数据
		int count = iInclinationConfigService.count();
		if (count == 0) {
			return ServerResponse.createByErrorMessage("please input the config of inclination !");
		}
		InclinationConfig inclinationConfig = iInclinationConfigService.getById(inclinationConfigAddress);
		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationConfig, "inclinationType", "address", "port", "baudrate");
		if (checkResponse.isFail()) {
			return checkResponse;
		}

		// 2.获得校验后的数据
		// 3.组装目标命令origin
		// 3.1 获取组装命令数组所需要的配置
		Integer address = inclinationConfig.getAddress();
		// 3.2 依据策略模式，获得对应的册率的对应结果
		String inclinationType = inclinationConfig.getInclinationType();

		byte[] originData = command2OriginStrategyContext.command2SetZeroOrigin(inclinationType, address, type);

		// 4.调用命令origin发送命令
		String portName = inclinationConfig.getPort();
		int baudrate = inclinationConfig.getBaudrate();

		int parity = SerialPort.PARITY_NONE;
		// 先在这里硬编码，后面有空整理一下（因为有些新想法）
		if (inclinationType.equals("116T")){
			parity = SerialPort.PARITY_EVEN;
		}

		ServerResponse serialSendResponse = serialSender.sendData(portName, baudrate, parity, originData);
		if (serialSendResponse.isFail()) {
			return serialSendResponse;
		}

		// 5.返回成功响应
		return ServerResponse.createBySuccess("inclination setZero command has sended to hardware .");
	}

	@Override
	public ServerResponse setAddress(int inclinationConfigAddress, int targetAddress) {
		// 1.校验数据
		InclinationConfig inclinationConfig = iInclinationConfigService.getById(inclinationConfigAddress);
		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationConfig, "inclinationType", "address", "port", "baudrate");
		if (checkResponse.isFail()) {
			return checkResponse;
		}

		// 2.获得校验后的数据
		// 3.组装目标命令origin
		// 3.1 获取组装命令数组所需要的配置
		Integer address = inclinationConfig.getAddress();
		// 3.2 依据策略模式，获得对应的策略的对应结果
		String inclinationType = inclinationConfig.getInclinationType();

		byte[] originData = command2OriginStrategyContext.command2SetAddressOrigin(inclinationType, address, targetAddress);

		// 4.调用命令origin发送命令
		String portName = inclinationConfig.getPort();
		int baudrate = inclinationConfig.getBaudrate();
		int parity = SerialPort.PARITY_NONE;
		// 先在这里硬编码，后面有空整理一下（因为有些新想法）
		if (inclinationType.equals("116T")){
			parity = SerialPort.PARITY_EVEN;
		}
		ServerResponse serialSendResponse = serialSender.sendData(portName, baudrate, parity, originData);
		if (serialSendResponse.isFail()) {
			return serialSendResponse;
		}

		// 5.返回成功响应
		return ServerResponse.createBySuccess("inclination setAddress command has sended to hardware .");
	}

	@Override
	public ServerResponse listRealPort() {
		// 直接调用底层（不经过数据库）
		ArrayList portArray = SerialPortUtil.findPort();
		return ServerResponse.createBySuccess(portArray);
	}

	@Override
	public ServerResponse setAddressWithoutDb(Integer address, String portName, Integer baudrate, String inclinationType, Integer targetAddress) {
		if (address == null || portName == null || baudrate == null || inclinationType == null || targetAddress == null) {
			// TODO 也许之后我要写个注解与反射，来解决这个问题
			return ServerResponse.createByErrorMessage("params must notNull !");
		}
		// 直接调用底层（不经过数据库）
		byte[] originData = command2OriginStrategyContext.command2SetAddressOrigin(inclinationType, address, targetAddress);
		// 4.调用命令origin发送命令
		int parity = SerialPort.PARITY_NONE;
		// 先在这里硬编码，后面有空整理一下（因为有些新想法）
		if (inclinationType.equals("116T")){
			parity = SerialPort.PARITY_EVEN;
		}
		ServerResponse serialSendResponse = serialSender.sendData(portName, baudrate, parity, originData);
		if (serialSendResponse.isFail()) {
			return serialSendResponse;
		}

		// 5.返回成功响应
		return ServerResponse.createBySuccess("inclination setAddress command has sended to hardware .");
	}

}
