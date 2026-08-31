package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewService.ReviewResponse>> addReview(
            Authentication authentication, @Valid @RequestBody ReviewService.ReviewRequest request) {
        String userEmail = authentication.getName();
        ReviewService.ReviewResponse response = reviewService.addReview(userEmail, request);
        return ResponseEntity.ok(ApiResponse.ok("Review added successfully", response));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<Page<ReviewService.ReviewResponse>>> getProductReviews(
            @PathVariable Long productId, Pageable pageable) {
        Page<ReviewService.ReviewResponse> reviews = reviewService.getProductReviews(productId, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Reviews retrieved successfully", reviews));
    }

    @GetMapping("/product/{productId}/stats")
    public ResponseEntity<ApiResponse<ReviewService.ReviewStats>> getProductReviewStats(@PathVariable Long productId) {
        ReviewService.ReviewStats stats = reviewService.getProductReviewStats(productId);
        return ResponseEntity.ok(ApiResponse.ok("Review stats retrieved successfully", stats));
    }

    @GetMapping("/my-reviews")
    public ResponseEntity<ApiResponse<java.util.List<ReviewService.ReviewResponse>>> getUserReviews(Authentication authentication) {
        String userEmail = authentication.getName();
        java.util.List<ReviewService.ReviewResponse> reviews = reviewService.getUserReviews(userEmail);
        return ResponseEntity.ok(ApiResponse.ok("Your reviews retrieved successfully", reviews));
    }

    @GetMapping("/product/{productId}/my-review")
    public ResponseEntity<ApiResponse<ReviewService.ReviewResponse>> getUserReviewForProduct(
            Authentication authentication, @PathVariable Long productId) {
        String userEmail = authentication.getName();
        ReviewService.ReviewResponse review = reviewService.getUserReviewForProduct(userEmail, productId);
        return ResponseEntity.ok(ApiResponse.ok("Your review retrieved successfully", review));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewService.ReviewResponse>> updateReview(
            Authentication authentication, @PathVariable Long reviewId, @Valid @RequestBody ReviewService.ReviewRequest request) {
        String userEmail = authentication.getName();
        ReviewService.ReviewResponse response = reviewService.updateReview(userEmail, reviewId, request);
        return ResponseEntity.ok(ApiResponse.ok("Review updated successfully", response));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            Authentication authentication, @PathVariable Long reviewId) {
        String userEmail = authentication.getName();
        reviewService.deleteReview(userEmail, reviewId);
        return ResponseEntity.ok(ApiResponse.ok("Review deleted successfully", null));
    }

    @PostMapping("/{reviewId}/helpful")
    public ResponseEntity<ApiResponse<ReviewService.ReviewResponse>> markHelpful(
            Authentication authentication, @PathVariable Long reviewId) {
        String userEmail = authentication.getName();
        ReviewService.ReviewResponse response = reviewService.markHelpful(userEmail, reviewId);
        return ResponseEntity.ok(ApiResponse.ok("Review marked as helpful", response));
    }
}