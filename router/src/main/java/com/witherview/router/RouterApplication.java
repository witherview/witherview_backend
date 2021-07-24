package com.witherview.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.HashMap;

//@CrossOrigin("*")
@SpringBootApplication
public class RouterApplication {
    public static void main(String[] args) {
       SpringApplication.run(RouterApplication.class, args);
    }

//    @Bean
//    public ServerCodecConfigurer serverCodecConfigurer() { return ServerCodecConfigurer.create(); }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        String accountServer = "http://localhost:8085/";
        String studyServer = "http://localhost:8083/";
        String chatServer = "http://localhost:8081/";
        String socket = "ws://localhost:8081/";

        return builder.routes()
                .route("myInfo",
                        r -> r.path("/api/myinfo/**")
                        .uri(accountServer)
                        .id("api-myInfo")
                )
                .route("user",
                    r -> r.path("/api/user")
                    .uri(accountServer)
                    .id("api-user")
                )
                .route("login",
                        r -> r.path("/login")
                        .uri(accountServer)
                        .id("login")
                )
                .route("register",
                        r -> r.path("/register")
                                .uri(accountServer)
                        .id("register")
                )
                .route("withedraw",
                        r -> r.path("/withdraw")
                                .uri(accountServer)
                        .id("withdraw")
                )
                .route("oauth",
                        r -> r.path("/oauth/**")
                                .uri(accountServer)
                )
                .route("groupStudy",
                        r -> r.path("/api/group/**")
                                .uri(studyServer)
                        .id("api-group")
                )
                .route("self",
                        r -> r.path("/api/self/**")
                                .uri(studyServer)
                        .id("api-self")
                )
                .route("chat",
                    r -> r.path("/api/messages/**")
                        .uri(chatServer)
                        .id("api-chat")
                )
                .route("socket",
                        r -> r.path("/socket/**")
                                .uri(chatServer)
                )
                // pub, sub, topic 연결.
                .route("pub",
                        r -> r.path("/pub/**")
                                .uri(socket)
                )
                .route("sub",
                        r -> r.path("/sub/**")
                                .uri(socket)
                )
                .route("topic",
                        r -> r.path("/topic/**")
                                .uri(socket)
                )
                .build();
    }


    @Bean
    public CorsConfiguration corsConfiguration(RoutePredicateHandlerMapping routePredicateHandlerMapping) {
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        Arrays.asList(HttpMethod.OPTIONS, HttpMethod.PUT, HttpMethod.GET, HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PATCH) .forEach(m -> corsConfiguration.addAllowedMethod(m));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);
        routePredicateHandlerMapping.setCorsConfigurations(new HashMap<String, CorsConfiguration>() {{ put("/**", corsConfiguration); }});
        return corsConfiguration;
    }
}
