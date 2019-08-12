package com.renewable.terminal.terminal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import jdk.nashorn.internal.objects.annotations.Constructor;

import javax.annotation.PostConstruct;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
public interface ITerminalService extends IService<Terminal> {

	/**
	 * 获取终端配置Terminal
	 * @return
	 */
	ServerResponse getTerminal();

	/**
	 * 刷新终端配置
	 * @return
	 */
	ServerResponse refresh();
}
