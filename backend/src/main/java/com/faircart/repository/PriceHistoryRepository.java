package com.faircart.repository;

import com.faircart.entity.PriceHistory;
import com.faircart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findByProductOrderByRecordedAtDesc(Product product);

    List<PriceHistory> findTop10ByProductOrderByRecordedAtDesc(Product product);
}