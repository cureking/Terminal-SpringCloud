package com.renewable.terminal.inclination.util;

import com.mathworks.toolbox.javabuilder.MWException;
import sinfit1.sinfit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public class MatlabUtil {

	/**
	 * @param initMeasureArray
	 * @param R
	 * @return 返回的数组，包含四个参数，分别是合倾角，方向角，X，Y。
	 */
	public static Object[] initAngleTotalCalMatlab(double[][] initMeasureArray, double R, Double modeTotalAngle, Double modeType) {
		sinfit calcul = null;
//		Calcul calcul = null;
		try {
//			calcul = new Calcul();
			calcul = new sinfit();
		} catch (MWException e) {
			e.printStackTrace();
		}
		Object[] resultObjectArray = null;
		Object object = null;
//
//		List<Double> dList = new ArrayList<Double>();
//		dList.add(R);
//		dList.add(modeTotalAngle);
//		dList.add(modeType);

		int resultLength = 6;

		try {
//			resultObjectArray = calcul.sinfit1(intList,dList);
			resultObjectArray = calcul.sinfit1(resultLength, initMeasureArray[0][0], initMeasureArray[0][1], initMeasureArray[1][0], initMeasureArray[1][1],
					initMeasureArray[2][0], initMeasureArray[2][1], initMeasureArray[3][0], initMeasureArray[3][1], R, modeTotalAngle, modeType);
			System.out.println(Arrays.toString(resultObjectArray));
		} catch (MWException e) {
			e.printStackTrace();
		}

//        double[] resultArray = null;
//        for (int i =0;i<resultObjectArray.length;i++){
////            resultArray[i] = Double.parseDouble(resultObjectArray[i].toString());     //无法将9.0000e+01 - 3.9520e+02i这样的字符串转为对应数值，包括Bigdecimbel也不可以。
////            resultArray[i] = (double) resultObjectArray[i];             //java.lang.ClassCastException: com.mathworks.toolbox.javabuilder.MWNumericArray cannot be cast to java.lang.Double     //实在想不通为什么MWNumericArray的数据结构信息还在，这是matlab的数据结构
//
//            //现在两个思路，一个我去追根溯源，寻找数据类型与MWNumericArray的信息，另一个我去写一个函数用来解析9.0000e+01 - 3.9520e+02i这样的信息。（目前怀疑matlab出来的信息被强（或是主动）转为了String。（他们简单了。我们辛苦了。。。）
//            //可以考虑后者，但在那之前需要构建一个复数类，并了解C#与C++是如何正确解析复数到double的。
//        }
//        MWNumericArray

		return resultObjectArray;
	}
}
