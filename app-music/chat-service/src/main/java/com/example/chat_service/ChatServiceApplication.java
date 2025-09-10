package com.example.chat_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@Slf4j
public class ChatServiceApplication {

	public static void main(String[] args) {

		log.info("Starting Chat Service");
		SpringApplication.run(ChatServiceApplication.class, args);
		log.info("Chat Service Application started successfully");
		log.info("WebSocket endpoint available at: ws://localhost:7171/ws");
		log.info("REST API available at: http://localhost:7171/api/chat");
	}

}
