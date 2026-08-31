package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_intelligence")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductIntelligence extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private Integer priceScore = 0;

    @Column(nullable = false)
    private Integer ratingScore = 0;

    @Column(nullable = false)
    private Integer sellerScore = 0;

    @Column(nullable = false)
    private Integer availabilityScore = 0;

    @Column(nullable = false)
    private Integer valueScore = 0;

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getPriceScore() { return priceScore; }
    public void setPriceScore(Integer priceScore) { this.priceScore = priceScore; }
    public Integer getRatingScore() { return ratingScore; }
    public void setRatingScore(Integer ratingScore) { this.ratingScore = ratingScore; }
    public Integer getSellerScore() { return sellerScore; }
    public void setSellerScore(Integer sellerScore) { this.sellerScore = sellerScore; }
    public Integer getAvailabilityScore() { return availabilityScore; }
    public void setAvailabilityScore(Integer availabilityScore) { this.availabilityScore = availabilityScore; }
    public Integer getValueScore() { return valueScore; }
    public void setValueScore(Integer valueScore) { this.valueScore = valueScore; }

    public static ProductIntelligenceBuilder builder() { return new ProductIntelligenceBuilder(); }
    public static class ProductIntelligenceBuilder {
        private Product product;
        private Integer priceScore = 0;
        private Integer ratingScore = 0;
        private Integer sellerScore = 0;
        private Integer availabilityScore = 0;
        private Integer valueScore = 0;

        public ProductIntelligenceBuilder product(Product product) { this.product = product; return this; }
        public ProductIntelligenceBuilder priceScore(Integer priceScore) { this.priceScore = priceScore; return this; }
        public ProductIntelligenceBuilder ratingScore(Integer ratingScore) { this.ratingScore = ratingScore; return this; }
        public ProductIntelligenceBuilder sellerScore(Integer sellerScore) { this.sellerScore = sellerScore; return this; }
        public ProductIntelligenceBuilder availabilityScore(Integer availabilityScore) { this.availabilityScore = availabilityScore; return this; }
        public ProductIntelligenceBuilder valueScore(Integer valueScore) { this.valueScore = valueScore; return this; }

        public ProductIntelligence build() {
            ProductIntelligence p = new ProductIntelligence();
            p.setProduct(product);
            p.setPriceScore(priceScore != null ? priceScore : 0);
            p.setRatingScore(ratingScore != null ? ratingScore : 0);
            p.setSellerScore(sellerScore != null ? sellerScore : 0);
            p.setAvailabilityScore(availabilityScore != null ? availabilityScore : 0);
            p.setValueScore(valueScore != null ? valueScore : 0);
            return p;
        }
    }
}