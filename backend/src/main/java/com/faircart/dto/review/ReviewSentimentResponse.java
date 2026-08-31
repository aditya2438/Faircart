package com.faircart.dto.review;

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
public class ReviewSentimentResponse {

    private Long id;
    private Long productId;
    private String platform;
    private Integer totalReviewsAnalyzed;
    private Integer genuineReviewsCount;
    private Integer fakeReviewsDetected;
    private BigDecimal overallSentimentScore;
    private List<String> positiveKeywords;
    private List<String> negativeKeywords;
    private List<String> topPros;
    private List<String> topCons;
    private BigDecimal sellerReliabilityIndex;
    private Integer analysisVersion;
    private java.time.Instant createdAt;
    private java.time.Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public Integer getTotalReviewsAnalyzed() { return totalReviewsAnalyzed; }
    public void setTotalReviewsAnalyzed(Integer totalReviewsAnalyzed) { this.totalReviewsAnalyzed = totalReviewsAnalyzed; }
    public Integer getGenuineReviewsCount() { return genuineReviewsCount; }
    public void setGenuineReviewsCount(Integer genuineReviewsCount) { this.genuineReviewsCount = genuineReviewsCount; }
    public Integer getFakeReviewsDetected() { return fakeReviewsDetected; }
    public void setFakeReviewsDetected(Integer fakeReviewsDetected) { this.fakeReviewsDetected = fakeReviewsDetected; }
    public BigDecimal getOverallSentimentScore() { return overallSentimentScore; }
    public void setOverallSentimentScore(BigDecimal overallSentimentScore) { this.overallSentimentScore = overallSentimentScore; }
    public List<String> getPositiveKeywords() { return positiveKeywords; }
    public void setPositiveKeywords(List<String> positiveKeywords) { this.positiveKeywords = positiveKeywords; }
    public List<String> getNegativeKeywords() { return negativeKeywords; }
    public void setNegativeKeywords(List<String> negativeKeywords) { this.negativeKeywords = negativeKeywords; }
    public List<String> getTopPros() { return topPros; }
    public void setTopPros(List<String> topPros) { this.topPros = topPros; }
    public List<String> getTopCons() { return topCons; }
    public void setTopCons(List<String> topCons) { this.topCons = topCons; }
    public BigDecimal getSellerReliabilityIndex() { return sellerReliabilityIndex; }
    public void setSellerReliabilityIndex(BigDecimal sellerReliabilityIndex) { this.sellerReliabilityIndex = sellerReliabilityIndex; }
    public Integer getAnalysisVersion() { return analysisVersion; }
    public void setAnalysisVersion(Integer analysisVersion) { this.analysisVersion = analysisVersion; }
    public java.time.Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.Instant createdAt) { this.createdAt = createdAt; }
    public java.time.Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.Instant updatedAt) { this.updatedAt = updatedAt; }

    public static ReviewSentimentResponseBuilder builder() { return new ReviewSentimentResponseBuilder(); }
    public static class ReviewSentimentResponseBuilder {
        private Long id;
        private Long productId;
        private String platform;
        private Integer totalReviewsAnalyzed;
        private Integer genuineReviewsCount;
        private Integer fakeReviewsDetected;
        private BigDecimal overallSentimentScore;
        private List<String> positiveKeywords;
        private List<String> negativeKeywords;
        private List<String> topPros;
        private List<String> topCons;
        private BigDecimal sellerReliabilityIndex;
        private Integer analysisVersion;
        private java.time.Instant createdAt;
        private java.time.Instant updatedAt;

        public ReviewSentimentResponseBuilder id(Long id) { this.id = id; return this; }
        public ReviewSentimentResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public ReviewSentimentResponseBuilder platform(String platform) { this.platform = platform; return this; }
        public ReviewSentimentResponseBuilder totalReviewsAnalyzed(Integer totalReviewsAnalyzed) { this.totalReviewsAnalyzed = totalReviewsAnalyzed; return this; }
        public ReviewSentimentResponseBuilder genuineReviewsCount(Integer genuineReviewsCount) { this.genuineReviewsCount = genuineReviewsCount; return this; }
        public ReviewSentimentResponseBuilder fakeReviewsDetected(Integer fakeReviewsDetected) { this.fakeReviewsDetected = fakeReviewsDetected; return this; }
        public ReviewSentimentResponseBuilder overallSentimentScore(BigDecimal overallSentimentScore) { this.overallSentimentScore = overallSentimentScore; return this; }
        public ReviewSentimentResponseBuilder positiveKeywords(List<String> positiveKeywords) { this.positiveKeywords = positiveKeywords; return this; }
        public ReviewSentimentResponseBuilder negativeKeywords(List<String> negativeKeywords) { this.negativeKeywords = negativeKeywords; return this; }
        public ReviewSentimentResponseBuilder topPros(List<String> topPros) { this.topPros = topPros; return this; }
        public ReviewSentimentResponseBuilder topCons(List<String> topCons) { this.topCons = topCons; return this; }
        public ReviewSentimentResponseBuilder sellerReliabilityIndex(BigDecimal sellerReliabilityIndex) { this.sellerReliabilityIndex = sellerReliabilityIndex; return this; }
        public ReviewSentimentResponseBuilder analysisVersion(Integer analysisVersion) { this.analysisVersion = analysisVersion; return this; }
        public ReviewSentimentResponseBuilder createdAt(java.time.Instant createdAt) { this.createdAt = createdAt; return this; }
        public ReviewSentimentResponseBuilder updatedAt(java.time.Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public ReviewSentimentResponse build() {
            ReviewSentimentResponse r = new ReviewSentimentResponse();
            r.setId(id);
            r.setProductId(productId);
            r.setPlatform(platform);
            r.setTotalReviewsAnalyzed(totalReviewsAnalyzed);
            r.setGenuineReviewsCount(genuineReviewsCount);
            r.setFakeReviewsDetected(fakeReviewsDetected);
            r.setOverallSentimentScore(overallSentimentScore);
            r.setPositiveKeywords(positiveKeywords);
            r.setNegativeKeywords(negativeKeywords);
            r.setTopPros(topPros);
            r.setTopCons(topCons);
            r.setSellerReliabilityIndex(sellerReliabilityIndex);
            r.setAnalysisVersion(analysisVersion);
            r.setCreatedAt(createdAt);
            r.setUpdatedAt(updatedAt);
            return r;
        }
    }

    public static ReviewSentimentResponse from(com.faircart.entity.ReviewSentiment sentiment) {
        if (sentiment == null) return null;
        return ReviewSentimentResponse.builder()
                .id(sentiment.getId())
                .productId(sentiment.getProduct() != null ? sentiment.getProduct().getId() : null)
                .platform(sentiment.getPlatform() != null ? sentiment.getPlatform().name() : null)
                .totalReviewsAnalyzed(sentiment.getTotalReviewsAnalyzed())
                .genuineReviewsCount(sentiment.getGenuineReviewsCount())
                .fakeReviewsDetected(sentiment.getFakeReviewsDetected())
                .overallSentimentScore(sentiment.getOverallSentimentScore())
                .positiveKeywords(parseJsonArray(sentiment.getPositiveKeywords()))
                .negativeKeywords(parseJsonArray(sentiment.getNegativeKeywords()))
                .topPros(parseJsonArray(sentiment.getTopPros()))
                .topCons(parseJsonArray(sentiment.getTopCons()))
                .sellerReliabilityIndex(sentiment.getSellerReliabilityIndex())
                .analysisVersion(sentiment.getAnalysisVersion())
                .createdAt(sentiment.getCreatedAt())
                .updatedAt(sentiment.getUpdatedAt())
                .build();
    }

    public static List<ReviewSentimentResponse> from(List<com.faircart.entity.ReviewSentiment> sentiments) {
        if (sentiments == null) return List.of();
        return sentiments.stream()
                .map(ReviewSentimentResponse::from)
                .toList();
    }

    private static List<String> parseJsonArray(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (Exception e) {
            return List.of();
        }
    }
}