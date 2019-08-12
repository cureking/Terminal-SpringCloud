package com.renewable.terminal.inclination;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
//@EnableFeignClients(basePackages = "com.renewable.terminal.terminal.client")
@EnableFeignClients(basePackages = {"com.renewable.terminal.terminal.client","com.renewable.terminal.message.client"})
@ComponentScan(basePackages = "com.renewable.terminal")
@MapperScan(basePackages = "com.renewable.terminal.inclination.dao")
public class InclinationApplication {

	public static void main(String[] args) {
		SpringApplication.run(InclinationApplication.class, args);
	}

}
