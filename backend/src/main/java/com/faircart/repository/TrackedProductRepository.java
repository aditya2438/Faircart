package com.faircart.repository;

import com.faircart.entity.TrackedProduct;
import com.faircart.entity.User;
import com.faircart.entity.Product;
import com.faircart.entity.ProductPlatformListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackedProductRepository extends JpaRepository<TrackedProduct, Long> {

    List<TrackedProduct> findByUser(User user);

    List<TrackedProduct> findByAlertEnabledTrue();

    Optional<TrackedProduct> findByUserAndProductAndPlatform(User user, Product product, ProductPlatformListing.Platform platform);

    List<TrackedProduct> findByUserAndAlertEnabledTrue(User user);

    List<TrackedProduct> findByProductAndPlatformAndAlertEnabledTrue(Product product, ProductPlatformListing.Platform platform);

    List<TrackedProduct> findByTargetPriceLessThanEqualAndAlertEnabledTrue(java.math.BigDecimal price);
}