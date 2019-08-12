package com.renewable.terminal.vibration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.renewable.terminal.vibration.dao")
public class VibrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(VibrationApplication.class, args);
	}

}
