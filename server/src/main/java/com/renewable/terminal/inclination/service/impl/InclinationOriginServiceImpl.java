package com.renewable.terminal.inclination.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.renewable.terminal.inclination.common.RedisTemplateUtil;
import com.renewable.terminal.inclination.constant.InclinationConfigConstant;
import com.renewable.terminal.inclination.dao.InclinationOriginMapper;
import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.entity.InclinationOrigin;
import com.renewable.terminal.inclination.service.IInclinationConfigService;
import com.renewable.terminal.inclination.service.IInclinationInitService;
import com.renewable.terminal.inclination.service.IInclinationOriginService;
import com.renewable.terminal.inclination.service.IInclinationTotalService;
import com.renewable.terminal.inclination.util.CheckDataUtil;
import com.renewable.terminal.inclination.util.OtherUtil;
import com.renewable.terminal.terminal.client.TerminalClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.terminal.entity.Terminal;
import com.renewable.terminal.terminal.exception.RemoteServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.renewable.terminal.terminal.common.ResponseCode.REMOTE_ERROR;
import static com.renewable.terminal.terminal.common.TerminalConstant.GLOBAL_TERMINAL_ID;

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
public class InclinationOriginServiceImpl extends ServiceImpl<InclinationOriginMapper, InclinationOrigin> implements IInclinationOriginService {

	@Autowired
	private IInclinationInitService iInclinationInitService;

	@Autowired
	private IInclinationTotalService iInclinationTotalService;


	@Override
	public ServerResponse listByTimeWithCount(Integer countRequest, String startTime, String endTime) {

		// 1.数据校验
		if (countRequest == null) {
			return ServerResponse.createByErrorMessage("the countRequest is null !");
		}

		// 2.获得总数，并计算ID步长
		Integer countTotal = this.count();
		if (countTotal == null) {
			return ServerResponse.createByErrorMessage("countTotal is null !");
		}

		int countpace = countTotal / countRequest;

		// 2.x 获取在指定日期内的第一个数据
		Wrapper<InclinationOrigin> inclinationOriginWrapper = new QueryWrapper<InclinationOrigin>().between("create_time", startTime, endTime).last("LIMIT 1");
		InclinationOrigin inclinationOriginStart = this.getOne(inclinationOriginWrapper);
		if (inclinationOriginStart == null) {
			return ServerResponse.createByErrorMessage("inclinationOriginStart is null With the startTime: " + startTime + "and endTime:" + endTime + " !");
		}

		// 2.y 获得指定日期内第一个数据的ID
		long idStart = inclinationOriginStart.getId();
		List<Long> idList = Lists.newArrayList();

		// 2.z 组装所求的ID集合
		for (long i = 0; i < countRequest; i++) {
			idList.add(idStart + countpace * i);
		}

		// 3.请求指定ID集合的数据
		List<InclinationOrigin> inclinationOriginList = (List<InclinationOrigin>) this.listByIds(idList);
		if (inclinationOriginList == null || CollectionUtils.isEmpty(inclinationOriginList)) {
			return ServerResponse.createByErrorMessage("inclinationOriginList is null or inclinationOriginList's size is zero !");
		}

		// 4.返回成功响应
		return ServerResponse.createBySuccess(inclinationOriginList);
	}


	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	@Autowired
	private TerminalClient terminalClient;

	@Autowired
	private IInclinationConfigService iInclinationConfigService;

	@Autowired
	private IInclinationOriginService iInclinationOriginService;

