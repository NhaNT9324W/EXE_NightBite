package com.nightbite.features.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO dùng để trả về thông tin sản phẩm Flash Sale cho Frontend
 * Phục vụ cho các task: BE-05, BE-06, BE-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private Long shopId;

    private String shopName;

    private String name;

    private String description;

    private String imageUrl;

    private BigDecimal originalPrice;

    private BigDecimal salePrice;

    private Integer quantityAvailable;

    private Integer quantitySold;

    private LocalTime saleStartTime;

    private LocalTime saleEndTime;

    private LocalDate saleDate;

    private String allergenTags;

    private String category;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

