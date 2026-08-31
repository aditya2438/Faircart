package com.faircart.service.scraper;

import com.faircart.dto.platform.ProductPlatformListingResponse;
import com.faircart.entity.ProductPlatformListing;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ParallelScraperService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ParallelScraperService.class);

    private static final List<ProductPlatformListing.Platform> TARGET_PLATFORMS = List.of(
            ProductPlatformListing.Platform.AMAZON,
            ProductPlatformListing.Platform.FLIPKART,
            ProductPlatformListing.Platform.TATA_NEU,
            ProductPlatformListing.Platform.MYNTRA,
            ProductPlatformListing.Platform.CROMA
    );

    /**
     * Executes non-blocking parallel scraping across all supported platforms
     * using Java Virtual Threads (Project Loom).
     */
    public List<ScrapedPlatformDeal> scrapeProductDealsConcurrently(String productQuery, BigDecimal targetBudget) {
        log.info("Initiating concurrent virtual-thread scraping for query: '{}' with budget: ₹{}", productQuery, targetBudget);

        List<ScrapedPlatformDeal> results = new CopyOnWriteArrayList<>();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<ScrapedPlatformDeal>> tasks = TARGET_PLATFORMS.stream()
                    .map(platform -> (Callable<ScrapedPlatformDeal>) () -> scrapeSinglePlatform(platform, productQuery, targetBudget))
                    .toList();

            List<Future<ScrapedPlatformDeal>> futures = executor.invokeAll(tasks, 5, TimeUnit.SECONDS);

            for (Future<ScrapedPlatformDeal> future : futures) {
                try {
                    if (future.isDone() && !future.isCancelled()) {
                        ScrapedPlatformDeal deal = future.get();
                        if (deal != null) {
                            results.add(deal);
                        }
                    }
                } catch (ExecutionException e) {
                    log.warn("Platform scraping failed for task: {}", e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Parallel scraping interrupted: {}", e.getMessage());
        }

        // Sort by effective price ascending (best deal first)
        return results.stream()
                .sorted(Comparator.comparing(ScrapedPlatformDeal::getEffectivePrice))
                .toList();
    }

    /**
     * Platform-specific data extraction simulating live scraped response with
     * coupon deduction and instant bank discounts.
     */
    private ScrapedPlatformDeal scrapeSinglePlatform(ProductPlatformListing.Platform platform, String query, BigDecimal budget) {
        double jitter = 0.85 + (Math.random() * 0.35); // 85% to 120% of base
        BigDecimal basePrice = budget.multiply(BigDecimal.valueOf(jitter)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal originalPrice = basePrice.multiply(BigDecimal.valueOf(1.20)).setScale(2, RoundingMode.HALF_UP);

        // Calculate coupon / bank discounts
        BigDecimal couponDiscount = platform == ProductPlatformListing.Platform.AMAZON || platform == ProductPlatformListing.Platform.FLIPKART
                ? basePrice.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal bankDiscount = basePrice.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal effectivePrice = basePrice.subtract(couponDiscount).subtract(bankDiscount).max(BigDecimal.valueOf(10.00));

        BigDecimal discountPercent = originalPrice.subtract(effectivePrice)
                .divide(originalPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(1, RoundingMode.HALF_UP);

        BigDecimal rating = BigDecimal.valueOf(3.8 + (Math.random() * 1.1)).setScale(1, RoundingMode.HALF_UP);
        int reviews = 150 + (int)(Math.random() * 4500);

        String delivery = switch (platform) {
            case AMAZON -> "Prime Tomorrow, 11 AM";
            case FLIPKART -> "Express Delivery in 2 Days";
            case TATA_NEU -> "NeuPass Standard (2-3 Days)";
            case CROMA -> "Same-Day Store Pickup / Delivery";
            default -> "Standard Delivery (3-5 Days)";
        };

        return ScrapedPlatformDeal.builder()
                .platform(platform)
                .productTitle(query + " (" + platform.name() + " Verified Edition)")
                .originalPrice(originalPrice)
                .currentPrice(basePrice)
                .couponDiscount(couponDiscount)
                .bankDiscount(bankDiscount)
                .effectivePrice(effectivePrice)
                .discountPercentage(discountPercent)
                .sellerName(platform.name() + " Certified Retailer")
                .sellerRating(rating)
                .deliveryEstimate(delivery)
                .inStock(true)
                .ratingAverage(rating)
                .reviewCount(reviews)
                .productUrl("https://" + platform.name().toLowerCase().replace("_", "") + ".com/deal/" + UUID.randomUUID())
                .scrapedAt(Instant.now())
                .build();
    }

    public static class ScrapedPlatformDeal {
        private ProductPlatformListing.Platform platform;
        private String productTitle;
        private BigDecimal originalPrice;
        private BigDecimal currentPrice;
        private BigDecimal couponDiscount;
        private BigDecimal bankDiscount;
        private BigDecimal effectivePrice;
        private BigDecimal discountPercentage;
        private String sellerName;
        private BigDecimal sellerRating;
        private String deliveryEstimate;
        private boolean inStock;
        private BigDecimal ratingAverage;
        private Integer reviewCount;
        private String productUrl;
        private Instant scrapedAt;

        public ScrapedPlatformDeal() {}
        public ScrapedPlatformDeal(ProductPlatformListing.Platform platform, String productTitle, BigDecimal originalPrice,
                                   BigDecimal currentPrice, BigDecimal couponDiscount, BigDecimal bankDiscount,
                                   BigDecimal effectivePrice, BigDecimal discountPercentage, String sellerName,
                                   BigDecimal sellerRating, String deliveryEstimate, boolean inStock,
                                   BigDecimal ratingAverage, Integer reviewCount, String productUrl, Instant scrapedAt) {
            this.platform = platform;
            this.productTitle = productTitle;
            this.originalPrice = originalPrice;
            this.currentPrice = currentPrice;
            this.couponDiscount = couponDiscount;
            this.bankDiscount = bankDiscount;
            this.effectivePrice = effectivePrice;
            this.discountPercentage = discountPercentage;
            this.sellerName = sellerName;
            this.sellerRating = sellerRating;
            this.deliveryEstimate = deliveryEstimate;
            this.inStock = inStock;
            this.ratingAverage = ratingAverage;
            this.reviewCount = reviewCount;
            this.productUrl = productUrl;
            this.scrapedAt = scrapedAt;
        }

        public ProductPlatformListing.Platform getPlatform() { return platform; }
        public void setPlatform(ProductPlatformListing.Platform platform) { this.platform = platform; }
        public String getProductTitle() { return productTitle; }
        public void setProductTitle(String productTitle) { this.productTitle = productTitle; }
        public BigDecimal getOriginalPrice() { return originalPrice; }
        public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
        public BigDecimal getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
        public BigDecimal getCouponDiscount() { return couponDiscount; }
        public void setCouponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; }
        public BigDecimal getBankDiscount() { return bankDiscount; }
        public void setBankDiscount(BigDecimal bankDiscount) { this.bankDiscount = bankDiscount; }
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
        public String getProductUrl() { return productUrl; }
        public void setProductUrl(String productUrl) { this.productUrl = productUrl; }
        public Instant getScrapedAt() { return scrapedAt; }
        public void setScrapedAt(Instant scrapedAt) { this.scrapedAt = scrapedAt; }

        public static ScrapedPlatformDealBuilder builder() { return new ScrapedPlatformDealBuilder(); }
        public static class ScrapedPlatformDealBuilder {
            private ProductPlatformListing.Platform platform;
            private String productTitle;
            private BigDecimal originalPrice;
            private BigDecimal currentPrice;
            private BigDecimal couponDiscount;
            private BigDecimal bankDiscount;
            private BigDecimal effectivePrice;
            private BigDecimal discountPercentage;
            private String sellerName;
            private BigDecimal sellerRating;
            private String deliveryEstimate;
            private boolean inStock;
            private BigDecimal ratingAverage;
            private Integer reviewCount;
            private String productUrl;
            private Instant scrapedAt;

            public ScrapedPlatformDealBuilder platform(ProductPlatformListing.Platform platform) { this.platform = platform; return this; }
            public ScrapedPlatformDealBuilder productTitle(String productTitle) { this.productTitle = productTitle; return this; }
            public ScrapedPlatformDealBuilder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
            public ScrapedPlatformDealBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
            public ScrapedPlatformDealBuilder couponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; return this; }
            public ScrapedPlatformDealBuilder bankDiscount(BigDecimal bankDiscount) { this.bankDiscount = bankDiscount; return this; }
            public ScrapedPlatformDealBuilder effectivePrice(BigDecimal effectivePrice) { this.effectivePrice = effectivePrice; return this; }
            public ScrapedPlatformDealBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
            public ScrapedPlatformDealBuilder sellerName(String sellerName) { this.sellerName = sellerName; return this; }
            public ScrapedPlatformDealBuilder sellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; return this; }
            public ScrapedPlatformDealBuilder deliveryEstimate(String deliveryEstimate) { this.deliveryEstimate = deliveryEstimate; return this; }
            public ScrapedPlatformDealBuilder inStock(boolean inStock) { this.inStock = inStock; return this; }
            public ScrapedPlatformDealBuilder ratingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; return this; }
            public ScrapedPlatformDealBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
            public ScrapedPlatformDealBuilder productUrl(String productUrl) { this.productUrl = productUrl; return this; }
            public ScrapedPlatformDealBuilder scrapedAt(Instant scrapedAt) { this.scrapedAt = scrapedAt; return this; }

            public ScrapedPlatformDeal build() {
                return new ScrapedPlatformDeal(platform, productTitle, originalPrice, currentPrice, couponDiscount,
                        bankDiscount, effectivePrice, discountPercentage, sellerName, sellerRating, deliveryEstimate,
                        inStock, ratingAverage, reviewCount, productUrl, scrapedAt);
            }
        }
    }
}
