package com.faircart.dto.tracked;

import com.faircart.entity.TrackedProduct;
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
public class TrackedProductResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String platform;
    private BigDecimal targetPrice;
    private BigDecimal currentPrice;
    private Boolean alertEnabled;
    private Instant lastNotifiedAt;
    private String notificationChannel;
    private Instant lastCheckedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public BigDecimal getTargetPrice() { return targetPrice; }
    public void setTargetPrice(BigDecimal targetPrice) { this.targetPrice = targetPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public Boolean getAlertEnabled() { return alertEnabled; }
    public void setAlertEnabled(Boolean alertEnabled) { this.alertEnabled = alertEnabled; }
    public Instant getLastNotifiedAt() { return lastNotifiedAt; }
    public void setLastNotifiedAt(Instant lastNotifiedAt) { this.lastNotifiedAt = lastNotifiedAt; }
    public String getNotificationChannel() { return notificationChannel; }
    public void setNotificationChannel(String notificationChannel) { this.notificationChannel = notificationChannel; }
    public Instant getLastCheckedAt() { return lastCheckedAt; }
    public void setLastCheckedAt(Instant lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static TrackedProductResponseBuilder builder() { return new TrackedProductResponseBuilder(); }
    public static class TrackedProductResponseBuilder {
        private Long id;
        private Long userId;
        private Long productId;
        private String productName;
        private String platform;
        private BigDecimal targetPrice;
        private BigDecimal currentPrice;
        private Boolean alertEnabled;
        private Instant lastNotifiedAt;
        private String notificationChannel;
        private Instant lastCheckedAt;
        private Instant createdAt;
        private Instant updatedAt;

        public TrackedProductResponseBuilder id(Long id) { this.id = id; return this; }
        public TrackedProductResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public TrackedProductResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public TrackedProductResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public TrackedProductResponseBuilder platform(String platform) { this.platform = platform; return this; }
        public TrackedProductResponseBuilder targetPrice(BigDecimal targetPrice) { this.targetPrice = targetPrice; return this; }
        public TrackedProductResponseBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
        public TrackedProductResponseBuilder alertEnabled(Boolean alertEnabled) { this.alertEnabled = alertEnabled; return this; }
        public TrackedProductResponseBuilder lastNotifiedAt(Instant lastNotifiedAt) { this.lastNotifiedAt = lastNotifiedAt; return this; }
        public TrackedProductResponseBuilder notificationChannel(String notificationChannel) { this.notificationChannel = notificationChannel; return this; }
        public TrackedProductResponseBuilder lastCheckedAt(Instant lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; return this; }
        public TrackedProductResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public TrackedProductResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public TrackedProductResponse build() {
            TrackedProductResponse t = new TrackedProductResponse();
            t.setId(id);
            t.setUserId(userId);
            t.setProductId(productId);
            t.setProductName(productName);
            t.setPlatform(platform);
            t.setTargetPrice(targetPrice);
            t.setCurrentPrice(currentPrice);
            t.setAlertEnabled(alertEnabled);
            t.setLastNotifiedAt(lastNotifiedAt);
            t.setNotificationChannel(notificationChannel);
            t.setLastCheckedAt(lastCheckedAt);
            t.setCreatedAt(createdAt);
            t.setUpdatedAt(updatedAt);
            return t;
        }
    }

    public static TrackedProductResponse from(TrackedProduct tracked) {
        if (tracked == null) return null;
        return TrackedProductResponse.builder()
                .id(tracked.getId())
                .userId(tracked.getUser() != null ? tracked.getUser().getId() : null)
                .productId(tracked.getProduct() != null ? tracked.getProduct().getId() : null)
                .productName(tracked.getProduct() != null ? tracked.getProduct().getName() : null)
                .platform(tracked.getPlatform() != null ? tracked.getPlatform().name() : null)
                .targetPrice(tracked.getTargetPrice())
                .currentPrice(tracked.getCurrentPrice())
                .alertEnabled(tracked.isAlertEnabled())
                .lastNotifiedAt(tracked.getLastNotifiedAt())
                .notificationChannel(tracked.getNotificationChannel())
                .lastCheckedAt(tracked.getLastCheckedAt())
                .createdAt(tracked.getCreatedAt())
                .updatedAt(tracked.getUpdatedAt())
                .build();
    }

    public static List<TrackedProductResponse> from(List<TrackedProduct> trackedProducts) {
        if (trackedProducts == null) return List.of();
        return trackedProducts.stream()
                .map(TrackedProductResponse::from)
                .toList();
    }
}