	@Override
	public void receiveAndDealOriginData(int address, InclinationOrigin inclinationOrigin) {
		//放入计算后，不含初始角度的合倾角与对应方位角
		double[] resultTDArray = calAngleTotal(inclinationOrigin.getAngleX(), inclinationOrigin.getAngleY());
		inclinationOrigin.setAngleTotal(resultTDArray[0]);
		inclinationOrigin.setDirectAngle(resultTDArray[1]);

		//获取算法三所需参数（调用算法二matlab版）   //todo_finished 其实这里应该是根据port与address的，但是 获取的数据还没有address。。当然上一层的数据是有的，只是没有传下来，之后有时间弄一下。

		InclinationConfig inclinationConfig = iInclinationConfigService.getById(address);


		// 判断初始配置表的状态，决定是否进行计算
		if (inclinationConfig.getRadius() == null ||
				inclinationConfig.getInitH1() == null || inclinationConfig.getInitAngle1() == null || inclinationConfig.getInitH2() == null || inclinationConfig.getInitAngle2() == null ||
				inclinationConfig.getInitH3() == null || inclinationConfig.getInitAngle3() == null || inclinationConfig.getInitH4() == null || inclinationConfig.getInitAngle4() == null ||
				inclinationConfig.getTotalAngleLimit() == null || inclinationConfig.getTotalInitAngleLimit() == null ||
				inclinationConfig.getInitX() == null || inclinationConfig.getInitY() == null) {
			// 以后可以改变表内state字段，不用每次的都判断
			log.error("inclinationConfig with address = " + inclinationConfig.getAddress() + " need input ! please input the initializationInclination");
			return;
		}

		double R = inclinationConfig.getRadius();
		double[][] initMeasuerArray = OtherUtil.assembleMatlabArray(
				inclinationConfig.getInitH1(), inclinationConfig.getInitH2(), inclinationConfig.getInitH3(), inclinationConfig.getInitH4(),
				inclinationConfig.getInitAngle1(), inclinationConfig.getInitAngle2(), inclinationConfig.getInitAngle3(), inclinationConfig.getInitAngle4());


		//开始算法三的计算          //InclinationConst.InclinationInstallModeEnum.FOUR
		double[] resultTDInitArray = this.calInitAngleTotal(inclinationOrigin.getAngleX(), inclinationOrigin.getAngleY(), inclinationConfig.getInitX(), inclinationConfig.getInitY(), inclinationConfig.getInitTotalAngle(), InclinationConfigConstant.InclinationInstallModeEnum.codeOf(1));


		//放入计算后，包含初始角度的合倾角，极其对应的方位角
		inclinationOrigin.setAngleInitTotal(resultTDInitArray[0]);
		inclinationOrigin.setDirectAngleInit(resultTDInitArray[1]);
		//日后需要，这里可以扩展加入方向角  //方向角的计算可以查看ipad上概念化画板/Renewable/未命名8（包含方位角的计算）
		//添加该数据对应的sensorID
		inclinationOrigin.setInclinationId(inclinationConfig.getAddress());
		// 添加该数据对应的状态（尚未进行清洗）
		inclinationOrigin.setStatus(InclinationConfigConstant.InclinationStateEnum.Noclean.getCode());
		// 添加该数据对应的终端编号
		String terminalId = this.getTerminalId();
		inclinationOrigin.setTerminalId(terminalId);

		// 数据持久化操作
		boolean result = iInclinationOriginService.save(inclinationOrigin);
		// 将数据发往清洗池	// TODO 目测这个模块日后会拆分，甚至会通过注解和反射来进行简化，降低耦合度与复杂度
		this.originDataCleanSubmit(inclinationOrigin);
		if (!result) {
			log.warn("inclinationOrigin save to database fail !");
		} else {
			log.info("inclinationOrigin:{} save to database .", inclinationOrigin.toString());
		}
	}

	private double[] calAngleTotal(double angleX, double angleY) {
		double[] angleTotal = null;

		if (angleX == 0 && angleY == 0) {    //这样的条件看起来较为更为清晰，即计算公式无法计算X=Y=0的情况。
			//可以不做处理
		} else {
			angleTotal = OtherUtil.calAngleTotal(angleX, angleY, 0, 1);
		}
		return angleTotal;
	}

	private double[] calInitAngleTotal(double angleX, double angleY, double X, double Y, double angleInitTotal, InclinationConfigConstant.InclinationInstallModeEnum installModeEnum) {
		return OtherUtil.calInitAngleTotal(angleX, angleY, X, Y, angleInitTotal, installModeEnum);

	}

	private String getTerminalId() {
		String terminalId = redisTemplateUtil.get(GLOBAL_TERMINAL_ID);
		if (terminalId == null) {
			Terminal terminal = new Terminal();
			ServerResponse terminalResponse = terminalClient.refreshTerminal();
			if (terminalResponse.isFail()) {
				log.error("ISerialCommandRecieveServcieImpl/getTerminalId:get remote terminal service fail !");
				throw new RemoteServiceException(REMOTE_ERROR.getCode(),REMOTE_ERROR.getDesc());
			}
			terminal = (Terminal) terminalResponse.getData();
			terminalId = terminal.getId();
		}
		return terminalId;
	}

	private ServerResponse originDataCleanSubmit(InclinationOrigin inclinationOrigin) {
		ServerResponse checkResponse = CheckDataUtil.checkData(inclinationOrigin, "angleTotal", "angleInitTotal");
		if (checkResponse.isFail()) {
			return checkResponse;
		}

		iInclinationInitService.originDataSubmit(inclinationOrigin);
		iInclinationTotalService.originDataSubmit(inclinationOrigin);
		return ServerResponse.createBySuccess();
	}
}
