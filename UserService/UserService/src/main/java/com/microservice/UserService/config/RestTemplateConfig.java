package com.microservice.UserService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

   
    private String url = "http://localhost:8000/api/auth/getRole";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
