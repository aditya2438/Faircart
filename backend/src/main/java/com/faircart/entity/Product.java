package com.faircart.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 500)
    private String imageUrl;

    /** Product Intelligence Score (0–100) */
    @Builder.Default
    @Column(nullable = false)
    private Integer intelligenceScore = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductIntelligence intelligence;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlistItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceHistory> priceHistory;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getIntelligenceScore() { return intelligenceScore; }
    public void setIntelligenceScore(Integer intelligenceScore) { this.intelligenceScore = intelligenceScore; }
    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }
    public ProductIntelligence getIntelligence() { return intelligence; }
    public void setIntelligence(ProductIntelligence intelligence) { this.intelligence = intelligence; }
    public List<Wishlist> getWishlistItems() { return wishlistItems; }
    public void setWishlistItems(List<Wishlist> wishlistItems) { this.wishlistItems = wishlistItems; }
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
    public List<PriceHistory> getPriceHistory() { return priceHistory; }
    public void setPriceHistory(List<PriceHistory> priceHistory) { this.priceHistory = priceHistory; }

    public static ProductBuilder builder() { return new ProductBuilder(); }
    public static class ProductBuilder {
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stockQuantity;
        private Category category;
        private String imageUrl;
        private Integer intelligenceScore = 0;
        private ProductStatus status = ProductStatus.ACTIVE;
        public ProductBuilder name(String name) { this.name = name; return this; }
        public ProductBuilder description(String description) { this.description = description; return this; }
        public ProductBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductBuilder stockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; return this; }
        public ProductBuilder category(Category category) { this.category = category; return this; }
        public ProductBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductBuilder intelligenceScore(Integer intelligenceScore) { this.intelligenceScore = intelligenceScore; return this; }
        public ProductBuilder status(ProductStatus status) { this.status = status; return this; }
        public Product build() {
            Product p = new Product();
            p.setName(name);
            p.setDescription(description);
            p.setPrice(price);
            p.setStockQuantity(stockQuantity);
            p.setCategory(category);
            p.setImageUrl(imageUrl);
            p.setIntelligenceScore(intelligenceScore != null ? intelligenceScore : 0);
            p.setStatus(status != null ? status : ProductStatus.ACTIVE);
            return p;
        }
    }

    public enum ProductStatus {
        ACTIVE, INACTIVE, DISCONTINUED
    }
}
