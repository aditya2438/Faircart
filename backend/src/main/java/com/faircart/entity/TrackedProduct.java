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
import java.time.Instant;

@Entity
@Table(name = "tracked_products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackedProduct extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, length = 50)
    private ProductPlatformListing.Platform platform;

    @Column(name = "target_price", precision = 12, scale = 2)
    private BigDecimal targetPrice;

    @Column(name = "current_price", precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "alert_enabled", nullable = false)
    private boolean alertEnabled = true;

    @Column(name = "last_notified_at")
    private Instant lastNotifiedAt;

    @Column(name = "notification_channel", length = 20)
    private String notificationChannel = "EMAIL";

    @Column(name = "last_checked_at")
    private Instant lastCheckedAt;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public ProductPlatformListing.Platform getPlatform() { return platform; }
    public void setPlatform(ProductPlatformListing.Platform platform) { this.platform = platform; }
    public BigDecimal getTargetPrice() { return targetPrice; }
    public void setTargetPrice(BigDecimal targetPrice) { this.targetPrice = targetPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public boolean isAlertEnabled() { return alertEnabled; }
    public void setAlertEnabled(boolean alertEnabled) { this.alertEnabled = alertEnabled; }
    public Instant getLastNotifiedAt() { return lastNotifiedAt; }
    public void setLastNotifiedAt(Instant lastNotifiedAt) { this.lastNotifiedAt = lastNotifiedAt; }
    public String getNotificationChannel() { return notificationChannel; }
    public void setNotificationChannel(String notificationChannel) { this.notificationChannel = notificationChannel; }
    public Instant getLastCheckedAt() { return lastCheckedAt; }
    public void setLastCheckedAt(Instant lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; }

    public static TrackedProductBuilder builder() { return new TrackedProductBuilder(); }
    public static class TrackedProductBuilder {
        private User user;
        private Product product;
        private ProductPlatformListing.Platform platform;
        private BigDecimal targetPrice;
        private BigDecimal currentPrice;
        private boolean alertEnabled = true;
        private Instant lastNotifiedAt;
        private String notificationChannel = "EMAIL";
        private Instant lastCheckedAt;

        public TrackedProductBuilder user(User user) { this.user = user; return this; }
        public TrackedProductBuilder product(Product product) { this.product = product; return this; }
        public TrackedProductBuilder platform(ProductPlatformListing.Platform platform) { this.platform = platform; return this; }
        public TrackedProductBuilder targetPrice(BigDecimal targetPrice) { this.targetPrice = targetPrice; return this; }
        public TrackedProductBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
        public TrackedProductBuilder alertEnabled(boolean alertEnabled) { this.alertEnabled = alertEnabled; return this; }
        public TrackedProductBuilder lastNotifiedAt(Instant lastNotifiedAt) { this.lastNotifiedAt = lastNotifiedAt; return this; }
        public TrackedProductBuilder notificationChannel(String notificationChannel) { this.notificationChannel = notificationChannel; return this; }
        public TrackedProductBuilder lastCheckedAt(Instant lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; return this; }

        public TrackedProduct build() {
            TrackedProduct t = new TrackedProduct();
            t.setUser(user);
            t.setProduct(product);
            t.setPlatform(platform);
            t.setTargetPrice(targetPrice);
            t.setCurrentPrice(currentPrice);
            t.setAlertEnabled(alertEnabled);
            t.setLastNotifiedAt(lastNotifiedAt);
            t.setNotificationChannel(notificationChannel != null ? notificationChannel : "EMAIL");
            t.setLastCheckedAt(lastCheckedAt);
            return t;
        }
    }
}