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
import java.time.Instant;

@Entity
@Table(name = "price_history", indexes = {
    @Index(name = "idx_price_history_product", columnList = "product_id"),
    @Index(name = "idx_price_history_agg_product", columnList = "aggregated_product_id"),
    @Index(name = "idx_price_history_recorded", columnList = "recordedAt"),
    @Index(name = "idx_price_history_product_recorded", columnList = "product_id, recordedAt"),
    @Index(name = "idx_price_history_agg_product_recorded", columnList = "aggregated_product_id, recordedAt")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "aggregated_product_id")
    private AggregatedProduct aggregatedProduct;

    @Column(name = "original_price", precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "old_price", precision = 12, scale = 2)
    private BigDecimal oldPrice;

    @Column(name = "current_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "new_price", precision = 12, scale = 2)
    private BigDecimal newPrice;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "currency", length = 3)
    private String currency = "INR";

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    @Column(name = "source")
    private String source = "AUTOMATED_SCRAPE";

    @Enumerated(EnumType.STRING)
    @Column(name = "price_change_type", length = 20)
    private PriceChangeType priceChangeType = PriceChangeType.NONE;

    @Column(name = "price_change_amount", precision = 12, scale = 2)
    private BigDecimal priceChangeAmount;

    @Column(name = "price_change_percentage", precision = 5, scale = 2)
    private BigDecimal priceChangePercentage;

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public AggregatedProduct getAggregatedProduct() { return aggregatedProduct; }
    public void setAggregatedProduct(AggregatedProduct aggregatedProduct) { this.aggregatedProduct = aggregatedProduct; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public BigDecimal getOldPrice() { return oldPrice; }
    public void setOldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public BigDecimal getNewPrice() { return newPrice; }
    public void setNewPrice(BigDecimal newPrice) { this.newPrice = newPrice; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public PriceChangeType getPriceChangeType() { return priceChangeType; }
    public void setPriceChangeType(PriceChangeType priceChangeType) { this.priceChangeType = priceChangeType; }
    public BigDecimal getPriceChangeAmount() { return priceChangeAmount; }
    public void setPriceChangeAmount(BigDecimal priceChangeAmount) { this.priceChangeAmount = priceChangeAmount; }
    public BigDecimal getPriceChangePercentage() { return priceChangePercentage; }
    public void setPriceChangePercentage(BigDecimal priceChangePercentage) { this.priceChangePercentage = priceChangePercentage; }

    public static PriceHistoryBuilder builder() { return new PriceHistoryBuilder(); }
    public static class PriceHistoryBuilder {
        private Product product;
        private AggregatedProduct aggregatedProduct;
        private BigDecimal originalPrice;
        private BigDecimal oldPrice;
        private BigDecimal currentPrice;
        private BigDecimal newPrice;
        private BigDecimal discountPercentage;
        private String currency = "INR";
        private Instant recordedAt;
        private String source = "AUTOMATED_SCRAPE";
        private PriceChangeType priceChangeType = PriceChangeType.NONE;
        private BigDecimal priceChangeAmount;
        private BigDecimal priceChangePercentage;

        public PriceHistoryBuilder product(Product product) { this.product = product; return this; }
        public PriceHistoryBuilder aggregatedProduct(AggregatedProduct aggregatedProduct) { this.aggregatedProduct = aggregatedProduct; return this; }
        public PriceHistoryBuilder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
        public PriceHistoryBuilder oldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; return this; }
        public PriceHistoryBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
        public PriceHistoryBuilder newPrice(BigDecimal newPrice) { this.newPrice = newPrice; return this; }
        public PriceHistoryBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
        public PriceHistoryBuilder currency(String currency) { this.currency = currency; return this; }
        public PriceHistoryBuilder recordedAt(Instant recordedAt) { this.recordedAt = recordedAt; return this; }
        public PriceHistoryBuilder source(String source) { this.source = source; return this; }
        public PriceHistoryBuilder priceChangeType(PriceChangeType priceChangeType) { this.priceChangeType = priceChangeType; return this; }
        public PriceHistoryBuilder priceChangeAmount(BigDecimal priceChangeAmount) { this.priceChangeAmount = priceChangeAmount; return this; }
        public PriceHistoryBuilder priceChangePercentage(BigDecimal priceChangePercentage) { this.priceChangePercentage = priceChangePercentage; return this; }

        public PriceHistory build() {
            PriceHistory p = new PriceHistory();
            p.setProduct(product);
            p.setAggregatedProduct(aggregatedProduct);
            p.setOriginalPrice(originalPrice);
            p.setOldPrice(oldPrice);
            p.setCurrentPrice(currentPrice);
            p.setNewPrice(newPrice);
            p.setDiscountPercentage(discountPercentage);
            p.setCurrency(currency != null ? currency : "INR");
            p.setRecordedAt(recordedAt);
            p.setSource(source != null ? source : "AUTOMATED_SCRAPE");
            p.setPriceChangeType(priceChangeType != null ? priceChangeType : PriceChangeType.NONE);
            p.setPriceChangeAmount(priceChangeAmount);
            p.setPriceChangePercentage(priceChangePercentage);
            return p;
        }
    }

    public enum PriceChangeType {
        INCREASE, DECREASE, NONE, NEW_LOW, NEW_HIGH
    }
}