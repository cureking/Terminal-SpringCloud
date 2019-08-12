package com.renewable.terminal.terminal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.renewable.terminal.terminal.dao")
@EnableFeignClients(basePackages = {"com.renewable.terminal.message.client"})
@ComponentScan(basePackages = "com.renewable.terminal")
public class TerminalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerminalApplication.class, args);
	}

}
