package com.renewable.terminal.message.client;

import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.inclination.entity.InclinationWarning;
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
@FeignClient(name = "message", fallback = InclinationWarningMessageClient.InclinationWarningMessageClientFallback.class)
public interface InclinationWarningMessageClient {

	@PostMapping("/message/inclination_warning/upload_warning_list.do")
	ServerResponse uploadInclinationWarningList(@RequestBody List<InclinationWarning> inclinationWarningList);

	@PostMapping("/message/inclination_warning/upload_warning_list_str.do")
	ServerResponse uploadInclinationWarningListStr(String inclinationWarningListStr);

	@Component
	class InclinationWarningMessageClientFallback implements InclinationWarningMessageClient {

		@Override
		public ServerResponse uploadInclinationWarningList(List<InclinationWarning> inclinationWarningList) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadInclinationWarningList().");
		}

		@Override
		public ServerResponse uploadInclinationWarningListStr(String inclinationWarningListStr) {
			return ServerResponse.createByErrorMessage("Busy service about Message/InclinationMessageClient/uploadInclinationWarningListStr().");
		}
	}
}
