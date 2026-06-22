package com.nightbite.shared.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Tiện ích tạo và xác thực JWT cho NightBite.
 * Subject = "ROLE:id", ví dụ "USER:42", "SHOP:7", "ADMIN:1"
 */
@Slf4j
@Component
public class JwtUtil {

    private final Key signingKey;
    private final long accessTokenExpMs;
    private final long refreshTokenExpMs;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expiration:86400000}") long accessTokenExpMs,
            @Value("${app.jwt.refresh-token-expiration:604800000}") long refreshTokenExpMs) {

        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpMs = accessTokenExpMs;
        this.refreshTokenExpMs = refreshTokenExpMs;
    }

    // -------------------------------------------------------------------------
    // Token generation
    // -------------------------------------------------------------------------

    /**
     * Tạo Access Token.
     *
     * @param id   ID của user/shop/admin
     * @param role "ROLE_USER" | "ROLE_SHOP" | "ROLE_ADMIN"
     */
    public String generateAccessToken(Long id, String role) {
        return buildToken(id, role, accessTokenExpMs);
    }

    /**
     * Tạo Refresh Token (chỉ chứa id + role, TTL dài hơn).
     */
    public String generateRefreshToken(Long id, String role) {
        return buildToken(id, role, refreshTokenExpMs);
    }

    private String buildToken(Long id, String role, long ttlMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ttlMs);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // -------------------------------------------------------------------------
    // Token parsing
    // -------------------------------------------------------------------------

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    // -------------------------------------------------------------------------
    // Token validation
    // -------------------------------------------------------------------------

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("JWT unsupported: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.warn("JWT malformed: {}", ex.getMessage());
        } catch (SecurityException ex) {
            log.warn("JWT signature invalid: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims empty: {}", ex.getMessage());
        }
        return false;
    }

    public boolean isTokenExpired(String token) {
        try {
            return parseClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }
}
