package com.faircart.service.scraper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelScraperServiceTest {

    private final ParallelScraperService scraperService = new ParallelScraperService();

    @Test
    @DisplayName("Should concurrently scrape deals across 5 platforms with Java Virtual Threads")
    void shouldScrapeDealsConcurrently() {
        List<ParallelScraperService.ScrapedPlatformDeal> deals = scraperService
                .scrapeProductDealsConcurrently("Realme Buds 2", BigDecimal.valueOf(300));

        assertThat(deals).isNotEmpty();
        assertThat(deals.size()).isGreaterThanOrEqualTo(4);

        // Verify all deals have positive prices and platforms
        for (ParallelScraperService.ScrapedPlatformDeal deal : deals) {
            assertThat(deal.getPlatform()).isNotNull();
            assertThat(deal.getEffectivePrice()).isGreaterThan(BigDecimal.ZERO);
            assertThat(deal.getProductUrl()).isNotBlank();
        }

        // Verify sorted by price ascending (best deal first)
        for (int i = 0; i < deals.size() - 1; i++) {
            assertThat(deals.get(i).getEffectivePrice())
                    .isLessThanOrEqualTo(deals.get(i + 1).getEffectivePrice());
        }
    }
}
