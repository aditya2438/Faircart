package com.faircart.repository;

import com.faircart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Optional<Category> findByName(String name);

    List<Category> findByActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT c FROM Category c WHERE c.active = true AND (c.name LIKE %:search% OR c.description LIKE %:search%)")
    List<Category> search(String search);
}