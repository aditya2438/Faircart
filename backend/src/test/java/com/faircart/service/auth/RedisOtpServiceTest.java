package com.faircart.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RedisOtpServiceTest {

    private RedisOtpService redisOtpService;

    @BeforeEach
    void setUp() {
        // Test with in-memory fallback (null redisTemplate)
        redisOtpService = new RedisOtpService();
    }

    @Test
    @DisplayName("Should generate a 6-digit numeric OTP")
    void shouldGenerateSixDigitOtp() {
        String otp = redisOtpService.generateAndSendOtp("user@example.com", RedisOtpService.OtpChannel.EMAIL);
        
        assertThat(otp).isNotNull();
        assertThat(otp).hasSize(6);
        assertThat(otp).matches("\\d{6}");
    }

    @Test
    @DisplayName("Should verify valid OTP successfully")
    void shouldVerifyValidOtp() {
        String destination = "verify@example.com";
        String otp = redisOtpService.generateAndSendOtp(destination, RedisOtpService.OtpChannel.EMAIL);

        boolean verified = redisOtpService.verifyOtp(destination, otp);
        assertThat(verified).isTrue();

        // OTP is consumed upon single use
        boolean secondAttempt = redisOtpService.verifyOtp(destination, otp);
        assertThat(secondAttempt).isFalse();
    }

    @Test
    @DisplayName("Should lock out after 3 failed verification attempts")
    void shouldLockoutAfterMaxAttempts() {
        String destination = "lockout@example.com";
        String correctOtp = redisOtpService.generateAndSendOtp(destination, RedisOtpService.OtpChannel.EMAIL);

        // Attempt 1: wrong OTP
        assertThat(redisOtpService.verifyOtp(destination, "000000")).isFalse();
        // Attempt 2: wrong OTP
        assertThat(redisOtpService.verifyOtp(destination, "111111")).isFalse();
        // Attempt 3: wrong OTP (max reached)
        assertThat(redisOtpService.verifyOtp(destination, "222222")).isFalse();

        // Attempt 4: even if correct OTP is provided now, it is invalidated/locked
        assertThat(redisOtpService.verifyOtp(destination, correctOtp)).isFalse();
    }
}
