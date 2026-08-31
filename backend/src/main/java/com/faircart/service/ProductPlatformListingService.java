package com.faircart.service;

import com.faircart.dto.platform.ProductPlatformListingRequest;
import com.faircart.dto.platform.ProductPlatformListingResponse;
import com.faircart.entity.Product;
import com.faircart.entity.ProductPlatformListing;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.ProductPlatformListingRepository;
import com.faircart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPlatformListingService {

    private final ProductPlatformListingRepository platformListingRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ProductPlatformListingResponse createListing(ProductPlatformListingRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        ProductPlatformListing listing = ProductPlatformListing.builder()
                .product(product)
                .platform(ProductPlatformListing.Platform.valueOf(request.getPlatform().toUpperCase()))
                .externalId(request.getExternalId())
                .platformProductUrl(request.getPlatformProductUrl())
                .originalPrice(request.getOriginalPrice())
                .currentPrice(request.getCurrentPrice())
                .effectivePrice(request.getEffectivePrice() != null ? request.getEffectivePrice() : request.getCurrentPrice())
                .discountPercentage(request.getDiscountPercentage())
                .sellerName(request.getSellerName())
                .sellerRating(request.getSellerRating())
                .deliveryEstimate(request.getDeliveryEstimate())
                .inStock(request.getInStock())
                .ratingAverage(request.getRatingAverage())
                .reviewCount(request.getReviewCount())
                .lastSyncedAt(Instant.now())
                .build();

        ProductPlatformListing saved = platformListingRepository.save(listing);
        return ProductPlatformListingResponse.from(saved);
    }

    @Transactional
    public ProductPlatformListingResponse updateListing(Long id, ProductPlatformListingRequest request) {
        ProductPlatformListing listing = platformListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        listing.setProduct(product);
        listing.setPlatform(ProductPlatformListing.Platform.valueOf(request.getPlatform().toUpperCase()));
        listing.setExternalId(request.getExternalId());
        listing.setPlatformProductUrl(request.getPlatformProductUrl());
        listing.setOriginalPrice(request.getOriginalPrice());
        listing.setCurrentPrice(request.getCurrentPrice());
        listing.setEffectivePrice(request.getEffectivePrice() != null ? request.getEffectivePrice() : request.getCurrentPrice());
        listing.setDiscountPercentage(request.getDiscountPercentage());
        listing.setSellerName(request.getSellerName());
        listing.setSellerRating(request.getSellerRating());
        listing.setDeliveryEstimate(request.getDeliveryEstimate());
        listing.setInStock(request.getInStock());
        listing.setRatingAverage(request.getRatingAverage());
        listing.setReviewCount(request.getReviewCount());
        listing.setLastSyncedAt(Instant.now());

        ProductPlatformListing saved = platformListingRepository.save(listing);
        return ProductPlatformListingResponse.from(saved);
    }

    public ProductPlatformListingResponse getListingById(Long id) {
        ProductPlatformListing listing = platformListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));
        return ProductPlatformListingResponse.from(listing);
    }

    public List<ProductPlatformListingResponse> getListingsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        List<ProductPlatformListing> listings = platformListingRepository.findByProduct(product);
        return ProductPlatformListingResponse.from(listings);
    }

    public List<ProductPlatformListingResponse> getListingsByProductAndPlatform(Long productId, String platform) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        List<ProductPlatformListing> listings = platformListingRepository.findByProductAndPlatform(
                product, ProductPlatformListing.Platform.valueOf(platform.toUpperCase()));
        return ProductPlatformListingResponse.from(listings);
    }

    public List<ProductPlatformListingResponse> getInStockListingsByProductOrderByEffectivePrice(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        List<ProductPlatformListing> listings = platformListingRepository.findInStockByProductOrderByEffectivePriceAsc(product);
        return ProductPlatformListingResponse.from(listings);
    }

    public Page<ProductPlatformListingResponse> getAllListings(Pageable pageable) {
        return platformListingRepository.findAll(pageable)
                .map(ProductPlatformListingResponse::from);
    }

    public List<ProductPlatformListingResponse> getListingsByPlatform(String platform) {
        List<ProductPlatformListing> listings = platformListingRepository.findByPlatformAndInStockTrue(
                ProductPlatformListing.Platform.valueOf(platform.toUpperCase()));
        return ProductPlatformListingResponse.from(listings);
    }

    @Transactional
    public void deleteListing(Long id) {
        platformListingRepository.deleteById(id);
    }

    @Transactional
    public void syncPrices(Long productId, List<ProductPlatformListingRequest> newListings) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        for (ProductPlatformListingRequest request : newListings) {
            platformListingRepository.findByProductAndPlatformAndExternalId(
                    product,
                    ProductPlatformListing.Platform.valueOf(request.getPlatform().toUpperCase()),
                    request.getExternalId()
            ).ifPresentOrElse(
                    existing -> {
                        existing.setCurrentPrice(request.getCurrentPrice());
                        existing.setEffectivePrice(request.getEffectivePrice() != null ? request.getEffectivePrice() : request.getCurrentPrice());
                        existing.setDiscountPercentage(request.getDiscountPercentage());
                        existing.setInStock(request.getInStock());
                        existing.setSellerRating(request.getSellerRating());
                        existing.setRatingAverage(request.getRatingAverage());
                        existing.setReviewCount(request.getReviewCount());
                        existing.setLastSyncedAt(Instant.now());
                        platformListingRepository.save(existing);
                    },
                    () -> {
                        ProductPlatformListing listing = ProductPlatformListing.builder()
                                .product(product)
                                .platform(ProductPlatformListing.Platform.valueOf(request.getPlatform().toUpperCase()))
                                .externalId(request.getExternalId())
                                .platformProductUrl(request.getPlatformProductUrl())
                                .originalPrice(request.getOriginalPrice())
                                .currentPrice(request.getCurrentPrice())
                                .effectivePrice(request.getEffectivePrice() != null ? request.getEffectivePrice() : request.getCurrentPrice())
                                .discountPercentage(request.getDiscountPercentage())
                                .sellerName(request.getSellerName())
                                .sellerRating(request.getSellerRating())
                                .deliveryEstimate(request.getDeliveryEstimate())
                                .inStock(request.getInStock())
                                .ratingAverage(request.getRatingAverage())
                                .reviewCount(request.getReviewCount())
                                .lastSyncedAt(Instant.now())
                                .build();
                        platformListingRepository.save(listing);
                    }
            );
        }
    }

    public BigDecimal getLowestEffectivePrice(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        List<ProductPlatformListing> listings = platformListingRepository.findInStockByProductOrderByEffectivePriceAsc(product);
        return listings.isEmpty() ? null : listings.get(0).getEffectivePrice();
    }
}