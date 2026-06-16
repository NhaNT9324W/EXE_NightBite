package com.nightbite.features.shop;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopRequest {

    @NotBlank(message = "shopName is required")
    @Size(max = 200, message = "shopName must be at most 200 characters")
    private String shopName;

    @NotBlank(message = "ownerName is required")
    @Size(max = 150, message = "ownerName must be at most 150 characters")
    private String ownerName;

    @NotBlank(message = "phone is required")
    @Size(max = 20, message = "phone must be at most 20 characters")
    private String phone;

    @Email(message = "email must be valid")
    @Size(max = 150, message = "email must be at most 150 characters")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 100, message = "password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "address is required")
    private String address;

    @Size(max = 100, message = "district must be at most 100 characters")
    private String district;

    private String description;

    @Size(max = 500, message = "logoUrl must be at most 500 characters")
    private String logoUrl;

    @Size(max = 500, message = "bannerUrl must be at most 500 characters")
    private String bannerUrl;
}

