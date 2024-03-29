package com.witherview.account.configuration;

import static springfox.documentation.builders.RequestHandlerSelectors.withMethodAnnotation;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String title;

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Bean
    public Docket apiV1() {
        title = "Witherview API ";

        return new Docket(DocumentationType.SWAGGER_2)
                .host(host+":"+port) // 개발을 위한 포트세팅
                .select()
                .apis(withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(title))
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo(String title) {
        return new ApiInfoBuilder()
                .title(title)
                .description("Witherview Account API DOCS")
                .build();
    }
}
