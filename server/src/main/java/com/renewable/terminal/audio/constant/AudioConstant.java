package com.renewable.terminal.audio.constant;

/**
 * @Description：
 * @Author: jarry
 */
public class AudioConstant {

	// 每个音频文件的时长	// 1分钟		// 加上括号，避免等价替换带来的计算式问题
	public static final int AUDIO_FILE_DURATION = (1000 * 60);
	// 音频文件的采样周期	// 1小时
	public static final int AUDIO_CLEAN_DURATION = (1000 * 60 * 60);
	// 音频文件的日期偏移量	// 2分钟		// 这个值不是随意设置的，是根据定时任务中音频传感器处理周期决定的（两者保持一致）
	public static final int AUDIO_DATE_OFFSET = (1000 * 60 * 2);
	// matlab程序关闭时间	// 2分钟		// 由于matlab启动时间太长（在本机上约45秒，处理时间10秒），如果在最终终端机中1分钟不够，需要进行修改	// 由于采用async，所有其他的可以不用修改。
	public static final long AUDIO_MATLAB_DESTROY_DURATION = (1000 * 60 * 2);

	public static final String AUDIO_ORIGIN_DIRECTORY_RELATIVE = "audio\\";
	public static final String AUDIO_TOOL_DIRECTORY_RELATIVE = "audio\\tool\\";
	public static final String AUDIO_PERSISTENCE_DIRECTORY_RELATIVE = "audio\\persistence\\";

	public static final String AUDIO_ORIGIN_FILE_NAME = "audio_origin";
	public static final String AUDIO_TOOL_READ_FILE_NAME = "wstest";
	public static final String AUDIO_FILE_EXTENSION_NAME = ".wav";
	public static final String AUDIO_TOOL_APPLICATION_NAME = "wsrun";
	public static final String AUDIO_TOOL_EXTENSION_NAME = ".exe";

	public static final String AUDIO_DBA_FILE_NAME_AND_EXTENSION_NAME = "dBA.txt";
	public static final String AUDIO_AMNOUT_FILE_NAME_AND_EXTENSION_NAME = "AMNout.txt";
}
