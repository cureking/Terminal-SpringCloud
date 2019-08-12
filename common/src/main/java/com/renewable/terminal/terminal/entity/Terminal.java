package com.renewable.terminal.terminal.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor		// 避免feign调用方的deserializable失败
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Terminal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 终端服务器的局域网IP地址
	 */
	private String ip;

	/**
	 * 终端服务器的mac
	 */
	private String mac;

	/**
	 * 终端名称
	 */
	private String name;

	/**
	 * 备注
	 */
	private String mark;

	/**
	 * 0表示正常运行，1表示警告状态，7表示删除
	 */
	private Integer state;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;


}
