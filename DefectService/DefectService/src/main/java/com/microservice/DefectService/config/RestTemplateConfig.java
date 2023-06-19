package com.microservice.DefectService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import lombok.Data;


@Configuration
@Data
public class RestTemplateConfig {

	@Value("${url1}")
	private String url1;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}