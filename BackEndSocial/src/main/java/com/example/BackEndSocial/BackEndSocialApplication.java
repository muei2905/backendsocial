package com.example.BackEndSocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@SpringBootApplication
@EnableScheduling
@EnableWebSocketMessageBroker
public class  BackEndSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndSocialApplication.class, args);
	}
}
