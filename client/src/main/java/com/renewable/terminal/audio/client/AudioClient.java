package com.renewable.terminal.audio.client;

import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
		name = "audio",
		fallback = AudioClient.AudioClientFallback.class
)
public interface AudioClient {

	/**
	 * 开启音频录制，解析，以及存储
	 * @return
	 */
	@GetMapping("/audio/listener_task.do")
	ServerResponse audioListenerTask();

	// 降级服务的class（通过内部class实现，之后需要，也可以分离出去，但是我感觉放在一起挺好的。毕竟现在服务较少）
	@Component
	static class AudioClientFallback implements AudioClient{

		@Override
		public ServerResponse audioListenerTask() {
			return ServerResponse.createByErrorMessage("Busy service about audioListenerTask()");
		}
	}
}