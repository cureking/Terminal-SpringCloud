package com.renewable.terminal.terminal.client;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@FeignClient(name = "terminal", fallback = TerminalClient.TerminalClientFallback.class)

public interface TerminalClient {

//	@PostMapping("/product/listForOrder")
//	// 下面使用了@RequestBod，所以得用@PostMapping
//	public List<ProductInfoOutput> listForOrder(@RequestBody List<String> productIdList);


	// 获取terminal配置（在缓存获取失败时。也不对啊，直接调用初始化程序就OK了啊）	// 这条不用写
	// 更新terminal配置
	// 初始化terminal配置
	@PostMapping("/terminal/update_from_center.do")
	ServerResponse updateTerminalFromCenter(@RequestBody Terminal terminal);

	@PostMapping("/terminal/update.do")
	ServerResponse updateTerminal(@RequestBody Terminal terminal);

	@GetMapping("/terminal/refresh.do")
	ServerResponse refreshTerminal();

	@Component
	public static class TerminalClientFallback implements TerminalClient {
		@Override
		public ServerResponse updateTerminalFromCenter(@RequestBody Terminal terminal){
			return ServerResponse.createByErrorMessage("Busy service about Terminal/updateTerminalFromCenter().");
		}

		@Override
		public ServerResponse updateTerminal(Terminal terminal) {
			return ServerResponse.createByErrorMessage("Busy service about Terminal/updateTerminal().");
		}

		@Override
		public ServerResponse refreshTerminal(){
			return ServerResponse.createByErrorMessage("Busy service about Terminal/refreshTerminal().");
		}
	}
}
