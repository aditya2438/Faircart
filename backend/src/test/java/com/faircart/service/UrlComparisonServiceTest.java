package com.faircart.service;

import com.faircart.dto.comparison.UrlComparisonRequest;
import com.faircart.dto.comparison.UrlComparisonResponse;
import com.faircart.entity.ProductPlatformListing;
import com.faircart.service.comparison.UrlComparisonService;
import com.faircart.service.scraper.ParallelScraperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UrlComparisonServiceTest {

    @Mock
    private ParallelScraperService scraperService;

    private UrlComparisonService urlComparisonService;

    @BeforeEach
    void setUp() {
        urlComparisonService = new UrlComparisonService(scraperService);
    }

    @Test
    @DisplayName("Should parse and compare 2 product URLs from Amazon and Flipkart")
    void testCompareTwoUrls() {
        UrlComparisonRequest request = UrlComparisonRequest.builder()
                .urls(List.of(
                        "https://www.flipkart.com/apple-iphone-15-pro-natural-titanium-128-gb/p/itm123",
                        "https://www.amazon.in/dp/B0CQ2W2T5P"
                ))
                .build();

        UrlComparisonResponse response = urlComparisonService.compareProductUrls(request);

        assertThat(response).isNotNull();
        assertThat(response.getTotalProductsCompared()).isEqualTo(2);
        assertThat(response.getWinner()).isNotNull();
        assertThat(response.getWinner().getWinningProductTitle()).isNotEmpty();
        assertThat(response.getProducts()).hasSize(2);
        assertThat(response.getAlternativeBetterDeals()).isNotEmpty();
        assertThat(response.getProducts().get(0).getSourcePlatform()).isEqualTo(ProductPlatformListing.Platform.FLIPKART);
        assertThat(response.getProducts().get(1).getSourcePlatform()).isEqualTo(ProductPlatformListing.Platform.AMAZON);
    }

    @Test
    @DisplayName("Should detect Meesho, Croma, Blinkit and Realme URLs")
    void testDetectVariousPlatformUrls() {
        UrlComparisonRequest request = UrlComparisonRequest.builder()
                .urls(List.of(
                        "https://meesho.com/realme-12-pro-plus/p/123",
                        "https://blinkit.com/prn/boat-bassheads-100",
                        "https://croma.com/apple-iphone-15-pro"
                ))
                .build();

        UrlComparisonResponse response = urlComparisonService.compareProductUrls(request);

        assertThat(response.getTotalProductsCompared()).isEqualTo(3);
        assertThat(response.getProducts().get(0).getSourcePlatform()).isEqualTo(ProductPlatformListing.Platform.MEESHO);
        assertThat(response.getProducts().get(1).getSourcePlatform()).isEqualTo(ProductPlatformListing.Platform.BLINKIT);
        assertThat(response.getProducts().get(2).getSourcePlatform()).isEqualTo(ProductPlatformListing.Platform.CROMA);
    }
}