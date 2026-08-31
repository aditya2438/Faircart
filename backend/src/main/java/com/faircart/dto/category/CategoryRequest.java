package com.faircart.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    private Integer displayOrder = 0;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public static CategoryRequestBuilder builder() { return new CategoryRequestBuilder(); }
    public static class CategoryRequestBuilder {
        private String name;
        private String description;
        private String imageUrl;
        private Integer displayOrder = 0;

        public CategoryRequestBuilder name(String name) { this.name = name; return this; }
        public CategoryRequestBuilder description(String description) { this.description = description; return this; }
        public CategoryRequestBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public CategoryRequestBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }

        public CategoryRequest build() {
            CategoryRequest c = new CategoryRequest();
            c.setName(name);
            c.setDescription(description);
            c.setImageUrl(imageUrl);
            c.setDisplayOrder(displayOrder != null ? displayOrder : 0);
            return c;
        }
    }
}