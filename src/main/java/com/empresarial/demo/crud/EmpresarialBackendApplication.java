package com.empresarial.demo.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EmpresarialBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpresarialBackendApplication.class, args);
	}
}
