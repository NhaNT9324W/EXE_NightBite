package com.nightbite.features.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {

    private Long id;
    private String shopName;
    private String ownerName;
    private String phone;
    private String email;
    private String address;
    private String district;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private Boolean isActive;
    private BigDecimal ratingAvg;
    private Integer reviewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

