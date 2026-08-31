package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.dto.review.ReviewSentimentResponse;
import com.faircart.service.ReviewSentimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/review-sentiments")
@RequiredArgsConstructor
public class ReviewSentimentController {

    private final ReviewSentimentService sentimentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReviewSentimentResponse>> createOrUpdateSentiment(
            @RequestBody ReviewSentimentService.ReviewSentimentRequest request) {
        ReviewSentimentResponse response = sentimentService.createOrUpdateSentiment(request);
        return ResponseEntity.ok(ApiResponse.ok("Sentiment created/updated successfully", response));
    }

    @GetMapping("/product/{productId}/platform/{platform}")
    public ResponseEntity<ApiResponse<ReviewSentimentResponse>> getSentiment(
            @PathVariable Long productId, @PathVariable String platform) {
        ReviewSentimentResponse response = sentimentService.getSentimentByProductAndPlatform(productId, platform);
        return ResponseEntity.ok(ApiResponse.ok("Sentiment retrieved successfully", response));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewSentimentResponse>>> getSentimentsByProduct(@PathVariable Long productId) {
        List<ReviewSentimentResponse> responses = sentimentService.getSentimentsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.ok("Sentiments retrieved successfully", responses));
    }

    @PostMapping("/analyze/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> analyzeSentiment(@PathVariable Long productId) {
        sentimentService.analyzeSentimentWithAI(productId);
        return ResponseEntity.ok(ApiResponse.ok("Sentiment analysis initiated", null));
    }
}