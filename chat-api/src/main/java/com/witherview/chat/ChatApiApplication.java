package com.witherview.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages="repository")
@EnableMongoRepositories(basePackages="mongo.repository")
@EntityScan(basePackages={"mongo.entity", "entity"})
public class ChatApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(ChatApiApplication.class, args);
  }

  @Bean
  public ChatApplicationContext chatApplicationContext() {
    return new ChatApplicationContext();
  }
}
