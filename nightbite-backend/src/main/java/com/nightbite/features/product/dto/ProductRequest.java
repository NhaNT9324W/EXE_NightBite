package com.nightbite.features.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO dùng để tiếp nhận dữ liệu yêu cầu từ Frontend khi tạo/cập nhật sản phẩm Flash Sale
 * Phục vụ cho các task: BE-05, BE-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long id;  // Chỉ truyền lên khi cập nhật

    private Long shopId;  // ID của cửa hàng sở hữu sản phẩm

    private String name;

    private String description;

    private String imageUrl;

    private BigDecimal originalPrice;

    private BigDecimal salePrice;

    private Integer quantityAvailable;

    private LocalTime saleStartTime;

    private LocalTime saleEndTime;

    private LocalDate saleDate;

    private String allergenTags;  // VD: "Peanut, Dairy, Gluten"

    private String category;

    private Boolean isActive;
}

