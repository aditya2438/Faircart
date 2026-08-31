package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.service.ProductComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/comparison")
@RequiredArgsConstructor
public class ProductComparisonController {

    private final ProductComparisonService comparisonService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductComparisonService.ComparisonResult>> compareProducts(
            @RequestBody CompareRequest request) {
        ProductComparisonService.ComparisonResult result = comparisonService.compareProducts(request.getProductIds());
        return ResponseEntity.ok(ApiResponse.ok("Products compared successfully", result));
    }

    @GetMapping("/quick/{productId1}/{productId2}")
    public ResponseEntity<ApiResponse<ProductComparisonService.ComparisonResult>> quickCompare(
            @PathVariable Long productId1, @PathVariable Long productId2) {
        ProductComparisonService.ComparisonResult result = comparisonService.compareProducts(List.of(productId1, productId2));
        return ResponseEntity.ok(ApiResponse.ok("Products compared successfully", result));
    }

    @lombok.Data
    public static class CompareRequest {
        private List<Long> productIds;

        public List<Long> getProductIds() { return productIds; }
        public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
    }
}