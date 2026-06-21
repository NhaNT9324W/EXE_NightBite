package com.nightbite.features.nightbitebox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dùng để tiếp nhận dữ liệu yêu cầu khi User đặt NightBite Box
 * Phục vụ cho task: BE-15 (đặt NightBite Box)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NightBiteBoxOrderRequest {

    private Long userId;

    private Long nightBiteBoxId;

    private Integer quantity;

    private String pickupTime;  // Giờ nhận hàng

    private String notes;  // Ghi chú thêm
}

