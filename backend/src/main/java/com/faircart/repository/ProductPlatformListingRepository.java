package com.faircart.repository;

import com.faircart.entity.ProductPlatformListing;
import com.faircart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPlatformListingRepository extends JpaRepository<ProductPlatformListing, Long> {

    List<ProductPlatformListing> findByProduct(Product product);

    List<ProductPlatformListing> findByProductAndPlatform(Product product, ProductPlatformListing.Platform platform);

    Optional<ProductPlatformListing> findByProductAndPlatformAndExternalId(Product product, ProductPlatformListing.Platform platform, String externalId);

    @Query("SELECT ppl FROM ProductPlatformListing ppl WHERE ppl.product = :product AND ppl.inStock = true ORDER BY ppl.effectivePrice ASC")
    List<ProductPlatformListing> findInStockByProductOrderByEffectivePriceAsc(@Param("product") Product product);

    List<ProductPlatformListing> findByPlatformAndInStockTrue(ProductPlatformListing.Platform platform);
}