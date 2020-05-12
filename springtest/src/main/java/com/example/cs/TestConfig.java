package com.example.cs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

	@Bean
	public String newString() {
		System.out.println("config----TestConfig------");
		return "sss";
	}

}
