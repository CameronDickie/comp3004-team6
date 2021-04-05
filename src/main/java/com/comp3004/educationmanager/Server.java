package com.comp3004.educationmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Server extends SpringBootServletInitializer {
	public void run(String[] args) { SpringApplication.run(Server.class, args); }

	public static void main(String[] args) {
		Server server = new Server();
		server.run(args);
	}
}
