package com.renewable.terminal.inclination.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class InclinationConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 传感器address，作为唯一标识
	 */
	@TableId(value = "address")
	private Integer address;

	/**
	 * （主动冗余字段，该信息可从sensor_register中获取）查询终端id
	 */
	private String terminalId;

	private String name;

	private String port;

	private Integer baudrate;

	/**
	 * 标识传感器类型，目前都是0x68
	 */
	private Integer sensorType;

	/**
	 * 倾斜传感器类型：如826T，526T
	 */
	private String inclinationType;

	private Integer model;

	private Integer zero;

	/**
	 * 筒半径
	 */
	private Double radius;

	private Double initH1;

	private Double initAngle1;

	private Double initH2;

	private Double initAngle2;

	private Double initH3;

	private Double initAngle3;

	private Double initH4;

	private Double initAngle4;

	/**
	 * 初始合倾角
	 */
	private Double initTotalAngle;

	/**
	 * 初始方位角
	 */
	private Double initDirectAngle;

	/**
	 * 合倾角警示上限（不包含初始倾角值）
	 */
	private Double totalAngleLimit;

	/**
	 * 综合倾角警示上限（包含初始倾角）
	 */
	private Double totalInitAngleLimit;

	/**
	 * 初始算法的中间变量x，即传感器倾斜角向量于X方向上的分解
	 */
	private Double initX;

	/**
	 * 初始算法的中间变量y，即传感器倾斜角向量于Y方向上的分解
	 */
	private Double initY;

	/**
	 * 状态
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
