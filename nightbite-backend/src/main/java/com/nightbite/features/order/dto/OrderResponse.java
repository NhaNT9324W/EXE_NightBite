package com.nightbite.features.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO trả thông tin chi tiết cấu trúc đơn hàng hoàn chỉnh về phía Frontend hiển thị.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderCode;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime pickupTime;
    private String note;
    private String paymentMethod;
    private String paymentStatus;
    private String cancelReason;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}