package com.renewable.terminal.vibration.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class VibrationPeak implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;


	/**
	 * 分组id
	 */
	private Integer groupId;


	private String devConfigId;

	/**
	 * 数据来源的终端服务器标示
	 */
	private String terminalId;

	/**
	 * 振动位移
	 */
	private Double displacement;

	/**
	 * 振动位移的方向
	 */
	private Double displacementDirect;

	/**
	 * 振动速度
	 */
	private Double speed;

	/**
	 * 振动速度的方向
	 */
	private Double speedDirect;

	/**
	 * 振动加速度
	 */
	private Double accelerate;

	/**
	 * 振动加速度的方向
	 */
	private Double accelerateDirect;

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
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;


}
