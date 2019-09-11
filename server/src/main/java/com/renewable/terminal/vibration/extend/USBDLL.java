package com.renewable.terminal.vibration.extend;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.sun.jna.*;

import java.util.Arrays;

import static com.renewable.terminal.vibration.constant.DLLConstant.*;
import static com.renewable.terminal.vibration.constant.DLLConstant.DllADRanerTypeEnum.TYPE_0;
import static com.renewable.terminal.vibration.constant.DLLConstant.DllADRanerTypeEnum.TYPE_1;
import static com.renewable.terminal.vibration.constant.DLLConstant.DllExecuteResultTypeEnum.FAIL;
import static com.renewable.terminal.vibration.constant.DLLConstant.DllExecuteResultTypeEnum.SUCCESS;


/**
 * @Description： 注意，该方法目前只有一个INSTANCE的实例，所以，你懂的。
 * @Author: jarry
 */

public class USBDLL {

	// 默认的是继承Library ，如果动态链接库里的函数是以stdcall方式输出的，那么就继承StdCallLibrary
	// 此处继承StdCallLibrary，因为程序的返回都是__stdcall
	private interface CLibrary extends Library {
		// 增加对系统的判断（以后可能移植到linux
		CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? DLL_WINDOWS_FILE_NAME : DLL_LINUX_FILE_NAME), CLibrary.class);

		//		void printf(String format, Object... args);
		int openUSB();

		int closeUSB();

		int get_device_num();

		int Reset_Usb_Device(int dev);

		//		int ad_single(int dev, int ad_os, int ad_range, float*databuf);
		int ad_single(int dev, int ad_os, int ad_range, Pointer databuf);

		//
		int ad_continu_conf(int dev, int ad_os, int ad_range, int freq, int trig_sl, int trig_pol, int clk_sl, int ext_clk_pol);

		int Get_AdBuf_Size(int dev);

		//		int Read_AdBuf(int dev, float*databuf, int num);
		int Read_AdBuf(int dev, Pointer databuf, int num);

		int AD_continu_stop(int dev);

		//		int ad_continu(int dev, int ad_os, int ad_range, int freq, int trig_sl, int trig_pol, int clk_sl, int ext_clk_pol, int num, float*databuf);
		int ad_continu(int dev, int ad_os, int ad_range, int freq, int trig_sl, int trig_pol, int clk_sl, int ext_clk_pol, int num, Pointer databuf);

