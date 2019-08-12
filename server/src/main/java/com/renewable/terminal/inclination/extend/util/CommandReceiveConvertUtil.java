package com.renewable.terminal.inclination.extend.util;

import com.renewable.terminal.inclination.util.HexUtil;

/**
 * @Description：
 * @Author: jarry
 */
public class CommandReceiveConvertUtil {

	/**
	 * 获取对应数据域中，数据域中数据的解析，并以数组的方式返回
	 *
	 * @param dataAreaStart            数据域开始的位置
	 * @param dataAreaSingleDataLength 数据域中单个数据的长度（这里默认每个数据都是一样长的，一般也确实是这样的）
	 * @param dataAreaSingleDataCount  数据域中单个数据的个数
	 * @param integerLength            数据域中单个数据的整数位长度
	 * @param decimalLength            数据域中单个数据的小数位长度
	 * @param originData               传入的原始数据（二进制数组）
	 * @return 返回数据域中每个数据组成的数组
	 */
	public static double[] originData2Object(int dataAreaStart, int dataAreaSingleDataLength, int dataAreaSingleDataCount, int integerLength, int decimalLength, byte[] originData) {

		double[] dataAreaArray = new double[dataAreaSingleDataCount];
		for (int i = 0; i < dataAreaSingleDataCount; i++) {
			dataAreaArray[i] = origin2Double(integerLength, decimalLength, subBytes(originData, dataAreaStart, dataAreaSingleDataLength));
		}

		return dataAreaArray;
	}

	/**
	 * 获取对应数据域中，数据域中数据的解析，并以数组的方式返回
	 * 表示光速打脸啊。刚说一般情况下数据域的数据都是长度一样的，立马来了一个不一样的。。。不过也算把代码优化得更具扩展性了
	 * @param originData 原始数据数组
	 * @param dataAreaStart 数组中数据域的开始位置
	 * @param dataAreaFormat 数据域中数据的格式。如dataAreayFormat = [[2,4],[2,4],[2,2]]表示输出结果数组由三个数A,B,C组成。A与B的整数位有2位，小数位有4位，而C的整数位有2位，小数位有2位
	 * @return
	 */
	public static double[] originData2Object(byte[] originData, int dataAreaStart, int[][] dataAreaFormat){
		double[] dataAreaArray = new double[dataAreaFormat.length];
		int subDataStart = dataAreaStart;
		for (int i = 0; i < dataAreaArray.length; i++){

			// 计算出数据域中每个数据在originArray中的起点位置
			if (i != 0){
				subDataStart = subDataStart + dataAreaFormat[i-1][0] + dataAreaFormat[i-1][1];
			}

			// 计算出当前计算的数据的数据长度（最后千万别忘了每个数据都有一个符号位，需要占据一个位置）
			int subDataLength = dataAreaFormat[i][0] + dataAreaFormat[i][1] + 1;
			dataAreaArray[i] = origin2Double(dataAreaFormat[i][0],dataAreaFormat[i][1], subBytes(originData, subDataStart, subDataLength));
		}
		return dataAreaArray;
	}

	/**
	 * 实现了不同数据域情况下，数据域中单个数据的解析
	 *
	 * @param originData
	 * @return
	 */
	private static Double origin2Double(int integerLength, int decimalLength, byte[] originData) {
		byte[] originDataSplit = arraySplitByInt(bcdArray2intArray(originData));
		double result = 0;
//		System.out.println(Arrays.toString(originDataSplit));
		// 数据域中单个数据的符号位长度（一般为1，我在想这个有没有必要写出来啊，一般也就1吧）。最终决定，将其作为默认值。日后需要，再扩展吧
		int signedLength = 1;

		//数据符号与高位
		int signedValue = (originDataSplit[0] >= 1) ? -1 : 1;
		//整数域部分
		int integerValue = HexUtil.bcd2int(subBytes(originDataSplit, signedLength, integerLength));
		//小数域
		int decimalValue = HexUtil.bcd2int(subBytes(originDataSplit, signedLength + integerLength, decimalLength));

		result = signedValue * (integerValue + decimalValue * Math.pow(10, -decimalLength));
		return result;
	}

	/**
	 * bac解码。将bcd值组成的数组转为正常的int数组
	 *
	 * @param bcdData
	 * @return
	 */
	private static int[] bcdArray2intArray(byte[] bcdData) {
		int[] resultArray = new int[bcdData.length];

		for (int i = 0; i < resultArray.length; i++) {
			resultArray[i] = Byte.toUnsignedInt(bcdData[i]);
		}

		return resultArray;
	}

	/**
	 * 进行数组切割
	 *
	 * @param source
	 * @param begin
	 * @param count
	 * @return
	 */
	private static byte[] subBytes(byte[] source, int begin, int count) {
		byte[] bs = new byte[count];
		try {
			System.arraycopy(source, begin, bs, 0, count);
		} catch (Exception e) {
			System.out.println("CommandReceiveUtil/subBytes执行异常：" + e.toString());
		}

		return bs;
	}

	/**
	 * bcd转码
	 *
	 * @param src
	 * @return
	 */
	private static byte[] arraySplitByInt(int[] src) {
		int length = src.length;
		byte[] bs = new byte[length * 2];
		for (int i = 0; i < length; i++) {
			bs[i * 2] = (byte) ((src[i] / 16) & 0xff);
			bs[i * 2 + 1] = (byte) (src[i] % 16 & 0xff);
		}
		return bs;
	}
}
