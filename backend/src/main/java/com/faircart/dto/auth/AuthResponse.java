package com.faircart.dto.auth;

import com.faircart.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private UserResponse user;

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }
    public static class AuthResponseBuilder {
        private String accessToken;
        private String tokenType = "Bearer";
        private long expiresIn;
        private UserResponse user;

        public AuthResponseBuilder accessToken(String accessToken) { this.accessToken = accessToken; return this; }
        public AuthResponseBuilder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
        public AuthResponseBuilder expiresIn(long expiresIn) { this.expiresIn = expiresIn; return this; }
        public AuthResponseBuilder user(UserResponse user) { this.user = user; return this; }

        public AuthResponse build() {
            AuthResponse a = new AuthResponse();
            a.setAccessToken(accessToken);
            a.setTokenType(tokenType);
            a.setExpiresIn(expiresIn);
            a.setUser(user);
            return a;
        }
    }

    public static class UserResponse {
        private Long id;
        private String email;
        private String fullName;
        private String phone;
        private User.Role role;
        private boolean enabled;

        public UserResponse() {}
        public UserResponse(Long id, String email, String fullName, String phone, User.Role role, boolean enabled) {
            this.id = id;
            this.email = email;
            this.fullName = fullName;
            this.phone = phone;
            this.role = role;
            this.enabled = enabled;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public static UserResponseBuilder builder() { return new UserResponseBuilder(); }
        public static class UserResponseBuilder {
            private Long id;
            private String email;
            private String fullName;
            private String phone;
            private User.Role role;
            private boolean enabled;

            public UserResponseBuilder id(Long id) { this.id = id; return this; }
            public UserResponseBuilder email(String email) { this.email = email; return this; }
            public UserResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public UserResponseBuilder phone(String phone) { this.phone = phone; return this; }
            public UserResponseBuilder role(User.Role role) { this.role = role; return this; }
            public UserResponseBuilder enabled(boolean enabled) { this.enabled = enabled; return this; }

            public UserResponse build() {
                return new UserResponse(id, email, fullName, phone, role, enabled);
            }
        }

        public static UserResponse from(User user) {
            if (user == null) return null;
            return UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .phone(user.getPhone())
                    .role(user.getRole())
                    .enabled(user.isEnabled())
                    .build();
        }
    }
}