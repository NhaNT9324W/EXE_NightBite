package com.nightbite.features.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin yêu cầu của từng mặt hàng khi tiến hành đặt đơn.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private String itemType; // PRODUCT hoặc BOX
    private Long productId;
    private Long boxId;
    private Integer quantity;
}