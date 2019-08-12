package com.renewable.terminal.audio.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renewable.terminal.audio.dao.AudioAmnoutMapper;
import com.renewable.terminal.audio.entity.AudioAmnout;
import com.renewable.terminal.audio.service.IAudioAmnoutService;
import com.renewable.terminal.message.client.AudioMessageClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-07-25
 */
@Service
public class AudioAmnoutServiceImpl extends ServiceImpl<AudioAmnoutMapper, AudioAmnout> implements IAudioAmnoutService {

	@Autowired
	private AudioMessageClient audioMessageClient;

	@Override
	public ServerResponse saveAndUploadBatch(List<AudioAmnout> audioAmnoutList) {
		if (CollectionUtils.isEmpty(audioAmnoutList)){
			return ServerResponse.createByErrorMessage("audioAmnoutList is null !");
		}

		boolean result = this.saveBatch(audioAmnoutList);
		if (!result){
			return ServerResponse.createByErrorMessage("audioAmnoutList save fail !");
		}

		// 4.上传中控室
		audioMessageClient.uploadAmnoutList(audioAmnoutList);

		return ServerResponse.createBySuccessMessage("audioAmnoutList has sended to mq.");
	}
}
