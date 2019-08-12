package com.renewable.terminal.message.client;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@FeignClient(name = "message", fallback = InclinationMessageClient.InclinationMessageClientFallback.class)
public interface InclinationMessageClient {

	@PostMapping("/message/inclination/upload_init_list.do")
	ServerResponse uploadInitList(@RequestBody List<InclinationInit> inclinationInitList);

	@PostMapping("/message/inclination/upload_init_list_str.do")
	ServerResponse uploadInitListStr(String inclinationInitListStr);

	@PostMapping("/message/inclination/upload_total_list.do")
	ServerResponse uploadTotalList(@RequestBody List<InclinationTotal> inclinationTotalList);

	@PostMapping("/message/inclination/upload_total_list_str.do")
	ServerResponse uploadTotalListStr(String inclinationTotalListStr);

	@PostMapping("/message/inclination/upload_config_list.do")
	ServerResponse uploadConfigList(@RequestBody List<InclinationConfig> inclinationConfigList);

	@PostMapping("/message/inclination/upload_config_list_str.do")
	ServerResponse uploadConfigListStr(String inclinationConfigListStr);

	@Component
	class InclinationMessageClientFallback implements InclinationMessageClient {

		@Override
		public ServerResponse uploadInitList(@RequestBody List<InclinationInit> inclinationInitList) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadInitList().");
		}

		@Override
		public ServerResponse uploadInitListStr(String inclinationInitListStr) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadInitListStr().");
		}

		@Override
		public ServerResponse uploadTotalList(List<InclinationTotal> inclinationTotalList) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadTotalList().");
		}

		@Override
		public ServerResponse uploadTotalListStr(String inclinationTotalListStr) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadTotalListStr().");
		}

		@Override
		public ServerResponse uploadConfigList(List<InclinationConfig> inclinationConfigList) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadConfigList().");
		}

		@Override
		public ServerResponse uploadConfigListStr(String inclinationConfigListStr) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadConfigListStr().");
		}
	}
}
