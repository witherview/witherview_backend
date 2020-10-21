package com.witherview;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WitherviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(WitherviewApplication.class, args);
	}

	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
