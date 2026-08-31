package com.faircart.dto.product;

import com.faircart.entity.Category;
import com.faircart.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private CategoryResponse category;
    private String imageUrl;
    private Integer intelligenceScore;
    private Product.ProductStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public CategoryResponse getCategory() { return category; }
    public void setCategory(CategoryResponse category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getIntelligenceScore() { return intelligenceScore; }
    public void setIntelligenceScore(Integer intelligenceScore) { this.intelligenceScore = intelligenceScore; }
    public Product.ProductStatus getStatus() { return status; }
    public void setStatus(Product.ProductStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static ProductResponseBuilder builder() { return new ProductResponseBuilder(); }
    public static class ProductResponseBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stockQuantity;
        private CategoryResponse category;
        private String imageUrl;
        private Integer intelligenceScore;
        private Product.ProductStatus status;
        private Instant createdAt;
        private Instant updatedAt;

        public ProductResponseBuilder id(Long id) { this.id = id; return this; }
        public ProductResponseBuilder name(String name) { this.name = name; return this; }
        public ProductResponseBuilder description(String description) { this.description = description; return this; }
        public ProductResponseBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductResponseBuilder stockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; return this; }
        public ProductResponseBuilder category(CategoryResponse category) { this.category = category; return this; }
        public ProductResponseBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductResponseBuilder intelligenceScore(Integer intelligenceScore) { this.intelligenceScore = intelligenceScore; return this; }
        public ProductResponseBuilder status(Product.ProductStatus status) { this.status = status; return this; }
        public ProductResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ProductResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public ProductResponse build() {
            ProductResponse p = new ProductResponse();
            p.setId(id);
            p.setName(name);
            p.setDescription(description);
            p.setPrice(price);
            p.setStockQuantity(stockQuantity);
            p.setCategory(category);
            p.setImageUrl(imageUrl);
            p.setIntelligenceScore(intelligenceScore);
            p.setStatus(status);
            p.setCreatedAt(createdAt);
            p.setUpdatedAt(updatedAt);
            return p;
        }
    }

    public static class CategoryResponse {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private String imageUrl;

        public CategoryResponse() {}
        public CategoryResponse(Long id, String name, String slug, String description, String imageUrl) {
            this.id = id;
            this.name = name;
            this.slug = slug;
            this.description = description;
            this.imageUrl = imageUrl;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

        public static CategoryResponseBuilder builder() { return new CategoryResponseBuilder(); }
        public static class CategoryResponseBuilder {
            private Long id;
            private String name;
            private String slug;
            private String description;
            private String imageUrl;
            public CategoryResponseBuilder id(Long id) { this.id = id; return this; }
            public CategoryResponseBuilder name(String name) { this.name = name; return this; }
            public CategoryResponseBuilder slug(String slug) { this.slug = slug; return this; }
            public CategoryResponseBuilder description(String description) { this.description = description; return this; }
            public CategoryResponseBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
            public CategoryResponse build() { return new CategoryResponse(id, name, slug, description, imageUrl); }
        }

        public static CategoryResponse from(Category category) {
            if (category == null) return null;
            return CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .slug(category.getSlug())
                    .description(category.getDescription())
                    .imageUrl(category.getImageUrl())
                    .build();
        }
    }

    public static ProductResponse from(Product product) {
        if (product == null) return null;
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(CategoryResponse.from(product.getCategory()))
                .imageUrl(product.getImageUrl())
                .intelligenceScore(product.getIntelligenceScore())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}