package com.renewable.terminal.audio.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renewable.terminal.audio.entity.AudioAmnout;
import com.renewable.terminal.terminal.common.ServerResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jarry
 * @since 2019-07-25
 */
public interface IAudioAmnoutService extends IService<AudioAmnout> {

	ServerResponse saveAndUploadBatch(List<AudioAmnout> audioAmnoutList);
}
