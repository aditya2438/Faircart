package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.dto.platform.ProductPlatformListingRequest;
import com.faircart.dto.platform.ProductPlatformListingResponse;
import com.faircart.service.ProductPlatformListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/platform-listings")
@RequiredArgsConstructor
public class ProductPlatformListingController {

    private final ProductPlatformListingService listingService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProductPlatformListingResponse>> createListing(@Valid @RequestBody ProductPlatformListingRequest request) {
        ProductPlatformListingResponse response = listingService.createListing(request);
        return ResponseEntity.ok(ApiResponse.ok("Platform listing created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductPlatformListingResponse>> getListingById(@PathVariable Long id) {
        ProductPlatformListingResponse response = listingService.getListingById(id);
        return ResponseEntity.ok(ApiResponse.ok("Listing retrieved successfully", response));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductPlatformListingResponse>>> getListingsByProduct(@PathVariable Long productId) {
        List<ProductPlatformListingResponse> listings = listingService.getListingsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.ok("Listings retrieved successfully", listings));
    }

    @GetMapping("/product/{productId}/platform/{platform}")
    public ResponseEntity<ApiResponse<List<ProductPlatformListingResponse>>> getListingsByProductAndPlatform(
            @PathVariable Long productId, @PathVariable String platform) {
        List<ProductPlatformListingResponse> listings = listingService.getListingsByProductAndPlatform(productId, platform);
        return ResponseEntity.ok(ApiResponse.ok("Listings retrieved successfully", listings));
    }

    @GetMapping("/product/{productId}/best-price")
    public ResponseEntity<ApiResponse<List<ProductPlatformListingResponse>>> getBestPrices(@PathVariable Long productId) {
        List<ProductPlatformListingResponse> listings = listingService.getInStockListingsByProductOrderByEffectivePrice(productId);
        return ResponseEntity.ok(ApiResponse.ok("Best prices retrieved successfully", listings));
    }

    @GetMapping("/product/{productId}/lowest-price")
    public ResponseEntity<ApiResponse<BigDecimal>> getLowestEffectivePrice(@PathVariable Long productId) {
        BigDecimal price = listingService.getLowestEffectivePrice(productId);
        return ResponseEntity.ok(ApiResponse.ok("Lowest effective price retrieved", price));
    }

    @GetMapping("/platform/{platform}")
    public ResponseEntity<ApiResponse<List<ProductPlatformListingResponse>>> getListingsByPlatform(@PathVariable String platform) {
        List<ProductPlatformListingResponse> listings = listingService.getListingsByPlatform(platform);
        return ResponseEntity.ok(ApiResponse.ok("Listings retrieved successfully", listings));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductPlatformListingResponse>>> getAllListings(Pageable pageable) {
        Page<ProductPlatformListingResponse> page = listingService.getAllListings(pageable);
        return ResponseEntity.ok(ApiResponse.ok("Listings retrieved successfully", page));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProductPlatformListingResponse>> updateListing(
            @PathVariable Long id, @Valid @RequestBody ProductPlatformListingRequest request) {
        ProductPlatformListingResponse response = listingService.updateListing(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Listing updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
        return ResponseEntity.ok(ApiResponse.ok("Listing deleted successfully", null));
    }

    @PostMapping("/sync/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> syncPrices(@PathVariable Long productId, @Valid @RequestBody List<ProductPlatformListingRequest> listings) {
        listingService.syncPrices(productId, listings);
        return ResponseEntity.ok(ApiResponse.ok("Prices synced successfully", null));
    }
}