package com.example.chat_webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatWebappApplication.class, args);
		System.out.println("server is running");
		System.out.println();
	}
	
}
