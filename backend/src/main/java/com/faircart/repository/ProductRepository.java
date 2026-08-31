package com.faircart.repository;

import com.faircart.entity.Product;
import com.faircart.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryAndStatus(Category category, Product.ProductStatus status, Pageable pageable);

    Page<Product> findByStatus(Product.ProductStatus status, Pageable pageable);

    List<Product> findByStatus(Product.ProductStatus status);

    Page<Product> findByNameContainingIgnoreCaseAndStatus(String name, Product.ProductStatus status, Pageable pageable);

    Page<Product> findByCategoryAndPriceBetweenAndStatus(Category category, BigDecimal minPrice, BigDecimal maxPrice, Product.ProductStatus status, Pageable pageable);

    Page<Product> findByPriceBetweenAndStatus(BigDecimal minPrice, BigDecimal maxPrice, Product.ProductStatus status, Pageable pageable);

    Page<Product> findByCategoryAndIntelligenceScoreGreaterThanEqualAndStatus(Category category, Integer minScore, Product.ProductStatus status, Pageable pageable);

    Page<Product> findByIntelligenceScoreGreaterThanEqualAndStatus(Integer minScore, Product.ProductStatus status, Pageable pageable);

    List<Product> findTop10ByStatusOrderByIntelligenceScoreDesc(Product.ProductStatus status);

    @Query("SELECT p FROM Product p WHERE p.status = :status AND " +
           "(p.name LIKE %:search% OR p.description LIKE %:search%)")
    Page<Product> search(@Param("search") String search, @Param("status") Product.ProductStatus status, Pageable pageable);

    Optional<Product> findByNameAndStatus(String name, Product.ProductStatus status);

    boolean existsByNameAndStatus(String name, Product.ProductStatus status);
}
