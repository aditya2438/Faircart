package com.faircart.service;

import com.faircart.dto.review.ReviewSentimentResponse;
import com.faircart.entity.Product;
import com.faircart.entity.ProductPlatformListing;
import com.faircart.entity.ReviewSentiment;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.ProductRepository;
import com.faircart.repository.ReviewSentimentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewSentimentService {

    private final ReviewSentimentRepository sentimentRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ReviewSentimentResponse createOrUpdateSentiment(ReviewSentimentRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        ReviewSentiment sentiment = sentimentRepository.findByProductAndPlatform(
                product, ProductPlatformListing.Platform.valueOf(request.getPlatform().toUpperCase()))
                .orElseGet(() -> ReviewSentiment.builder()
                        .product(product)
                        .platform(ProductPlatformListing.Platform.valueOf(request.getPlatform().toUpperCase()))
                        .build());

        sentiment.setTotalReviewsAnalyzed(request.getTotalReviewsAnalyzed());
        sentiment.setGenuineReviewsCount(request.getGenuineReviewsCount());
        sentiment.setFakeReviewsDetected(request.getFakeReviewsDetected());
        sentiment.setOverallSentimentScore(request.getOverallSentimentScore());
        sentiment.setPositiveKeywords(toJson(request.getPositiveKeywords()));
        sentiment.setNegativeKeywords(toJson(request.getNegativeKeywords()));
        sentiment.setTopPros(toJson(request.getTopPros()));
        sentiment.setTopCons(toJson(request.getTopCons()));
        sentiment.setSellerReliabilityIndex(request.getSellerReliabilityIndex());
        sentiment.setAnalysisVersion(sentiment.getAnalysisVersion() + 1);

        ReviewSentiment saved = sentimentRepository.save(sentiment);
        return ReviewSentimentResponse.from(saved);
    }

    public ReviewSentimentResponse getSentimentByProductAndPlatform(Long productId, String platform) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        ReviewSentiment sentiment = sentimentRepository.findByProductAndPlatform(
                product, ProductPlatformListing.Platform.valueOf(platform.toUpperCase()))
                .orElseThrow(() -> new ResourceNotFoundException("Sentiment not found for product and platform"));
        return ReviewSentimentResponse.from(sentiment);
    }

    public List<ReviewSentimentResponse> getSentimentsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        List<ReviewSentiment> sentiments = sentimentRepository.findByProduct(product);
        return ReviewSentimentResponse.from(sentiments);
    }

    @Transactional
    public void analyzeSentimentWithAI(Long productId) {
        // This would integrate with Spring AI / LangChain4j to analyze reviews
        // For now, we'll create a placeholder implementation
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Mock sentiment analysis - in production this would call an AI service
        ReviewSentiment sentiment = ReviewSentiment.builder()
                .product(product)
                .platform(ProductPlatformListing.Platform.AMAZON)
                .totalReviewsAnalyzed(100)
                .genuineReviewsCount(85)
                .fakeReviewsDetected(15)
                .overallSentimentScore(new BigDecimal("4.2"))
                .positiveKeywords(toJson(List.of("Great sound quality", "Comfortable fit", "Good battery life", "Fast charging")))
                .negativeKeywords(toJson(List.of("Expensive", "No noise cancellation", "Touch controls sensitive")))
                .topPros(toJson(List.of("Excellent audio clarity", "All-day comfort", "Reliable connectivity", "Quick charge feature")))
                .topCons(toJson(List.of("Premium price point", "Limited ANC performance", "Touch sensitivity issues")))
                .sellerReliabilityIndex(new BigDecimal("4.5"))
                .analysisVersion(1)
                .build();

        sentimentRepository.save(sentiment);
    }

    private String toJson(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return "[]";
        }
    }

    // Inner class for request
    public static class ReviewSentimentRequest {
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
    }
}