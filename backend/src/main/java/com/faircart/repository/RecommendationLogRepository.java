package com.faircart.repository;

import com.faircart.entity.RecommendationLog;
import com.faircart.entity.User;
import com.faircart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationLogRepository extends JpaRepository<RecommendationLog, Long> {

    List<RecommendationLog> findByUserOrderByCreatedAtDesc(User user);

    List<RecommendationLog> findByUserAndRecommendationType(User user, RecommendationLog.RecommendationType type);

    List<RecommendationLog> findByProduct(Product product);

    List<RecommendationLog> findByUserAndProduct(User user, Product product);
}