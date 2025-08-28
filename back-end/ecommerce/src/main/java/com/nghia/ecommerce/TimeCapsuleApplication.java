package com.nghia.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimeCapsuleApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimeCapsuleApplication.class, args);
	}
}
