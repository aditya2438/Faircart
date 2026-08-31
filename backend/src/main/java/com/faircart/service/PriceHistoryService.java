package com.faircart.service;

import com.faircart.dto.pricehistory.PriceHistoryResponse;
import com.faircart.entity.PriceHistory;
import com.faircart.entity.Product;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.PriceHistoryRepository;
import com.faircart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public PriceHistoryResponse recordPriceChange(Long productId, BigDecimal oldPrice, BigDecimal newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        PriceHistory history = PriceHistory.builder()
                .product(product)
                .oldPrice(oldPrice)
                .newPrice(newPrice)
                .currentPrice(newPrice)
                .recordedAt(Instant.now())
                .build();

        PriceHistory saved = priceHistoryRepository.save(history);
        return PriceHistoryResponse.from(saved);
    }

    public List<PriceHistoryResponse> getPriceHistory(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<PriceHistory> history = priceHistoryRepository.findByProductOrderByRecordedAtDesc(product);
        return PriceHistoryResponse.from(history);
    }

    public List<PriceHistoryResponse> getRecentPriceHistory(Long productId, int limit) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<PriceHistory> history = priceHistoryRepository.findTop10ByProductOrderByRecordedAtDesc(product);
        return PriceHistoryResponse.from(history.subList(0, Math.min(limit, history.size())));
    }

    public PriceStats getPriceStats(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<PriceHistory> history = priceHistoryRepository.findByProductOrderByRecordedAtDesc(product);
        
        if (history.isEmpty()) {
            return PriceStats.builder()
                    .currentPrice(product.getPrice())
                    .lowestPrice(product.getPrice())
                    .highestPrice(product.getPrice())
                    .averagePrice(product.getPrice())
                    .totalChanges(0)
                    .build();
        }

        BigDecimal currentPrice = history.get(0).getNewPrice();
        BigDecimal lowest = history.stream()
                .map(PriceHistory::getNewPrice)
                .min(BigDecimal::compareTo)
                .orElse(currentPrice);
        BigDecimal highest = history.stream()
                .map(PriceHistory::getNewPrice)
                .max(BigDecimal::compareTo)
                .orElse(currentPrice);
        BigDecimal average = history.stream()
                .map(PriceHistory::getNewPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(history.size()), 2, java.math.RoundingMode.HALF_UP);

        return PriceStats.builder()
                .currentPrice(currentPrice)
                .lowestPrice(lowest)
                .highestPrice(highest)
                .averagePrice(average)
                .totalChanges(history.size())
                .build();
    }

    // Inner DTO classes
    public static class PriceHistoryResponse {
        private Long id;
        private Long productId;
        private BigDecimal oldPrice;
        private BigDecimal newPrice;
        private Instant changedAt;

        public PriceHistoryResponse() {}
        public PriceHistoryResponse(Long id, Long productId, BigDecimal oldPrice, BigDecimal newPrice, Instant changedAt) {
            this.id = id;
            this.productId = productId;
            this.oldPrice = oldPrice;
            this.newPrice = newPrice;
            this.changedAt = changedAt;
        }

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
                return new PriceHistoryResponse(id, productId, oldPrice, newPrice, changedAt);
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
            return history.stream()
                    .map(PriceHistoryResponse::from)
                    .collect(java.util.stream.Collectors.toList());
        }
    }

    public static class PriceStats {
        private BigDecimal currentPrice;
        private BigDecimal lowestPrice;
        private BigDecimal highestPrice;
        private BigDecimal averagePrice;
        private int totalChanges;

        public PriceStats() {}
        public PriceStats(BigDecimal currentPrice, BigDecimal lowestPrice, BigDecimal highestPrice, BigDecimal averagePrice, int totalChanges) {
            this.currentPrice = currentPrice;
            this.lowestPrice = lowestPrice;
            this.highestPrice = highestPrice;
            this.averagePrice = averagePrice;
            this.totalChanges = totalChanges;
        }

        public BigDecimal getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
        public BigDecimal getLowestPrice() { return lowestPrice; }
        public void setLowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; }
        public BigDecimal getHighestPrice() { return highestPrice; }
        public void setHighestPrice(BigDecimal highestPrice) { this.highestPrice = highestPrice; }
        public BigDecimal getAveragePrice() { return averagePrice; }
        public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }
        public int getTotalChanges() { return totalChanges; }
        public void setTotalChanges(int totalChanges) { this.totalChanges = totalChanges; }

        public static PriceStatsBuilder builder() { return new PriceStatsBuilder(); }
        public static class PriceStatsBuilder {
            private BigDecimal currentPrice;
            private BigDecimal lowestPrice;
            private BigDecimal highestPrice;
            private BigDecimal averagePrice;
            private int totalChanges;

            public PriceStatsBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
            public PriceStatsBuilder lowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; return this; }
            public PriceStatsBuilder highestPrice(BigDecimal highestPrice) { this.highestPrice = highestPrice; return this; }
            public PriceStatsBuilder averagePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; return this; }
            public PriceStatsBuilder totalChanges(int totalChanges) { this.totalChanges = totalChanges; return this; }

            public PriceStats build() {
                return new PriceStats(currentPrice, lowestPrice, highestPrice, averagePrice, totalChanges);
            }
        }
    }
}