package com.faircart.dto.category;

import com.faircart.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Boolean active;
    private Integer displayOrder;
    private Instant createdAt;
    private Instant updatedAt;

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
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static CategoryResponseBuilder builder() { return new CategoryResponseBuilder(); }
    public static class CategoryResponseBuilder {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private String imageUrl;
        private Boolean active;
        private Integer displayOrder;
        private Instant createdAt;
        private Instant updatedAt;

        public CategoryResponseBuilder id(Long id) { this.id = id; return this; }
        public CategoryResponseBuilder name(String name) { this.name = name; return this; }
        public CategoryResponseBuilder slug(String slug) { this.slug = slug; return this; }
        public CategoryResponseBuilder description(String description) { this.description = description; return this; }
        public CategoryResponseBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public CategoryResponseBuilder active(Boolean active) { this.active = active; return this; }
        public CategoryResponseBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }
        public CategoryResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public CategoryResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public CategoryResponse build() {
            CategoryResponse c = new CategoryResponse();
            c.setId(id);
            c.setName(name);
            c.setSlug(slug);
            c.setDescription(description);
            c.setImageUrl(imageUrl);
            c.setActive(active);
            c.setDisplayOrder(displayOrder);
            c.setCreatedAt(createdAt);
            c.setUpdatedAt(updatedAt);
            return c;
        }
    }

    public static CategoryResponse from(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .active(category.isActive())
                .displayOrder(category.getDisplayOrder())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public static List<CategoryResponse> from(List<Category> categories) {
        if (categories == null) return List.of();
        return categories.stream()
                .map(CategoryResponse::from)
                .toList();
    }
}