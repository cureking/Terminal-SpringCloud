package com.renewable.terminal.message.service;

import com.renewable.terminal.audio.entity.AudioAmnout;
import com.renewable.terminal.audio.entity.AudioDba;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public interface IAudioProducerService {

	void sendAudioDbaList(String audioDbaListStr);

	void sendAudioDbaList(List<AudioDba> audioDbaList);

	void sendAudioAmnoutList(String audioAmnoutListStr);

	void sendAudioAmnoutList(List<AudioAmnout> audioAmnoutList);
}
