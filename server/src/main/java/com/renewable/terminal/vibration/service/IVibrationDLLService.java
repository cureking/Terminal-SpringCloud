package com.renewable.terminal.vibration.service;

import com.renewable.terminal.terminal.common.ServerResponse;

/**
 * @Description： 用于调用dll方法，获取响应数据
 * @Author: jarry
 */
public interface IVibrationDLLService {

	// 根据配置，读取数据
	ServerResponse readAdContinueData();
}
