package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/price-history")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<PriceHistoryService.PriceHistoryResponse>>> getPriceHistory(@PathVariable Long productId) {
        List<PriceHistoryService.PriceHistoryResponse> history = priceHistoryService.getPriceHistory(productId);
        return ResponseEntity.ok(ApiResponse.ok("Price history retrieved successfully", history));
    }

    @GetMapping("/product/{productId}/recent")
    public ResponseEntity<ApiResponse<List<PriceHistoryService.PriceHistoryResponse>>> getRecentPriceHistory(
            @PathVariable Long productId, @RequestParam(defaultValue = "10") int limit) {
        List<PriceHistoryService.PriceHistoryResponse> history = priceHistoryService.getRecentPriceHistory(productId, limit);
        return ResponseEntity.ok(ApiResponse.ok("Recent price history retrieved successfully", history));
    }

    @GetMapping("/product/{productId}/stats")
    public ResponseEntity<ApiResponse<PriceHistoryService.PriceStats>> getPriceStats(@PathVariable Long productId) {
        PriceHistoryService.PriceStats stats = priceHistoryService.getPriceStats(productId);
        return ResponseEntity.ok(ApiResponse.ok("Price stats retrieved successfully", stats));
    }

    @PostMapping("/record")
    public ResponseEntity<ApiResponse<PriceHistoryService.PriceHistoryResponse>> recordPriceChange(
            @RequestParam Long productId,
            @RequestParam BigDecimal oldPrice,
            @RequestParam BigDecimal newPrice) {
        PriceHistoryService.PriceHistoryResponse response = priceHistoryService.recordPriceChange(productId, oldPrice, newPrice);
        return ResponseEntity.ok(ApiResponse.ok("Price change recorded successfully", response));
    }
}