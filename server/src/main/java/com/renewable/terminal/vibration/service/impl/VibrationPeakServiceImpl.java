package com.renewable.terminal.vibration.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mathworks.toolbox.javabuilder.MWException;
import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.dao.VibrationPeakMapper;
import com.renewable.terminal.vibration.dto.SensorGroupInfo;
import com.renewable.terminal.vibration.entity.VibrationOrigin;
import com.renewable.terminal.vibration.entity.VibrationPeak;
import com.renewable.terminal.vibration.extend.VibrationMatlabUtil;
import com.renewable.terminal.vibration.service.IVibrationDevConfigService;
import com.renewable.terminal.vibration.service.IVibrationPeakService;
import com.renewable.terminal.vibration.service.IVibrationSensorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.renewable.terminal.vibration.constant.DLLConstant.DEVIDE_GROUP_ARRAY_SIZE;
import static com.renewable.terminal.vibration.constant.DLLConstant.DLL_AD_SINGLE_ARRAY_SIZE;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-08-09
 */
@Service
@Slf4j
public class VibrationPeakServiceImpl extends ServiceImpl<VibrationPeakMapper, VibrationPeak> implements IVibrationPeakService {

	@Autowired
	private IVibrationSensorConfigService iVibrationSensorConfigService;
	@Autowired
	private IVibrationDevConfigService iVibrationDevConfigService;

	@Override
	public ServerResponse data2PeakCalAndSave(List<VibrationOrigin> vibrationOriginList) {
		// 1.数据校验
		if (CollectionUtils.isEmpty(vibrationOriginList)) {
			return ServerResponse.createByErrorMessage("vibrationOriginList is empty !");
		}

		// 2.对数据进行分组
		Map<Integer, Double[][]> originDataWithGroupMap = this.grouping(vibrationOriginList);

		// 3.对分组数据进行处理
		for (Integer groupId : originDataWithGroupMap.keySet()) {

		}
		for (Map.Entry<Integer, Double[][]> integerEntry : originDataWithGroupMap.entrySet()) {
			Integer groupId = integerEntry.getKey();
			Double[][] originDataArray = integerEntry.getValue();
			// 调用算法处理方法
			String devConfigId = iVibrationDevConfigService.getDevConfigIdFromDevConfig();
			ServerResponse response = this.data2PeakCalAndSaveByGroup(originDataArray,groupId,devConfigId);
			if (response.isFail()){
				log.warn(response.getMsg());
			}
		}

		// 5.返回成功响应
		return ServerResponse.createBySuccessMessage("data has dealed");
	}

	/**
	 * 对原始数据进心分组
	 * @param vibrationOriginList Map<groupId,Double[groupNumber(0/1)][具体数据]
	 * @return
	 */
	private Map<Integer, Double[][]> grouping(List<VibrationOrigin> vibrationOriginList){

		// 建立容器，用于存放处理结果。Map<groupId,Double[0/1需要输入的data][具体数据]
		Map<Integer, Double[][]> originDataWithGroupMap = new HashMap<>();
		int divideGroupOriginListSize = vibrationOriginList.size()/DLL_AD_SINGLE_ARRAY_SIZE;

		// 获取groupId的数组
		int[] groupIdArray = iVibrationSensorConfigService.listGroupId();

		for (int i = 0; i < groupIdArray.length; i++){
			Double[][] originDatadivideByGroup = new Double[DEVIDE_GROUP_ARRAY_SIZE][divideGroupOriginListSize];
			originDataWithGroupMap.put(groupIdArray[i],originDatadivideByGroup);
		}

		// 获取分组配置 Map<passageCode,Integer[groupId,groupNumber]>。
		Map<Integer, Integer[]> groupInfoMap = iVibrationSensorConfigService.mapGroupInfo();

		// 下列代码取消，因为我认为分组数据的大小是可以确定的
//		// 根据groupInfoMap的大小，动态建立原始数据分组列表（数组不可以动态添加数据）
//		Map<Integer, List<VibrationOrigin>> originDataWithPassageWayCodeMap = new HashMap<>();
//		for (int i = 1; i < groupInfoMap.size(); i++){
//			originDataWithPassageWayCodeMap.put();
//		}

		// 预分组
		Map<Integer, List<Double>[]> preOriginDataWithGroupMap = new HashMap<>();
		for (int i = 0; i < groupIdArray.length; i++){
			List<Double>[] originDataGroupListArray = new List[DEVIDE_GROUP_ARRAY_SIZE];
			for (int j = 0; j < DEVIDE_GROUP_ARRAY_SIZE; j++){
				List<Double> devideGroupList = new ArrayList<>();
				originDataGroupListArray[j] = devideGroupList;
			}
			preOriginDataWithGroupMap.put(groupIdArray[i],originDataGroupListArray);
		}

		// 利用分组信息，循环进行预分组
		for (VibrationOrigin vibrationOrigin : vibrationOriginList) {
			Integer groupId = groupInfoMap.get(vibrationOrigin.getPassagewayCode())[0];
			Integer groupNumber = groupInfoMap.get(vibrationOrigin.getPassagewayCode())[1];
//			int index = ArrayUtils.indexOf(originDataWithGroupMap.get(groupId)[groupNumber],null);
//			originDataWithGroupMap.get(groupId)[groupNumber][index+1] = vibrationOrigin.getVibratingValue();
			// 之所以抛弃ArrayUtils.add()，是因为该方法每次调用都会创建新的对象，对于大量数据的处理，简直是性能噩梦
//			ArrayUtils.add(originDataWithGroupMap.get(groupId)[groupNumber],vibrationOrigin);
			preOriginDataWithGroupMap.get(groupId)[groupNumber].add(Double.valueOf(String.valueOf(vibrationOrigin.getVibratingValue())));
		}

		for (Map.Entry<Integer, List<Double>[]> integerEntry : preOriginDataWithGroupMap.entrySet()) {
			Integer groupId = integerEntry.getKey();
			List<Double>[] groupListArray = integerEntry.getValue();
			for (int i = 0; i < groupListArray.length; i++){
				List<Double> groupList = groupListArray[i];
				originDataWithGroupMap.get(groupId)[i] = groupList.toArray(new Double[divideGroupOriginListSize]);
			}
		}

		return originDataWithGroupMap;
	}

