package com.witherview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WitherviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(WitherviewApplication.class, args);
	}

	@Bean
	public WitherviewApplicationContext witherviewApplicationContext() {
		return new WitherviewApplicationContext();
	}
}
