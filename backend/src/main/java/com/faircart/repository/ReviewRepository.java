package com.faircart.repository;

import com.faircart.entity.Review;
import com.faircart.entity.User;
import com.faircart.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    Page<Review> findByProduct(Product product, Pageable pageable);

    List<Review> findByUser(User user);

    Optional<Review> findByUserAndProduct(User user, Product product);

    boolean existsByUserAndProduct(User user, Product product);
}