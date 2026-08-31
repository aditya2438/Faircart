package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.dto.tracked.TrackedProductRequest;
import com.faircart.dto.tracked.TrackedProductResponse;
import com.faircart.service.TrackedProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/tracked-products")
@RequiredArgsConstructor
public class TrackedProductController {

    private final TrackedProductService trackedService;

    @PostMapping
    public ResponseEntity<ApiResponse<TrackedProductResponse>> trackProduct(
            Authentication authentication, @Valid @RequestBody TrackedProductRequest request) {
        String userEmail = authentication.getName();
        TrackedProductResponse response = trackedService.trackProduct(userEmail, request);
        return ResponseEntity.ok(ApiResponse.ok("Product tracked successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TrackedProductResponse>>> getTrackedProducts(Authentication authentication) {
        String userEmail = authentication.getName();
        List<TrackedProductResponse> tracked = trackedService.getUserTrackedProducts(userEmail);
        return ResponseEntity.ok(ApiResponse.ok("Tracked products retrieved successfully", tracked));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TrackedProductResponse>>> getActiveTrackedProducts(Authentication authentication) {
        String userEmail = authentication.getName();
        List<TrackedProductResponse> tracked = trackedService.getUserActiveTrackedProducts(userEmail);
        return ResponseEntity.ok(ApiResponse.ok("Active tracked products retrieved successfully", tracked));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrackedProductResponse>> updateTrackedProduct(
            @PathVariable Long id, @Valid @RequestBody TrackedProductRequest request) {
        TrackedProductResponse response = trackedService.updateTrackedProduct(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Tracked product updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrackedProduct(@PathVariable Long id) {
        trackedService.deleteTrackedProduct(id);
        return ResponseEntity.ok(ApiResponse.ok("Tracked product removed successfully", null));
    }

    @PostMapping("/check-price-drops")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> checkPriceDrops() {
        trackedService.checkAndNotifyPriceDrops();
        return ResponseEntity.ok(ApiResponse.ok("Price drop check completed", null));
    }
}