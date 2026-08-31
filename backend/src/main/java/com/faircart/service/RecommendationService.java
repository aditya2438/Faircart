package com.faircart.service;

import com.faircart.dto.recommendation.RecommendationLogResponse;
import com.faircart.entity.*;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationLogRepository recommendationRepository;
    private final ProductRepository productRepository;
    private final ProductPlatformListingRepository listingRepository;
    private final ReviewSentimentRepository sentimentRepository;
    private final UserRepository userRepository;
    private final TrackedProductRepository trackedRepository;

    /**
     * Core recommendation engine implementing "Smart Stretch" algorithm
     */
    @Transactional
    public RecommendationResult getRecommendations(String userEmail, RecommendationRequest request) {
        User user = userRepository.findByEmail(userEmail).orElse(null);

        BigDecimal budget = request.getBudget();
        BigDecimal stretchLimit = budget.multiply(new BigDecimal("1.25")); // 25% stretch max
        String category = request.getCategory();
        List<String> preferredPlatforms = request.getPreferredPlatforms();

        // 1. Get products within strict budget
        List<Product> budgetProducts = findProductsInBudget(budget, category, preferredPlatforms);
        
        // 2. Get products in stretch range (budget to 25% above)
        List<Product> stretchProducts = findProductsInStretchRange(budget, stretchLimit, category, preferredPlatforms);

        // 3. Score all products
        List<ScoredProduct> scoredBudget = scoreProducts(budgetProducts, false);
        List<ScoredProduct> scoredStretch = scoreProducts(stretchProducts, true);

        // 4. Find best strict budget match
        ScoredProduct bestBudget = scoredBudget.stream()
                .max(Comparator.comparing(ScoredProduct::getCompositeScore))
                .orElse(null);

        // 5. Find best stretch upgrade
        ScoredProduct bestStretch = scoredStretch.stream()
                .max(Comparator.comparing(ScoredProduct::getValueDelta))
                .orElse(null);

        // 6. Generate Smart Stretch recommendation if significant value jump
        RecommendationLog.SmartStretchRecommendation stretchRec = null;
        if (bestBudget != null && bestStretch != null) {
            double valueJump = calculateValueJump(bestBudget, bestStretch);
            if (valueJump > 1.5) { // 50% better value for 25% more cost
                stretchRec = RecommendationLog.SmartStretchRecommendation.builder()
                        .baseProductId(bestBudget.getProduct().getId())
                        .baseProductName(bestBudget.getProduct().getName())
                        .basePrice(bestBudget.getLowestPrice())
                        .baseScore(bestBudget.getCompositeScore())
                        .upgradeProductId(bestStretch.getProduct().getId())
                        .upgradeProductName(bestStretch.getProduct().getName())
                        .upgradePrice(bestStretch.getLowestPrice())
                        .upgradeScore(bestStretch.getCompositeScore())
                        .valueJumpRatio(valueJump)
                        .priceIncreasePercent(bestStretch.getLowestPrice()
                                .subtract(bestBudget.getLowestPrice())
                                .divide(bestBudget.getLowestPrice(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100")))
                        .reasoning(generateStretchReasoning(bestBudget, bestStretch))
                        .build();
            }
        }

        // 7. Create verdict
        String verdictLabel = determineVerdict(bestBudget);
        Integer verdictScore = bestBudget != null ? bestBudget.getCompositeScore() : 0;

        // 8. Log recommendation if user is authenticated
        if (user != null && bestBudget != null) {
            RecommendationLog log = RecommendationLog.builder()
                    .user(user)
                    .product(bestBudget.getProduct())
                    .recommendationType(RecommendationLog.RecommendationType.STRICT_BUDGET)
                    .queryText(request.getQueryText())
                    .verdictScore(verdictScore)
                    .verdictLabel(verdictLabel)
                    .stretchBudgetSuggested(stretchRec != null ? bestStretch.getLowestPrice() : null)
                    .stretchProductId(stretchRec != null ? bestStretch.getProduct().getId() : null)
                    .reasoning(stretchRec != null ? stretchRec.getReasoning() : "Best match within budget")
                    .aiModelUsed("FairCart-Recommendation-Engine-v1")
                    .confidenceScore(new BigDecimal(Math.min(0.95, 0.5 + (verdictScore / 200.0))))
                    .build();
            recommendationRepository.save(log);

            // 9. If stretch recommendation, log it too
            if (stretchRec != null) {
                RecommendationLog stretchLog = RecommendationLog.builder()
                        .user(user)
                        .product(bestStretch.getProduct())
                        .recommendationType(RecommendationLog.RecommendationType.SMART_STRETCH)
                        .queryText(request.getQueryText())
                        .verdictScore(bestStretch.getCompositeScore())
                        .verdictLabel("SMART UPGRADE")
                        .stretchBudgetSuggested(bestStretch.getLowestPrice())
                        .stretchProductId(bestStretch.getProduct().getId())
                        .reasoning(stretchRec.getReasoning())
                        .aiModelUsed("FairCart-Recommendation-Engine-v1")
                        .confidenceScore(new BigDecimal("0.9"))
                        .build();
                recommendationRepository.save(stretchLog);
            }
        }

        return RecommendationResult.builder()
                .bestBudgetMatch(bestBudget != null ? toResponse(bestBudget) : null)
                .smartStretchUpgrade(stretchRec)
                .verdictScore(verdictScore)
                .verdictLabel(verdictLabel)
                .allBudgetOptions(scoredBudget.stream().limit(5).map(this::toResponse).collect(Collectors.toList()))
                .allStretchOptions(scoredStretch.stream().limit(3).map(this::toResponse).collect(Collectors.toList()))
                .build();
    }

    /**
     * AI-powered natural language query processing
     */
    @Transactional
    public RecommendationResult processAIQuery(String userEmail, String query) {
        User user = userRepository.findByEmail(userEmail).orElse(null);

        // Parse intent from query (in production, use Spring AI / LLM)
        QueryIntent intent = parseQueryIntent(query);
        
        // Execute search based on intent
        RecommendationRequest request = RecommendationRequest.builder()
                .budget(intent.getBudget())
                .category(intent.getCategory())
                .preferredPlatforms(intent.getPlatforms())
                .queryText(query)
                .build();

        RecommendationResult result = getRecommendations(userEmail, request);
        
        // Update log with AI query type
        if (user != null && result.getBestBudgetMatch() != null) {
            productRepository.findById(result.getBestBudgetMatch().getProductId()).ifPresent(prod -> {
                Optional<RecommendationLog> latestLog = recommendationRepository
                        .findByUserAndProduct(user, prod)
                        .stream().findFirst();
                latestLog.ifPresent(log -> {
                    log.setRecommendationType(RecommendationLog.RecommendationType.AI_QUERY);
                    recommendationRepository.save(log);
                });
            });
        }

        return result;
    }

    private List<Product> findProductsInBudget(BigDecimal budget, String category, List<String> platforms) {
        // In production, this would query with platform price joins
        return productRepository.findByStatus(Product.ProductStatus.ACTIVE);
    }

    private List<Product> findProductsInStretchRange(BigDecimal budget, BigDecimal stretchLimit, String category, List<String> platforms) {
        return productRepository.findByStatus(Product.ProductStatus.ACTIVE);
    }

    private List<ScoredProduct> scoreProducts(List<Product> products, boolean isStretch) {
        return products.parallelStream()
                .map(product -> {
                    BigDecimal lowestPrice = getLowestEffectivePrice(product);
                    if (lowestPrice == null) return null;
                    
                    int priceScore = calculatePriceScore(product, lowestPrice);
                    int ratingScore = calculateRatingScore(product);
                    int sentimentScore = calculateSentimentScore(product);
                    int sellerScore = calculateSellerScore(product);
                    int availabilityScore = calculateAvailabilityScore(product);
                    int priceHistoryScore = calculatePriceHistoryScore(product);
                    
                    // Weighted composite score (0-100)
                    int composite = (int) Math.round(
                            priceScore * 0.25 +
                            ratingScore * 0.20 +
                            sentimentScore * 0.20 +
                            sellerScore * 0.15 +
                            availabilityScore * 0.10 +
                            priceHistoryScore * 0.10
                    );
                    
                    // Value delta for stretch products
                    double valueDelta = isStretch ? composite / (lowestPrice.doubleValue() + 1) : 0;
                    
                    return ScoredProduct.builder()
                            .product(product)
                            .lowestPrice(lowestPrice)
                            .priceScore(priceScore)
                            .ratingScore(ratingScore)
                            .sentimentScore(sentimentScore)
                            .sellerScore(sellerScore)
                            .availabilityScore(availabilityScore)
                            .priceHistoryScore(priceHistoryScore)
                            .compositeScore(composite)
                            .valueDelta(valueDelta)
                            .build();
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ScoredProduct::getCompositeScore).reversed())
                .collect(Collectors.toList());
    }

    private BigDecimal getLowestEffectivePrice(Product product) {
        List<ProductPlatformListing> listings = listingRepository
                .findInStockByProductOrderByEffectivePriceAsc(product);
        return listings.isEmpty() ? product.getPrice() : listings.get(0).getEffectivePrice();
    }

    private int calculatePriceScore(Product product, BigDecimal lowestPrice) {
        // Compare against category average (mock)
        BigDecimal categoryAvg = new BigDecimal("5000"); // Would be computed from data
        double ratio = categoryAvg.subtract(lowestPrice).divide(categoryAvg, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return (int) Math.max(0, Math.min(100, ratio * 100));
    }

    private int calculateRatingScore(Product product) {
        // Would use actual platform ratings
        return 75; // Mock
    }

    private int calculateSentimentScore(Product product) {
        List<ReviewSentiment> sentiments = sentimentRepository.findByProduct(product);
        if (sentiments.isEmpty()) return 50;
        double avg = sentiments.stream()
                .map(s -> s.getOverallSentimentScore() != null ? s.getOverallSentimentScore().doubleValue() : 0)
                .mapToDouble(d -> d)
                .average().orElse(2.5);
        return (int) (avg * 20); // Convert 5-scale to 100-scale
    }

    private int calculateSellerScore(Product product) {
        List<ProductPlatformListing> listings = listingRepository.findByProduct(product);
        if (listings.isEmpty()) return 50;
        double avg = listings.stream()
                .map(l -> l.getSellerRating() != null ? l.getSellerRating().doubleValue() : 0)
                .mapToDouble(d -> d)
                .average().orElse(3.0);
        return (int) (avg * 20);
    }

    private int calculateAvailabilityScore(Product product) {
        List<ProductPlatformListing> listings = listingRepository.findByProduct(product);
        long inStock = listings.stream().filter(ProductPlatformListing::isInStock).count();
        return listings.isEmpty() ? 0 : (int) ((double) inStock / listings.size() * 100);
    }

    private int calculatePriceHistoryScore(Product product) {
        // Would check if current price is near historical low
        return 60; // Mock
    }

    private double calculateValueJump(ScoredProduct base, ScoredProduct upgrade) {
        double baseValue = base.getCompositeScore() / (base.getLowestPrice().doubleValue() + 1);
        double upgradeValue = upgrade.getCompositeScore() / (upgrade.getLowestPrice().doubleValue() + 1);
        return upgradeValue / baseValue;
    }

    private String generateStretchReasoning(ScoredProduct base, ScoredProduct upgrade) {
        StringBuilder sb = new StringBuilder();
        sb.append("Spending ").append(upgrade.getLowestPrice().subtract(base.getLowestPrice()))
                .append(" more gets you ").append((int)(calculateValueJump(base, upgrade) * 100 - 100))
                .append("% better value. ");
        
        if (upgrade.getRatingScore() > base.getRatingScore() + 10) {
            sb.append("Significantly higher user ratings (").append(upgrade.getRatingScore()).append(" vs ").append(base.getRatingScore()).append("). ");
        }
        if (upgrade.getSentimentScore() > base.getSentimentScore() + 10) {
            sb.append("Much better review sentiment. ");
        }
        if (upgrade.getSellerScore() > base.getSellerScore() + 10) {
            sb.append("More reliable sellers. ");
        }
        return sb.toString();
    }

    private String determineVerdict(ScoredProduct best) {
        if (best == null) return "INSUFFICIENT_DATA";
        if (best.getCompositeScore() >= 80) return "BUY_NOW";
        if (best.getCompositeScore() >= 60) return "GOOD_CHOICE";
        if (best.getCompositeScore() >= 40) return "WAIT_FOR_SALE";
        return "DO_NOT_BUY";
    }

    private QueryIntent parseQueryIntent(String query) {
        // Simplified parsing - in production use LLM
        QueryIntent intent = new QueryIntent();
        intent.setQueryText(query);
        
        // Extract budget (look for numbers with currency)
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(?:under|below|max|budget)\\s*(?:₹|rs|inr)?\\s*(\\d+)").matcher(query.toLowerCase());
        if (m.find()) {
            intent.setBudget(new BigDecimal(m.group(1)));
        }
        
        // Extract category keywords
        if (query.toLowerCase().contains("earphone") || query.toLowerCase().contains("headphone")) {
            intent.setCategory("Audio");
        } else if (query.toLowerCase().contains("phone") || query.toLowerCase().contains("mobile")) {
            intent.setCategory("Mobile");
        }
        
        return intent;
    }

    private ProductRecommendationResponse toResponse(ScoredProduct sp) {
        return ProductRecommendationResponse.builder()
                .productId(sp.getProduct().getId())
                .productName(sp.getProduct().getName())
                .lowestPrice(sp.getLowestPrice())
                .compositeScore(sp.getCompositeScore())
                .priceScore(sp.getPriceScore())
                .ratingScore(sp.getRatingScore())
                .sentimentScore(sp.getSentimentScore())
                .sellerScore(sp.getSellerScore())
                .availabilityScore(sp.getAvailabilityScore())
                .priceHistoryScore(sp.getPriceHistoryScore())
                .build();
    }

    // Inner classes
    public static class RecommendationRequest {
        private BigDecimal budget;
        private String category;
        private List<String> preferredPlatforms;
        private String queryText;

        public RecommendationRequest() {}
        public RecommendationRequest(BigDecimal budget, String category, List<String> preferredPlatforms, String queryText) {
            this.budget = budget;
            this.category = category;
            this.preferredPlatforms = preferredPlatforms;
            this.queryText = queryText;
        }

        public BigDecimal getBudget() { return budget; }
        public void setBudget(BigDecimal budget) { this.budget = budget; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getPreferredPlatforms() { return preferredPlatforms; }
        public void setPreferredPlatforms(List<String> preferredPlatforms) { this.preferredPlatforms = preferredPlatforms; }
        public String getQueryText() { return queryText; }
        public void setQueryText(String queryText) { this.queryText = queryText; }

        public static RecommendationRequestBuilder builder() { return new RecommendationRequestBuilder(); }
        public static class RecommendationRequestBuilder {
            private BigDecimal budget;
            private String category;
            private List<String> preferredPlatforms;
            private String queryText;
            public RecommendationRequestBuilder budget(BigDecimal budget) { this.budget = budget; return this; }
            public RecommendationRequestBuilder category(String category) { this.category = category; return this; }
            public RecommendationRequestBuilder preferredPlatforms(List<String> preferredPlatforms) { this.preferredPlatforms = preferredPlatforms; return this; }
            public RecommendationRequestBuilder queryText(String queryText) { this.queryText = queryText; return this; }
            public RecommendationRequest build() { return new RecommendationRequest(budget, category, preferredPlatforms, queryText); }
        }
    }

    public static class RecommendationResult {
        private ProductRecommendationResponse bestBudgetMatch;
        private RecommendationLog.SmartStretchRecommendation smartStretchUpgrade;
        private Integer verdictScore;
        private String verdictLabel;
        private List<ProductRecommendationResponse> allBudgetOptions;
        private List<ProductRecommendationResponse> allStretchOptions;

        public RecommendationResult() {}
        public RecommendationResult(ProductRecommendationResponse bestBudgetMatch, RecommendationLog.SmartStretchRecommendation smartStretchUpgrade, Integer verdictScore, String verdictLabel, List<ProductRecommendationResponse> allBudgetOptions, List<ProductRecommendationResponse> allStretchOptions) {
            this.bestBudgetMatch = bestBudgetMatch;
            this.smartStretchUpgrade = smartStretchUpgrade;
            this.verdictScore = verdictScore;
            this.verdictLabel = verdictLabel;
            this.allBudgetOptions = allBudgetOptions;
            this.allStretchOptions = allStretchOptions;
        }

        public ProductRecommendationResponse getBestBudgetMatch() { return bestBudgetMatch; }
        public void setBestBudgetMatch(ProductRecommendationResponse bestBudgetMatch) { this.bestBudgetMatch = bestBudgetMatch; }
        public RecommendationLog.SmartStretchRecommendation getSmartStretchUpgrade() { return smartStretchUpgrade; }
        public void setSmartStretchUpgrade(RecommendationLog.SmartStretchRecommendation smartStretchUpgrade) { this.smartStretchUpgrade = smartStretchUpgrade; }
        public Integer getVerdictScore() { return verdictScore; }
        public void setVerdictScore(Integer verdictScore) { this.verdictScore = verdictScore; }
        public String getVerdictLabel() { return verdictLabel; }
        public void setVerdictLabel(String verdictLabel) { this.verdictLabel = verdictLabel; }
        public List<ProductRecommendationResponse> getAllBudgetOptions() { return allBudgetOptions; }
        public void setAllBudgetOptions(List<ProductRecommendationResponse> allBudgetOptions) { this.allBudgetOptions = allBudgetOptions; }
        public List<ProductRecommendationResponse> getAllStretchOptions() { return allStretchOptions; }
        public void setAllStretchOptions(List<ProductRecommendationResponse> allStretchOptions) { this.allStretchOptions = allStretchOptions; }

        public static RecommendationResultBuilder builder() { return new RecommendationResultBuilder(); }
        public static class RecommendationResultBuilder {
            private ProductRecommendationResponse bestBudgetMatch;
            private RecommendationLog.SmartStretchRecommendation smartStretchUpgrade;
            private Integer verdictScore;
            private String verdictLabel;
            private List<ProductRecommendationResponse> allBudgetOptions;
            private List<ProductRecommendationResponse> allStretchOptions;
            public RecommendationResultBuilder bestBudgetMatch(ProductRecommendationResponse bestBudgetMatch) { this.bestBudgetMatch = bestBudgetMatch; return this; }
            public RecommendationResultBuilder smartStretchUpgrade(RecommendationLog.SmartStretchRecommendation smartStretchUpgrade) { this.smartStretchUpgrade = smartStretchUpgrade; return this; }
            public RecommendationResultBuilder verdictScore(Integer verdictScore) { this.verdictScore = verdictScore; return this; }
            public RecommendationResultBuilder verdictLabel(String verdictLabel) { this.verdictLabel = verdictLabel; return this; }
            public RecommendationResultBuilder allBudgetOptions(List<ProductRecommendationResponse> allBudgetOptions) { this.allBudgetOptions = allBudgetOptions; return this; }
            public RecommendationResultBuilder allStretchOptions(List<ProductRecommendationResponse> allStretchOptions) { this.allStretchOptions = allStretchOptions; return this; }
            public RecommendationResult build() { return new RecommendationResult(bestBudgetMatch, smartStretchUpgrade, verdictScore, verdictLabel, allBudgetOptions, allStretchOptions); }
        }
    }

    public static class ProductRecommendationResponse {
        private Long productId;
        private String productName;
        private BigDecimal lowestPrice;
        private Integer compositeScore;
        private Integer priceScore;
        private Integer ratingScore;
        private Integer sentimentScore;
        private Integer sellerScore;
        private Integer availabilityScore;
        private Integer priceHistoryScore;

        public ProductRecommendationResponse() {}
        public ProductRecommendationResponse(Long productId, String productName, BigDecimal lowestPrice, Integer compositeScore, Integer priceScore, Integer ratingScore, Integer sentimentScore, Integer sellerScore, Integer availabilityScore, Integer priceHistoryScore) {
            this.productId = productId;
            this.productName = productName;
            this.lowestPrice = lowestPrice;
            this.compositeScore = compositeScore;
            this.priceScore = priceScore;
            this.ratingScore = ratingScore;
            this.sentimentScore = sentimentScore;
            this.sellerScore = sellerScore;
            this.availabilityScore = availabilityScore;
            this.priceHistoryScore = priceHistoryScore;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public BigDecimal getLowestPrice() { return lowestPrice; }
        public void setLowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; }
        public Integer getCompositeScore() { return compositeScore; }
        public void setCompositeScore(Integer compositeScore) { this.compositeScore = compositeScore; }
        public Integer getPriceScore() { return priceScore; }
        public void setPriceScore(Integer priceScore) { this.priceScore = priceScore; }
        public Integer getRatingScore() { return ratingScore; }
        public void setRatingScore(Integer ratingScore) { this.ratingScore = ratingScore; }
        public Integer getSentimentScore() { return sentimentScore; }
        public void setSentimentScore(Integer sentimentScore) { this.sentimentScore = sentimentScore; }
        public Integer getSellerScore() { return sellerScore; }
        public void setSellerScore(Integer sellerScore) { this.sellerScore = sellerScore; }
        public Integer getAvailabilityScore() { return availabilityScore; }
        public void setAvailabilityScore(Integer availabilityScore) { this.availabilityScore = availabilityScore; }
        public Integer getPriceHistoryScore() { return priceHistoryScore; }
        public void setPriceHistoryScore(Integer priceHistoryScore) { this.priceHistoryScore = priceHistoryScore; }

        public static ProductRecommendationResponseBuilder builder() { return new ProductRecommendationResponseBuilder(); }
        public static class ProductRecommendationResponseBuilder {
            private Long productId;
            private String productName;
            private BigDecimal lowestPrice;
            private Integer compositeScore;
            private Integer priceScore;
            private Integer ratingScore;
            private Integer sentimentScore;
            private Integer sellerScore;
            private Integer availabilityScore;
            private Integer priceHistoryScore;
            public ProductRecommendationResponseBuilder productId(Long productId) { this.productId = productId; return this; }
            public ProductRecommendationResponseBuilder productName(String productName) { this.productName = productName; return this; }
            public ProductRecommendationResponseBuilder lowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; return this; }
            public ProductRecommendationResponseBuilder compositeScore(Integer compositeScore) { this.compositeScore = compositeScore; return this; }
            public ProductRecommendationResponseBuilder priceScore(Integer priceScore) { this.priceScore = priceScore; return this; }
            public ProductRecommendationResponseBuilder ratingScore(Integer ratingScore) { this.ratingScore = ratingScore; return this; }
            public ProductRecommendationResponseBuilder sentimentScore(Integer sentimentScore) { this.sentimentScore = sentimentScore; return this; }
            public ProductRecommendationResponseBuilder sellerScore(Integer sellerScore) { this.sellerScore = sellerScore; return this; }
            public ProductRecommendationResponseBuilder availabilityScore(Integer availabilityScore) { this.availabilityScore = availabilityScore; return this; }
            public ProductRecommendationResponseBuilder priceHistoryScore(Integer priceHistoryScore) { this.priceHistoryScore = priceHistoryScore; return this; }
            public ProductRecommendationResponse build() { return new ProductRecommendationResponse(productId, productName, lowestPrice, compositeScore, priceScore, ratingScore, sentimentScore, sellerScore, availabilityScore, priceHistoryScore); }
        }
    }

    public static class ScoredProduct {
        private Product product;
        private BigDecimal lowestPrice;
        private int priceScore;
        private int ratingScore;
        private int sentimentScore;
        private int sellerScore;
        private int availabilityScore;
        private int priceHistoryScore;
        private int compositeScore;
        private double valueDelta;

        public ScoredProduct() {}
        public ScoredProduct(Product product, BigDecimal lowestPrice, int priceScore, int ratingScore, int sentimentScore, int sellerScore, int availabilityScore, int priceHistoryScore, int compositeScore, double valueDelta) {
            this.product = product;
            this.lowestPrice = lowestPrice;
            this.priceScore = priceScore;
            this.ratingScore = ratingScore;
            this.sentimentScore = sentimentScore;
            this.sellerScore = sellerScore;
            this.availabilityScore = availabilityScore;
            this.priceHistoryScore = priceHistoryScore;
            this.compositeScore = compositeScore;
            this.valueDelta = valueDelta;
        }

        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }
        public BigDecimal getLowestPrice() { return lowestPrice; }
        public void setLowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; }
        public int getPriceScore() { return priceScore; }
        public void setPriceScore(int priceScore) { this.priceScore = priceScore; }
        public int getRatingScore() { return ratingScore; }
        public void setRatingScore(int ratingScore) { this.ratingScore = ratingScore; }
        public int getSentimentScore() { return sentimentScore; }
        public void setSentimentScore(int sentimentScore) { this.sentimentScore = sentimentScore; }
        public int getSellerScore() { return sellerScore; }
        public void setSellerScore(int sellerScore) { this.sellerScore = sellerScore; }
        public int getAvailabilityScore() { return availabilityScore; }
        public void setAvailabilityScore(int availabilityScore) { this.availabilityScore = availabilityScore; }
        public int getPriceHistoryScore() { return priceHistoryScore; }
        public void setPriceHistoryScore(int priceHistoryScore) { this.priceHistoryScore = priceHistoryScore; }
        public int getCompositeScore() { return compositeScore; }
        public void setCompositeScore(int compositeScore) { this.compositeScore = compositeScore; }
        public double getValueDelta() { return valueDelta; }
        public void setValueDelta(double valueDelta) { this.valueDelta = valueDelta; }

        public static ScoredProductBuilder builder() { return new ScoredProductBuilder(); }
        public static class ScoredProductBuilder {
            private Product product;
            private BigDecimal lowestPrice;
            private int priceScore;
            private int ratingScore;
            private int sentimentScore;
            private int sellerScore;
            private int availabilityScore;
            private int priceHistoryScore;
            private int compositeScore;
            private double valueDelta;
            public ScoredProductBuilder product(Product product) { this.product = product; return this; }
            public ScoredProductBuilder lowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; return this; }
            public ScoredProductBuilder priceScore(int priceScore) { this.priceScore = priceScore; return this; }
            public ScoredProductBuilder ratingScore(int ratingScore) { this.ratingScore = ratingScore; return this; }
            public ScoredProductBuilder sentimentScore(int sentimentScore) { this.sentimentScore = sentimentScore; return this; }
            public ScoredProductBuilder sellerScore(int sellerScore) { this.sellerScore = sellerScore; return this; }
            public ScoredProductBuilder availabilityScore(int availabilityScore) { this.availabilityScore = availabilityScore; return this; }
            public ScoredProductBuilder priceHistoryScore(int priceHistoryScore) { this.priceHistoryScore = priceHistoryScore; return this; }
            public ScoredProductBuilder compositeScore(int compositeScore) { this.compositeScore = compositeScore; return this; }
            public ScoredProductBuilder valueDelta(double valueDelta) { this.valueDelta = valueDelta; return this; }
            public ScoredProduct build() { return new ScoredProduct(product, lowestPrice, priceScore, ratingScore, sentimentScore, sellerScore, availabilityScore, priceHistoryScore, compositeScore, valueDelta); }
        }
    }

    public static class QueryIntent {
        private String queryText;
        private BigDecimal budget;
        private String category;
        private List<String> platforms;

        public QueryIntent() {}
        public QueryIntent(String queryText, BigDecimal budget, String category, List<String> platforms) {
            this.queryText = queryText;
            this.budget = budget;
            this.category = category;
            this.platforms = platforms;
        }

        public String getQueryText() { return queryText; }
        public void setQueryText(String queryText) { this.queryText = queryText; }
        public BigDecimal getBudget() { return budget; }
        public void setBudget(BigDecimal budget) { this.budget = budget; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getPlatforms() { return platforms; }
        public void setPlatforms(List<String> platforms) { this.platforms = platforms; }

        public static QueryIntentBuilder builder() { return new QueryIntentBuilder(); }
        public static class QueryIntentBuilder {
            private String queryText;
            private BigDecimal budget;
            private String category;
            private List<String> platforms;
            public QueryIntentBuilder queryText(String queryText) { this.queryText = queryText; return this; }
            public QueryIntentBuilder budget(BigDecimal budget) { this.budget = budget; return this; }
            public QueryIntentBuilder category(String category) { this.category = category; return this; }
            public QueryIntentBuilder platforms(List<String> platforms) { this.platforms = platforms; return this; }
            public QueryIntent build() { return new QueryIntent(queryText, budget, category, platforms); }
        }
    }
}