package com.nightbite.features.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO dùng để tiếp nhận yêu cầu đặt NightBite Box từ Frontend
 * Phục vụ cho task: BE-15 (đặt NightBite Box)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NightBiteBoxOrderRequest {

    private Long userId;

    private Long shopId;

    private Long nightBiteBoxId;

    private Integer quantity;

    private LocalDateTime pickupTime;

    private String note;
}

