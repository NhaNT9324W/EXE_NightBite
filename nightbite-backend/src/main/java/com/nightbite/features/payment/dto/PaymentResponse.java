package com.nightbite.features.payment.dto;

import com.nightbite.domain.PaymentMethod;
import com.nightbite.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO dùng để trả về thông tin thanh toán cho Frontend
 * Phục vụ cho task: BE-16 (tích hợp cổng thanh toán)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private String transactionId;  // ID giao dịch từ gateway

    private String paymentUrl;  // URL để redirect đến gateway thanh toán

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String message;
}

