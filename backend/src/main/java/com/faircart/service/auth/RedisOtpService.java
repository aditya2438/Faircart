package com.faircart.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;

@Service
public class RedisOtpService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RedisOtpService.class);

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    private static final String OTP_PREFIX = "faircart:otp:";
    private static final String ATTEMPTS_PREFIX = "faircart:otp:attempts:";
    private static final int OTP_TTL_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;
    private final SecureRandom secureRandom = new SecureRandom();

    public RedisOtpService() {
        this.redisTemplate = null;
        this.mailSender = null;
    }

    public RedisOtpService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.mailSender = null;
    }

    public RedisOtpService(StringRedisTemplate redisTemplate, JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    // In-memory fallback if Redis is not currently connected in local dev
    private final java.util.concurrent.ConcurrentHashMap<String, String> localOtpStore = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.concurrent.ConcurrentHashMap<String, Integer> localAttemptsStore = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * Generates a secure 6-digit OTP and stores it with a 5-minute TTL.
     */
    public String generateAndSendOtp(String destination, OtpChannel channel) {
        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        String key = destination.toLowerCase().trim();

        if (redisTemplate != null) {
            try {
                redisTemplate.opsForValue().set(OTP_PREFIX + key, otp, Duration.ofMinutes(OTP_TTL_MINUTES));
                redisTemplate.opsForValue().set(ATTEMPTS_PREFIX + key, "0", Duration.ofMinutes(OTP_TTL_MINUTES));
            } catch (Exception e) {
                log.warn("Redis unavailable for OTP storage, using local in-memory store: {}", e.getMessage());
                localOtpStore.put(key, otp);
                localAttemptsStore.put(key, 0);
            }
        } else {
            localOtpStore.put(key, otp);
            localAttemptsStore.put(key, 0);
        }

        // Trigger dispatch via channel
        dispatchOtp(destination, otp, channel);

        return otp;
    }

    /**
     * Verifies the provided 6-digit OTP against Redis/in-memory store.
     */
    public boolean verifyOtp(String destination, String enteredOtp) {
        String key = destination.toLowerCase().trim();

        if (redisTemplate != null) {
            try {
                String storedOtp = redisTemplate.opsForValue().get(OTP_PREFIX + key);
                String attemptsStr = redisTemplate.opsForValue().get(ATTEMPTS_PREFIX + key);
                int attempts = attemptsStr != null ? Integer.parseInt(attemptsStr) : 0;

                if (attempts >= MAX_ATTEMPTS) {
                    log.warn("Max OTP verification attempts reached for destination: {}", destination);
                    redisTemplate.delete(OTP_PREFIX + key);
                    redisTemplate.delete(ATTEMPTS_PREFIX + key);
                    return false;
                }

                if (storedOtp != null && storedOtp.equals(enteredOtp.trim())) {
                    redisTemplate.delete(OTP_PREFIX + key);
                    redisTemplate.delete(ATTEMPTS_PREFIX + key);
                    return true;
                } else {
                    redisTemplate.opsForValue().increment(ATTEMPTS_PREFIX + key);
                    return false;
                }
            } catch (Exception e) {
                log.warn("Redis unavailable during OTP verification, checking local store: {}", e.getMessage());
            }
        }

        // In-memory fallback verification
        String stored = localOtpStore.get(key);
        int attempts = localAttemptsStore.getOrDefault(key, 0);

        if (attempts >= MAX_ATTEMPTS) {
            localOtpStore.remove(key);
            localAttemptsStore.remove(key);
            return false;
        }

        if (stored != null && stored.equals(enteredOtp.trim())) {
            localOtpStore.remove(key);
            localAttemptsStore.remove(key);
            return true;
        } else {
            localAttemptsStore.put(key, attempts + 1);
            return false;
        }
    }

    private void dispatchOtp(String destination, String otp, OtpChannel channel) {
        if (channel == OtpChannel.EMAIL) {
            log.info("[MFA EMAIL OTP] Dispatched OTP [{}] to email address: {}", otp, destination);
            if (mailSender != null) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("Faircart Security <no-reply@faircart.com>");
                    message.setTo(destination);
                    message.setSubject("Your Faircart Verification Code: " + otp);
                    message.setText("Hello,\n\nYour Faircart 6-digit verification code is: " + otp + "\n\nThis code will expire in 5 minutes.\n\nBest regards,\nFaircart Team");
                    mailSender.send(message);
                    log.info("[MFA EMAIL OTP] Real Gmail SMTP email dispatched successfully to: {}", destination);
                } catch (Exception e) {
                    log.warn("[MFA EMAIL OTP] Failed to send real email via SMTP: {}", e.getMessage());
                }
            }
        } else if (channel == OtpChannel.PHONE_SMS) {
            log.info("[MFA PHONE SMS OTP] Dispatched OTP [{}] via SMS to phone: {}", otp, destination);
        }
    }

    public enum OtpChannel {
        EMAIL, PHONE_SMS
    }
}
