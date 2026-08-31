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

@Entity
@Table(name = "recommendation_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_type", nullable = false, length = 30)
    private RecommendationType recommendationType;

    @Column(name = "query_text", columnDefinition = "TEXT")
    private String queryText;

    @Column(name = "verdict_score")
    private Integer verdictScore;

    @Column(name = "verdict_label", length = 20)
    private String verdictLabel;

    @Column(name = "stretch_budget_suggested", precision = 12, scale = 2)
    private BigDecimal stretchBudgetSuggested;

    @Column(name = "stretch_product_id")
    private Long stretchProductId;

    @Column(name = "reasoning", columnDefinition = "TEXT")
    private String reasoning;

    @Column(name = "ai_model_used", length = 100)
    private String aiModelUsed;

    @Column(name = "confidence_score", precision = 5, scale = 2)
    private BigDecimal confidenceScore;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public RecommendationType getRecommendationType() { return recommendationType; }
    public void setRecommendationType(RecommendationType recommendationType) { this.recommendationType = recommendationType; }
    public String getQueryText() { return queryText; }
    public void setQueryText(String queryText) { this.queryText = queryText; }
    public Integer getVerdictScore() { return verdictScore; }
    public void setVerdictScore(Integer verdictScore) { this.verdictScore = verdictScore; }
    public String getVerdictLabel() { return verdictLabel; }
    public void setVerdictLabel(String verdictLabel) { this.verdictLabel = verdictLabel; }
    public BigDecimal getStretchBudgetSuggested() { return stretchBudgetSuggested; }
    public void setStretchBudgetSuggested(BigDecimal stretchBudgetSuggested) { this.stretchBudgetSuggested = stretchBudgetSuggested; }
    public Long getStretchProductId() { return stretchProductId; }
    public void setStretchProductId(Long stretchProductId) { this.stretchProductId = stretchProductId; }
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    public String getAiModelUsed() { return aiModelUsed; }
    public void setAiModelUsed(String aiModelUsed) { this.aiModelUsed = aiModelUsed; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }

    public static RecommendationLogBuilder builder() { return new RecommendationLogBuilder(); }
    public static class RecommendationLogBuilder {
        private User user;
        private Product product;
        private RecommendationType recommendationType;
        private String queryText;
        private Integer verdictScore;
        private String verdictLabel;
        private BigDecimal stretchBudgetSuggested;
        private Long stretchProductId;
        private String reasoning;
        private String aiModelUsed;
        private BigDecimal confidenceScore;

        public RecommendationLogBuilder user(User user) { this.user = user; return this; }
        public RecommendationLogBuilder product(Product product) { this.product = product; return this; }
        public RecommendationLogBuilder recommendationType(RecommendationType recommendationType) { this.recommendationType = recommendationType; return this; }
        public RecommendationLogBuilder queryText(String queryText) { this.queryText = queryText; return this; }
        public RecommendationLogBuilder verdictScore(Integer verdictScore) { this.verdictScore = verdictScore; return this; }
        public RecommendationLogBuilder verdictLabel(String verdictLabel) { this.verdictLabel = verdictLabel; return this; }
        public RecommendationLogBuilder stretchBudgetSuggested(BigDecimal stretchBudgetSuggested) { this.stretchBudgetSuggested = stretchBudgetSuggested; return this; }
        public RecommendationLogBuilder stretchProductId(Long stretchProductId) { this.stretchProductId = stretchProductId; return this; }
        public RecommendationLogBuilder reasoning(String reasoning) { this.reasoning = reasoning; return this; }
        public RecommendationLogBuilder aiModelUsed(String aiModelUsed) { this.aiModelUsed = aiModelUsed; return this; }
        public RecommendationLogBuilder confidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; return this; }

        public RecommendationLog build() {
            RecommendationLog r = new RecommendationLog();
            r.setUser(user);
            r.setProduct(product);
            r.setRecommendationType(recommendationType);
            r.setQueryText(queryText);
            r.setVerdictScore(verdictScore);
            r.setVerdictLabel(verdictLabel);
            r.setStretchBudgetSuggested(stretchBudgetSuggested);
            r.setStretchProductId(stretchProductId);
            r.setReasoning(reasoning);
            r.setAiModelUsed(aiModelUsed);
            r.setConfidenceScore(confidenceScore);
            return r;
        }
    }

    public enum RecommendationType {
        STRICT_BUDGET, SMART_STRETCH, AI_QUERY, PRICE_DROP_ALERT, SIMILAR_PRODUCT
    }

    public static class SmartStretchRecommendation {
        private Long baseProductId;
        private String baseProductName;
        private BigDecimal basePrice;
        private Integer baseScore;
        private Long upgradeProductId;
        private String upgradeProductName;
        private BigDecimal upgradePrice;
        private Integer upgradeScore;
        private double valueJumpRatio;
        private BigDecimal priceIncreasePercent;
        private String reasoning;

        public SmartStretchRecommendation() {}
        public SmartStretchRecommendation(Long baseProductId, String baseProductName, BigDecimal basePrice, Integer baseScore,
                                          Long upgradeProductId, String upgradeProductName, BigDecimal upgradePrice, Integer upgradeScore,
                                          double valueJumpRatio, BigDecimal priceIncreasePercent, String reasoning) {
            this.baseProductId = baseProductId;
            this.baseProductName = baseProductName;
            this.basePrice = basePrice;
            this.baseScore = baseScore;
            this.upgradeProductId = upgradeProductId;
            this.upgradeProductName = upgradeProductName;
            this.upgradePrice = upgradePrice;
            this.upgradeScore = upgradeScore;
            this.valueJumpRatio = valueJumpRatio;
            this.priceIncreasePercent = priceIncreasePercent;
            this.reasoning = reasoning;
        }

        public Long getBaseProductId() { return baseProductId; }
        public void setBaseProductId(Long baseProductId) { this.baseProductId = baseProductId; }
        public String getBaseProductName() { return baseProductName; }
        public void setBaseProductName(String baseProductName) { this.baseProductName = baseProductName; }
        public BigDecimal getBasePrice() { return basePrice; }
        public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
        public Integer getBaseScore() { return baseScore; }
        public void setBaseScore(Integer baseScore) { this.baseScore = baseScore; }
        public Long getUpgradeProductId() { return upgradeProductId; }
        public void setUpgradeProductId(Long upgradeProductId) { this.upgradeProductId = upgradeProductId; }
        public String getUpgradeProductName() { return upgradeProductName; }
        public void setUpgradeProductName(String upgradeProductName) { this.upgradeProductName = upgradeProductName; }
        public BigDecimal getUpgradePrice() { return upgradePrice; }
        public void setUpgradePrice(BigDecimal upgradePrice) { this.upgradePrice = upgradePrice; }
        public Integer getUpgradeScore() { return upgradeScore; }
        public void setUpgradeScore(Integer upgradeScore) { this.upgradeScore = upgradeScore; }
        public double getValueJumpRatio() { return valueJumpRatio; }
        public void setValueJumpRatio(double valueJumpRatio) { this.valueJumpRatio = valueJumpRatio; }
        public BigDecimal getPriceIncreasePercent() { return priceIncreasePercent; }
        public void setPriceIncreasePercent(BigDecimal priceIncreasePercent) { this.priceIncreasePercent = priceIncreasePercent; }
        public String getReasoning() { return reasoning; }
        public void setReasoning(String reasoning) { this.reasoning = reasoning; }

        public static SmartStretchRecommendationBuilder builder() { return new SmartStretchRecommendationBuilder(); }
        public static class SmartStretchRecommendationBuilder {
            private Long baseProductId;
            private String baseProductName;
            private BigDecimal basePrice;
            private Integer baseScore;
            private Long upgradeProductId;
            private String upgradeProductName;
            private BigDecimal upgradePrice;
            private Integer upgradeScore;
            private double valueJumpRatio;
            private BigDecimal priceIncreasePercent;
            private String reasoning;

            public SmartStretchRecommendationBuilder baseProductId(Long baseProductId) { this.baseProductId = baseProductId; return this; }
            public SmartStretchRecommendationBuilder baseProductName(String baseProductName) { this.baseProductName = baseProductName; return this; }
            public SmartStretchRecommendationBuilder basePrice(BigDecimal basePrice) { this.basePrice = basePrice; return this; }
            public SmartStretchRecommendationBuilder baseScore(Integer baseScore) { this.baseScore = baseScore; return this; }
            public SmartStretchRecommendationBuilder upgradeProductId(Long upgradeProductId) { this.upgradeProductId = upgradeProductId; return this; }
            public SmartStretchRecommendationBuilder upgradeProductName(String upgradeProductName) { this.upgradeProductName = upgradeProductName; return this; }
            public SmartStretchRecommendationBuilder upgradePrice(BigDecimal upgradePrice) { this.upgradePrice = upgradePrice; return this; }
            public SmartStretchRecommendationBuilder upgradeScore(Integer upgradeScore) { this.upgradeScore = upgradeScore; return this; }
            public SmartStretchRecommendationBuilder valueJumpRatio(double valueJumpRatio) { this.valueJumpRatio = valueJumpRatio; return this; }
            public SmartStretchRecommendationBuilder priceIncreasePercent(BigDecimal priceIncreasePercent) { this.priceIncreasePercent = priceIncreasePercent; return this; }
            public SmartStretchRecommendationBuilder reasoning(String reasoning) { this.reasoning = reasoning; return this; }

            public SmartStretchRecommendation build() {
                return new SmartStretchRecommendation(baseProductId, baseProductName, basePrice, baseScore,
                        upgradeProductId, upgradeProductName, upgradePrice, upgradeScore, valueJumpRatio, priceIncreasePercent, reasoning);
            }
        }
    }
}