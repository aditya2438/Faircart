package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "product_platform_listings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPlatformListing extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, length = 50)
    private Platform platform;

    @Column(name = "external_id", length = 100)
    private String externalId;

    @Column(name = "platform_product_url", length = 500)
    private String platformProductUrl;

    @Column(name = "original_price", precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "current_price", precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "effective_price", precision = 12, scale = 2)
    private BigDecimal effectivePrice;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "seller_name", length = 200)
    private String sellerName;

    @Column(name = "seller_rating", precision = 3, scale = 2)
    private BigDecimal sellerRating;

    @Column(name = "delivery_estimate", length = 100)
    private String deliveryEstimate;

    @Column(name = "in_stock", nullable = false)
    private boolean inStock = true;

    @Column(name = "rating_average", precision = 3, scale = 2)
    private BigDecimal ratingAverage;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "last_synced_at")
    private java.time.Instant lastSyncedAt;

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Platform getPlatform() { return platform; }
    public void setPlatform(Platform platform) { this.platform = platform; }
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
    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
    public BigDecimal getRatingAverage() { return ratingAverage; }
    public void setRatingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; }
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    public java.time.Instant getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(java.time.Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }

    public static ProductPlatformListingBuilder builder() { return new ProductPlatformListingBuilder(); }
    public static class ProductPlatformListingBuilder {
        private Product product;
        private Platform platform;
        private String externalId;
        private String platformProductUrl;
        private BigDecimal originalPrice;
        private BigDecimal currentPrice;
        private BigDecimal effectivePrice;
        private BigDecimal discountPercentage;
        private String sellerName;
        private BigDecimal sellerRating;
        private String deliveryEstimate;
        private boolean inStock = true;
        private BigDecimal ratingAverage;
        private Integer reviewCount = 0;
        private java.time.Instant lastSyncedAt;

        public ProductPlatformListingBuilder product(Product product) { this.product = product; return this; }
        public ProductPlatformListingBuilder platform(Platform platform) { this.platform = platform; return this; }
        public ProductPlatformListingBuilder externalId(String externalId) { this.externalId = externalId; return this; }
        public ProductPlatformListingBuilder platformProductUrl(String platformProductUrl) { this.platformProductUrl = platformProductUrl; return this; }
        public ProductPlatformListingBuilder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
        public ProductPlatformListingBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
        public ProductPlatformListingBuilder effectivePrice(BigDecimal effectivePrice) { this.effectivePrice = effectivePrice; return this; }
        public ProductPlatformListingBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
        public ProductPlatformListingBuilder sellerName(String sellerName) { this.sellerName = sellerName; return this; }
        public ProductPlatformListingBuilder sellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; return this; }
        public ProductPlatformListingBuilder deliveryEstimate(String deliveryEstimate) { this.deliveryEstimate = deliveryEstimate; return this; }
        public ProductPlatformListingBuilder inStock(boolean inStock) { this.inStock = inStock; return this; }
        public ProductPlatformListingBuilder ratingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; return this; }
        public ProductPlatformListingBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
        public ProductPlatformListingBuilder lastSyncedAt(java.time.Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; return this; }

        public ProductPlatformListing build() {
            ProductPlatformListing p = new ProductPlatformListing();
            p.setProduct(product);
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
            return p;
        }
    }

    public enum Platform {
        AMAZON, FLIPKART, MEESHO, TATA_NEU, MYNTRA, CROMA, RELIANCE_DIGITAL, AJIO, NYKAA, SAMSUNG, APPLE, REALME, BLINKIT, INSTAMART, ZEPTO
    }
}