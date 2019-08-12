package com.renewable.terminal.inclination.extend.converter.impl;

import com.renewable.terminal.inclination.constant.InclinationConfigConstant;
import com.renewable.terminal.inclination.extend.converter.Command2Origin;
import com.renewable.terminal.inclination.extend.util.CommandSendConvertUtil;
import org.springframework.stereotype.Component;

import static com.renewable.terminal.inclination.constant.InclinationConfigConstant.SensorEnum.Inclination1;

/**
 * @Description：
 * @Author: jarry
 */
@Component("Command2Origin726T")
public class Command2Origin726T implements Command2Origin {

	private static final int SENSOR_TYPE_CODE = Inclination1.getCode();

	@Override
	public byte[] command2ReadAllOrigin(int address) {
		int command = InclinationConfigConstant.InclinationSensorCommandEnum.READALL.getCode();
		byte[] targetOrigin = CommandSendConvertUtil.command2Origin(SENSOR_TYPE_CODE, address, command);
		return targetOrigin;
	}

	@Override
	public byte[] command2SetZeroOrigin(int address, int type) {
		int command = InclinationConfigConstant.InclinationSensorCommandEnum.SETZERO.getCode();
		byte[] targetOrigin = CommandSendConvertUtil.command2OriginWithDataArea(SENSOR_TYPE_CODE, address, command, type);
		return targetOrigin;
	}

	@Override
	public byte[] command2SetSpeedOrigin(int address, int level) {
		int command = InclinationConfigConstant.InclinationSensorCommandEnum.SETSPEED.getCode();
		byte[] targetOrigin = CommandSendConvertUtil.command2OriginWithDataArea(SENSOR_TYPE_CODE, address, command, level);
		return targetOrigin;
	}

	@Override
	public byte[] command2SetModelOrigin(int address, int type) {
		return new byte[0];
	}

	@Override
	public byte[] command2SetAddressOrigin(int address, int targetAddress) {
		int command = InclinationConfigConstant.InclinationSensorCommandEnum.SETADDR.getCode();
		byte[] targetOrigin = CommandSendConvertUtil.command2OriginWithDataArea(SENSOR_TYPE_CODE, address, command, targetAddress);
		return targetOrigin;
	}

	@Override
	public byte[] command2GetZeroOrigin(int address) {
		int command = InclinationConfigConstant.InclinationSensorCommandEnum.SETSPEED.getCode();
		byte[] targetOrigin = CommandSendConvertUtil.command2Origin(SENSOR_TYPE_CODE, address, command);
		return targetOrigin;
	}

	@Override
	public byte[] command2ResponseOrigin(int address) {
		int command = InclinationConfigConstant.InclinationSensorCommandEnum.RESP.getCode();
		byte[] targetOrigin = CommandSendConvertUtil.command2Origin(SENSOR_TYPE_CODE, address, command);
		return targetOrigin;
	}
}
