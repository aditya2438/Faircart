package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.dto.product.ProductResponse;
import com.faircart.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> addToWishlist(
            Authentication authentication, @PathVariable Long productId) {
        String userEmail = authentication.getName();
        ProductResponse response = wishlistService.addToWishlist(userEmail, productId);
        return ResponseEntity.ok(ApiResponse.ok("Product added to wishlist", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getWishlist(Authentication authentication) {
        String userEmail = authentication.getName();
        List<ProductResponse> wishlist = wishlistService.getWishlist(userEmail);
        return ResponseEntity.ok(ApiResponse.ok("Wishlist retrieved successfully", wishlist));
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> checkWishlist(
            Authentication authentication, @PathVariable Long productId) {
        String userEmail = authentication.getName();
        boolean inWishlist = wishlistService.isInWishlist(userEmail, productId);
        return ResponseEntity.ok(ApiResponse.ok("Wishlist status retrieved", inWishlist));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            Authentication authentication, @PathVariable Long productId) {
        String userEmail = authentication.getName();
        wishlistService.removeFromWishlist(userEmail, productId);
        return ResponseEntity.ok(ApiResponse.ok("Product removed from wishlist", null));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getWishlistCount(Authentication authentication) {
        String userEmail = authentication.getName();
        long count = wishlistService.getWishlistCount(userEmail);
        return ResponseEntity.ok(ApiResponse.ok("Wishlist count retrieved", count));
    }
}