package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<ApiResponse<RecommendationService.RecommendationResult>> getRecommendations(
            Authentication authentication, @Valid @RequestBody RecommendationService.RecommendationRequest request) {
        String userEmail = authentication != null ? authentication.getName() : "guest@faircart.com";
        RecommendationService.RecommendationResult result = recommendationService.getRecommendations(userEmail, request);
        return ResponseEntity.ok(ApiResponse.ok("Recommendations generated successfully", result));
    }

    @PostMapping("/ai-query")
    public ResponseEntity<ApiResponse<RecommendationService.RecommendationResult>> processAIQuery(
            Authentication authentication, @RequestBody AIQueryRequest request) {
        String userEmail = authentication != null ? authentication.getName() : "guest@faircart.com";
        RecommendationService.RecommendationResult result = recommendationService.processAIQuery(userEmail, request.getQuery());
        return ResponseEntity.ok(ApiResponse.ok("AI query processed successfully", result));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<java.util.List<com.faircart.dto.recommendation.RecommendationLogResponse>>> getRecommendationHistory(
            Authentication authentication) {
        // This would fetch from RecommendationLogRepository
        return ResponseEntity.ok(ApiResponse.ok("Recommendation history retrieved", java.util.List.of()));
    }

    public static class AIQueryRequest {
        private String query;
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
    }
}