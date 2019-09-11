package com.renewable.terminal.inclination.exception.serial;

public class SendDataToSerialPortFailure extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Exception exception;

	//	public SendDataToSerialPortFailure() {
//	}
	public SendDataToSerialPortFailure(Exception exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "数据发送端口失败. Exception:" + exception.toString();
	}

}
