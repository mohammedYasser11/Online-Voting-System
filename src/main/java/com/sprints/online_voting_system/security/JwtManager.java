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
import java.util.Map;

@Slf4j
@Component
public class JwtManager {

    @Value("${jwt.secret:mySecretKeyForVotingSystemThatShouldBeAtLeast256BitsLongForHS256Algorithm}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400}") // Default 24 hours in seconds
    private long jwtExpirationInSeconds;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate JWT token with custom claims
     * @param username the username
     * @param customClaims additional claims to include in the token
     * @return JWT token string
     */
    public String generateToken(String username, Map<String, Object> customClaims) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtExpirationInSeconds, ChronoUnit.SECONDS);

        JwtBuilder builder = Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey());

        // Add custom claims
        if (customClaims != null && !customClaims.isEmpty()) {
            customClaims.forEach(builder::claim);
        }

        return builder.compact();
    }

    /**
     * Generate JWT token with username only
     * @param username the username
     * @return JWT token string
     */
    public String generateToken(String username) {
        return generateToken(username, (String) null);
    }

    /**
     * Generate JWT token with username and role
     * @param username the username
     * @param role the user role
     * @return JWT token string
     */
    public String generateToken(String username, String role) {
        return generateToken(username, Map.of("role", role));
    }

    /**
     * Generate JWT token with username, role, and userId
     * @param username the username
     * @param role the user role
     * @param userId the user ID
     * @return JWT token string
     */
    public String generateToken(String username, String role, Long userId) {
        return generateToken(username, Map.of(
                "role", role,
                "userId", userId
        ));
    }

    /**
     * Extract username from JWT token
     * @param token JWT token
     * @return username
     */
    public String getUsernameFromToken(String token) {
        return getEncodedElementFromToken(token).getSubject();
    }

    /**
     * Extract custom claim from JWT token
     * @param token JWT token
     * @param claimName name of the claim
     * @param claimType class type of the claim
     * @return claim value
     */
    public <T> T getClaimFromToken(String token, String claimName, Class<T> claimType) {
        Claims claims = getEncodedElementFromToken(token);
        return claims.get(claimName, claimType);
    }

    /**
     * Extract role from JWT token
     * @param token JWT token
     * @return role
     */
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, "role", String.class);
    }

    public Long getUserIdFromToken(String token) {
        return getClaimFromToken(token, "userId", Long.class);
    }

    public Date getExpirationDateFromToken(String token) {
        return getEncodedElementFromToken(token).getExpiration();
    }

    // That is a big function
    // Will be extremely useful TBH
    public Claims getEncodedElementFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
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
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT token is malformed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT token is invalid: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT token validation error: {}", e.getMessage());
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

    /**
     * Extract token from Authorization header
     * @param authHeader Authorization header value
     * @return JWT token without "Bearer " prefix
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}