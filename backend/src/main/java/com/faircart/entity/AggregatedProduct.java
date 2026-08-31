package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "aggregated_products", indexes = {
    @Index(name = "idx_agg_product_platform", columnList = "platform_id"),
    @Index(name = "idx_agg_product_category", columnList = "category_id"),
    @Index(name = "idx_agg_product_external_id", columnList = "externalId"),
    @Index(name = "idx_agg_product_price", columnList = "currentPrice"),
    @Index(name = "idx_agg_product_rating", columnList = "averageRating")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedProduct extends BaseEntity {

    @Column(nullable = false, length = 500)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true, length = 100)
    private String externalId;

    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "original_price", precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "current_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "currency", length = 3)
    private String currency = "INR";

    @Column(length = 1000)
    private String productUrl;

    @Column(columnDefinition = "TEXT")
    private String imageUrlsJson;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    private Long totalReviews = 0L;

    @Column(name = "seller_name", length = 200)
    private String sellerName;

    @Column(name = "seller_rating", precision = 3, scale = 2)
    private BigDecimal sellerRating;

    @Column(name = "delivery_days_min")
    private Integer deliveryDaysMin;

    @Column(name = "delivery_days_max")
    private Integer deliveryDaysMax;

    @Column(name = "availability_status", length = 50)
    private String availabilityStatus = "IN_STOCK";

    @Column(name = "specifications_json", columnDefinition = "TEXT")
    private String specificationsJson;

    @Column(name = "features_json", columnDefinition = "TEXT")
    private String featuresJson;

    @Column(name = "variant_options_json", columnDefinition = "TEXT")
    private String variantOptionsJson;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "last_scraped_at")
    private java.time.Instant lastScrapedAt;

    @Column(name = "scrape_error_count", nullable = false)
    private Integer scrapeErrorCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "deal_authenticity", length = 20)
    private DealAuthenticity dealAuthenticity = DealAuthenticity.UNKNOWN;

    @Column(name = "fake_sale_score", precision = 3, scale = 2)
    private BigDecimal fakeSaleScore;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public Platform getPlatform() { return platform; }
    public void setPlatform(Platform platform) { this.platform = platform; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getProductUrl() { return productUrl; }
    public void setProductUrl(String productUrl) { this.productUrl = productUrl; }
    public String getImageUrlsJson() { return imageUrlsJson; }
    public void setImageUrlsJson(String imageUrlsJson) { this.imageUrlsJson = imageUrlsJson; }
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    public Long getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Long totalReviews) { this.totalReviews = totalReviews; }
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public BigDecimal getSellerRating() { return sellerRating; }
    public void setSellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; }
    public Integer getDeliveryDaysMin() { return deliveryDaysMin; }
    public void setDeliveryDaysMin(Integer deliveryDaysMin) { this.deliveryDaysMin = deliveryDaysMin; }
    public Integer getDeliveryDaysMax() { return deliveryDaysMax; }
    public void setDeliveryDaysMax(Integer deliveryDaysMax) { this.deliveryDaysMax = deliveryDaysMax; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public String getSpecificationsJson() { return specificationsJson; }
    public void setSpecificationsJson(String specificationsJson) { this.specificationsJson = specificationsJson; }
    public String getFeaturesJson() { return featuresJson; }
    public void setFeaturesJson(String featuresJson) { this.featuresJson = featuresJson; }
    public String getVariantOptionsJson() { return variantOptionsJson; }
    public void setVariantOptionsJson(String variantOptionsJson) { this.variantOptionsJson = variantOptionsJson; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public java.time.Instant getLastScrapedAt() { return lastScrapedAt; }
    public void setLastScrapedAt(java.time.Instant lastScrapedAt) { this.lastScrapedAt = lastScrapedAt; }
    public Integer getScrapeErrorCount() { return scrapeErrorCount; }
    public void setScrapeErrorCount(Integer scrapeErrorCount) { this.scrapeErrorCount = scrapeErrorCount; }
    public DealAuthenticity getDealAuthenticity() { return dealAuthenticity; }
    public void setDealAuthenticity(DealAuthenticity dealAuthenticity) { this.dealAuthenticity = dealAuthenticity; }
    public BigDecimal getFakeSaleScore() { return fakeSaleScore; }
    public void setFakeSaleScore(BigDecimal fakeSaleScore) { this.fakeSaleScore = fakeSaleScore; }

    public static AggregatedProductBuilder builder() { return new AggregatedProductBuilder(); }
    public static class AggregatedProductBuilder {
        private String name;
        private String description;
        private String externalId;
        private Platform platform;
        private Category category;
        private BigDecimal originalPrice;
        private BigDecimal currentPrice;
        private BigDecimal discountPercentage;
        private String currency = "INR";
        private String productUrl;
        private String imageUrlsJson;
        private BigDecimal averageRating = BigDecimal.ZERO;
        private Long totalReviews = 0L;
        private String sellerName;
        private BigDecimal sellerRating;
        private Integer deliveryDaysMin;
        private Integer deliveryDaysMax;
        private String availabilityStatus = "IN_STOCK";
        private String specificationsJson;
        private String featuresJson;
        private String variantOptionsJson;
        private boolean active = true;
        private java.time.Instant lastScrapedAt;
        private Integer scrapeErrorCount = 0;
        private DealAuthenticity dealAuthenticity = DealAuthenticity.UNKNOWN;
        private BigDecimal fakeSaleScore;

        public AggregatedProductBuilder name(String name) { this.name = name; return this; }
        public AggregatedProductBuilder description(String description) { this.description = description; return this; }
        public AggregatedProductBuilder externalId(String externalId) { this.externalId = externalId; return this; }
        public AggregatedProductBuilder platform(Platform platform) { this.platform = platform; return this; }
        public AggregatedProductBuilder category(Category category) { this.category = category; return this; }
        public AggregatedProductBuilder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
        public AggregatedProductBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
        public AggregatedProductBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
        public AggregatedProductBuilder currency(String currency) { this.currency = currency; return this; }
        public AggregatedProductBuilder productUrl(String productUrl) { this.productUrl = productUrl; return this; }
        public AggregatedProductBuilder imageUrlsJson(String imageUrlsJson) { this.imageUrlsJson = imageUrlsJson; return this; }
        public AggregatedProductBuilder averageRating(BigDecimal averageRating) { this.averageRating = averageRating; return this; }
        public AggregatedProductBuilder totalReviews(Long totalReviews) { this.totalReviews = totalReviews; return this; }
        public AggregatedProductBuilder sellerName(String sellerName) { this.sellerName = sellerName; return this; }
        public AggregatedProductBuilder sellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; return this; }
        public AggregatedProductBuilder deliveryDaysMin(Integer deliveryDaysMin) { this.deliveryDaysMin = deliveryDaysMin; return this; }
        public AggregatedProductBuilder deliveryDaysMax(Integer deliveryDaysMax) { this.deliveryDaysMax = deliveryDaysMax; return this; }
        public AggregatedProductBuilder availabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; return this; }
        public AggregatedProductBuilder specificationsJson(String specificationsJson) { this.specificationsJson = specificationsJson; return this; }
        public AggregatedProductBuilder featuresJson(String featuresJson) { this.featuresJson = featuresJson; return this; }
        public AggregatedProductBuilder variantOptionsJson(String variantOptionsJson) { this.variantOptionsJson = variantOptionsJson; return this; }
        public AggregatedProductBuilder active(boolean active) { this.active = active; return this; }
        public AggregatedProductBuilder lastScrapedAt(java.time.Instant lastScrapedAt) { this.lastScrapedAt = lastScrapedAt; return this; }
        public AggregatedProductBuilder scrapeErrorCount(Integer scrapeErrorCount) { this.scrapeErrorCount = scrapeErrorCount; return this; }
        public AggregatedProductBuilder dealAuthenticity(DealAuthenticity dealAuthenticity) { this.dealAuthenticity = dealAuthenticity; return this; }
        public AggregatedProductBuilder fakeSaleScore(BigDecimal fakeSaleScore) { this.fakeSaleScore = fakeSaleScore; return this; }

        public AggregatedProduct build() {
            AggregatedProduct p = new AggregatedProduct();
            p.setName(name);
            p.setDescription(description);
            p.setExternalId(externalId);
            p.setPlatform(platform);
            p.setCategory(category);
            p.setOriginalPrice(originalPrice);
            p.setCurrentPrice(currentPrice);
            p.setDiscountPercentage(discountPercentage);
            p.setCurrency(currency != null ? currency : "INR");
            p.setProductUrl(productUrl);
            p.setImageUrlsJson(imageUrlsJson);
            p.setAverageRating(averageRating != null ? averageRating : BigDecimal.ZERO);
            p.setTotalReviews(totalReviews != null ? totalReviews : 0L);
            p.setSellerName(sellerName);
            p.setSellerRating(sellerRating);
            p.setDeliveryDaysMin(deliveryDaysMin);
            p.setDeliveryDaysMax(deliveryDaysMax);
            p.setAvailabilityStatus(availabilityStatus != null ? availabilityStatus : "IN_STOCK");
            p.setSpecificationsJson(specificationsJson);
            p.setFeaturesJson(featuresJson);
            p.setVariantOptionsJson(variantOptionsJson);
            p.setActive(active);
            p.setLastScrapedAt(lastScrapedAt);
            p.setScrapeErrorCount(scrapeErrorCount != null ? scrapeErrorCount : 0);
            p.setDealAuthenticity(dealAuthenticity != null ? dealAuthenticity : DealAuthenticity.UNKNOWN);
            p.setFakeSaleScore(fakeSaleScore);
            return p;
        }
    }

    public enum DealAuthenticity {
        GENUINE, SUSPICIOUS, FAKE_SALE, UNKNOWN
    }

    // Helper methods
    public List<String> getImageUrls() {
        if (imageUrlsJson == null || imageUrlsJson.isBlank()) return List.of();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(imageUrlsJson, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }

    public void setImageUrls(List<String> urls) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            this.imageUrlsJson = mapper.writeValueAsString(urls);
        } catch (Exception e) {
            this.imageUrlsJson = "[]";
        }
    }
}