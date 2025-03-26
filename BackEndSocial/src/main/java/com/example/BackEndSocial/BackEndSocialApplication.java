package com.example.BackEndSocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@SpringBootApplication
@EnableWebSocketMessageBroker
public class BackEndSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndSocialApplication.class, args);
	}
}
