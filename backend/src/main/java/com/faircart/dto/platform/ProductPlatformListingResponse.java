package com.faircart.dto.platform;

import com.faircart.entity.ProductPlatformListing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPlatformListingResponse {

    private Long id;
    private Long productId;
    private String platform;
    private String externalId;
    private String platformProductUrl;
    private BigDecimal originalPrice;
    private BigDecimal currentPrice;
    private BigDecimal effectivePrice;
    private BigDecimal discountPercentage;
    private String sellerName;
    private BigDecimal sellerRating;
    private String deliveryEstimate;
    private Boolean inStock;
    private BigDecimal ratingAverage;
    private Integer reviewCount;
    private Instant lastSyncedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public String getPlatformProductUrl() { return platformProductUrl; }
    public void setPlatformProductUrl(String platformProductUrl) { this.platformProductUrl = platformProductUrl; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public BigDecimal getEffectivePrice() { return effectivePrice; }
    public void setEffectivePrice(BigDecimal effectivePrice) { this.effectivePrice = effectivePrice; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public BigDecimal getSellerRating() { return sellerRating; }
    public void setSellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; }
    public String getDeliveryEstimate() { return deliveryEstimate; }
    public void setDeliveryEstimate(String deliveryEstimate) { this.deliveryEstimate = deliveryEstimate; }
    public Boolean getInStock() { return inStock; }
    public void setInStock(Boolean inStock) { this.inStock = inStock; }
    public BigDecimal getRatingAverage() { return ratingAverage; }
    public void setRatingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; }
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    public Instant getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static ProductPlatformListingResponseBuilder builder() { return new ProductPlatformListingResponseBuilder(); }
    public static class ProductPlatformListingResponseBuilder {
        private Long id;
        private Long productId;
        private String platform;
        private String externalId;
        private String platformProductUrl;
        private BigDecimal originalPrice;
        private BigDecimal currentPrice;
        private BigDecimal effectivePrice;
        private BigDecimal discountPercentage;
        private String sellerName;
        private BigDecimal sellerRating;
        private String deliveryEstimate;
        private Boolean inStock;
        private BigDecimal ratingAverage;
        private Integer reviewCount;
        private Instant lastSyncedAt;
        private Instant createdAt;
        private Instant updatedAt;

        public ProductPlatformListingResponseBuilder id(Long id) { this.id = id; return this; }
        public ProductPlatformListingResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public ProductPlatformListingResponseBuilder platform(String platform) { this.platform = platform; return this; }
        public ProductPlatformListingResponseBuilder externalId(String externalId) { this.externalId = externalId; return this; }
        public ProductPlatformListingResponseBuilder platformProductUrl(String platformProductUrl) { this.platformProductUrl = platformProductUrl; return this; }
        public ProductPlatformListingResponseBuilder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
        public ProductPlatformListingResponseBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
        public ProductPlatformListingResponseBuilder effectivePrice(BigDecimal effectivePrice) { this.effectivePrice = effectivePrice; return this; }
        public ProductPlatformListingResponseBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
        public ProductPlatformListingResponseBuilder sellerName(String sellerName) { this.sellerName = sellerName; return this; }
        public ProductPlatformListingResponseBuilder sellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; return this; }
        public ProductPlatformListingResponseBuilder deliveryEstimate(String deliveryEstimate) { this.deliveryEstimate = deliveryEstimate; return this; }
        public ProductPlatformListingResponseBuilder inStock(Boolean inStock) { this.inStock = inStock; return this; }
        public ProductPlatformListingResponseBuilder ratingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; return this; }
        public ProductPlatformListingResponseBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
        public ProductPlatformListingResponseBuilder lastSyncedAt(Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; return this; }
        public ProductPlatformListingResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ProductPlatformListingResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public ProductPlatformListingResponse build() {
            ProductPlatformListingResponse p = new ProductPlatformListingResponse();
            p.setId(id);
            p.setProductId(productId);
            p.setPlatform(platform);
            p.setExternalId(externalId);
            p.setPlatformProductUrl(platformProductUrl);
            p.setOriginalPrice(originalPrice);
            p.setCurrentPrice(currentPrice);
            p.setEffectivePrice(effectivePrice);
            p.setDiscountPercentage(discountPercentage);
            p.setSellerName(sellerName);
            p.setSellerRating(sellerRating);
            p.setDeliveryEstimate(deliveryEstimate);
            p.setInStock(inStock);
            p.setRatingAverage(ratingAverage);
            p.setReviewCount(reviewCount);
            p.setLastSyncedAt(lastSyncedAt);
            p.setCreatedAt(createdAt);
            p.setUpdatedAt(updatedAt);
            return p;
        }
    }

    public static ProductPlatformListingResponse from(ProductPlatformListing listing) {
        if (listing == null) return null;
        return ProductPlatformListingResponse.builder()
                .id(listing.getId())
                .productId(listing.getProduct() != null ? listing.getProduct().getId() : null)
                .platform(listing.getPlatform() != null ? listing.getPlatform().name() : null)
                .externalId(listing.getExternalId())
                .platformProductUrl(listing.getPlatformProductUrl())
                .originalPrice(listing.getOriginalPrice())
                .currentPrice(listing.getCurrentPrice())
                .effectivePrice(listing.getEffectivePrice())
                .discountPercentage(listing.getDiscountPercentage())
                .sellerName(listing.getSellerName())
                .sellerRating(listing.getSellerRating())
                .deliveryEstimate(listing.getDeliveryEstimate())
                .inStock(listing.isInStock())
                .ratingAverage(listing.getRatingAverage())
                .reviewCount(listing.getReviewCount())
                .lastSyncedAt(listing.getLastSyncedAt())
                .createdAt(listing.getCreatedAt())
                .updatedAt(listing.getUpdatedAt())
                .build();
    }

    public static List<ProductPlatformListingResponse> from(List<ProductPlatformListing> listings) {
        if (listings == null) return List.of();
        return listings.stream()
                .map(ProductPlatformListingResponse::from)
                .toList();
    }
}