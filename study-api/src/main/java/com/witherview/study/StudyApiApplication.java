package com.witherview.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"com.witherview.study", "com.witherview.upload", "com.witherview.mysql"})
@EnableJpaAuditing
@EnableJpaRepositories(basePackages="com.witherview.mysql.repository")
@EntityScan(basePackages="com.witherview.mysql")
public class StudyApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(StudyApiApplication.class, args);
  }

  @Bean
  public StudyApplicationContext studyApplicationContext() {
    return new StudyApplicationContext();
  }
}
