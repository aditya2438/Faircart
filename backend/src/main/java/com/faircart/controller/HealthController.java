package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}")
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        return ApiResponse.ok(Map.of(
                "service", applicationName,
                "status", "UP",
                "timestamp", Instant.now().toString()
        ));
    }
}
