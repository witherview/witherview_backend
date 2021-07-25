package com.witherview.upload.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
public class WebClientConfig {
  @Value("${cloud.aws.gateway.url}")
  private String url;

  @Bean
  public WebClient webClient() {

    HttpClient httpClient = HttpClient.create()
        .tcpConfiguration(
            client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) //miliseconds
                .doOnConnected(
                    conn -> conn.addHandlerLast(new ReadTimeoutHandler(10))  //sec
                        .addHandlerLast(new WriteTimeoutHandler(10)) //sec
                )
        )
        .wiretap(true);

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .baseUrl(url)
        .filters(exchangeFilterFunctions -> {
          exchangeFilterFunctions.add(logRequest());
          exchangeFilterFunctions.add(logResponse());
        })
        .build();
  }

  private ExchangeFilterFunction logRequest() {
    return (clientRequest, next) -> {
      log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
      clientRequest.headers()
          .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
      return next.exchange(clientRequest);
    };
  }

  private ExchangeFilterFunction logResponse() {
    return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
      log.info("Response: {}", clientResponse.headers().asHttpHeaders().get("property-header"));
      return Mono.just(clientResponse);
    });
  }
}
