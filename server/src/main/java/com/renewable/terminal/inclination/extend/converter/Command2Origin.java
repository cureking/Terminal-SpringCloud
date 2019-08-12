package com.renewable.terminal.inclination.extend.converter;

/**
 * @Description： 调用方直接调用相关接口即可（之后通过反射实现，命令的自动发送）
 * @Author: jarry
 */

public interface Command2Origin {

	byte[] command2ReadAllOrigin(int address);

	byte[] command2SetZeroOrigin(int address, int type);

	byte[] command2SetSpeedOrigin(int address, int level);

	byte[] command2SetModelOrigin(int address, int type);

	byte[] command2SetAddressOrigin(int address, int targetAddress);

	byte[] command2GetZeroOrigin(int address);

	byte[] command2ResponseOrigin(int address);
}
