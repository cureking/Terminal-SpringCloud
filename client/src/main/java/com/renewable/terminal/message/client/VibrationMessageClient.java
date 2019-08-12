package com.renewable.terminal.message.client;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.entity.VibrationArea;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@FeignClient(name = "message", fallback = VibrationMessageClient.VibrationMessageClientFallback.class)
public interface VibrationMessageClient {

	@PostMapping("/message/vibration/upload_area_list.do")
	ServerResponse uploadAreaList(@RequestBody List<VibrationArea> vibrationAreaList);

	@PostMapping("/message/vibration/upload_area_list_str.do")
	ServerResponse uploadAreaListStr(String vibrationAreaListStr);

	@PostMapping("/message/vibration/upload_config_list.do")
	ServerResponse uploadConfigList(@RequestBody List<VibrationDevConfig> vibrationDevConfigList);

	@PostMapping("/message/vibration/upload_config_list_str.do")
	ServerResponse uploadConfigListStr(String vibrationDevConfigListStr);

	@Component
	class VibrationMessageClientFallback implements VibrationMessageClient {


		@Override
		public ServerResponse uploadAreaList(List<VibrationArea> vibrationAreaList) {
			return ServerResponse.createByErrorMessage("Busy service about Message/VibrationMessageClient/uploadAreaList().");
		}

		@Override
		public ServerResponse uploadAreaListStr(String vibrationAreaListStr) {
			return ServerResponse.createByErrorMessage("Busy service about Message/VibrationMessageClient/uploadAreaListStr().");
		}

		@Override
		public ServerResponse uploadConfigList(List<VibrationDevConfig> vibrationDevConfigList) {
			return ServerResponse.createByErrorMessage("Busy service about Message/VibrationMessageClient/uploadConfigList().");
		}

		@Override
		public ServerResponse uploadConfigListStr(String inclinationConfigListStr) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadConfigListStr().");
		}
	}
}
