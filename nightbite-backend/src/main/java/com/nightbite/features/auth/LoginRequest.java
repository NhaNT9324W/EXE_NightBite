package com.nightbite.features.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    /**
     * Email hoặc số điện thoại – AuthService tự nhận diện.
     */
    @NotBlank(message = "Email / số điện thoại không được để trống")
    private String identifier;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
