package com.faircart.repository;

import com.faircart.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Seller> findByActiveTrueOrderByRatingDesc();
}