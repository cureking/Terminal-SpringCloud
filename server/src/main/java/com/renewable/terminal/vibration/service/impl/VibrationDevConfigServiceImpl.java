package com.renewable.terminal.vibration.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renewable.terminal.vibration.dao.VibrationDevConfigMapper;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;
import com.renewable.terminal.vibration.service.IVibrationDevConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-08-09
 */
@Service
public class VibrationDevConfigServiceImpl extends ServiceImpl<VibrationDevConfigMapper, VibrationDevConfig> implements IVibrationDevConfigService {

	@Override
	public double getSamplingFromDevConfig(String devId) {

		Wrapper<VibrationDevConfig> vibrationDevConfigWrapper = new QueryWrapper<VibrationDevConfig>()
				.eq("dev_id",devId)
				.select("sampling_frequency");
		VibrationDevConfig vibrationDevConfig = this.getOne(vibrationDevConfigWrapper);

		Integer sampling = vibrationDevConfig.getSamplingFrequency();

		return Double.valueOf(sampling.toString());
	}

	@Override
	public String getDevConfigIdFromDevConfig() {
		Wrapper<VibrationDevConfig> vibrationDevConfigWrapper = new QueryWrapper<VibrationDevConfig>()
				.select("id")
				.last("limit 1");
		VibrationDevConfig vibrationDevConfig = this.getOne(vibrationDevConfigWrapper);

		String devConfigId = vibrationDevConfig.getId();
		return devConfigId;
	}
}
