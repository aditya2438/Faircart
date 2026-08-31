package com.faircart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {

    @Value("${faircart.jwt.secret}")
    private String secret;

    @Value("${faircart.jwt.expiration-ms}")
    private long expirationMs;

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig(secret, expirationMs);
    }

    public record JwtConfig(String secret, long expirationMs) {}
}
