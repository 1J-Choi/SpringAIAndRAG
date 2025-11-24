package com.ll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChatAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatAiApplication.class, args);
	}

}
