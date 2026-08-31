package com.faircart.repository;

import com.faircart.entity.Wishlist;
import com.faircart.entity.User;
import com.faircart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserAndProduct(User user, Product product);

    List<Wishlist> findByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    long countByUser(User user);
}