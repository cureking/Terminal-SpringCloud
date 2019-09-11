package com.renewable.terminal.inclination.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.terminal.common.ServerResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
public interface IInclinationConfigService extends IService<InclinationConfig> {

	/**
	 * 计算incliation的X与Y
	 *
	 * @param inclinationConfig
	 * @return
	 */
	ServerResponse<InclinationConfig> calXAndY(InclinationConfig inclinationConfig);

	/**
	 * 通过matlab，计算incliation的X与Y
	 *
	 * @param inclinationConfig
	 * @return
	 */
	ServerResponse<InclinationConfig> calInitWithMatlab(InclinationConfig inclinationConfig);
}
