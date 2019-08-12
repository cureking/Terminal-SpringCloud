package com.renewable.terminal.vibration.client;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
		name = "vibration",
		fallback = VibrationClient.VibrationClientFallback.class
)
public interface VibrationClient {

	/**
	 * 根据数据库中的配置列表，执行所有读取
	 * 注意，该方法的执行间隔不可小于配置中设置的读取长度所需时间
	 *
	 * @return
	 */
	@GetMapping("/dll/read_ad_continue_data.do")
	ServerResponse readAdContinueData();

	/**
	 * 从message服务那里获取来自center的配置信息
	 *
	 * @return
	 */
	@PostMapping("/vibration_dev_config/update_from_center.do")
	ServerResponse updateDevConfigFromCenter(@RequestBody VibrationDevConfig vibrationDevConfig);


	// 降级服务的class（通过内部class实现，之后需要，也可以分离出去，但是我感觉放在一起挺好的。毕竟现在服务较少）
	@Component
	static class VibrationClientFallback implements VibrationClient {


		@Override
		public ServerResponse readAdContinueData() {
			return ServerResponse.createByErrorMessage("Busy service about readAdContinueData()");
		}

		@Override
		public ServerResponse updateDevConfigFromCenter(VibrationDevConfig vibrationDevConfig) {
			return ServerResponse.createByErrorMessage("Busy service about updateDevConfigFromCenter()");
		}
	}
}