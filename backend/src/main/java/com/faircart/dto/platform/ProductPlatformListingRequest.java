package com.faircart.dto.platform;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPlatformListingRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Platform is required")
    private String platform;

    @Size(max = 100, message = "External ID cannot exceed 100 characters")
    private String externalId;

    @Size(max = 500, message = "Platform product URL cannot exceed 500 characters")
    private String platformProductUrl;

    @NotNull(message = "Original price is required")
    @DecimalMin(value = "0.0", message = "Original price cannot be negative")
    private BigDecimal originalPrice;

    @NotNull(message = "Current price is required")
    @DecimalMin(value = "0.0", message = "Current price cannot be negative")
    private BigDecimal currentPrice;

    @DecimalMin(value = "0.0", message = "Effective price cannot be negative")
    private BigDecimal effectivePrice;

    @DecimalMin(value = "0.0", message = "Discount percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100")
    private BigDecimal discountPercentage;

    @Size(max = 200, message = "Seller name cannot exceed 200 characters")
    private String sellerName;

    @DecimalMin(value = "0.0", message = "Seller rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Seller rating cannot exceed 5.0")
    private BigDecimal sellerRating;

    @Size(max = 100, message = "Delivery estimate cannot exceed 100 characters")
    private String deliveryEstimate;

    private Boolean inStock = true;

    @DecimalMin(value = "0.0", message = "Rating average cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating average cannot exceed 5.0")
    private BigDecimal ratingAverage;

    private Integer reviewCount = 0;

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

    public static ProductPlatformListingRequestBuilder builder() { return new ProductPlatformListingRequestBuilder(); }
    public static class ProductPlatformListingRequestBuilder {
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
        private Boolean inStock = true;
        private BigDecimal ratingAverage;
        private Integer reviewCount = 0;

        public ProductPlatformListingRequestBuilder productId(Long productId) { this.productId = productId; return this; }
        public ProductPlatformListingRequestBuilder platform(String platform) { this.platform = platform; return this; }
        public ProductPlatformListingRequestBuilder externalId(String externalId) { this.externalId = externalId; return this; }
        public ProductPlatformListingRequestBuilder platformProductUrl(String platformProductUrl) { this.platformProductUrl = platformProductUrl; return this; }
        public ProductPlatformListingRequestBuilder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
        public ProductPlatformListingRequestBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
        public ProductPlatformListingRequestBuilder effectivePrice(BigDecimal effectivePrice) { this.effectivePrice = effectivePrice; return this; }
        public ProductPlatformListingRequestBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
        public ProductPlatformListingRequestBuilder sellerName(String sellerName) { this.sellerName = sellerName; return this; }
        public ProductPlatformListingRequestBuilder sellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; return this; }
        public ProductPlatformListingRequestBuilder deliveryEstimate(String deliveryEstimate) { this.deliveryEstimate = deliveryEstimate; return this; }
        public ProductPlatformListingRequestBuilder inStock(Boolean inStock) { this.inStock = inStock; return this; }
        public ProductPlatformListingRequestBuilder ratingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; return this; }
        public ProductPlatformListingRequestBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }

        public ProductPlatformListingRequest build() {
            ProductPlatformListingRequest p = new ProductPlatformListingRequest();
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
            return p;
        }
    }
}