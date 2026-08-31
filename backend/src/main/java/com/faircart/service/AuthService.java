package com.faircart.service;

import com.faircart.dto.auth.AuthResponse;
import com.faircart.dto.auth.LoginRequest;
import com.faircart.dto.auth.RegisterRequest;
import com.faircart.entity.User;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.UserRepository;
import com.faircart.security.JwtTokenProvider;
import com.faircart.service.auth.RedisOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisOtpService redisOtpService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        // Check if phone already exists
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already registered: " + request.getPhone());
        }

        // Create new user
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.CUSTOMER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String accessToken = jwtTokenProvider.generateToken(savedUser.getEmail());
        long expiresIn = jwtTokenProvider.getExpirationMs();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(AuthResponse.UserResponse.from(savedUser))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Load user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("Account is disabled");
        }

        // Generate JWT token
        String accessToken = jwtTokenProvider.generateToken(user.getEmail());
        long expiresIn = jwtTokenProvider.getExpirationMs();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(AuthResponse.UserResponse.from(user))
                .build();
    }

    @Transactional
    public AuthResponse loginWithOtp(String destination, String otp) {
        boolean valid = redisOtpService.verifyOtp(destination, otp);
        if (!valid) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(destination)
                .or(() -> userRepository.findByPhone(destination))
                .orElseGet(() -> {
                    // Quick auto-registration for OTP user if not exists
                    boolean isEmail = destination.contains("@");
                    User newUser = User.builder()
                            .fullName("Faircart User")
                            .email(isEmail ? destination : "user_" + destination + "@faircart.internal")
                            .phone(isEmail ? "+919999999999" : destination)
                            .password(passwordEncoder.encode("OtpAutoAuthSecret123!"))
                            .role(User.Role.CUSTOMER)
                            .enabled(true)
                            .build();
                    return userRepository.save(newUser);
                });

        String accessToken = jwtTokenProvider.generateToken(user.getEmail());
        long expiresIn = jwtTokenProvider.getExpirationMs();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(AuthResponse.UserResponse.from(user))
                .build();
    }

    public String requestOtp(String destination, String channel) {
        RedisOtpService.OtpChannel otpChannel = "PHONE_SMS".equalsIgnoreCase(channel)
                ? RedisOtpService.OtpChannel.PHONE_SMS
                : RedisOtpService.OtpChannel.EMAIL;

        return redisOtpService.generateAndSendOtp(destination, otpChannel);
    }

    @Transactional
    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        userRepository.delete(user);
    }

    public AuthResponse.UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        return AuthResponse.UserResponse.from(user);
    }
}