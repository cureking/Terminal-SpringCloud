package com.renewable.terminal.message.client;

import com.renewable.terminal.audio.entity.AudioAmnout;
import com.renewable.terminal.audio.entity.AudioDba;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@FeignClient(name = "message", fallback = AudioMessageClient.AudioMessageClientFallback.class)
public interface AudioMessageClient {

	@PostMapping("/message/audio/upload_amnout_list.do")
	ServerResponse uploadAmnoutList(@RequestBody List<AudioAmnout> audioAmnoutList);

	@PostMapping("/message/audio/upload_amnout_list_str.do")
	ServerResponse uploadAmnoutListStr(String audioAmnoutListStr);

	@PostMapping("/message/audio/upload_dba_list.do")
	ServerResponse uploadDbaList(@RequestBody List<AudioDba> audioDbaList);

	@PostMapping("/message/audio/upload_dba_list_str.do")
	ServerResponse uploadDbaListStr(String audioDbaListStr);

	@Component
	class AudioMessageClientFallback implements AudioMessageClient {

		@Override
		public ServerResponse uploadAmnoutList(List<AudioAmnout> audioAmnoutList) {
			return ServerResponse.createByErrorMessage("Busy service about Message/AudioMessageClient/uploadAmnoutList().");
		}

		@Override
		public ServerResponse uploadAmnoutListStr(String audioAmnoutListStr) {
			return ServerResponse.createByErrorMessage("Busy service about Message/AudioMessageClient/uploadAmnoutListStr().");
		}

		@Override
		public ServerResponse uploadDbaList(List<AudioDba> audioDbaList) {
			return ServerResponse.createByErrorMessage("Busy service about Message/AudioMessageClient/uploadDbaList().");
		}

		@Override
		public ServerResponse uploadDbaListStr(String audioDbaListStr) {
			return ServerResponse.createByErrorMessage("Busy service about Message/AudioMessageClient/uploadDbaListStr().");
		}
	}
}
