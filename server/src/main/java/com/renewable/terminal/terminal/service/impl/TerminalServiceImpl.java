package com.renewable.terminal.terminal.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renewable.terminal.message.client.TerminalMessageClient;
import com.renewable.terminal.terminal.common.RedisTemplateUtil;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import com.renewable.terminal.terminal.dao.TerminalMapper;
import com.renewable.terminal.terminal.service.ITerminalService;
import com.renewable.terminal.terminal.util.JsonUtil;
import com.renewable.terminal.terminal.util.NetIndentificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.renewable.terminal.terminal.common.TerminalConstant.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
@Service
@Slf4j
public class TerminalServiceImpl extends ServiceImpl<TerminalMapper, Terminal> implements ITerminalService {

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	@Autowired
	private TerminalMessageClient terminalMessageClient;

	@Override
	public ServerResponse<Terminal> getTerminal() {
		//		Wrapper<AudioAmnout> audioAmnoutWrapper = new QueryWrapper<AudioAmnout>().between("create_time",startTime,endTime);
		Wrapper<Terminal> terminalWrapper = new QueryWrapper<Terminal>().orderByDesc("create_time").last("limit 1");
		Terminal terminal = this.getOne(terminalWrapper);
		if (terminal == null){
			return ServerResponse.createByErrorMessage("fail");
		}
		return ServerResponse.createBySuccess(terminal);
	}

	@Override
	public ServerResponse refresh(){
		// 1.判断是否已经有terminal信息
		int countTerminal = this.count();
		if (countTerminal == 0){
			// 2.如果没有就调用this.init方法
			ServerResponse initResponse = this.init();
			if (initResponse.isFail()){
				return initResponse;
			}
		}

		// 3.读取读取信息
		ServerResponse terminalResponse = this.getTerminal();
		if (terminalResponse.isFail()){
			return ServerResponse.createByErrorMessage("terminalResponse is fail !");
		}
		Terminal terminalPull = (Terminal)terminalResponse.getData();
		if (terminalPull == null){
			return ServerResponse.createByErrorMessage("terminalPull is null !");
		}

		// 4.更新cache
		ServerResponse updateCacheResponse = this.updateCache();
		if (updateCacheResponse.isFail()){
			return updateCacheResponse;
		}

		// 5.发送远程中控室
		terminalMessageClient.uploadTerminal(terminalPull);

		// 6.返回成功响应
		return ServerResponse.createBySuccess(terminalPull);
	}



	private ServerResponse init() {
		// 1.新建terminal
		Terminal terminalInit = this.terminalGenerator();

		// 2.插入数据库
		boolean result = this.save(terminalInit);
		if (!result){
			ServerResponse.createByErrorMessage("terminalInit fail !");
		}

		// 3.发往中控室

		// 3.返回成功响应（这里返回的Terminal的时间不够准确）
		log.info("初始化终端配置：{}。",JsonUtil.obj2String(terminalInit));
		return ServerResponse.createBySuccess(terminalInit);
	}

	private ServerResponse updateCache(){
		// 1.判断数据库是否存在terminal
		int countTerminal = this.count();
		if (countTerminal == 0){
			// 2.无则返回错误
			return ServerResponse.createByErrorMessage("there is no terminalconfig in database !");
		}

		// 3.有则获取数据
		ServerResponse terminalResponse = this.getTerminal();
		if (terminalResponse.isFail()){
			return ServerResponse.createByErrorMessage("terminalResponse is fail !");
		}
		Terminal terminalPull = (Terminal)terminalResponse.getData();
		if (terminalPull == null){
			return ServerResponse.createByErrorMessage("terminalPull is null !");
		}

		// 4.更新缓存
		String terminalStr = JsonUtil.obj2String(terminalPull);
		redisTemplateUtil.set(GLOBAL_TERMINAL_CONFIG, terminalStr);
		redisTemplateUtil.set(GLOBAL_TERMINAL_ID, terminalPull.getId());
		redisTemplateUtil.set(GLOBAL_TERMINAL_MAC, terminalPull.getMac());
		redisTemplateUtil.set(GLOBAL_TERMINAL_IP, terminalPull.getIp());
		redisTemplateUtil.set(GLOBAL_TERMINAL_NAME, terminalPull.getName());

		return ServerResponse.createBySuccess();
	}

	private Terminal terminalGenerator(){
		Terminal terminalInit = new Terminal();
		terminalInit.setId(UUID.randomUUID().toString());
		terminalInit.setIp(NetIndentificationUtil.getLocalIP());
		terminalInit.setMac(NetIndentificationUtil.getLocalMac());
		String nameInit = "No named";
		terminalInit.setName(nameInit);
		terminalInit.setState(TerminalStateEnum.Running.getCode());

		return terminalInit;
	}
}
