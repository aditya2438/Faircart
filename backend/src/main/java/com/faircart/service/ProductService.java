package com.faircart.service;

import com.faircart.dto.product.ProductRequest;
import com.faircart.dto.product.ProductResponse;
import com.faircart.entity.Category;
import com.faircart.entity.Product;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.CategoryRepository;
import com.faircart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PriceHistoryService priceHistoryService;

    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .category(category)
                .imageUrl(request.getImageUrl())
                .status(Product.ProductStatus.ACTIVE)
                .build();

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        // Record price change if price has changed
        if (!product.getPrice().equals(request.getPrice())) {
            priceHistoryService.recordPriceChange(id, product.getPrice(), request.getPrice());
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setImageUrl(request.getImageUrl());

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductResponse.from(product);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findByStatus(Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponse::from);
    }

    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        return productRepository.findByCategoryAndStatus(category, Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponse::from);
    }

    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.search(keyword, Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponse::from);
    }

    public Page<ProductResponse> filterProducts(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Integer minIntelligenceScore, Pageable pageable) {
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

            if (minPrice != null && maxPrice != null) {
                return productRepository.findByCategoryAndPriceBetweenAndStatus(category, minPrice, maxPrice, Product.ProductStatus.ACTIVE, pageable)
                        .map(ProductResponse::from);
            }

            if (minIntelligenceScore != null) {
                return productRepository.findByCategoryAndIntelligenceScoreGreaterThanEqualAndStatus(category, minIntelligenceScore, Product.ProductStatus.ACTIVE, pageable)
                        .map(ProductResponse::from);
            }

            return productRepository.findByCategoryAndStatus(category, Product.ProductStatus.ACTIVE, pageable)
                    .map(ProductResponse::from);
        }

        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetweenAndStatus(minPrice, maxPrice, Product.ProductStatus.ACTIVE, pageable)
                    .map(ProductResponse::from);
        }

        if (minIntelligenceScore != null) {
            return productRepository.findByIntelligenceScoreGreaterThanEqualAndStatus(minIntelligenceScore, Product.ProductStatus.ACTIVE, pageable)
                    .map(ProductResponse::from);
        }

        return productRepository.findByStatus(Product.ProductStatus.ACTIVE, pageable)
                .map(ProductResponse::from);
    }

    public List<ProductResponse> getTopProducts(int limit) {
        return productRepository.findTop10ByStatusOrderByIntelligenceScoreDesc(Product.ProductStatus.ACTIVE)
                .stream()
                .limit(limit)
                .map(ProductResponse::from)
                .toList();
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setStatus(Product.ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    @Transactional
    public ProductResponse calculateIntelligenceScore(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        int score = calculateIntelligenceScore(product);
        product.setIntelligenceScore(score);

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    @Transactional
    public void batchCalculateIntelligenceScores() {
        List<Product> products = productRepository.findByStatus(Product.ProductStatus.ACTIVE);
        for (Product product : products) {
            int score = calculateIntelligenceScore(product);
            product.setIntelligenceScore(score);
        }
        productRepository.saveAll(products);
    }

    private int calculateIntelligenceScore(Product product) {
        // Price Score (30%): Compare price against market average
        // For simplicity, we'll use a baseline price based on category
        double priceScore = calculatePriceScore(product);
        
        // Rating Score (25%): Based on average user rating
        // Currently we don't have ratings, so we'll use a placeholder
        double ratingScore = 5.0; // Default mid-range
        
        // Seller Score (25%): Based on seller rating and total sales
        // Currently we don't have sellers, so we'll use a placeholder
        double sellerScore = 5.0; // Default mid-range
        
        // Availability Score (20%): Based on stock quantity
        double availabilityScore = calculateAvailabilityScore(product);
        
        // Weighted average
        double totalScore = (priceScore * 0.30) + (ratingScore * 0.25) + (sellerScore * 0.25) + (availabilityScore * 0.20);
        
        // Scale to 0-100
        return Math.max(0, Math.min(100, (int) Math.round(totalScore * 10)));
    }

    private double calculatePriceScore(Product product) {
        // Simulate market average price comparison
        // In a real system, this would query actual market data
        // For now, we'll use a simple heuristic based on price ranges
        double price = product.getPrice().doubleValue();
        
        // Assume average price in category is around $100
        // Products below average get higher scores
        double avgPrice = 100.0;
        double score = Math.max(0, (avgPrice - price) / avgPrice * 10);
        return Math.min(10, score);
    }

    private double calculateAvailabilityScore(Product product) {
        int stock = product.getStockQuantity();
        // Score based on stock: 100+ = 10, 50-99 = 8, 20-49 = 6, 10-19 = 4, 5-9 = 2, 1-4 = 1, 0 = 0
        if (stock >= 100) return 10.0;
        if (stock >= 50) return 8.0;
        if (stock >= 20) return 6.0;
        if (stock >= 10) return 4.0;
        if (stock >= 5) return 2.0;
        if (stock >= 1) return 1.0;
        return 0.0;
    }
}