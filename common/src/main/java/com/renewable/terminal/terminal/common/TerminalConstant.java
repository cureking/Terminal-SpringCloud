package com.renewable.terminal.terminal.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description：
 * @Author: jarry
 */
public interface TerminalConstant {

	static final String GLOBAL_TERMINAL_CONFIG = "global_terminal_config";
	static final String GLOBAL_TERMINAL_ID = "global_terminal_id";
	static final String GLOBAL_TERMINAL_MAC = "global_terminal_mac";
	static final String GLOBAL_TERMINAL_IP = "global_terminal_ip";
	static final String GLOBAL_TERMINAL_NAME = "global_terminal_name";

	// 描述终端系统运行状态
	public enum TerminalStateEnum {

		Running(0, "正常运行"),
		Warning(1, "警告状态"),
		Deleted(7, "删除状态");

		private String value;
		private Integer code;

		TerminalStateEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}

		public static TerminalStateEnum codeOf(int code) {
			for (TerminalStateEnum terminalStateEnum : values()) {
				if (terminalStateEnum.getCode() == code) {
					return terminalStateEnum;
				}
			}
//			log.warn("没有找到对应的枚举", code);
			throw new RuntimeException("没有找到对应的枚举");    //这里由于是在java程序内，无页面响应。故将相关信息导入到日志中。这里抛出的异常由调用方处理
		}
	}
}
