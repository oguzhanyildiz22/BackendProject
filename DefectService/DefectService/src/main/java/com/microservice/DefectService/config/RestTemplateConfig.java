package com.microservice.DefectService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


import lombok.Data;

@Configuration
@Data
public class RestTemplateConfig {

   
	private String url1 = "http://Container-B:8000/api/auth/getRole";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
