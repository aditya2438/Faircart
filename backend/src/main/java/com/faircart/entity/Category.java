package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;

    @Column
    private Integer displayOrder = 0;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public static CategoryBuilder builder() { return new CategoryBuilder(); }
    public static class CategoryBuilder {
        private String name;
        private String slug;
        private String description;
        private String imageUrl;
        private boolean active = true;
        private Integer displayOrder = 0;

        public CategoryBuilder name(String name) { this.name = name; return this; }
        public CategoryBuilder slug(String slug) { this.slug = slug; return this; }
        public CategoryBuilder description(String description) { this.description = description; return this; }
        public CategoryBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public CategoryBuilder active(boolean active) { this.active = active; return this; }
        public CategoryBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }

        public Category build() {
            Category c = new Category();
            c.setName(name);
            c.setSlug(slug);
            c.setDescription(description);
            c.setImageUrl(imageUrl);
            c.setActive(active);
            c.setDisplayOrder(displayOrder != null ? displayOrder : 0);
            return c;
        }
    }
}