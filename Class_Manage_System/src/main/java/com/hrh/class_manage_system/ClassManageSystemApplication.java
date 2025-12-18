package com.hrh.class_manage_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hrh.class_manage_system.server.mapper")
public class ClassManageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassManageSystemApplication.class, args);
	}

}
