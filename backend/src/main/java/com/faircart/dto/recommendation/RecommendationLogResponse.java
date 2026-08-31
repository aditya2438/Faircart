package com.faircart.dto.recommendation;

import com.faircart.entity.RecommendationLog;
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
public class RecommendationLogResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String recommendationType;
    private String queryText;
    private Integer verdictScore;
    private String verdictLabel;
    private BigDecimal stretchBudgetSuggested;
    private Long stretchProductId;
    private String stretchProductName;
    private String reasoning;
    private String aiModelUsed;
    private BigDecimal confidenceScore;
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
    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }
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
    public String getStretchProductName() { return stretchProductName; }
    public void setStretchProductName(String stretchProductName) { this.stretchProductName = stretchProductName; }
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    public String getAiModelUsed() { return aiModelUsed; }
    public void setAiModelUsed(String aiModelUsed) { this.aiModelUsed = aiModelUsed; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static RecommendationLogResponseBuilder builder() { return new RecommendationLogResponseBuilder(); }
    public static class RecommendationLogResponseBuilder {
        private Long id;
        private Long userId;
        private Long productId;
        private String productName;
        private String recommendationType;
        private String queryText;
        private Integer verdictScore;
        private String verdictLabel;
        private BigDecimal stretchBudgetSuggested;
        private Long stretchProductId;
        private String stretchProductName;
        private String reasoning;
        private String aiModelUsed;
        private BigDecimal confidenceScore;
        private Instant createdAt;
        private Instant updatedAt;

        public RecommendationLogResponseBuilder id(Long id) { this.id = id; return this; }
        public RecommendationLogResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public RecommendationLogResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public RecommendationLogResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public RecommendationLogResponseBuilder recommendationType(String recommendationType) { this.recommendationType = recommendationType; return this; }
        public RecommendationLogResponseBuilder queryText(String queryText) { this.queryText = queryText; return this; }
        public RecommendationLogResponseBuilder verdictScore(Integer verdictScore) { this.verdictScore = verdictScore; return this; }
        public RecommendationLogResponseBuilder verdictLabel(String verdictLabel) { this.verdictLabel = verdictLabel; return this; }
        public RecommendationLogResponseBuilder stretchBudgetSuggested(BigDecimal stretchBudgetSuggested) { this.stretchBudgetSuggested = stretchBudgetSuggested; return this; }
        public RecommendationLogResponseBuilder stretchProductId(Long stretchProductId) { this.stretchProductId = stretchProductId; return this; }
        public RecommendationLogResponseBuilder stretchProductName(String stretchProductName) { this.stretchProductName = stretchProductName; return this; }
        public RecommendationLogResponseBuilder reasoning(String reasoning) { this.reasoning = reasoning; return this; }
        public RecommendationLogResponseBuilder aiModelUsed(String aiModelUsed) { this.aiModelUsed = aiModelUsed; return this; }
        public RecommendationLogResponseBuilder confidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public RecommendationLogResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public RecommendationLogResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public RecommendationLogResponse build() {
            RecommendationLogResponse r = new RecommendationLogResponse();
            r.setId(id);
            r.setUserId(userId);
            r.setProductId(productId);
            r.setProductName(productName);
            r.setRecommendationType(recommendationType);
            r.setQueryText(queryText);
            r.setVerdictScore(verdictScore);
            r.setVerdictLabel(verdictLabel);
            r.setStretchBudgetSuggested(stretchBudgetSuggested);
            r.setStretchProductId(stretchProductId);
            r.setStretchProductName(stretchProductName);
            r.setReasoning(reasoning);
            r.setAiModelUsed(aiModelUsed);
            r.setConfidenceScore(confidenceScore);
            r.setCreatedAt(createdAt);
            r.setUpdatedAt(updatedAt);
            return r;
        }
    }

    public static RecommendationLogResponse from(RecommendationLog log) {
        if (log == null) return null;
        return RecommendationLogResponse.builder()
                .id(log.getId())
                .userId(log.getUser() != null ? log.getUser().getId() : null)
                .productId(log.getProduct() != null ? log.getProduct().getId() : null)
                .productName(log.getProduct() != null ? log.getProduct().getName() : null)
                .recommendationType(log.getRecommendationType() != null ? log.getRecommendationType().name() : null)
                .queryText(log.getQueryText())
                .verdictScore(log.getVerdictScore())
                .verdictLabel(log.getVerdictLabel())
                .stretchBudgetSuggested(log.getStretchBudgetSuggested())
                .stretchProductId(log.getStretchProductId())
                .stretchProductName(null) // Would need to fetch separately
                .reasoning(log.getReasoning())
                .aiModelUsed(log.getAiModelUsed())
                .confidenceScore(log.getConfidenceScore())
                .createdAt(log.getCreatedAt())
                .updatedAt(log.getUpdatedAt())
                .build();
    }

    public static List<RecommendationLogResponse> from(List<RecommendationLog> logs) {
        if (logs == null) return List.of();
        return logs.stream()
                .map(RecommendationLogResponse::from)
                .toList();
    }
}