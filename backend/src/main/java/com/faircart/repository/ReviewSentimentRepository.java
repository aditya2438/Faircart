package com.faircart.repository;

import com.faircart.entity.ReviewSentiment;
import com.faircart.entity.Product;
import com.faircart.entity.ProductPlatformListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewSentimentRepository extends JpaRepository<ReviewSentiment, Long> {

    List<ReviewSentiment> findByProduct(Product product);

    Optional<ReviewSentiment> findByProductAndPlatform(Product product, ProductPlatformListing.Platform platform);

    List<ReviewSentiment> findByProductOrderByCreatedAtDesc(Product product);
}