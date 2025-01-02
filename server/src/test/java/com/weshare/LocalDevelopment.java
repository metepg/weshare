package com.weshare;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

public class LocalDevelopment {

    public static void main(String[] args) {
        SpringApplication.from(Application::main)
                .with(LocalDevelopment.class)
                .run("--spring.profiles.active=testcontainers");
    }

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer(
            @Value("${reuse-database:true}") boolean reuse,
            @Value("${database-port:5435}") String port,
            @Value("${database-name:weshare}") String name) {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName(name)
                .withReuse(reuse);

        // Set fixed port in the application-testcontainers.properties
        // Default is 5435
        container.setPortBindings(List.of(port + ":5432"));

        return container;
    }

}