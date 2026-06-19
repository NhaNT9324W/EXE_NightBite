package com.nightbite.features.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO chứa toàn bộ dữ liệu yêu cầu gửi lên từ Frontend để khởi tạo đơn hàng mới (Task BE-11).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long userId; // Tạm thời dùng truyền trực tiếp ở Sprint 1-2 khi chưa bật JWT
    private Long shopId;
    private LocalDateTime pickupTime;
    private String note;
    private String paymentMethod; // MOMO, ZALOPAY, CASH, MOCK
    private List<OrderItemRequest> items;
}