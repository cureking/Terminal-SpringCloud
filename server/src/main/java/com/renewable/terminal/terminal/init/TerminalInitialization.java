package com.renewable.terminal.terminal.init;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.service.ITerminalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description：
 * @Author: jarry
 */
@Component
@Slf4j
public class TerminalInitialization {
	@Autowired
	private ITerminalService iTerminalService;

	@PostConstruct
	public ServerResponse init(){
		ServerResponse initResponse = iTerminalService.refresh();
		if (initResponse.isFail()){
			log.error("terminal init fail :{}",initResponse.getMsg());
			return initResponse;
		}
		log.info("terminal init success :{}",initResponse.getMsg());
		return ServerResponse.createByErrorMessage("terminal init success .");
	}
}
