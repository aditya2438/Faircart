package com.faircart.dto.seller;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
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
public class SellerRequest {

    @NotBlank(message = "Seller name is required")
    @Size(min = 2, max = 200, message = "Seller name must be between 2 and 200 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 10, max = 20, message = "Phone number must be between 10 and 20 digits")
    private String phone;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private BigDecimal rating;

    @NotNull(message = "Active status is required")
    private Boolean active = true;

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
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public static SellerRequestBuilder builder() { return new SellerRequestBuilder(); }
    public static class SellerRequestBuilder {
        private String name;
        private String email;
        private String phone;
        private String address;
        private BigDecimal rating;
        private Boolean active = true;

        public SellerRequestBuilder name(String name) { this.name = name; return this; }
        public SellerRequestBuilder email(String email) { this.email = email; return this; }
        public SellerRequestBuilder phone(String phone) { this.phone = phone; return this; }
        public SellerRequestBuilder address(String address) { this.address = address; return this; }
        public SellerRequestBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public SellerRequestBuilder active(Boolean active) { this.active = active; return this; }

        public SellerRequest build() {
            SellerRequest s = new SellerRequest();
            s.setName(name);
            s.setEmail(email);
            s.setPhone(phone);
            s.setAddress(address);
            s.setRating(rating);
            s.setActive(active != null ? active : true);
            return s;
        }
    }
}