	private ServerResponse data2PeakCalAndSaveByGroup(Double[][] devideGroupOriginDataArray, Integer groupId, String devConfigId) {
		// 1.数据校验
		if (ArrayUtils.isEmpty(devideGroupOriginDataArray)){
			return ServerResponse.createByErrorMessage("devideGroupOriginDataArray is empty !");
		}

		// 2.获取算法所需参数
		SensorGroupInfo sensorGroupInfo = iVibrationSensorConfigService.mapGroupInfoByGroupId(groupId);
		int sensorType = sensorGroupInfo.getSensorType();
		double installMode = sensorGroupInfo.getInstallMode();
		double installAngle = sensorGroupInfo.getInstallAngle();
		String terminalId = sensorGroupInfo.getTerminalId();
		double sampling = iVibrationDevConfigService.getSamplingFromDevConfig(devConfigId);

		// 3.调用算法
		Object[] result = null;
		try {
			result = VibrationMatlabUtil.calMaxArrayWithTimeArea(devideGroupOriginDataArray, sensorType,sampling,installMode,installAngle);
		} catch (MWException e) {
			log.warn("MWException:{}",e.toString());
		}
		if (result == null){
			log.warn("matlab call fail !");
			return ServerResponse.createByErrorMessage("matlab call fail !");
		}

		// 4.组装数据
		double displacement = VibrationMatlabUtil.getDoubleFromMWNumericArray(result,0,1);
		double displacementDirect = VibrationMatlabUtil.getDoubleFromMWNumericArray(result,0,2);
		double speed = VibrationMatlabUtil.getDoubleFromMWNumericArray(result,1,1);
		double speedDirect = VibrationMatlabUtil.getDoubleFromMWNumericArray(result,1,2);
		double accelerate = VibrationMatlabUtil.getDoubleFromMWNumericArray(result,2,1);
		double accelerateDirect = VibrationMatlabUtil.getDoubleFromMWNumericArray(result,2,2);

		VibrationPeak vibrationPeak = new VibrationPeak();
		vibrationPeak.setGroupId(groupId);
		vibrationPeak.setDevConfigId(devConfigId);
		vibrationPeak.setTerminalId(terminalId);
		vibrationPeak.setDisplacement(displacement);
		vibrationPeak.setDisplacementDirect(displacementDirect);
		vibrationPeak.setSpeed(speed);
		vibrationPeak.setSpeedDirect(speedDirect);
		vibrationPeak.setAccelerate(accelerate);
		vibrationPeak.setAccelerateDirect(accelerateDirect);
		vibrationPeak.setMark("test");

		// 5.保存数据
		boolean saveResult = this.save(vibrationPeak);
		if (!saveResult){
			return ServerResponse.createByErrorMessage("save fail !");
		}

		// 6.返回成功响应
		return ServerResponse.createBySuccess(vibrationPeak);
	}

}
