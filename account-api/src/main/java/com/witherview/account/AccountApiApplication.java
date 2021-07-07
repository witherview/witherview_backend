package com.witherview.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages="com.witherview.mysql.repository")
@EntityScan(basePackages="com.witherview.mysql")
public class AccountApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(AccountApiApplication.class, args);
  }

  @Bean
  public AccountApplicationContext accountApplicationContext() {
    return new AccountApplicationContext();
  }
}
