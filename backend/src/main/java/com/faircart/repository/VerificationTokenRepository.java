package com.faircart.repository;

import com.faircart.entity.VerificationToken;
import com.faircart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByOtpAndUserAndOtpTypeAndVerifiedFalse(String otp, User user, VerificationToken.OtpType otpType);

    List<VerificationToken> findByUserAndVerifiedFalse(User user);

    void deleteByUserAndVerifiedFalse(User user);

    void deleteByExpiresAtBefore(Instant instant);
}