//		下列方法都是DA等的，暂时不用，之后需要再完善
//		int Pwm_Out(int dev, int ch, int en, int freq, float duty);//ch0--3
//
//		int Pulse_Out(int dev, int ch, int pulse);
//
//		int Set_Pwm_In(int dev, int ch, int en);
//
//		int Read_Pwm_In(int dev, int ch, float*freq, float*duty);
//
//		int Read_Port_In(int dev, unsigned short*in_port);
//
//		int Read_Port_Out(int dev, unsigned short*out_port);
//
//
//		int Write_Port_Out(int dev, unsigned short out_port);
//
//		int Set_Port_Out(int dev, unsigned short out_port);
//
//		int Reset_Port_Out(int dev, unsigned short out_port);
//
//		int Write_Port_OutL(int dev, unsigned char out_port);
//
//		int Write_Port_OutH(int dev, unsigned char out_port);
//
//		int Set_DA_Single(int dev, int ch, float da_value);
	}


	public static class USBDLLUtil {

		/**
		 * 打开设备
		 *
		 * @return ServerResponse
		 */
		public static ServerResponse openUSB() {
			int resultCode = CLibrary.INSTANCE.openUSB();
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			return ServerResponse.createBySuccessMessage(SUCCESS.getDesc());
		}

		/**
		 * 关闭设备
		 *
		 * @return ServerResponse
		 */
		public static ServerResponse closeUSB() {
			int resultCode = CLibrary.INSTANCE.closeUSB();
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			return ServerResponse.createBySuccessMessage(SUCCESS.getDesc());
		}

		/**
		 * 获取设备总数
		 *
		 * @return ServerResponse Data正常返回设备个数
		 */
		public static ServerResponse getDeviceNumber() {
			int resultCode = CLibrary.INSTANCE.get_device_num();
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			return ServerResponse.createBySuccess(resultCode);

		}

		/**
		 * 重置？文档中没有对应接口说明，文档中有：int set_current_device(int num);用于设置当前设备号。暂时不用
		 *
		 * @param devId
		 * @return
		 */
		public static ServerResponse resetUSBDevice(int devId) {
			int resultCode = CLibrary.INSTANCE.Reset_Usb_Device(devId);
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			return ServerResponse.createBySuccess(resultCode);

		}

		/**
		 * @param devId            设备ID 32 位有符号整型参数，子设备号，由系统自动分配，第一个插上电脑的采集设备号为0，第二个为1，以此类推。
		 * @param oversamplingRate 采样频率 32 位有符号整型参数，设定AD 采集的过采样率。
		 * @param range            量程类型 32 位有符号整型参数，设置对应AD量程。 1表示+-10V，分辨率305uv，0表示+-5V，分辨率152uv
		 * @return 同步采集一次并返回一个8 float数据的数组
		 */
		public static ServerResponse<float[]> getAdSingleArray(int devId, int oversamplingRate, int range) {

			// 数据校验
			if (range != TYPE_1.getCode() && range != TYPE_0.getCode()) {
				return ServerResponse.createByErrorMessage("range value error !");
			}

			float[] adResultArray = new float[DLL_AD_SINGLE_ARRAY_SIZE];

			int dev = devId;
			int ad_os = oversamplingRate;
			int ad_range = range;
			int floatArraysize = Native.getNativeSize(Float.class) * DLL_AD_SINGLE_ARRAY_SIZE;
			Pointer floatArrayPointer = new Memory(floatArraysize);
			int executeResult = CLibrary.INSTANCE.ad_single(dev, ad_os, ad_range, floatArrayPointer);
			if (DllExecuteResultTypeEnum.FAIL.getCode() == executeResult) {
				return ServerResponse.createByErrorCodeMessage(DllExecuteResultTypeEnum.FAIL.getCode(), DllExecuteResultTypeEnum.FAIL.getDesc());
			}

			adResultArray = floatArrayPointer.getFloatArray(0, DLL_AD_SINGLE_ARRAY_SIZE);
			return ServerResponse.createBySuccess(adResultArray);
		}

		/**
		 * 连续采样设置，并启动AD 采集
		 *
		 * @param devId               设备ID 32 位有符号整型参数，子设备号，由系统自动分配，第一个插上电脑的采集设备号为0，第二个为1，以此类推。
		 * @param oversamplingRate    采样频率 32 位有符号整型参数，设定AD 采集的过采样率。
		 * @param range               量程类型 32 位有符号整型参数，设置对应AD量程。 1表示+-10V，分辨率305uv，0表示+-5V，分辨率152uv
		 * @param samplingFrequency   32 位有符号整型参数，设置连续采样频率，设置范围100—100000
		 * @param triggerModel        设置触发模式。=0 设置软件启动一次采样过程/=1：设置外部触发启动一次采样过程。
		 * @param triggerPolarity     设置触发输入极性。=0 设置外部触发上升边沿有效/=1 设置外部触发下降边沿有效。
		 * @param clockModel          设置时钟模式。=0 设置AD 启动利用内部时钟/=1：外部时钟。
		 * @param extendClockPolarity 设置外部时钟输入极性。=0 设置上升边沿有效/=1 设置下降边沿有效。
		 * @return ServerResponse 出口参数Data：=0 操作成功/-1 失败。
		 */
		public static ServerResponse getAdContinueConfig(int devId, int oversamplingRate, int range, int samplingFrequency,
														 int triggerModel, int triggerPolarity, int clockModel, int extendClockPolarity) {
			// 1.数据校验
			// 之后还是写个通用方法吧。。。

			int resultCode = CLibrary.INSTANCE.ad_continu_conf(devId, oversamplingRate, range, samplingFrequency, triggerModel, triggerPolarity, clockModel, extendClockPolarity);
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			return ServerResponse.createBySuccessMessage(SUCCESS.getDesc());
		}

		/**
		 * 查询通道连续采集已经有的数据长度。
		 *
		 * @param devId
		 * @return
		 */
		public static ServerResponse getAdBufferSize(int devId) {

			int resultCode = CLibrary.INSTANCE.Get_AdBuf_Size(devId);
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			return ServerResponse.createBySuccess(resultCode);
		}

		/**
		 * 该函数用于读取缓冲区FIFO 的数据
		 *
		 * @param devId
		 * @param number ，number 为要读取的数据个数，number 必须小于等于FIFO 的大小，如果大于则只读完FIFO 就返回，
		 * @return
		 */
		public static ServerResponse<float[]> readAdBuffer(int devId, int number) {

			float[] adResultArray = new float[number];
			int floatArraysize = Native.getNativeSize(Float.class) * number;
			Pointer floatArrayPointer = new Memory(floatArraysize);

			int resultCode = CLibrary.INSTANCE.Read_AdBuf(devId, floatArrayPointer, number);
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			adResultArray = floatArrayPointer.getFloatArray(0, number);
			return ServerResponse.createBySuccess(adResultArray);
		}

		/**
		 * 强行停止采样过程并复位硬件采样电路。用于用户已经完成本次采样任务。
		 * 特别提醒的是，在调用本函数前，必须先调用Read_AdBuf（）把需要的数据读走，该函数停止AD 采集后会清空缓存区。
		 *
		 * @param devId
		 * @return
		 */
		public static ServerResponse adContinueStop(int devId) {

			int resultCode = CLibrary.INSTANCE.AD_continu_stop(devId);
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			return ServerResponse.createBySuccessMessage(SUCCESS.getDesc());
		}

		/**
		 * 单通道连续采集一段数据
		 *
		 * @param devId               设备ID 32 位有符号整型参数，子设备号，由系统自动分配，第一个插上电脑的采集设备号为0，第二个为1，以此类推。
		 * @param oversamplingRate    采样频率 32 位有符号整型参数，设定AD 采集的过采样率。
		 * @param range               量程类型 32 位有符号整型参数，设置对应AD量程。0表示+-5V，分辨率152uv, 1表示+-10V，分辨率305uv
		 * @param samplingFrequency   32 位有符号整型参数，设置连续采样频率，设置范围100—100000
		 * @param triggerModel        设置触发模式。=0 设置软件启动一次采样过程.=1：设置外部触发启动一次采样过程。
		 * @param triggerPolarity     设置触发输入极性。=0 设置外部触发上升边沿有效/=1 设置外部触发下降边沿有效。
		 * @param clockModel          设置时钟模式。=0 设置AD 启动利用内部时钟/=1：外部时钟。
		 * @param extendClockPolarity 设置外部时钟输入极性。=0 设置上升边沿有效/=1 设置下降边沿有效。
		 * @param number              32 位整型数，设定要连续采集的数据个数
		 * @return
		 */
		public static ServerResponse<float[]> adContinue(int devId, int oversamplingRate, int range, int samplingFrequency,
														 int triggerModel, int triggerPolarity, int clockModel, int extendClockPolarity, int number) {

			float[] adResultArray = new float[number];
			int floatArraysize = Native.getNativeSize(Float.class) * number;
			Pointer floatArrayPointer = new Memory(floatArraysize);

			int resultCode = CLibrary.INSTANCE.ad_continu(devId, oversamplingRate, range, samplingFrequency, triggerModel, triggerPolarity, clockModel, extendClockPolarity, number, floatArrayPointer);
			if (resultCode == FAIL.getCode()) {
				return ServerResponse.createByErrorCodeMessage(FAIL.getCode(), FAIL.getDesc());
			}
			adResultArray = floatArrayPointer.getFloatArray(0, number);
			return ServerResponse.createBySuccess(adResultArray);
		}

	}

	// test
	public static void main(String[] args) throws InterruptedException {
//		int ad_os = 100;
//		int ad_range = 0;

		int devId = 0;
		int oversamplingRate = 4;
		int range = 0;
		int samplingFrequency = 100;
		int triggerModel = 0;
		int triggerPolarity = 0;
		int clockModel = 0;
		int extendClockPolarity = 0;
		int number = 16000;


//		System.out.println(CLibrary.INSTANCE.openUSB());
//
//		System.out.println(Arrays.toString(USBDLLUtil.getAdSingleArray(0, ad_os, ad_range).getData()));
//
//		System.out.println(CLibrary.INSTANCE.closeUSB());

		USBDLLUtil.openUSB();

		System.out.println(USBDLLUtil.getDeviceNumber().getData());

		ServerResponse<float[]> adContinueResponse = USBDLLUtil.adContinue(devId, oversamplingRate, range, samplingFrequency, triggerModel, triggerPolarity, clockModel, extendClockPolarity, number);

		if (adContinueResponse.isFail()) {
			System.out.println(adContinueResponse.getMsg());
			USBDLLUtil.closeUSB();
			return;
		}
		System.out.println(Arrays.toString(adContinueResponse.getData()));
		System.out.println(adContinueResponse.getData().length);

		USBDLLUtil.closeUSB();


	}
}
