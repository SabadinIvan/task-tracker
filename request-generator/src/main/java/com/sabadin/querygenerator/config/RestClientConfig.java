package com.sabadin.querygenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
//                .baseUrl("http://localhost:8080") // в случае запуска на хосте, а не в Docker
                .baseUrl("http://host.docker.internal:8080")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
