package com.renewable.terminal.inclination.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-07-22
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InclinationTotal implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 终端标识
	 */
	private String terminalId;

	/**
	 * 记录数据的来源传感器id（关联sensor_register）
	 */
	private Integer inclinationId;

	/**
	 * 记录该清洗后的数据来自哪条原始数据
	 */
	private Long originId;

	/**
	 * X方向倾斜角度
	 */
	private Double angleX;

	/**
	 * Y方向倾斜角度
	 */
	private Double angleY;

	/**
	 * 合倾角
	 */
	private Double angleTotal;

	/**
	 * 不含初始倾斜的方向角
	 */
	private Double directAngle;

	/**
	 * 包含初始倾角的总合倾角
	 */
	private Double angleInitTotal;

	/**
	 * 含初始倾斜的方向角
	 */
	private Double directAngleInit;

	/**
	 * 温度
	 */
	private Double temperature;

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
