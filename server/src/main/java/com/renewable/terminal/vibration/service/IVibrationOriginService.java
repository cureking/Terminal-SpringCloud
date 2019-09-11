package com.renewable.terminal.vibration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.entity.VibrationOrigin;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jarry
 * @since 2019-08-09
 */
public interface IVibrationOriginService extends IService<VibrationOrigin> {

	ServerResponse listByTimeWithCount(Integer devId, Integer passagewayCode, Integer count, String startTime, String endTime);
}
