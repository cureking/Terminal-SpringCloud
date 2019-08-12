package com.renewable.terminal.inclination.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class InclinationOrigin implements Serializable {

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
	 * 就是原先的sensor_register_id，也就是address
	 */
	private Integer inclinationId;

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
	 * 包含初始倾斜的合倾角
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

	/**
	 * 备注信息
	 */
	private String mark;

	/**
	 * Noclean(0, "原始倾斜观测数据"),Cleaned(1, "数据已经经过清洗"),Uploaded(2, "数据已经提交企业服务器");
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

}
