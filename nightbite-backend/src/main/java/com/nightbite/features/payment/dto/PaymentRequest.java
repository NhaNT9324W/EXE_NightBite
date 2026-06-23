package com.nightbite.features.payment.dto;

import com.nightbite.domain.PaymentMethod;
import com.nightbite.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO dùng để tiếp nhận dữ liệu yêu cầu thanh toán từ Frontend
 * Phục vụ cho task: BE-16 (tích hợp cổng thanh toán)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;  // MOMO, ZALOPAY, CASH

    private String returnUrl;  // URL trả về sau khi thanh toán

    private String description;  // Mô tả giao dịch
}

