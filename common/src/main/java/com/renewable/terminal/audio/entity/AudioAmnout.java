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
public class AudioAmnout implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	private String terminalId;

	private Integer audioId;

	private Double originTime;

	private Double amnout;

	private Integer status;

	private String mark;

	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;


}
