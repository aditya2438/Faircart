package com.faircart.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public static ProductRequestBuilder builder() { return new ProductRequestBuilder(); }
    public static class ProductRequestBuilder {
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stockQuantity;
        private Long categoryId;
        private String imageUrl;

        public ProductRequestBuilder name(String name) { this.name = name; return this; }
        public ProductRequestBuilder description(String description) { this.description = description; return this; }
        public ProductRequestBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductRequestBuilder stockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; return this; }
        public ProductRequestBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public ProductRequestBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }

        public ProductRequest build() {
            ProductRequest p = new ProductRequest();
            p.setName(name);
            p.setDescription(description);
            p.setPrice(price);
            p.setStockQuantity(stockQuantity);
            p.setCategoryId(categoryId);
            p.setImageUrl(imageUrl);
            return p;
        }
    }
}