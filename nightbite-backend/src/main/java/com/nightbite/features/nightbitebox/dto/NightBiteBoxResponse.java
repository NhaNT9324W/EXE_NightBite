package com.nightbite.features.nightbitebox.dto;

import com.nightbite.domain.BoxType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO dùng để trả về thông tin NightBite Box cho Frontend
 * Phục vụ cho các task: BE-14 (tạo), BE-15 (đặt)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NightBiteBoxResponse {

    private Long id;

    private Long shopId;

    private String shopName;

    private String boxName;

    private BoxType boxType;

    private String description;

    private String imageUrl;

    private BigDecimal price;

    private Integer quantityAvailable;

    private String allergenWarning;

    private LocalDate saleDate;

    private Boolean isActive;

    private LocalDateTime createdAt;
}

