package com.faircart.dto.seller;

import com.faircart.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private BigDecimal rating;
    private Long totalSales;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static SellerResponseBuilder builder() { return new SellerResponseBuilder(); }
    public static class SellerResponseBuilder {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String address;
        private BigDecimal rating;
        private Long totalSales;
        private Boolean active;
        private Instant createdAt;
        private Instant updatedAt;

        public SellerResponseBuilder id(Long id) { this.id = id; return this; }
        public SellerResponseBuilder name(String name) { this.name = name; return this; }
        public SellerResponseBuilder email(String email) { this.email = email; return this; }
        public SellerResponseBuilder phone(String phone) { this.phone = phone; return this; }
        public SellerResponseBuilder address(String address) { this.address = address; return this; }
        public SellerResponseBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public SellerResponseBuilder totalSales(Long totalSales) { this.totalSales = totalSales; return this; }
        public SellerResponseBuilder active(Boolean active) { this.active = active; return this; }
        public SellerResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public SellerResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public SellerResponse build() {
            SellerResponse s = new SellerResponse();
            s.setId(id);
            s.setName(name);
            s.setEmail(email);
            s.setPhone(phone);
            s.setAddress(address);
            s.setRating(rating);
            s.setTotalSales(totalSales);
            s.setActive(active);
            s.setCreatedAt(createdAt);
            s.setUpdatedAt(updatedAt);
            return s;
        }
    }

    public static SellerResponse from(Seller seller) {
        if (seller == null) return null;
        return SellerResponse.builder()
                .id(seller.getId())
                .name(seller.getName())
                .email(seller.getEmail())
                .phone(seller.getPhone())
                .address(seller.getAddress())
                .rating(seller.getRating())
                .totalSales(seller.getTotalSales())
                .active(seller.isActive())
                .createdAt(seller.getCreatedAt())
                .updatedAt(seller.getUpdatedAt())
                .build();
    }

    public static List<SellerResponse> from(List<Seller> sellers) {
        if (sellers == null) return List.of();
        return sellers.stream()
                .map(SellerResponse::from)
                .toList();
    }
}