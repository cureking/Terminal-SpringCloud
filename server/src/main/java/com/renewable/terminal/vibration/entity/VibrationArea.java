package com.renewable.terminal.vibration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VibrationArea implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 数据来源的终端服务器标示
	 */
	private String terminalId;

	/**
	 * 记录数据的来源传感器id（关联sensor_register）
	 */
	private Integer devId;

	/**
	 * 通道，表示这是第几个通道的数据（八个通道选一个）
	 */
	private Integer passagewayCode;

	/**
	 * 振动的幅度
	 */
	private Float vibratingValue;

	/**
	 * 备注
	 */
	private String mark;

	/**
	 * 数据状态
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private Date createTime;


}
