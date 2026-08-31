package com.faircart.repository;

import com.faircart.entity.ProductIntelligence;
import com.faircart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductIntelligenceRepository extends JpaRepository<ProductIntelligence, Long> {

    Optional<ProductIntelligence> findByProduct(Product product);

    boolean existsByProduct(Product product);
}