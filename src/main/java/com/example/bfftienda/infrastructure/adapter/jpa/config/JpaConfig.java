package com.example.bfftienda.infrastructure.adapter.jpa.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
//@ConfigurationProperties("spring.datasource")
//@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.example.bfftienda.infrastructure.adapter.jpa.repository")
@EntityScan(basePackages = "com.example.bfftienda.infrastructure.adapter.jpa.entity")
public class JpaConfig {


}
