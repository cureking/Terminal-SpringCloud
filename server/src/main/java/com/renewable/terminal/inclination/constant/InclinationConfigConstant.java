package com.renewable.terminal.inclination.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
public class InclinationConfigConstant {

	//相关传感器命令字解析枚举
	public enum InclinationInstallModeEnum {

		ONE(1, "安装模式一：水平y与垂直x：(+x,+y)"),
		TWO(2, "安装模式二：水平y与垂直x：(+x,-y)"),
		THIRD(3, "安装模式三：水平y与垂直x：(-x,+y)"),
		FOUR(4, "安装模式四：水平y与垂直x：(-x,-y)"),

		FIVE(5, "安装模式五：水平x与垂直y：(+x,+y)"),
		SIX(6, "安装模式六：水平x与垂直y：(+x,-y)"),
		SEVEN(7, "安装模式七：水平x与垂直y：(-x,+y)"),
		EIGHT(8, "安装模式八：水平x与垂直y：(-x,-y)");


		private int code;
		private String value;
		//TODO 视情形，将参数类型改为String.etc

		InclinationInstallModeEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}

		public static InclinationInstallModeEnum codeOf(int code) {
			for (InclinationInstallModeEnum inclinationSensor1Enum : values()) {
				if (inclinationSensor1Enum.getCode() == code) {
					return inclinationSensor1Enum;
				}
			}
			log.warn("倾斜传感器-1没有找到对应的枚举", code);
			throw new RuntimeException("没有找到对应的枚举");
		}
	}

	// 传感器类型，及其数据数组长度
	public enum InclinationSerialTypeEnum {

		T826(17, "826T"),
		T526(14, "526T"),
		T726(15, "726T"),
		;


		private Integer code;
		private String messeg;
		//TODO 视情形，将参数类型改为String.etc

		InclinationSerialTypeEnum(Integer code, String messeg) {
			this.code = code;
			this.messeg = messeg;
		}

		public String getValue() {
			return messeg;
		}

		public int getCode() {
			return code;
		}

		public static InclinationSerialTypeEnum codeOf(int code) {
			for (InclinationSerialTypeEnum inclinationSerialTypeEnum : values()) {
				if (inclinationSerialTypeEnum.getCode() == code) {
					return inclinationSerialTypeEnum;
				}
			}

			if (code == 5 || code == 6) {
				// 我们应该庆幸，这个时候的三个倾斜传感器对这几个响应都是一致的。不过日后如果扩展出不一致的传感器，那么这个设计从硬件底层就死掉了（因为没有标识，只能靠一些不合理的人工设置）。
				// 这里就用T526来处理所有传感器非数据采集的响应
				return InclinationSerialTypeEnum.T526;
			}
			log.warn("倾斜传感器-1没有找到对应的枚举", code);
			throw new RuntimeException("没有找到对应的枚举");    //这里由于是在java程序内，无页面响应。故将相关信息导入到日志中。这里抛出的异常由调用方处理
		}
	}

	//相关传感器命令字解析枚举
	public enum InclinationSensorCommandEnum {

		READALL(Integer.parseInt("04", 16), 4, "同时读角度命令"),
		READALLR(Integer.parseInt("84", 16), 13, "同时读角度命令——传感器应答回复"),

		SETZERO(Integer.parseInt("05", 16), 5, "设置相对/绝对零点"),
		SETZEROR(Integer.parseInt("85", 16), 5, "设置相对/绝对零点——传感器应答回复命令"),
		//
		SETSPEED(Integer.parseInt("0B", 16), 5, "设置通讯速率"),
		SETSPEEDR(Integer.parseInt("8B", 16), 5, "设置通讯速率——传感器应答回复命令"),

		SETMOD(Integer.parseInt("0C", 16), 5, "设置传感器输出模式"),
		SETMODR(Integer.parseInt("8C", 16), 5, "设置传感器输出模式——传感器应答回复命令"),
		//
		SETADDR(Integer.parseInt("0F", 16), 5, "设置模块地址命令"),
		SETADDRR(Integer.parseInt("8F", 16), 5, "设置模块地址命令——传感器应答回复命令"),

		GETZERO(Integer.parseInt("0D", 16), 4, "查询相对/绝对零点命令"),
		GETZEROR(Integer.parseInt("8D", 16), 5, "查询相对/绝对零点命令——传感器应答回复命令"),

		RESP(Integer.parseInt("97", 16), 5, "传感器应答回复命令");


		private int code;
		private String value;
		private int commandLength;

		//TODO 视情形，将参数类型改为String.etc

		InclinationSensorCommandEnum(int code, int commandLength, String value) {
			this.code = code;
			this.commandLength = commandLength;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public int getCommandLength() {
			return commandLength;
		}

		public int getCode() {
			return code;
		}

		public static InclinationSensorCommandEnum codeOf(int code) {
			for (InclinationSensorCommandEnum inclinationSensorCommandEnum : values()) {
				if (inclinationSensorCommandEnum.getCode() == code) {
					return inclinationSensorCommandEnum;
				}
			}
			log.warn("倾斜传感器-1没有找到对应的枚举", code);
			throw new RuntimeException("没有找到对应的枚举");    //这里由于是在java程序内，无页面响应。故将相关信息导入到日志中。这里抛出的异常由调用方处理
		}
	}

	public enum SensorEnum {

		Inclination1(Integer.parseInt("68", 16), "倾斜传感器-1"),
		;

		private String value;
		private Integer code;

		SensorEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}

		public static SensorEnum codeOf(int code) {
			for (SensorEnum inclinationSensor1Enum : values()) {
				if (inclinationSensor1Enum.getCode() == code) {
					return inclinationSensor1Enum;
				}
			}
			log.warn("传感器没有找到对应的枚举", code);
			throw new RuntimeException("没有找到对应的枚举");    //这里由于是在java程序内，无页面响应。故将相关信息导入到日志中。这里抛出的异常由调用方处理
		}
	}

	// Version升级版 Status
	public enum InclinationStateEnum {

		Noclean(0, "原始倾斜观测数据"),
		Cleaned(1, "数据已经经过清洗"),
		Uploaded(2, "数据已经提交企业服务器");

		private String value;
		private Integer code;

		InclinationStateEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}

		public static InclinationStateEnum codeOf(int code) {
			for (InclinationStateEnum dataCleanType : values()) {
				if (dataCleanType.getCode() == code) {
					return dataCleanType;
				}
			}
			log.warn("没有找到对应的枚举", code);
			throw new RuntimeException("没有找到对应的枚举");    //这里由于是在java程序内，无页面响应。故将相关信息导入到日志中。这里抛出的异常由调用方处理
		}
	}

}
