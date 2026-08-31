package com.faircart.service;

import com.faircart.entity.Product;
import com.faircart.entity.Review;
import com.faircart.entity.User;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.ProductRepository;
import com.faircart.repository.ReviewRepository;
import com.faircart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse addReview(String userEmail, ReviewRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        if (reviewRepository.existsByUserAndProduct(user, product)) {
            throw new IllegalArgumentException("You have already reviewed this product");
        }

        if (request.getComment() != null && request.getComment().trim().length() < 10) {
            throw new IllegalArgumentException("Review comment must be at least 10 characters");
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .helpfulCount(0L)
                .build();

        Review saved = reviewRepository.save(review);
        updateProductRating(product);

        return ReviewResponse.from(saved);
    }

    public Page<ReviewResponse> getProductReviews(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return reviewRepository.findByProduct(product, pageable)
                .map(ReviewResponse::from);
    }

    public List<ReviewResponse> getUserReviews(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        List<Review> reviews = reviewRepository.findByUser(user);
        return ReviewResponse.from(reviews);
    }

    @Transactional
    public ReviewResponse updateReview(String userEmail, Long reviewId, ReviewRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only update your own reviews");
        }

        if (request.getComment() != null && request.getComment().trim().length() < 10) {
            throw new IllegalArgumentException("Review comment must be at least 10 characters");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        updateProductRating(review.getProduct());

        return ReviewResponse.from(saved);
    }

    @Transactional
    public void deleteReview(String userEmail, Long reviewId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only delete your own reviews");
        }

        Product product = review.getProduct();
        reviewRepository.delete(review);
        updateProductRating(product);
    }

    @Transactional
    public ReviewResponse markHelpful(String userEmail, Long reviewId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        review.setHelpfulCount(review.getHelpfulCount() + 1);
        Review saved = reviewRepository.save(review);

        return ReviewResponse.from(saved);
    }

    public ReviewResponse getUserReviewForProduct(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return reviewRepository.findByUserAndProduct(user, product)
                .map(ReviewResponse::from)
                .orElse(null);
    }

    public ReviewStats getProductReviewStats(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<Review> reviews = reviewRepository.findByProduct(product);
        
        if (reviews.isEmpty()) {
            return ReviewStats.builder()
                    .averageRating(BigDecimal.ZERO)
                    .totalReviews(0)
                    .ratingDistribution(java.util.Map.of(1, 0L, 2, 0L, 3, 0L, 4, 0L, 5, 0L))
                    .build();
        }

        double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        BigDecimal averageRating = BigDecimal.valueOf(avgRating)
                .setScale(1, RoundingMode.HALF_UP);

        java.util.Map<Integer, Long> distribution = reviews.stream()
                .collect(Collectors.groupingBy(
                        Review::getRating,
                        Collectors.counting()
                ));

        // Ensure all ratings 1-5 are present
        java.util.Map<Integer, Long> fullDistribution = new java.util.LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            fullDistribution.put(i, distribution.getOrDefault(i, 0L));
        }

        return ReviewStats.builder()
                .averageRating(averageRating)
                .totalReviews(reviews.size())
                .ratingDistribution(fullDistribution)
                .build();
    }

    private void updateProductRating(Product product) {
        List<Review> reviews = reviewRepository.findByProduct(product);
        if (!reviews.isEmpty()) {
            double avg = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            // In a real app, we'd have a rating field on Product
            // product.setRatingAvg(BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP));
            // productRepository.save(product);
        }
    }

    // Inner DTO classes
    public static class ReviewRequest {
        private Long productId;
        private Integer rating;
        private String comment;

        public ReviewRequest() {}
        public ReviewRequest(Long productId, Integer rating, String comment) {
            this.productId = productId;
            this.rating = rating;
            this.comment = comment;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }

        public static ReviewRequestBuilder builder() { return new ReviewRequestBuilder(); }
        public static class ReviewRequestBuilder {
            private Long productId;
            private Integer rating;
            private String comment;
            public ReviewRequestBuilder productId(Long productId) { this.productId = productId; return this; }
            public ReviewRequestBuilder rating(Integer rating) { this.rating = rating; return this; }
            public ReviewRequestBuilder comment(String comment) { this.comment = comment; return this; }
            public ReviewRequest build() { return new ReviewRequest(productId, rating, comment); }
        }
    }

    public static class ReviewResponse {
        private Long id;
        private Long userId;
        private String userName;
        private Long productId;
        private Integer rating;
        private String comment;
        private Long helpfulCount;
        private java.time.Instant createdAt;
        private java.time.Instant updatedAt;

        public ReviewResponse() {}
        public ReviewResponse(Long id, Long userId, String userName, Long productId, Integer rating,
                              String comment, Long helpfulCount, java.time.Instant createdAt, java.time.Instant updatedAt) {
            this.id = id;
            this.userId = userId;
            this.userName = userName;
            this.productId = productId;
            this.rating = rating;
            this.comment = comment;
            this.helpfulCount = helpfulCount;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
        public Long getHelpfulCount() { return helpfulCount; }
        public void setHelpfulCount(Long helpfulCount) { this.helpfulCount = helpfulCount; }
        public java.time.Instant getCreatedAt() { return createdAt; }
        public void setCreatedAt(java.time.Instant createdAt) { this.createdAt = createdAt; }
        public java.time.Instant getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(java.time.Instant updatedAt) { this.updatedAt = updatedAt; }

        public static ReviewResponseBuilder builder() { return new ReviewResponseBuilder(); }
        public static class ReviewResponseBuilder {
            private Long id;
            private Long userId;
            private String userName;
            private Long productId;
            private Integer rating;
            private String comment;
            private Long helpfulCount;
            private java.time.Instant createdAt;
            private java.time.Instant updatedAt;

            public ReviewResponseBuilder id(Long id) { this.id = id; return this; }
            public ReviewResponseBuilder userId(Long userId) { this.userId = userId; return this; }
            public ReviewResponseBuilder userName(String userName) { this.userName = userName; return this; }
            public ReviewResponseBuilder productId(Long productId) { this.productId = productId; return this; }
            public ReviewResponseBuilder rating(Integer rating) { this.rating = rating; return this; }
            public ReviewResponseBuilder comment(String comment) { this.comment = comment; return this; }
            public ReviewResponseBuilder helpfulCount(Long helpfulCount) { this.helpfulCount = helpfulCount; return this; }
            public ReviewResponseBuilder createdAt(java.time.Instant createdAt) { this.createdAt = createdAt; return this; }
            public ReviewResponseBuilder updatedAt(java.time.Instant updatedAt) { this.updatedAt = updatedAt; return this; }

            public ReviewResponse build() {
                return new ReviewResponse(id, userId, userName, productId, rating, comment, helpfulCount, createdAt, updatedAt);
            }
        }

        public static ReviewResponse from(Review review) {
            if (review == null) return null;
            return ReviewResponse.builder()
                    .id(review.getId())
                    .userId(review.getUser() != null ? review.getUser().getId() : null)
                    .userName(review.getUser() != null ? review.getUser().getFullName() : null)
                    .productId(review.getProduct() != null ? review.getProduct().getId() : null)
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .helpfulCount(review.getHelpfulCount())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }

        public static List<ReviewResponse> from(List<Review> reviews) {
            if (reviews == null) return List.of();
            return reviews.stream()
                    .map(ReviewResponse::from)
                    .collect(Collectors.toList());
        }
    }

    public static class ReviewStats {
        private BigDecimal averageRating;
        private int totalReviews;
        private java.util.Map<Integer, Long> ratingDistribution;

        public ReviewStats() {}
        public ReviewStats(BigDecimal averageRating, int totalReviews, java.util.Map<Integer, Long> ratingDistribution) {
            this.averageRating = averageRating;
            this.totalReviews = totalReviews;
            this.ratingDistribution = ratingDistribution;
        }

        public BigDecimal getAverageRating() { return averageRating; }
        public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
        public int getTotalReviews() { return totalReviews; }
        public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }
        public java.util.Map<Integer, Long> getRatingDistribution() { return ratingDistribution; }
        public void setRatingDistribution(java.util.Map<Integer, Long> ratingDistribution) { this.ratingDistribution = ratingDistribution; }

        public static ReviewStatsBuilder builder() { return new ReviewStatsBuilder(); }
        public static class ReviewStatsBuilder {
            private BigDecimal averageRating;
            private int totalReviews;
            private java.util.Map<Integer, Long> ratingDistribution;

            public ReviewStatsBuilder averageRating(BigDecimal averageRating) { this.averageRating = averageRating; return this; }
            public ReviewStatsBuilder totalReviews(int totalReviews) { this.totalReviews = totalReviews; return this; }
            public ReviewStatsBuilder ratingDistribution(java.util.Map<Integer, Long> ratingDistribution) { this.ratingDistribution = ratingDistribution; return this; }
            public ReviewStats build() { return new ReviewStats(averageRating, totalReviews, ratingDistribution); }
        }
    }
}