package com.weshare.security;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.security.default-success-url:/}")
    private String defaultSuccessUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile("production")
    public SecurityFilterChain prodFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .formLogin(form -> form
                .defaultSuccessUrl(defaultSuccessUrl, true)
                .failureUrl("/login?error"))
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/logout"))
            .build();
    }

    @Bean
    @Profile("!production")
    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .formLogin(form -> form
                .defaultSuccessUrl(defaultSuccessUrl, true)
                .failureUrl("/login?error"))
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/logout"))
            .build();
    }
}