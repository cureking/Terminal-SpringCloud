package com.renewable.terminal.vibration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.entity.VibrationOrigin;
import com.renewable.terminal.vibration.entity.VibrationPeak;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jarry
 * @since 2019-08-09
 */
public interface IVibrationPeakService extends IService<VibrationPeak> {

	ServerResponse data2PeakCalAndSave(List<VibrationOrigin> vibrationOriginList);
}
