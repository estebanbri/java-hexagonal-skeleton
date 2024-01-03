package com.example.bfftienda.infrastructure.adapter.gateway.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.bfftienda.infrastructure.adapter.gateway")
public class FeignClientConfig {
}
