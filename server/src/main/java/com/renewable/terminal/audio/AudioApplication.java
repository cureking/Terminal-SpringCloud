package com.renewable.terminal.audio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.renewable.terminal.terminal.client","com.renewable.terminal.message.client"})
@ComponentScan(basePackages = "com.renewable.terminal")
@MapperScan(basePackages = "com.renewable.terminal.audio.dao")
public class AudioApplication {

	public static void main(String[] args) {
		SpringApplication.run(AudioApplication.class, args);
	}

}
