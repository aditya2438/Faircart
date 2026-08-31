package com.faircart.service;

import com.faircart.dto.product.ProductResponse;
import com.faircart.entity.Product;
import com.faircart.entity.ProductPlatformListing;
import com.faircart.entity.ReviewSentiment;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.ProductPlatformListingRepository;
import com.faircart.repository.ProductRepository;
import com.faircart.repository.ReviewSentimentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductComparisonService {

    private final ProductRepository productRepository;
    private final ProductPlatformListingRepository listingRepository;
    private final ReviewSentimentRepository sentimentRepository;

    public ComparisonResult compareProducts(List<Long> productIds) {
        if (productIds == null || productIds.size() < 2) {
            throw new IllegalArgumentException("At least 2 products required for comparison");
        }
        if (productIds.size() > 4) {
            throw new IllegalArgumentException("Maximum 4 products can be compared at once");
        }

        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ResourceNotFoundException("One or more products not found");
        }

        List<ComparisonProduct> comparisonProducts = products.stream()
                .map(this::buildComparisonProduct)
                .collect(Collectors.toList());

        // Determine best in each category
        determineBestProducts(comparisonProducts);

        return ComparisonResult.builder()
                .products(comparisonProducts)
                .bestOverall(findBestOverall(comparisonProducts))
                .bestValue(findBestValue(comparisonProducts))
                .bestRating(findBestRating(comparisonProducts))
                .bestSentiment(findBestSentiment(comparisonProducts))
                .build();
    }

    private ComparisonProduct buildComparisonProduct(Product product) {
        // Get platform listings
        List<ProductPlatformListing> listings = listingRepository.findByProduct(product);
        List<ComparisonPlatformListing> platformListings = listings.stream()
                .map(this::toPlatformListing)
                .collect(Collectors.toList());

        // Get lowest price
        BigDecimal lowestPrice = platformListings.stream()
                .map(ComparisonPlatformListing::getEffectivePrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(product.getPrice());

        // Get sentiment
        List<ReviewSentiment> sentiments = sentimentRepository.findByProduct(product);
        ComparisonSentiment sentiment = buildSentiment(sentiments);

        // Calculate intelligence score
        int intelligenceScore = calculateComparisonIntelligenceScore(product, platformListings, sentiment);

        return ComparisonProduct.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory() != null ? product.getCategory().getName() : null)
                .imageUrl(product.getImageUrl())
                .lowestPrice(lowestPrice)
                .intelligenceScore(intelligenceScore)
                .platformListings(platformListings)
                .sentiment(sentiment)
                .stockQuantity(product.getStockQuantity())
                .status(product.getStatus().name())
                .build();
    }

    private ComparisonPlatformListing toPlatformListing(ProductPlatformListing listing) {
        return ComparisonPlatformListing.builder()
                .platform(listing.getPlatform().name())
                .originalPrice(listing.getOriginalPrice())
                .currentPrice(listing.getCurrentPrice())
                .effectivePrice(listing.getEffectivePrice())
                .discountPercentage(listing.getDiscountPercentage())
                .sellerName(listing.getSellerName())
                .sellerRating(listing.getSellerRating())
                .deliveryEstimate(listing.getDeliveryEstimate())
                .inStock(listing.isInStock())
                .ratingAverage(listing.getRatingAverage())
                .reviewCount(listing.getReviewCount())
                .productUrl(listing.getPlatformProductUrl())
                .build();
    }

    private ComparisonSentiment buildSentiment(List<ReviewSentiment> sentiments) {
        if (sentiments.isEmpty()) {
            return ComparisonSentiment.builder()
                    .overallScore(BigDecimal.ZERO)
                    .genuineCount(0)
                    .fakeCount(0)
                    .topPros(List.of())
                    .topCons(List.of())
                    .build();
        }

        ReviewSentiment latest = sentiments.get(0);
        return ComparisonSentiment.builder()
                .overallScore(latest.getOverallSentimentScore())
                .genuineCount(latest.getGenuineReviewsCount())
                .fakeCount(latest.getFakeReviewsDetected())
                .topPros(parseJsonArray(latest.getTopPros()))
                .topCons(parseJsonArray(latest.getTopCons()))
                .sellerReliability(latest.getSellerReliabilityIndex())
                .build();
    }

    private List<String> parseJsonArray(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (Exception e) {
            return List.of();
        }
    }

    private int calculateComparisonIntelligenceScore(Product product, List<ComparisonPlatformListing> listings, ComparisonSentiment sentiment) {
        // Price Score (25%)
        double priceScore = calculatePriceScore(listings);
        
        // Rating Score (20%)
        double ratingScore = listings.stream()
                .map(ComparisonPlatformListing::getRatingAverage)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(3.0) * 20;

        // Sentiment Score (20%)
        double sentimentScore = sentiment.getOverallScore() != null ? sentiment.getOverallScore().doubleValue() * 20 : 50;

        // Seller Score (15%)
        double sellerScore = listings.stream()
                .map(ComparisonPlatformListing::getSellerRating)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(3.0) * 20;

        // Availability Score (10%)
        double availabilityScore = listings.stream()
                .filter(ComparisonPlatformListing::getInStock)
                .count() * 100.0 / Math.max(1, listings.size());

        // Platform Diversity Score (10%)
        double platformScore = Math.min(100, listings.size() * 25);

        double total = priceScore * 0.25 + ratingScore * 0.20 + sentimentScore * 0.20 
                + sellerScore * 0.15 + availabilityScore * 0.10 + platformScore * 0.10;

        return Math.max(0, Math.min(100, (int) Math.round(total)));
    }

    private double calculatePriceScore(List<ComparisonPlatformListing> listings) {
        if (listings.isEmpty()) return 50;
        BigDecimal minPrice = listings.stream()
                .map(ComparisonPlatformListing::getEffectivePrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        
        // Score based on discount percentage
        double avgDiscount = listings.stream()
                .map(ComparisonPlatformListing::getDiscountPercentage)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(0);
        
        return Math.min(100, 50 + avgDiscount * 2);
    }

    private void determineBestProducts(List<ComparisonProduct> products) {
        // Best overall (highest intelligence score)
        products.stream()
                .max(Comparator.comparing(ComparisonProduct::getIntelligenceScore))
                .ifPresent(p -> p.setBestOverall(true));

        // Best value (highest score per rupee)
        products.stream()
                .max(Comparator.comparing(p -> p.getIntelligenceScore() / (p.getLowestPrice().doubleValue() + 1)))
                .ifPresent(p -> p.setBestValue(true));

        // Best rating
        products.stream()
                .max(Comparator.comparing(p -> p.getPlatformListings().stream()
                        .map(ComparisonPlatformListing::getRatingAverage)
                        .filter(Objects::nonNull)
                        .mapToDouble(BigDecimal::doubleValue)
                        .average()
                        .orElse(0)))
                .ifPresent(p -> p.setBestRating(true));

        // Best sentiment
        products.stream()
                .max(Comparator.comparing(p -> p.getSentiment().getOverallScore() != null 
                        ? p.getSentiment().getOverallScore().doubleValue() : 0))
                .ifPresent(p -> p.setBestSentiment(true));
    }

    private String findBestOverall(List<ComparisonProduct> products) {
        return products.stream()
                .max(Comparator.comparing(ComparisonProduct::getIntelligenceScore))
                .map(ComparisonProduct::getName)
                .orElse(null);
    }

    private String findBestValue(List<ComparisonProduct> products) {
        return products.stream()
                .max(Comparator.comparing(p -> p.getIntelligenceScore() / (p.getLowestPrice().doubleValue() + 1)))
                .map(ComparisonProduct::getName)
                .orElse(null);
    }

    private String findBestRating(List<ComparisonProduct> products) {
        return products.stream()
                .max(Comparator.comparing(p -> p.getPlatformListings().stream()
                        .map(ComparisonPlatformListing::getRatingAverage)
                        .filter(Objects::nonNull)
                        .mapToDouble(BigDecimal::doubleValue)
                        .average()
                        .orElse(0)))
                .map(ComparisonProduct::getName)
                .orElse(null);
    }

    private String findBestSentiment(List<ComparisonProduct> products) {
        return products.stream()
                .max(Comparator.comparing(p -> p.getSentiment().getOverallScore() != null 
                        ? p.getSentiment().getOverallScore().doubleValue() : 0))
                .map(ComparisonProduct::getName)
                .orElse(null);
    }

    // Inner DTO classes
    public static class ComparisonResult {
        private List<ComparisonProduct> products;
        private String bestOverall;
        private String bestValue;
        private String bestRating;
        private String bestSentiment;

        public ComparisonResult() {}
        public ComparisonResult(List<ComparisonProduct> products, String bestOverall, String bestValue, String bestRating, String bestSentiment) {
            this.products = products;
            this.bestOverall = bestOverall;
            this.bestValue = bestValue;
            this.bestRating = bestRating;
            this.bestSentiment = bestSentiment;
        }

        public List<ComparisonProduct> getProducts() { return products; }
        public void setProducts(List<ComparisonProduct> products) { this.products = products; }
        public String getBestOverall() { return bestOverall; }
        public void setBestOverall(String bestOverall) { this.bestOverall = bestOverall; }
        public String getBestValue() { return bestValue; }
        public void setBestValue(String bestValue) { this.bestValue = bestValue; }
        public String getBestRating() { return bestRating; }
        public void setBestRating(String bestRating) { this.bestRating = bestRating; }
        public String getBestSentiment() { return bestSentiment; }
        public void setBestSentiment(String bestSentiment) { this.bestSentiment = bestSentiment; }

        public static ComparisonResultBuilder builder() { return new ComparisonResultBuilder(); }
        public static class ComparisonResultBuilder {
            private List<ComparisonProduct> products;
            private String bestOverall;
            private String bestValue;
            private String bestRating;
            private String bestSentiment;
            public ComparisonResultBuilder products(List<ComparisonProduct> products) { this.products = products; return this; }
            public ComparisonResultBuilder bestOverall(String bestOverall) { this.bestOverall = bestOverall; return this; }
            public ComparisonResultBuilder bestValue(String bestValue) { this.bestValue = bestValue; return this; }
            public ComparisonResultBuilder bestRating(String bestRating) { this.bestRating = bestRating; return this; }
            public ComparisonResultBuilder bestSentiment(String bestSentiment) { this.bestSentiment = bestSentiment; return this; }
            public ComparisonResult build() { return new ComparisonResult(products, bestOverall, bestValue, bestRating, bestSentiment); }
        }
    }

    public static class ComparisonProduct {
        private Long id;
        private String name;
        private String description;
        private String category;
        private String imageUrl;
        private BigDecimal lowestPrice;
        private Integer intelligenceScore;
        private List<ComparisonPlatformListing> platformListings;
        private ComparisonSentiment sentiment;
        private Integer stockQuantity;
        private String status;
        private Boolean bestOverall = false;
        private Boolean bestValue = false;
        private Boolean bestRating = false;
        private Boolean bestSentiment = false;

        public ComparisonProduct() {}
        public ComparisonProduct(Long id, String name, String description, String category, String imageUrl,
                                 BigDecimal lowestPrice, Integer intelligenceScore, List<ComparisonPlatformListing> platformListings,
                                 ComparisonSentiment sentiment, Integer stockQuantity, String status,
                                 Boolean bestOverall, Boolean bestValue, Boolean bestRating, Boolean bestSentiment) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.category = category;
            this.imageUrl = imageUrl;
            this.lowestPrice = lowestPrice;
            this.intelligenceScore = intelligenceScore;
            this.platformListings = platformListings;
            this.sentiment = sentiment;
            this.stockQuantity = stockQuantity;
            this.status = status;
            this.bestOverall = bestOverall;
            this.bestValue = bestValue;
            this.bestRating = bestRating;
            this.bestSentiment = bestSentiment;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public BigDecimal getLowestPrice() { return lowestPrice; }
        public void setLowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; }
        public Integer getIntelligenceScore() { return intelligenceScore; }
        public void setIntelligenceScore(Integer intelligenceScore) { this.intelligenceScore = intelligenceScore; }
        public List<ComparisonPlatformListing> getPlatformListings() { return platformListings; }
        public void setPlatformListings(List<ComparisonPlatformListing> platformListings) { this.platformListings = platformListings; }
        public ComparisonSentiment getSentiment() { return sentiment; }
        public void setSentiment(ComparisonSentiment sentiment) { this.sentiment = sentiment; }
        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Boolean getBestOverall() { return bestOverall; }
        public void setBestOverall(Boolean bestOverall) { this.bestOverall = bestOverall; }
        public Boolean getBestValue() { return bestValue; }
        public void setBestValue(Boolean bestValue) { this.bestValue = bestValue; }
        public Boolean getBestRating() { return bestRating; }
        public void setBestRating(Boolean bestRating) { this.bestRating = bestRating; }
        public Boolean getBestSentiment() { return bestSentiment; }
        public void setBestSentiment(Boolean bestSentiment) { this.bestSentiment = bestSentiment; }

        public static ComparisonProductBuilder builder() { return new ComparisonProductBuilder(); }
        public static class ComparisonProductBuilder {
            private Long id;
            private String name;
            private String description;
            private String category;
            private String imageUrl;
            private BigDecimal lowestPrice;
            private Integer intelligenceScore;
            private List<ComparisonPlatformListing> platformListings;
            private ComparisonSentiment sentiment;
            private Integer stockQuantity;
            private String status;
            private Boolean bestOverall = false;
            private Boolean bestValue = false;
            private Boolean bestRating = false;
            private Boolean bestSentiment = false;

            public ComparisonProductBuilder id(Long id) { this.id = id; return this; }
            public ComparisonProductBuilder name(String name) { this.name = name; return this; }
            public ComparisonProductBuilder description(String description) { this.description = description; return this; }
            public ComparisonProductBuilder category(String category) { this.category = category; return this; }
            public ComparisonProductBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
            public ComparisonProductBuilder lowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; return this; }
            public ComparisonProductBuilder intelligenceScore(Integer intelligenceScore) { this.intelligenceScore = intelligenceScore; return this; }
            public ComparisonProductBuilder platformListings(List<ComparisonPlatformListing> platformListings) { this.platformListings = platformListings; return this; }
            public ComparisonProductBuilder sentiment(ComparisonSentiment sentiment) { this.sentiment = sentiment; return this; }
            public ComparisonProductBuilder stockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; return this; }
            public ComparisonProductBuilder status(String status) { this.status = status; return this; }
            public ComparisonProductBuilder bestOverall(Boolean bestOverall) { this.bestOverall = bestOverall; return this; }
            public ComparisonProductBuilder bestValue(Boolean bestValue) { this.bestValue = bestValue; return this; }
            public ComparisonProductBuilder bestRating(Boolean bestRating) { this.bestRating = bestRating; return this; }
            public ComparisonProductBuilder bestSentiment(Boolean bestSentiment) { this.bestSentiment = bestSentiment; return this; }

            public ComparisonProduct build() {
                return new ComparisonProduct(id, name, description, category, imageUrl, lowestPrice, intelligenceScore,
                        platformListings, sentiment, stockQuantity, status, bestOverall, bestValue, bestRating, bestSentiment);
            }
        }
    }

    public static class ComparisonPlatformListing {
        private String platform;
        private BigDecimal originalPrice;
        private BigDecimal currentPrice;
        private BigDecimal effectivePrice;
        private BigDecimal discountPercentage;
        private String sellerName;
        private BigDecimal sellerRating;
        private String deliveryEstimate;
        private Boolean inStock;
        private BigDecimal ratingAverage;
        private Integer reviewCount;
        private String productUrl;

        public ComparisonPlatformListing() {}
        public ComparisonPlatformListing(String platform, BigDecimal originalPrice, BigDecimal currentPrice, BigDecimal effectivePrice,
                                         BigDecimal discountPercentage, String sellerName, BigDecimal sellerRating, String deliveryEstimate,
                                         Boolean inStock, BigDecimal ratingAverage, Integer reviewCount, String productUrl) {
            this.platform = platform;
            this.originalPrice = originalPrice;
            this.currentPrice = currentPrice;
            this.effectivePrice = effectivePrice;
            this.discountPercentage = discountPercentage;
            this.sellerName = sellerName;
            this.sellerRating = sellerRating;
            this.deliveryEstimate = deliveryEstimate;
            this.inStock = inStock;
            this.ratingAverage = ratingAverage;
            this.reviewCount = reviewCount;
            this.productUrl = productUrl;
        }

        public String getPlatform() { return platform; }
        public void setPlatform(String platform) { this.platform = platform; }
        public BigDecimal getOriginalPrice() { return originalPrice; }
        public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
        public BigDecimal getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
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
        public Boolean getInStock() { return inStock; }
        public void setInStock(Boolean inStock) { this.inStock = inStock; }
        public BigDecimal getRatingAverage() { return ratingAverage; }
        public void setRatingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; }
        public Integer getReviewCount() { return reviewCount; }
        public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
        public String getProductUrl() { return productUrl; }
        public void setProductUrl(String productUrl) { this.productUrl = productUrl; }

        public static ComparisonPlatformListingBuilder builder() { return new ComparisonPlatformListingBuilder(); }
        public static class ComparisonPlatformListingBuilder {
            private String platform;
            private BigDecimal originalPrice;
            private BigDecimal currentPrice;
            private BigDecimal effectivePrice;
            private BigDecimal discountPercentage;
            private String sellerName;
            private BigDecimal sellerRating;
            private String deliveryEstimate;
            private Boolean inStock;
            private BigDecimal ratingAverage;
            private Integer reviewCount;
            private String productUrl;

            public ComparisonPlatformListingBuilder platform(String platform) { this.platform = platform; return this; }
            public ComparisonPlatformListingBuilder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
            public ComparisonPlatformListingBuilder currentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; return this; }
            public ComparisonPlatformListingBuilder effectivePrice(BigDecimal effectivePrice) { this.effectivePrice = effectivePrice; return this; }
            public ComparisonPlatformListingBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
            public ComparisonPlatformListingBuilder sellerName(String sellerName) { this.sellerName = sellerName; return this; }
            public ComparisonPlatformListingBuilder sellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; return this; }
            public ComparisonPlatformListingBuilder deliveryEstimate(String deliveryEstimate) { this.deliveryEstimate = deliveryEstimate; return this; }
            public ComparisonPlatformListingBuilder inStock(Boolean inStock) { this.inStock = inStock; return this; }
            public ComparisonPlatformListingBuilder ratingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; return this; }
            public ComparisonPlatformListingBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
            public ComparisonPlatformListingBuilder productUrl(String productUrl) { this.productUrl = productUrl; return this; }

            public ComparisonPlatformListing build() {
                return new ComparisonPlatformListing(platform, originalPrice, currentPrice, effectivePrice, discountPercentage,
                        sellerName, sellerRating, deliveryEstimate, inStock, ratingAverage, reviewCount, productUrl);
            }
        }
    }

    public static class ComparisonSentiment {
        private BigDecimal overallScore;
        private Integer genuineCount;
        private Integer fakeCount;
        private List<String> topPros;
        private List<String> topCons;
        private BigDecimal sellerReliability;

        public ComparisonSentiment() {}
        public ComparisonSentiment(BigDecimal overallScore, Integer genuineCount, Integer fakeCount,
                                   List<String> topPros, List<String> topCons, BigDecimal sellerReliability) {
            this.overallScore = overallScore;
            this.genuineCount = genuineCount;
            this.fakeCount = fakeCount;
            this.topPros = topPros;
            this.topCons = topCons;
            this.sellerReliability = sellerReliability;
        }

        public BigDecimal getOverallScore() { return overallScore; }
        public void setOverallScore(BigDecimal overallScore) { this.overallScore = overallScore; }
        public Integer getGenuineCount() { return genuineCount; }
        public void setGenuineCount(Integer genuineCount) { this.genuineCount = genuineCount; }
        public Integer getFakeCount() { return fakeCount; }
        public void setFakeCount(Integer fakeCount) { this.fakeCount = fakeCount; }
        public List<String> getTopPros() { return topPros; }
        public void setTopPros(List<String> topPros) { this.topPros = topPros; }
        public List<String> getTopCons() { return topCons; }
        public void setTopCons(List<String> topCons) { this.topCons = topCons; }
        public BigDecimal getSellerReliability() { return sellerReliability; }
        public void setSellerReliability(BigDecimal sellerReliability) { this.sellerReliability = sellerReliability; }

        public static ComparisonSentimentBuilder builder() { return new ComparisonSentimentBuilder(); }
        public static class ComparisonSentimentBuilder {
            private BigDecimal overallScore;
            private Integer genuineCount;
            private Integer fakeCount;
            private List<String> topPros;
            private List<String> topCons;
            private BigDecimal sellerReliability;

            public ComparisonSentimentBuilder overallScore(BigDecimal overallScore) { this.overallScore = overallScore; return this; }
            public ComparisonSentimentBuilder genuineCount(Integer genuineCount) { this.genuineCount = genuineCount; return this; }
            public ComparisonSentimentBuilder fakeCount(Integer fakeCount) { this.fakeCount = fakeCount; return this; }
            public ComparisonSentimentBuilder topPros(List<String> topPros) { this.topPros = topPros; return this; }
            public ComparisonSentimentBuilder topCons(List<String> topCons) { this.topCons = topCons; return this; }
            public ComparisonSentimentBuilder sellerReliability(BigDecimal sellerReliability) { this.sellerReliability = sellerReliability; return this; }

            public ComparisonSentiment build() {
                return new ComparisonSentiment(overallScore, genuineCount, fakeCount, topPros, topCons, sellerReliability);
            }
        }
    }
}