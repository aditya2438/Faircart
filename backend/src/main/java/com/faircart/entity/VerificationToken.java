package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_verification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 10)
    private String otp;

    @Enumerated(EnumType.STRING)
    @Column(name = "otp_type", nullable = false, length = 10)
    private OtpType otpType;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
    public OtpType getOtpType() { return otpType; }
    public void setOtpType(OtpType otpType) { this.otpType = otpType; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public static VerificationTokenBuilder builder() { return new VerificationTokenBuilder(); }
    public static class VerificationTokenBuilder {
        private User user;
        private String otp;
        private OtpType otpType;
        private Instant expiresAt;
        private boolean verified = false;

        public VerificationTokenBuilder user(User user) { this.user = user; return this; }
        public VerificationTokenBuilder otp(String otp) { this.otp = otp; return this; }
        public VerificationTokenBuilder otpType(OtpType otpType) { this.otpType = otpType; return this; }
        public VerificationTokenBuilder expiresAt(Instant expiresAt) { this.expiresAt = expiresAt; return this; }
        public VerificationTokenBuilder verified(boolean verified) { this.verified = verified; return this; }

        public VerificationToken build() {
            VerificationToken v = new VerificationToken();
            v.setUser(user);
            v.setOtp(otp);
            v.setOtpType(otpType);
            v.setExpiresAt(expiresAt);
            v.setVerified(verified);
            return v;
        }
    }

    public enum OtpType {
        EMAIL, PHONE
    }
}