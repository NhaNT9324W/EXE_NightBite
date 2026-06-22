package com.nightbite.features.auth;

import lombok.Builder;
import lombok.Data;

/**
 * Trả về cho GET /auth/me
 */
@Data
@Builder
public class MeResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private String role;

    // User only
    private Integer trustScore;
    private Boolean isActive;

    // Shop only
    private String shopName;
    private String district;
}
