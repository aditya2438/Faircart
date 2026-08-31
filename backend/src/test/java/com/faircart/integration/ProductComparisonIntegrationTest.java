package com.faircart.integration;

import com.faircart.dto.ApiResponse;
import com.faircart.dto.product.ProductRequest;
import com.faircart.dto.product.ProductResponse;
import com.faircart.entity.Category;
import com.faircart.entity.Product;
import com.faircart.repository.CategoryRepository;
import com.faircart.repository.ProductRepository;
import com.faircart.repository.UserRepository;
import com.faircart.service.ProductComparisonService;
import com.faircart.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.faircart.FairCartApplication.class, properties = {
        "spring.autoconfigure.exclude=org.redisson.spring.starter.RedissonAutoConfigurationV2,org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductComparisonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductComparisonService comparisonService;

    @Autowired
    private ObjectMapper objectMapper;

    private Category testCategory;
    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create test category
        testCategory = Category.builder()
                .name("Test Electronics")
                .slug("test-electronics")
                .description("Test category for electronics")
                .active(true)
                .displayOrder(1)
                .build();
        testCategory = categoryRepository.save(testCategory);

        // Create test products
        product1 = Product.builder()
                .name("Premium Wireless Earbuds Pro")
                .description("High-end wireless earbuds with ANC")
                .price(new BigDecimal("299.99"))
                .stockQuantity(50)
                .category(testCategory)
                .imageUrl("https://example.com/earbuds1.jpg")
                .intelligenceScore(92)
                .status(Product.ProductStatus.ACTIVE)
                .build();
        product1 = productRepository.save(product1);

        product2 = Product.builder()
                .name("Budget Wireless Earbuds")
                .description("Affordable wireless earbuds")
                .price(new BigDecimal("79.99"))
                .stockQuantity(100)
                .category(testCategory)
                .imageUrl("https://example.com/earbuds2.jpg")
                .intelligenceScore(78)
                .status(Product.ProductStatus.ACTIVE)
                .build();
        product2 = productRepository.save(product2);

        product3 = Product.builder()
                .name("Mid-Range Wireless Earbuds")
                .description("Good quality mid-range earbuds")
                .price(new BigDecimal("149.99"))
                .stockQuantity(75)
                .category(testCategory)
                .imageUrl("https://example.com/earbuds3.jpg")
                .intelligenceScore(85)
                .status(Product.ProductStatus.ACTIVE)
                .build();
        product3 = productRepository.save(product3);

        product4 = Product.builder()
                .name("Basic Wired Earphones")
                .description("Basic wired earphones")
                .price(new BigDecimal("29.99"))
                .stockQuantity(200)
                .category(testCategory)
                .imageUrl("https://example.com/earbuds4.jpg")
                .intelligenceScore(65)
                .status(Product.ProductStatus.ACTIVE)
                .build();
        product4 = productRepository.save(product4);
    }

    @Test
    @DisplayName("Should compare two products successfully")
    void shouldCompareTwoProducts() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CompareRequest(List.of(product1.getId(), product2.getId()))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.products").isArray())
                .andExpect(jsonPath("$.data.products.length()").value(2))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("Premium Wireless Earbuds Pro");
        assertThat(responseBody).contains("Budget Wireless Earbuds");
    }

    @Test
    @DisplayName("Should compare three products successfully")
    void shouldCompareThreeProducts() throws Exception {
        mockMvc.perform(post("/api/v1/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CompareRequest(List.of(product1.getId(), product2.getId(), product3.getId()))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.products.length()").value(3));
    }

    @Test
    @DisplayName("Should compare four products successfully (max limit)")
    void shouldCompareFourProducts() throws Exception {
        mockMvc.perform(post("/api/v1/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CompareRequest(List.of(product1.getId(), product2.getId(), product3.getId(), product4.getId()))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.products.length()").value(4))
                .andExpect(jsonPath("$.data.bestOverall").exists())
                .andExpect(jsonPath("$.data.bestValue").exists())
                .andExpect(jsonPath("$.data.bestRating").exists())
                .andExpect(jsonPath("$.data.bestSentiment").exists());
    }

    @Test
    @DisplayName("Should reject comparison with less than 2 products")
    void shouldRejectLessThanTwoProducts() throws Exception {
        mockMvc.perform(post("/api/v1/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CompareRequest(List.of(product1.getId()))
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Should reject comparison with more than 4 products")
    void shouldRejectMoreThanFourProducts() throws Exception {
        // Create 5th product
        Product product5 = Product.builder()
                .name("Another Product")
                .description("Test")
                .price(new BigDecimal("99.99"))
                .stockQuantity(10)
                .category(testCategory)
                .intelligenceScore(70)
                .status(Product.ProductStatus.ACTIVE)
                .build();
        product5 = productRepository.save(product5);

        mockMvc.perform(post("/api/v1/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CompareRequest(List.of(product1.getId(), product2.getId(), product3.getId(), product4.getId(), product5.getId()))
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Should reject comparison with non-existent product")
    void shouldRejectNonExistentProduct() throws Exception {
        mockMvc.perform(post("/api/v1/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CompareRequest(List.of(product1.getId(), 99999L))
                        )))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Should identify best overall product (highest intelligence score)")
    void shouldIdentifyBestOverall() throws Exception {
        // product1 has score 92, product2 has 78
        MvcResult result = mockMvc.perform(post("/api/v1/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CompareRequest(List.of(product1.getId(), product2.getId()))
                        )))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("bestOverall");
        assertThat(responseBody).contains("Premium Wireless Earbuds Pro");
    }

    @Test
    @DisplayName("Should identify best value product (highest score per dollar)")
    void shouldIdentifyBestValue() throws Exception {
        mockMvc.perform(post("/api/v1/comparison")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CompareRequest(List.of(product1.getId(), product2.getId()))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.bestValue").exists());
    }

    @Test
    @DisplayName("Should quick compare two products via GET")
    void shouldQuickCompareViaGet() throws Exception {
        mockMvc.perform(get("/api/v1/comparison/quick/{id1}/{id2}", product1.getId(), product2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.products.length()").value(2));
    }

    // Service-level test for comparison logic
    @Test
    @DisplayName("Service should compare products and return detailed results")
    void serviceShouldCompareProducts() {
        ProductComparisonService.ComparisonResult result = comparisonService.compareProducts(
                List.of(product1.getId(), product2.getId())
        );

        assertThat(result).isNotNull();
        assertThat(result.getProducts()).hasSize(2);
        assertThat(result.getBestOverall()).isEqualTo("Premium Wireless Earbuds Pro");
        assertThat(result.getBestValue()).isNotNull();
        assertThat(result.getBestRating()).isNotNull();
        assertThat(result.getBestSentiment()).isNotNull();
    }

    @Test
    @DisplayName("Service should throw exception for invalid product IDs")
    void serviceShouldThrowForInvalidProducts() {
        assertThatThrownBy(() -> comparisonService.compareProducts(List.of(99999L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("At least 2 products");
    }

    // Request DTO for comparison
    static class CompareRequest {
        private List<Long> productIds;

        public CompareRequest() {}

        public CompareRequest(List<Long> productIds) {
            this.productIds = productIds;
        }

        public List<Long> getProductIds() {
            return productIds;
        }

        public void setProductIds(List<Long> productIds) {
            this.productIds = productIds;
        }
    }
}