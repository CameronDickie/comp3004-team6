package com.comp3004.educationmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Server extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}
}
