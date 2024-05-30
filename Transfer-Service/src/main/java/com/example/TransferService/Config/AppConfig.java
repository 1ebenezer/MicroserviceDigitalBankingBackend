package com.example.TransferService.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class AppConfig {

//    @Bean
//    public WebClient webClient () {
//
//         WebClient webClient = WebClient.builder()
//                .baseUrl("http://localhost:8083")
//                .defaultCookie("cookie-name", "cookie-value")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//            return (WebClient) WebClient.builder();
//    }
    @Bean
    public  WebClient.Builder webClientBuilder() {


        return WebClient.builder().filter((request, next) -> {
            System.out.println("request" + request);
            return next.exchange(request).doOnNext(clientResponse -> System.out.println("response" + clientResponse));
        });

}

}

