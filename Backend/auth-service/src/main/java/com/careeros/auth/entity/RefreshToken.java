package com.careeros.auth.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_token_value", columnList = "token", unique = true)
})
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    public RefreshToken() {}

    public RefreshToken(UUID id, User user, String token, Instant expiryDate) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Instant getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }

    public static RefreshTokenBuilder builder() { return new RefreshTokenBuilder(); }

    public static class RefreshTokenBuilder {
        private UUID id;
        private User user;
        private String token;
        private Instant expiryDate;

        public RefreshTokenBuilder id(UUID id) { this.id = id; return this; }
        public RefreshTokenBuilder user(User user) { this.user = user; return this; }
        public RefreshTokenBuilder token(String token) { this.token = token; return this; }
        public RefreshTokenBuilder expiryDate(Instant expiryDate) { this.expiryDate = expiryDate; return this; }

        public RefreshToken build() {
            return new RefreshToken(id, user, token, expiryDate);
        }
    }
}