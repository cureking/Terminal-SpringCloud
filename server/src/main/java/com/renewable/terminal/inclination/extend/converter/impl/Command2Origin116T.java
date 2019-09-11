package com.renewable.terminal.inclination.extend.converter.impl;

import com.renewable.terminal.inclination.extend.converter.Command2Origin;
import com.renewable.terminal.inclination.extend.util.CRC16Util;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * @Description：
 * @Author: jarry
 */
@Component("Command2Origin116T")
public class Command2Origin116T implements Command2Origin {
	@Override
	public byte[] command2ReadAllOrigin(int address) {
		// 这里由于时间关系，先半硬编码吧。毕竟可能就这一种modbus协议的倾斜传感器
		// 03表示功能码，00，02表示对应寄存地址，00，04表示数据长度
		byte[] originPre = new byte[]{Integer.valueOf(String.valueOf(address),16).byteValue(),03,00,02,00,04};
		byte[] crcArray = CRC16Util.getCrc16(originPre);
		byte[] origin = ArrayUtils.addAll(originPre,crcArray);

		return origin;
	}

	@Override
	public byte[] command2SetZeroOrigin(int address, int type) {
		// 06表示功能码，00，10表示对应寄存地址，00，00/FF表示置零类型
		byte[] originPre = new byte[]{Integer.valueOf(String.valueOf(address),16).byteValue(),06,00,10,00,Integer.valueOf(String.valueOf(type),16).byteValue()};
		byte[] crcArray = CRC16Util.getCrc16(originPre);
		byte[] origin = ArrayUtils.addAll(originPre,crcArray);

		return origin;
	}

	@Override
	public byte[] command2SetSpeedOrigin(int address, int level) {
		return new byte[0];
	}

	@Override
	public byte[] command2SetModelOrigin(int address, int type) {
		return new byte[0];
	}

	@Override
	public byte[] command2SetAddressOrigin(int address, int targetAddress) {
		// 06表示功能码，00，10表示对应寄存地址，00，targetAddress表示目标地址（不过这里，我只放开了后两位，也就是只有256-1个地址。一方面是为了统一，另一方面也确实用不到这么多。。。）
		byte[] originPre = new byte[]{Integer.valueOf(String.valueOf(address),16).byteValue(),06,00,11,00,Integer.valueOf(String.valueOf(targetAddress),16).byteValue()};
		byte[] crcArray = CRC16Util.getCrc16(originPre);
		byte[] origin = ArrayUtils.addAll(originPre,crcArray);

		return origin;
	}

	@Override
	public byte[] command2GetZeroOrigin(int address) {
		return new byte[0];
	}

	@Override
	public byte[] command2ResponseOrigin(int address) {
		return new byte[0];
	}
}
