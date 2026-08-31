package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "product_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private Long helpfulCount = 0L;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Long getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(Long helpfulCount) { this.helpfulCount = helpfulCount; }

    public static ReviewBuilder builder() { return new ReviewBuilder(); }
    public static class ReviewBuilder {
        private User user;
        private Product product;
        private Integer rating;
        private String comment;
        private Long helpfulCount = 0L;

        public ReviewBuilder user(User user) { this.user = user; return this; }
        public ReviewBuilder product(Product product) { this.product = product; return this; }
        public ReviewBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewBuilder comment(String comment) { this.comment = comment; return this; }
        public ReviewBuilder helpfulCount(Long helpfulCount) { this.helpfulCount = helpfulCount; return this; }

        public Review build() {
            Review r = new Review();
            r.setUser(user);
            r.setProduct(product);
            r.setRating(rating);
            r.setComment(comment);
            r.setHelpfulCount(helpfulCount != null ? helpfulCount : 0L);
            return r;
        }
    }
}