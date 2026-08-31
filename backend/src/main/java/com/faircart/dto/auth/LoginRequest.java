package com.faircart.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public static LoginRequestBuilder builder() { return new LoginRequestBuilder(); }
    public static class LoginRequestBuilder {
        private String email;
        private String password;
        public LoginRequestBuilder email(String email) { this.email = email; return this; }
        public LoginRequestBuilder password(String password) { this.password = password; return this; }
        public LoginRequest build() {
            LoginRequest l = new LoginRequest();
            l.setEmail(email);
            l.setPassword(password);
            return l;
        }
    }
}