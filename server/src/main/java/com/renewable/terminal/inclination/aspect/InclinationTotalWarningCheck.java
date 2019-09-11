package com.renewable.terminal.inclination.aspect;

import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.inclination.entity.InclinationWarning;
import com.renewable.terminal.inclination.service.IInclinationConfigService;
import com.renewable.terminal.inclination.service.IInclinationTotalService;
import com.renewable.terminal.inclination.service.IInclinationWarningService;
import com.renewable.terminal.inclination.util.CheckDataUtil;
import com.renewable.terminal.terminal.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@Component
@Aspect
@Slf4j
public class InclinationTotalWarningCheck {

	@Autowired
	private IInclinationConfigService iInclinationConfigService;

	@Autowired
	private IInclinationTotalService inclinationTotalService;

	@Autowired
	private IInclinationWarningService iInclinationWarningService;


	@Around("@annotation(com.renewable.terminal.inclination.annotation.InclinationTotalWarning)")
	// 说实话，这个切面写得挺糟糕的，没有用反射，还引入了一堆服务。不过这只是一个demo，后面可以改进
	public ServerResponse<InclinationTotal> doInclinationTotalWarningcheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		// doBefore
		// do
		ServerResponse<InclinationTotal> result = (ServerResponse<InclinationTotal>) proceedingJoinPoint.proceed();
		if (result.isFail()) {
			return result;
		}

		InclinationTotal inclinationTotal = result.getData();
		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationTotal, "inclinationId", "terminalId");
		if (checkResponse.isFail()) {
			return result;
		}
		Integer address = inclinationTotal.getInclinationId();

		// doAfter 插入数据后，获取最后两条数据
		ServerResponse<List<InclinationTotal>> inclinationTotalListResponse = inclinationTotalService.getLastByCount(address, 2);
		if (inclinationTotalListResponse.isFail()) {
			return result;
		}
		List<InclinationTotal> inclinationTotalList = (List<InclinationTotal>) inclinationTotalListResponse.getData();
		ServerResponse checkListResponse = CheckDataUtil.checkData(inclinationTotalList, "angleTotal");
		if (checkListResponse.isFail()) {
			return result;
		}

		// 计算，获取warning逻辑
		InclinationConfig inclinationConfig = iInclinationConfigService.getById(address);
		ServerResponse checkConfigResponse = CheckDataUtil.checkData(inclinationConfig, "totalAngleLimit");
		if (checkConfigResponse.isFail()) {
			return result;
		}

		if (inclinationTotalList.size() < 2) {
			return result;
		}

		if (inclinationTotalList.get(0).getAngleTotal() > inclinationConfig.getTotalAngleLimit() && inclinationTotalList.get(1).getAngleTotal() > inclinationConfig.getTotalAngleLimit()) {
			// TODO 新建warning，并持久化
			InclinationWarning inclinationWarning = new InclinationWarning();
			inclinationWarning.setInclinationId(address);
			inclinationWarning.setTerminalId(inclinationTotal.getTerminalId());
			inclinationWarning.setOriginId(inclinationTotal.getOriginId());
			inclinationWarning.setMark("InclinationTotal");

			log.warn("warning about inclinationTotal with inclinationTotalId:{}!", inclinationTotal.getId());
			iInclinationWarningService.saveAndUpload(inclinationWarning);
		}

		// 返还控制权，返回正确返回
		return result;
	}

}
