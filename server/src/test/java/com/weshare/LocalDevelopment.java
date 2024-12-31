package com.weshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

public class LocalDevelopment {

    public static void main(String[] args) {
        SpringApplication.from(Application::main)
                .with(LocalDevelopment.class)
                .run("--spring.profiles.active=local");
    }

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine");
    }

}