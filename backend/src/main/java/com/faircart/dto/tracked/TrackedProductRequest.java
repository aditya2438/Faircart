package com.faircart.dto.tracked;

import jakarta.validation.constraints.DecimalMin;
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
public class TrackedProductRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Platform is required")
    private String platform;

    @NotNull(message = "Target price is required")
    @DecimalMin(value = "0.01", message = "Target price must be greater than 0")
    private BigDecimal targetPrice;

    @Size(max = 20, message = "Notification channel cannot exceed 20 characters")
    private String notificationChannel = "EMAIL";

    private Boolean alertEnabled = true;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public BigDecimal getTargetPrice() { return targetPrice; }
    public void setTargetPrice(BigDecimal targetPrice) { this.targetPrice = targetPrice; }
    public String getNotificationChannel() { return notificationChannel; }
    public void setNotificationChannel(String notificationChannel) { this.notificationChannel = notificationChannel; }
    public Boolean getAlertEnabled() { return alertEnabled; }
    public void setAlertEnabled(Boolean alertEnabled) { this.alertEnabled = alertEnabled; }

    public static TrackedProductRequestBuilder builder() { return new TrackedProductRequestBuilder(); }
    public static class TrackedProductRequestBuilder {
        private Long productId;
        private String platform;
        private BigDecimal targetPrice;
        private String notificationChannel = "EMAIL";
        private Boolean alertEnabled = true;

        public TrackedProductRequestBuilder productId(Long productId) { this.productId = productId; return this; }
        public TrackedProductRequestBuilder platform(String platform) { this.platform = platform; return this; }
        public TrackedProductRequestBuilder targetPrice(BigDecimal targetPrice) { this.targetPrice = targetPrice; return this; }
        public TrackedProductRequestBuilder notificationChannel(String notificationChannel) { this.notificationChannel = notificationChannel; return this; }
        public TrackedProductRequestBuilder alertEnabled(Boolean alertEnabled) { this.alertEnabled = alertEnabled; return this; }

        public TrackedProductRequest build() {
            TrackedProductRequest t = new TrackedProductRequest();
            t.setProductId(productId);
            t.setPlatform(platform);
            t.setTargetPrice(targetPrice);
            t.setNotificationChannel(notificationChannel != null ? notificationChannel : "EMAIL");
            t.setAlertEnabled(alertEnabled != null ? alertEnabled : true);
            return t;
        }
    }
}