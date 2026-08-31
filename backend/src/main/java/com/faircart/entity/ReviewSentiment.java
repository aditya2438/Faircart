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
@Table(name = "review_sentiments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSentiment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, length = 50)
    private ProductPlatformListing.Platform platform;

    @Column(name = "total_reviews_analyzed")
    private Integer totalReviewsAnalyzed = 0;

    @Column(name = "genuine_reviews_count")
    private Integer genuineReviewsCount = 0;

    @Column(name = "fake_reviews_detected")
    private Integer fakeReviewsDetected = 0;

    @Column(name = "overall_sentiment_score", precision = 5, scale = 2)
    private BigDecimal overallSentimentScore;

    @Column(name = "positive_keywords", columnDefinition = "JSON")
    private String positiveKeywords;

    @Column(name = "negative_keywords", columnDefinition = "JSON")
    private String negativeKeywords;

    @Column(name = "top_pros", columnDefinition = "JSON")
    private String topPros;

    @Column(name = "top_cons", columnDefinition = "JSON")
    private String topCons;

    @Column(name = "seller_reliability_index", precision = 5, scale = 2)
    private BigDecimal sellerReliabilityIndex;

    @Column(name = "analysis_version")
    private Integer analysisVersion = 1;

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public ProductPlatformListing.Platform getPlatform() { return platform; }
    public void setPlatform(ProductPlatformListing.Platform platform) { this.platform = platform; }
    public Integer getTotalReviewsAnalyzed() { return totalReviewsAnalyzed; }
    public void setTotalReviewsAnalyzed(Integer totalReviewsAnalyzed) { this.totalReviewsAnalyzed = totalReviewsAnalyzed; }
    public Integer getGenuineReviewsCount() { return genuineReviewsCount; }
    public void setGenuineReviewsCount(Integer genuineReviewsCount) { this.genuineReviewsCount = genuineReviewsCount; }
    public Integer getFakeReviewsDetected() { return fakeReviewsDetected; }
    public void setFakeReviewsDetected(Integer fakeReviewsDetected) { this.fakeReviewsDetected = fakeReviewsDetected; }
    public BigDecimal getOverallSentimentScore() { return overallSentimentScore; }
    public void setOverallSentimentScore(BigDecimal overallSentimentScore) { this.overallSentimentScore = overallSentimentScore; }
    public String getPositiveKeywords() { return positiveKeywords; }
    public void setPositiveKeywords(String positiveKeywords) { this.positiveKeywords = positiveKeywords; }
    public String getNegativeKeywords() { return negativeKeywords; }
    public void setNegativeKeywords(String negativeKeywords) { this.negativeKeywords = negativeKeywords; }
    public String getTopPros() { return topPros; }
    public void setTopPros(String topPros) { this.topPros = topPros; }
    public String getTopCons() { return topCons; }
    public void setTopCons(String topCons) { this.topCons = topCons; }
    public BigDecimal getSellerReliabilityIndex() { return sellerReliabilityIndex; }
    public void setSellerReliabilityIndex(BigDecimal sellerReliabilityIndex) { this.sellerReliabilityIndex = sellerReliabilityIndex; }
    public Integer getAnalysisVersion() { return analysisVersion; }
    public void setAnalysisVersion(Integer analysisVersion) { this.analysisVersion = analysisVersion; }

    public static ReviewSentimentBuilder builder() { return new ReviewSentimentBuilder(); }
    public static class ReviewSentimentBuilder {
        private Product product;
        private ProductPlatformListing.Platform platform;
        private Integer totalReviewsAnalyzed = 0;
        private Integer genuineReviewsCount = 0;
        private Integer fakeReviewsDetected = 0;
        private BigDecimal overallSentimentScore;
        private String positiveKeywords;
        private String negativeKeywords;
        private String topPros;
        private String topCons;
        private BigDecimal sellerReliabilityIndex;
        private Integer analysisVersion = 1;

        public ReviewSentimentBuilder product(Product product) { this.product = product; return this; }
        public ReviewSentimentBuilder platform(ProductPlatformListing.Platform platform) { this.platform = platform; return this; }
        public ReviewSentimentBuilder totalReviewsAnalyzed(Integer totalReviewsAnalyzed) { this.totalReviewsAnalyzed = totalReviewsAnalyzed; return this; }
        public ReviewSentimentBuilder genuineReviewsCount(Integer genuineReviewsCount) { this.genuineReviewsCount = genuineReviewsCount; return this; }
        public ReviewSentimentBuilder fakeReviewsDetected(Integer fakeReviewsDetected) { this.fakeReviewsDetected = fakeReviewsDetected; return this; }
        public ReviewSentimentBuilder overallSentimentScore(BigDecimal overallSentimentScore) { this.overallSentimentScore = overallSentimentScore; return this; }
        public ReviewSentimentBuilder positiveKeywords(String positiveKeywords) { this.positiveKeywords = positiveKeywords; return this; }
        public ReviewSentimentBuilder negativeKeywords(String negativeKeywords) { this.negativeKeywords = negativeKeywords; return this; }
        public ReviewSentimentBuilder topPros(String topPros) { this.topPros = topPros; return this; }
        public ReviewSentimentBuilder topCons(String topCons) { this.topCons = topCons; return this; }
        public ReviewSentimentBuilder sellerReliabilityIndex(BigDecimal sellerReliabilityIndex) { this.sellerReliabilityIndex = sellerReliabilityIndex; return this; }
        public ReviewSentimentBuilder analysisVersion(Integer analysisVersion) { this.analysisVersion = analysisVersion; return this; }

        public ReviewSentiment build() {
            ReviewSentiment r = new ReviewSentiment();
            r.setProduct(product);
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
            return r;
        }
    }
}