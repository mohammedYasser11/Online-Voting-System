package com.sprints.online_voting_system.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtManager {

    /**
        CRITICAL: The fallback secret must be at least 32 characters for HS256 algorithm
        DO NOT UNDER ANY CIRCUMSTANCES MAKE THE SECRET LESS THAN  32 CHAR
        I SPENT AN HOUR NOT KNOWING HOW DOES @REQUIREDARCGS NOT WORK
    */
    @Value("${JWT_SECRET:mySecretKeyForVotingSystemThatShouldBeAtLeast256BitsLongForHS256AlgorithmSecureFallback}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION:86400}") // Default 24 hours in seconds
    private long jwtExpirationInSeconds;

    private SecretKey getSigningKey() {
        // Validate secret length to prevent runtime exceptions
        if (jwtSecret.getBytes().length < 32) {
            throw new IllegalArgumentException("JWT secret too short for HS256 algorithm");
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Long userId, String role) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtExpirationInSeconds, ChronoUnit.SECONDS);

        return Jwts.builder()
                .subject(String.valueOf(userId)) // Use userId as subject
                .claim("role", role)
                .claim("userId", userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public String getRoleFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("role", String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}" + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("JWT token is malformed: {}" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token is invalid: {}" + e.getMessage());
        } catch (Exception e) {
                System.out.println("JWT token validation error: {}" + e.getMessage());
        }
        return false;
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}