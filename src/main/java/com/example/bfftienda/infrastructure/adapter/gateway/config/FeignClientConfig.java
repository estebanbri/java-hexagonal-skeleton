package com.example.bfftienda.infrastructure.adapter.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
//@EnableConfigurationProperties(ApplicationProperties.class)
@Configuration
@EnableFeignClients(basePackages = "com.example.bfftienda.infrastructure.adapter.gateway")
public class FeignClientConfig {
}
