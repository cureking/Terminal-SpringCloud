package com.renewable.terminal.terminal.exception;

/**
 * @Description：
 * @Author: jarry
 */
public class RemoteServiceException extends RuntimeException {
	private Integer code;

	public RemoteServiceException(Integer code, String message){
		super(message);
		this.code = code;
	}

}
