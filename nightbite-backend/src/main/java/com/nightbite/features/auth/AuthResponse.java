package com.nightbite.features.auth;

import lombok.Builder;
import lombok.Data;

/**
 * Trả về sau khi đăng ký / đăng nhập thành công.
 */
@Data
@Builder
public class AuthResponse {

    private Long id;
    private String fullName;   // tên user hoặc tên shop
    private String email;
    private String role;       // "ROLE_USER" | "ROLE_SHOP" | "ROLE_ADMIN"
    private String accessToken;
    private String refreshToken;
    private long expiresIn;    // access token TTL tính bằng milliseconds
}
