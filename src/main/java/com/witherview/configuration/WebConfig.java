package com.witherview.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedOrigins("http://localhost:3001")
                .allowCredentials(true);

        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedOrigins("http://localhost:3001")
                .allowCredentials(true);
    }
}
