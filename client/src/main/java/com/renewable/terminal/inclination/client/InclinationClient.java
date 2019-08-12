package com.renewable.terminal.inclination.client;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.terminal.common.ServerResponse;
import jdk.nashorn.internal.ir.Terminal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
		name = "inclination",
		fallback = InclinationClient.InclinationClientFallback.class
)
public interface InclinationClient {

	/**
	 * 从倾斜硬件传感器上读取数据（或者说，发送读取数据指令）
	 * @return
	 */
	@GetMapping("/serial/read_all.do")
	ServerResponse readAll();

	/**
	 * 定时清洗InclinationInit数据
	 * @return
	 */
	@GetMapping("/inclination_init/clean_data_persistence.do")
	ServerResponse cleanData2InitPersistence();

	/**
	 * 定时清洗InclinationTotal数据
	 * @return
	 */
	@GetMapping("/inclination_total/clean_data_persistence.do")
	ServerResponse cleanData2TotalPersistence();

	/**
	 * 从message服务那里获取来自center的配置信息
	 * @return
	 */
	@GetMapping("/inclination_config/update_from_center.do")
	ServerResponse updateConfigFromCenter(@RequestBody InclinationConfig inclinationConfig);



	// 降级服务的class（通过内部class实现，之后需要，也可以分离出去，但是我感觉放在一起挺好的。毕竟现在服务较少）
	@Component
	static class InclinationClientFallback implements InclinationClient{

		@Override
		public ServerResponse readAll() {
			return ServerResponse.createByErrorMessage("Busy service about readAll()");
		}

		@Override
		public ServerResponse cleanData2InitPersistence() {
			return ServerResponse.createByErrorMessage("Busy service about cleanData2InitPersistence()");
		}

		@Override
		public ServerResponse cleanData2TotalPersistence() {
			return ServerResponse.createByErrorMessage("Busy service about cleanData2TotalPersistence()");
		}

		@Override
		public ServerResponse updateConfigFromCenter(InclinationConfig inclinationConfig) {
			return ServerResponse.createByErrorMessage("Busy service about updateConfigFromCenter()");
		}
	}
}