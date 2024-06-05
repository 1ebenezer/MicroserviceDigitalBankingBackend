package com.example.Bill_Payment_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {

        return WebClient.builder().filter((request, next) -> {
            System.out.println("request" + request);
            return next.exchange(request).doOnNext(clientResponse -> System.out.println("response" + clientResponse));
        });
    }
}
