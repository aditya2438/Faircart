package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;

    @Column
    private Long totalSales = 0L;

    @Column(nullable = false)
    private boolean active = true;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public Long getTotalSales() { return totalSales; }
    public void setTotalSales(Long totalSales) { this.totalSales = totalSales; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static SellerBuilder builder() { return new SellerBuilder(); }
    public static class SellerBuilder {
        private String name;
        private String email;
        private String phone;
        private String address;
        private BigDecimal rating = BigDecimal.ZERO;
        private Long totalSales = 0L;
        private boolean active = true;

        public SellerBuilder name(String name) { this.name = name; return this; }
        public SellerBuilder email(String email) { this.email = email; return this; }
        public SellerBuilder phone(String phone) { this.phone = phone; return this; }
        public SellerBuilder address(String address) { this.address = address; return this; }
        public SellerBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public SellerBuilder totalSales(Long totalSales) { this.totalSales = totalSales; return this; }
        public SellerBuilder active(boolean active) { this.active = active; return this; }

        public Seller build() {
            Seller s = new Seller();
            s.setName(name);
            s.setEmail(email);
            s.setPhone(phone);
            s.setAddress(address);
            s.setRating(rating != null ? rating : BigDecimal.ZERO);
            s.setTotalSales(totalSales != null ? totalSales : 0L);
            s.setActive(active);
            return s;
        }
    }
}