package com.renewable.terminal.inclination.aspect;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationWarning;
import com.renewable.terminal.inclination.service.IInclinationConfigService;
import com.renewable.terminal.inclination.service.IInclinationInitService;
import com.renewable.terminal.inclination.service.IInclinationWarningService;
import com.renewable.terminal.inclination.util.CheckDataUtil;
import com.renewable.terminal.terminal.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Component
@Aspect
@Slf4j
public class InclinationInitWarningCheck {

	@Autowired
	private IInclinationConfigService iInclinationConfigService;

	@Autowired
	private IInclinationInitService iInclinationInitService;

	@Autowired
	private IInclinationWarningService iInclinationWarningService;


	@Around("@annotation(com.renewable.terminal.inclination.annotation.InclinationInitWarning)")
	// 说实话，这个切面写得挺糟糕的，没有用反射，还引入了一堆服务。不过这只是一个demo，后面可以改进
	public ServerResponse<InclinationInit> doInclinationInitWarningcheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		// doBefore
		// do
		ServerResponse<InclinationInit> result = (ServerResponse<InclinationInit>) proceedingJoinPoint.proceed();
		if (result.isFail()) {
			return result;
		}

		InclinationInit inclinationInit = result.getData();
		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationInit, "inclinationId", "terminalId");
		if (checkResponse.isFail()) {
			return result;
		}
		Integer address = inclinationInit.getInclinationId();

		// doAfter 插入数据后，获取最后两条数据
		ServerResponse<List<InclinationInit>> inclinationInitListResponse = iInclinationInitService.getLastByCount(address, 2);
		if (inclinationInitListResponse.isFail()) {
			return result;
		}
		List<InclinationInit> inclinationInitList = (List<InclinationInit>) inclinationInitListResponse.getData();
		ServerResponse checkListResponse = CheckDataUtil.checkData(inclinationInitList, "angleInitTotal");
		if (checkListResponse.isFail()) {
			return result;
		}

		// 计算，获取warning逻辑
		InclinationConfig inclinationConfig = iInclinationConfigService.getById(address);
		ServerResponse checkConfigResponse = CheckDataUtil.checkData(inclinationConfig, "totalInitAngleLimit");
		if (checkConfigResponse.isFail()) {
			return result;
		}

		if (inclinationInitList.size() < 2) {
			return result;
		}

		if (inclinationInitList.get(0).getAngleInitTotal() > inclinationConfig.getTotalInitAngleLimit() && inclinationInitList.get(1).getAngleInitTotal() > inclinationConfig.getTotalInitAngleLimit()) {
			// TODO 新建warning，并持久化
			InclinationWarning inclinationWarning = new InclinationWarning();
			inclinationWarning.setInclinationId(address);
			inclinationWarning.setTerminalId(inclinationInit.getTerminalId());
			inclinationWarning.setOriginId(inclinationInit.getOriginId());
			inclinationWarning.setMark("InclinationInit");

			log.warn("warning about inclinationInit with inclinationInitId:{}!", inclinationInit.getId());
			iInclinationWarningService.saveAndUpload(inclinationWarning);
		}

		// 返还控制权，返回正确返回
		return result;
	}

}
