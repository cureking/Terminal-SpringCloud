package com.renewable.terminal.inclination.extend.converter;

import com.renewable.terminal.terminal.common.ServerResponse;
import gnu.io.SerialPort;

/**
 * @Description：
 * @Author: jarry
 */
public interface Origin2Command {

	public ServerResponse origin2Object(SerialPort serialPort, byte[] originBuffer);

}
