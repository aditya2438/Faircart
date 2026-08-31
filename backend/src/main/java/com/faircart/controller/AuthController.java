package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.dto.auth.AuthResponse;
import com.faircart.dto.auth.LoginRequest;
import com.faircart.dto.auth.RegisterRequest;
import com.faircart.security.JwtTokenProvider;
import com.faircart.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @PostMapping("/otp/send")
    public ResponseEntity<ApiResponse<String>> sendOtp(@Valid @RequestBody OtpSendRequest request) {
        authService.requestOtp(request.getDestination(), request.getChannel());
        return ResponseEntity.ok(ApiResponse.ok("OTP dispatched successfully", "6-digit OTP sent"));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        AuthResponse response = authService.loginWithOtp(request.getDestination(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.ok("OTP authentication successful", response));
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(java.security.Principal principal) {
        if (principal != null) {
            authService.deleteAccount(principal.getName());
        }
        return ResponseEntity.ok(ApiResponse.ok("Account deleted successfully in accordance with data privacy standards", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse.UserResponse>> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getSubject(token);
                AuthResponse.UserResponse user = authService.getCurrentUser(email);
                return ResponseEntity.ok(ApiResponse.ok(user));
            }
        }
        return ResponseEntity.status(401).body(ApiResponse.<AuthResponse.UserResponse>builder()
                .success(false)
                .message("Unauthorized")
                .build());
    }

    @Data
    public static class OtpSendRequest {
        @NotBlank(message = "Destination email or phone is required")
        private String destination;
        private String channel = "EMAIL";

        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        public String getChannel() { return channel; }
        public void setChannel(String channel) { this.channel = channel; }
    }

    @Data
    public static class OtpVerifyRequest {
        @NotBlank(message = "Destination email or phone is required")
        private String destination;
        @NotBlank(message = "OTP is required")
        private String otp;

        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
    }
}