package com.witherview.configuration;

import com.witherview.interceptor.AuthInterceptor;
import com.witherview.interceptor.ResourceAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/api/**");

        registry.addInterceptor(new ResourceAuthInterceptor())
                .addPathPatterns("/videos/**");
    }
}
