package com.renewable.terminal.vibration.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author jarry
 * @since 2019-08-09
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VibrationDevConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 传感器唯一标识，与硬件的八个通道一一对应（别忘了0与1的问题）
	 */
	private Integer id;

	/**
	 * 终端id
	 */
	private String terminalId;

	/**
	 * 昵称
	 */
	private String name;

	/**
	 * 采样频率 32 位有符号整型参数，设定AD 采集的过采样率。
	 */
	private Integer oversamplingRate;

	/**
	 * 量程类型 32 位有符号整型参数，设置对应AD量程。 1表示+-10V，分辨率305uv，0表示+-5V，分辨率152uv
	 */
	private Integer meterRange;

	/**
	 * 32 位有符号整型参数，设置连续采样频率，设置范围100—100000
	 */
	private Integer samplingFrequency;

	/**
	 * 设置触发模式。=0 设置软件启动一次采样过程.=1：设置外部触发启动一次采样过程。
	 */
	private Integer triggerModel;

	/**
	 * 设置触发输入极性。=0 设置外部触发上升边沿有效/=1 设置外部触发下降边沿有效。
	 */
	private Integer triggerPolarity;

	/**
	 * 设置时钟模式。=0 设置AD 启动利用内部时钟/=1：外部时钟。
	 */
	private Integer clockModel;

	/**
	 * 设置外部时钟输入极性。=0 设置上升边沿有效/=1 设置下降边沿有效。
	 */
	private Integer extendClockPolarity;

	/**
	 * AD连续采集时，每次数据采集的数量（八通道，最好设置为八的整数倍）
	 */
	private Integer number;

	/**
	 * 状态（0表示正常使用，1表示空置）
	 */
	private Integer status;

	/**
	 * 注释。暂时用于保留传感器长度表示。MD，早不说
	 */
	private String mark;

	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;


}
