package com.renewable.terminal.inclination.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import com.renewable.terminal.inclination.constant.InclinationConfigConstant;
import com.renewable.terminal.inclination.dao.InclinationConfigMapper;
import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.service.IInclinationConfigService;
import com.renewable.terminal.inclination.util.CheckDataUtil;
import com.renewable.terminal.inclination.util.MatlabUtil;
import com.renewable.terminal.inclination.util.OtherUtil;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
@Service
public class InclinationConfigServiceImpl extends ServiceImpl<InclinationConfigMapper, InclinationConfig> implements IInclinationConfigService {



	// 计算中间变量X与Y
	@Override
	public ServerResponse<InclinationConfig> calXAndY(InclinationConfig inclinationConfig) {
		// 数据校验以及数据组装
		CheckDataUtil.checkData(inclinationConfig,"radius","initH1","initAngle1","initH2","initAngle2","initH3","initAngle3","initH4","initAngle4");
		double[][] singlePlaneArray = new double[4][2];
		double radius;
//		if (inclinationConfig.getInitH1() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.InitH1 is null !");
//		}
//		double initH1 = inclinationConfig.getInitH1();
		singlePlaneArray[0][0] = inclinationConfig.getInitH1();
//		if (inclinationConfig.getInitAngle1() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.InitAngle1 is null !");
//		}
		singlePlaneArray[0][1] = inclinationConfig.getInitAngle1();

//		if (inclinationConfig.getInitH2() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.InitH2 is null !");
//		}
		singlePlaneArray[1][0] = inclinationConfig.getInitH2();
//		if (inclinationConfig.getInitAngle2() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.InitAngle2 is null !");
//		}
		singlePlaneArray[1][1] = inclinationConfig.getInitAngle2();

//		if (inclinationConfig.getInitH3() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.InitH3 is null !");
//		}
		singlePlaneArray[2][0] = inclinationConfig.getInitH3();
//		if (inclinationConfig.getInitAngle3() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.InitAngle3 is null !");
//		}
		singlePlaneArray[2][1] = inclinationConfig.getInitAngle3();

//		if (inclinationConfig.getInitH4() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.InitH4 is null !");
//		}
//		if (inclinationConfig.getInitAngle4() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.InitAngle4 is null !");
//		}
		singlePlaneArray[3][0] = inclinationConfig.getInitH4();
		singlePlaneArray[3][1] = inclinationConfig.getInitAngle4();

//		if (inclinationConfig.getRadius() == null) {
//			return ServerResponse.createByErrorMessage("inclinationConfig.Radius is null !");
//		}
		radius = inclinationConfig.getRadius();

		//要将三个点按高程从小到大排序
		singlePlaneArray = OtherUtil.bubbleSort(singlePlaneArray);  //默认是按0位排序的 //这里采用的是冒泡排序，之后可以改进

		//提取出需要的参数  //其实可以直接写，但为了后来者可以看懂，还是分开写吧。
		double p1h = singlePlaneArray[0][0];
		double p1angel = singlePlaneArray[0][1];
		double p2h = singlePlaneArray[1][0];
		double p2angel = singlePlaneArray[1][1];
		double p3h = singlePlaneArray[2][0];
		double p3angel = singlePlaneArray[2][1];

		//将角度转为弧度制。
		p1angel = OtherUtil.angle2radian(p1angel);
		p2angel = OtherUtil.angle2radian(p2angel);
		p3angel = OtherUtil.angle2radian(p3angel);

		double F = Math.sin(p2angel - p3angel)
				+ Math.sin(p3angel - p1angel)
				+ Math.sin(p1angel - p2angel);
		double Fx = (Math.sin(p3angel) - Math.sin(p2angel)) * p1h +
				(Math.sin(p1angel) - Math.sin(p3angel)) * p2h +
				(Math.sin(p2angel) - Math.sin(p1angel)) * p3h;

		double Fy = (Math.cos(p3angel) - Math.cos(p2angel)) * p1h +
				(Math.cos(p1angel) - Math.cos(p3angel)) * p2h +
				(Math.cos(p2angel) - Math.cos(p1angel)) * p3h;

		// 为防止出现F与radius出现0的情况，这里作简单校验
		double X;
		double Y;
		if (radius == 0){
			return ServerResponse.createByErrorMessage("Error: input correct config,please! the radius is zero !");
		}
		if (F == 0){
			if (Fx == 0){
				X = 1;
			}else{
				return ServerResponse.createByErrorMessage("Error: input correct config,please !");
			}
			if (Fy == 0){
				Y = 1;
			}else{
				return ServerResponse.createByErrorMessage("Error: input correct config,please !");
			}
		}else{
			X = Fx / (F * radius);
			Y = Fy / (F * radius);
		}


		// 根据传感器类型，对X与Y进行数据转换
		if (inclinationConfig.getInclinationType() == InclinationConfigConstant.InclinationSerialTypeEnum.T826.getValue()){
			X = X;
			Y = Y;
		}
		if (inclinationConfig.getInclinationType() == InclinationConfigConstant.InclinationSerialTypeEnum.T526.getValue()){
			X = X;
			Y = Y;
		}

		// 组装结果
		inclinationConfig.setInitX(X);
		inclinationConfig.setInitY(Y);

		return ServerResponse.createBySuccess(inclinationConfig);
	}


	// 通过matlab计算初始倾斜角&方向角，以及X与Y
	@Override
	public ServerResponse<InclinationConfig> calInitWithMatlab(InclinationConfig inclinationConfig){

		// 1.数据校验
		CheckDataUtil.checkData(inclinationConfig,"radius","initH1","initAngle1","initH2","initAngle2","initH3","initAngle3","initH4","initAngle4");

		// 2.数据对应参数获取 顺序（1234 angle/h）
		double R = inclinationConfig.getRadius();
		double[][] initMeasuerArray = OtherUtil.assembleMatlabArray(
				inclinationConfig.getInitH1(), inclinationConfig.getInitH2(), inclinationConfig.getInitH3(), inclinationConfig.getInitH4(),
				inclinationConfig.getInitAngle1(), inclinationConfig.getInitAngle2(), inclinationConfig.getInitAngle3(), inclinationConfig.getInitAngle4());


		// 3.数据计算
		Object[] matlabResult = MatlabUtil.initAngleTotalCalMatlab(initMeasuerArray, R, 15.0, 3.1);
//		Object[] matlabResult = MatlabUtil.initAngleTotalCalMatlab(initMeasuerArray, R, inclinationConfig.getInitDirectAngle());
//		Double initTotalAngle = (Double)matlabResult[0];
//		Double initDirectAngle = (Double)matlabResult[1];
//		Double initX = (Double)matlabResult[2];
//		Double initY = (Double)matlabResult[3];


		double initTotalAngle = ((MWNumericArray)matlabResult[0]).getDouble(1);
		double initDirectAngle = ((MWNumericArray)matlabResult[1]).getDouble(1);
		double initX = ((MWNumericArray)matlabResult[2]).getDouble(1);
		double initY = ((MWNumericArray)matlabResult[3]).getDouble(1);

		// 4.数据装填
		InclinationConfig inclinationConfigInit = new InclinationConfig();
		BeanUtils.copyProperties(inclinationConfig, inclinationConfigInit);
		inclinationConfigInit.setInitTotalAngle(initTotalAngle);
		inclinationConfigInit.setInitDirectAngle(initDirectAngle);
		inclinationConfigInit.setInitX(initX);
		inclinationConfigInit.setInitY(initY);

		// 5.返回成功调用
		return ServerResponse.createBySuccess(inclinationConfigInit);
	}
}
