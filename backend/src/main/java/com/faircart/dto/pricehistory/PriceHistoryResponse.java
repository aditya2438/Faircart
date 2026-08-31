package com.faircart.dto.pricehistory;

import com.faircart.entity.PriceHistory;
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
public class PriceHistoryResponse {

    private Long id;
    private Long productId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Instant changedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public BigDecimal getOldPrice() { return oldPrice; }
    public void setOldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; }
    public BigDecimal getNewPrice() { return newPrice; }
    public void setNewPrice(BigDecimal newPrice) { this.newPrice = newPrice; }
    public Instant getChangedAt() { return changedAt; }
    public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }

    public static PriceHistoryResponseBuilder builder() { return new PriceHistoryResponseBuilder(); }
    public static class PriceHistoryResponseBuilder {
        private Long id;
        private Long productId;
        private BigDecimal oldPrice;
        private BigDecimal newPrice;
        private Instant changedAt;

        public PriceHistoryResponseBuilder id(Long id) { this.id = id; return this; }
        public PriceHistoryResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public PriceHistoryResponseBuilder oldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; return this; }
        public PriceHistoryResponseBuilder newPrice(BigDecimal newPrice) { this.newPrice = newPrice; return this; }
        public PriceHistoryResponseBuilder changedAt(Instant changedAt) { this.changedAt = changedAt; return this; }

        public PriceHistoryResponse build() {
            PriceHistoryResponse p = new PriceHistoryResponse();
            p.setId(id);
            p.setProductId(productId);
            p.setOldPrice(oldPrice);
            p.setNewPrice(newPrice);
            p.setChangedAt(changedAt);
            return p;
        }
    }

    public static PriceHistoryResponse from(PriceHistory history) {
        if (history == null) return null;
        return PriceHistoryResponse.builder()
                .id(history.getId())
                .productId(history.getProduct() != null ? history.getProduct().getId() : null)
                .oldPrice(history.getOldPrice())
                .newPrice(history.getNewPrice())
                .changedAt(history.getCreatedAt())
                .build();
    }

    public static List<PriceHistoryResponse> from(List<PriceHistory> history) {
        if (history == null) return List.of();
        return history.stream()
                .map(PriceHistoryResponse::from)
                .collect(java.util.stream.Collectors.toList());
    }
}