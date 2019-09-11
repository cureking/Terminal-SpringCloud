package com.renewable.terminal.vibration.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
public class DLLConstant {

	public static final String DLL_WINDOWS_FILE_NAME = "Usb_Daq_V52_Dll";
	public static final String DLL_LINUX_FILE_NAME = "No_related_version_about_linux";

	public static final int DLL_AD_SINGLE_ARRAY_SIZE = 8;
	public static final int DEVIDE_GROUP_ARRAY_SIZE = 2;

	public enum DllExecuteResultTypeEnum {

		SUCCESS(0, "操作成功"),
		FAIL(-1, "操作失败"),
		;


		private int code;
		private String desc;
		//TODO 视情形，将参数类型改为String.etc

		DllExecuteResultTypeEnum(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public int getCode() {
			return code;
		}

		public static DllExecuteResultTypeEnum codeOf(int code) {
			for (DllExecuteResultTypeEnum dllExecuteResultTypeEnum : values()) {
				if (dllExecuteResultTypeEnum.getCode() == code) {
					return dllExecuteResultTypeEnum;
				}
			}
			log.warn("倾斜传感器-1没有找到对应的枚举", code);
			throw new RuntimeException("没有找到对应的枚举");
		}
	}

	public enum DllADRanerTypeEnum {

		TYPE_1(1, "输入范围：±10V，分辨率（uV）：305"),
		TYPE_0(0, "输入范围：±5V，分辨率（uV）：152"),
		;


		private int code;
		private String desc;
		//TODO 视情形，将参数类型改为String.etc

		DllADRanerTypeEnum(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public int getCode() {
			return code;
		}

		public static DllADRanerTypeEnum codeOf(int code) {
			for (DllADRanerTypeEnum dllADRanerTypeEnum : values()) {
				if (dllADRanerTypeEnum.getCode() == code) {
					return dllADRanerTypeEnum;
				}
			}
			log.warn("倾斜传感器-1没有找到对应的枚举", code);
			throw new RuntimeException("没有找到对应的枚举");
		}
	}

}
