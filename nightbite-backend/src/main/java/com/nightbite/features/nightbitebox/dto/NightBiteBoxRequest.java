package com.nightbite.features.nightbitebox.dto;

import com.nightbite.domain.BoxType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO dùng để tiếp nhận dữ liệu yêu cầu từ Frontend khi tạo NightBite Box
 * Phục vụ cho task: BE-14 (tạo NightBite Box)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NightBiteBoxRequest {

    private Long id;  // Chỉ truyền lên khi cập nhật

    private Long shopId;  // ID của cửa hàng sở hữu box

    private String boxName;

    private BoxType boxType;  // SMALL, MEDIUM, LARGE

    private String description;

    private String imageUrl;

    private BigDecimal price;

    private Integer quantityAvailable;

    private String allergenWarning;  // Cảnh báo dị ứng có trong hộp

    private LocalDate saleDate;

    private Boolean isActive;
}

