package com.renewable.terminal.vibration.service.impl;

//import com.renewable.terminal.message.client.VibrationMessageClient;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;
import com.renewable.terminal.vibration.entity.VibrationOrigin;
import com.renewable.terminal.vibration.extend.USBDLL;
import com.renewable.terminal.vibration.service.IVibrationDLLService;
import com.renewable.terminal.vibration.service.IVibrationDevConfigService;
import com.renewable.terminal.vibration.service.IVibrationOriginService;
import com.renewable.terminal.vibration.service.IVibrationPeakService;
import com.renewable.terminal.vibration.util.CheckDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.renewable.terminal.vibration.constant.DLLConstant.DLL_AD_SINGLE_ARRAY_SIZE;

/**
 * @Description：
 * @Author: jarry
 */
@Service
@Slf4j
public class VibrationDLLServiceImpl implements IVibrationDLLService {

	@Autowired
	private IVibrationDevConfigService iVibrationDevConfigService;

	@Autowired
	private IVibrationOriginService iVibrationOriginService;

	@Autowired
	private IVibrationPeakService iVibrationPeakService;

//	@Autowired
//	private VibrationMessageClient vibrationMessageClient;

	@Override
	public ServerResponse readAdContinueData() {
		// 获取配置
		// 这里增加一个条件，只挑选status正常的。这里先固定写0，后面设置常量
//		Wrapper<VibrationDevConfig> statusWrapper = new QueryWrapper<VibrationDevConfig>().eq("status", 0);
		List<VibrationDevConfig> vibrationDevConfigList = iVibrationDevConfigService.list();
		ServerResponse checkListResponse = CheckDataUtil.checkData(vibrationDevConfigList, "oversamplingRate", "meterRange", "samplingFrequency", "triggerModel", "triggerPolarity", "clockModel", "extendClockPolarity", "number");
		if (checkListResponse.isFail()) {
			return checkListResponse;
		}

		// 2.针对每个配置去读取读取数据
		for (VibrationDevConfig vibrationDevConfig : vibrationDevConfigList) {
			// 数据校验
			ServerResponse response = CheckDataUtil.checkData(vibrationDevConfig, "terminalId", "devId");

			// 2.1 获取数据
			ServerResponse<float[]> adContinueResponse = this.readAdContinueDataSingle(vibrationDevConfig);
			if (adContinueResponse.isFail()) {
				continue;
			}
			float[] resultArray = adContinueResponse.getData();

			// 2.2 处理数据
			String terminalId = vibrationDevConfig.getTerminalId();
			int devId = vibrationDevConfig.getDevId();
			String devConfigId = vibrationDevConfig.getId();
			ServerResponse<List<VibrationOrigin>> vibrationArrayDealResponse = this.adArrayDeal(terminalId, devId, devConfigId, resultArray);
			if (vibrationArrayDealResponse.isFail()) {
				return vibrationArrayDealResponse;
			}

			// 2.3 数据保存
			List<VibrationOrigin> vibrationOriginList = vibrationArrayDealResponse.getData();
			boolean result = iVibrationOriginService.saveBatch(vibrationOriginList);
			if (!result) {
				continue;
			}


			// 2.4 数据进行peak算法处理
			ServerResponse peakResponse = iVibrationPeakService.data2PeakCalAndSave(vibrationOriginList);
			if (peakResponse.isFail()){
				log.warn(peakResponse.getMsg());
				return peakResponse;
			}

			//2.4 数据发往中控室
//			vibrationMessageClient.uploadAreaList(vibrationAreaList);
		}

		// 3.返回成功返回
		return ServerResponse.createBySuccess();
	}

	private ServerResponse<float[]> readAdContinueDataSingle(VibrationDevConfig vibrationDevConfig) {

		int devId = vibrationDevConfig.getDevId();
		int oversamplingRate = vibrationDevConfig.getOversamplingRate();
		int range = vibrationDevConfig.getMeterRange();
		int samplingFrequency = vibrationDevConfig.getSamplingFrequency();
		int triggerModel = vibrationDevConfig.getTriggerModel();
		int triggerPolarity = vibrationDevConfig.getTriggerPolarity();
		int clockModel = vibrationDevConfig.getClockModel();
		int extendClockPolarity = vibrationDevConfig.getExtendClockPolarity();
		int number = vibrationDevConfig.getNumber();

		USBDLL.USBDLLUtil.openUSB();
		ServerResponse<float[]> adContinueResponse = USBDLL.USBDLLUtil.adContinue(devId, oversamplingRate, range, samplingFrequency, triggerModel, triggerPolarity, clockModel, extendClockPolarity, number);
		USBDLL.USBDLLUtil.closeUSB();
		return adContinueResponse;
	}

	private ServerResponse<List<VibrationOrigin>> adArrayDeal(String terminalId, int devId, String devConfigId, float[] originArray) {

		// 1.数据校验


		// 2.新建List
		List<VibrationOrigin> vibrationOriginList = new ArrayList<>();

		for (int i = 0; i < originArray.length; i++) {
			int passageWayCode = i % DLL_AD_SINGLE_ARRAY_SIZE;

			VibrationOrigin vibrationOrigin = new VibrationOrigin();

			vibrationOrigin.setDevConfigId(devConfigId);
			vibrationOrigin.setTerminalId(terminalId);
			vibrationOrigin.setDevId(devId);
			vibrationOrigin.setPassagewayCode(passageWayCode);
			vibrationOrigin.setVibratingValue(originArray[i]);

			vibrationOriginList.add(vibrationOrigin);
		}

		return ServerResponse.createBySuccess(vibrationOriginList);
	}
}
