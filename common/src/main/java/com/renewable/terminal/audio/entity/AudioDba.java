package com.renewable.terminal.audio.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author jarry
 * @since 2019-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AudioDba implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 声音传感器dba的数据表主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 终端Id
	 */
	private String terminalId;

	/**
	 * 传感器Id
	 */
	private Integer audioId;

	/**
	 * 原始数据中时间记录
	 */
	private Double originTime;

	/**
	 * 声音传感器生成的dba值
	 */
	private Double dba;

	/**
	 * 状态（0表示初始）
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String mark;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;


}
