package com.faircart.dto.comparison;

import com.faircart.entity.ProductPlatformListing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlComparisonResponse {

    private String comparisonId;
    private int totalProductsCompared;
    private String categoryDetected;
    private AIWinnerEvaluation winner;
    private List<ComparedProductItem> products;
    private List<AlternativeBetterDeal> alternativeBetterDeals;
    private String smartUpgradeRecommendation;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparedProductItem {
        private String originalUrl;
        private ProductPlatformListing.Platform sourcePlatform;
        private String productTitle;
        private String category;
        private BigDecimal listedPrice;
        private BigDecimal effectivePrice;
        private BigDecimal totalSavings;
        private BigDecimal bankDiscount;
        private BigDecimal couponDiscount;
        private BigDecimal customerRating;
        private int totalReviews;
        private int genuineReviewPercentage;
        private int intelligenceScore;
        private String verdict;
        private String deliverySpeed;
        private String directBuyUrl;
        private List<String> keyStrengths;
        private List<String> keyLimitations;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AIWinnerEvaluation {
        private String winningProductTitle;
        private ProductPlatformListing.Platform recommendedPlatform;
        private BigDecimal bestEffectivePrice;
        private int intelligenceScore;
        private String justification;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlternativeBetterDeal {
        private String productTitle;
        private ProductPlatformListing.Platform platform;
        private BigDecimal effectivePrice;
        private BigDecimal savingsVsPastedUrl;
        private String reason;
        private String buyUrl;
    }
}