package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.dto.product.ProductRequest;
import com.faircart.dto.product.ProductResponse;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.service.ProductService;
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
@RequestMapping("${faircart.api.base-path:/api/v1}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.ok("Product created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(Pageable pageable) {
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.ok("Products retrieved successfully", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.ok("Product retrieved successfully", response));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId, Pageable pageable) {
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Products retrieved successfully", products));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam(defaultValue = "") String keyword, Pageable pageable) {
        Page<ProductResponse> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Search results", products));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> filterProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minIntelligenceScore,
            Pageable pageable) {
        Page<ProductResponse> products = productService.filterProducts(categoryId, minPrice, maxPrice, minIntelligenceScore, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Filtered products", products));
    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getTopProducts(@RequestParam(defaultValue = "10") int limit) {
        List<ProductResponse> top = productService.getTopProducts(limit);
        return ResponseEntity.ok(ApiResponse.ok("Top products", top));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Product updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.ok("Product deactivated successfully", null));
    }

    @PostMapping("/{id}/score")
    public ResponseEntity<ApiResponse<ProductResponse>> calculateScore(@PathVariable Long id) {
        ProductResponse response = productService.calculateIntelligenceScore(id);
        return ResponseEntity.ok(ApiResponse.ok("Intelligence score calculated", response));
    }
}