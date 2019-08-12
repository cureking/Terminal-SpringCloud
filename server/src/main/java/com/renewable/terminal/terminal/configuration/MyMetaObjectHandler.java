package com.renewable.terminal.terminal.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * 拦截填充公共字段
 */
@Configuration
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("start insert fill ....");
		this.setFieldValByName("createTime", new Date(), metaObject);
		this.setFieldValByName("updateTime", new Date(), metaObject);

	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("start update fill ....");
		this.setFieldValByName("updateTime", new Date(), metaObject);
	}
}
