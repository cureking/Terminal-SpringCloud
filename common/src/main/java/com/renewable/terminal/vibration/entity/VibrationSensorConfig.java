package com.renewable.terminal.vibration.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author jarry
 * @since 2019-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VibrationSensorConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 传感器唯一标识，与硬件的八个通道一一对应（别忘了0与1的问题）
	 */
	private Integer id;

	/**
	 * 设备ID 32 位有符号整型参数，子设备号，由系统自动分配，第一个插上电脑的采集设备号为0，第二个为1，以此类推。目前就只有一个，默认为0
	 */
	private Integer devId;

	/**
	 * 用于辨识，是哪个通道
	 */
	private Integer passagewayCode;

	/**
	 * 终端id
	 */
	private String terminalId;

	/**
	 * 昵称
	 */
	private String name;

	/**
	 * 传感器分组ID
	 */
	private Integer groupId;

	/**
	 * 组内编号（如0，1）
	 */
	private Integer groupNumber;

	/**
	 * 传感器类型（1加速度 2速度 ）
	 */
	private Integer sensorType;

	/**
	 * 安装模式（1.1-4.2)
	 */
	private Double installMode;

	/**
	 * 安装角度
	 */
	private Double installAngle;

	/**
	 * 状态（0表示正常使用，1表示空置）
	 */
	private Integer status;

	/**
	 * 注释。暂时用于保留传感器长度表示。MD，早不说
	 */
	private String mark;

	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;


}
