package com.faircart.service;

import com.faircart.entity.Category;
import com.faircart.entity.Product;
import com.faircart.entity.ProductPlatformListing;
import com.faircart.entity.User;
import com.faircart.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationLogRepository recommendationRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductPlatformListingRepository listingRepository;

    @Mock
    private ReviewSentimentRepository sentimentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrackedProductRepository trackedRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private User testUser;
    private Product baseProduct;
    private Product upgradeProduct;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@faircart.com")
                .fullName("Test User")
                .role(User.Role.CUSTOMER)
                .build();

        Category category = Category.builder()
                .name("Audio")
                .slug("audio")
                .build();

        baseProduct = Product.builder()
                .name("Budget In-Ear Earphones")
                .price(new BigDecimal("299.00"))
                .intelligenceScore(65)
                .category(category)
                .status(Product.ProductStatus.ACTIVE)
                .build();

        upgradeProduct = Product.builder()
                .name("Pro Wireless ANC Earphones")
                .price(new BigDecimal("370.00"))
                .intelligenceScore(92)
                .category(category)
                .status(Product.ProductStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Should generate recommendations with Smart Stretch upgrade")
    void shouldGenerateSmartStretchRecommendations() {
        when(userRepository.findByEmail("test@faircart.com")).thenReturn(Optional.of(testUser));
        when(productRepository.findByStatus(Product.ProductStatus.ACTIVE))
                .thenReturn(List.of(baseProduct, upgradeProduct));

        ProductPlatformListing baseListing = ProductPlatformListing.builder()
                .product(baseProduct)
                .platform(ProductPlatformListing.Platform.AMAZON)
                .effectivePrice(new BigDecimal("299.00"))
                .inStock(true)
                .build();

        ProductPlatformListing upgradeListing = ProductPlatformListing.builder()
                .product(upgradeProduct)
                .platform(ProductPlatformListing.Platform.FLIPKART)
                .effectivePrice(new BigDecimal("370.00"))
                .inStock(true)
                .build();

        when(listingRepository.findInStockByProductOrderByEffectivePriceAsc(baseProduct))
                .thenReturn(List.of(baseListing));
        when(listingRepository.findInStockByProductOrderByEffectivePriceAsc(upgradeProduct))
                .thenReturn(List.of(upgradeListing));

        RecommendationService.RecommendationRequest request = RecommendationService.RecommendationRequest.builder()
                .budget(new BigDecimal("300.00"))
                .category("Audio")
                .preferredPlatforms(List.of("AMAZON", "FLIPKART"))
                .queryText("Earphones under 300")
                .build();

        RecommendationService.RecommendationResult result = recommendationService.getRecommendations("test@faircart.com", request);

        assertThat(result).isNotNull();
        assertThat(result.getBestBudgetMatch()).isNotNull();
        assertThat(result.getBestBudgetMatch().getProductName()).isEqualTo("Budget In-Ear Earphones");
        assertThat(result.getVerdictScore()).isGreaterThan(0);
        assertThat(result.getVerdictLabel()).isNotBlank();
    }

    @Test
    @DisplayName("Should handle guest recommendations gracefully without throwing user not found")
    void shouldHandleGuestRecommendationsGracefully() {
        when(userRepository.findByEmail("guest@faircart.com")).thenReturn(Optional.empty());
        when(productRepository.findByStatus(Product.ProductStatus.ACTIVE))
                .thenReturn(List.of(baseProduct));

        ProductPlatformListing baseListing = ProductPlatformListing.builder()
                .product(baseProduct)
                .platform(ProductPlatformListing.Platform.AMAZON)
                .effectivePrice(new BigDecimal("299.00"))
                .inStock(true)
                .build();

        when(listingRepository.findInStockByProductOrderByEffectivePriceAsc(baseProduct))
                .thenReturn(List.of(baseListing));

        RecommendationService.RecommendationRequest request = RecommendationService.RecommendationRequest.builder()
                .budget(new BigDecimal("300.00"))
                .category("Audio")
                .queryText("Earphones")
                .build();

        RecommendationService.RecommendationResult result = recommendationService.getRecommendations("guest@faircart.com", request);

        assertThat(result).isNotNull();
        assertThat(result.getBestBudgetMatch()).isNotNull();
        verify(recommendationRepository, never()).save(any());
    }
}
