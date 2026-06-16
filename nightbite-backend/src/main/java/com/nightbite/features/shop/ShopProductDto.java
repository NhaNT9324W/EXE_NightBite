package com.nightbite.features.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopProductDto {

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
}

