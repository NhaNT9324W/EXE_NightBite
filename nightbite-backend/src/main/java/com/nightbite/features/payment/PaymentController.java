package com.nightbite.features.payment;

import com.nightbite.features.payment.dto.PaymentRequest;
import com.nightbite.features.payment.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller xử lý các endpoint REST API cho thanh toán
 * Base URL: /savibite/payments
 * 
 * Phục vụ task:
 * - BE-16: Tích hợp thanh toán MoMo/ZaloPay
 * 
 * Phụ trách: Ngân (BE)
 */
@RestController
@RequestMapping("/savibite/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "API quản lý thanh toán")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * BE-16: Tạo yêu cầu thanh toán
     * Hỗ trợ: CASH, MOMO, ZALOPAY
     * Quyền hạn: User (authenticated)
     */
    @PostMapping
    @Operation(summary = "Tạo yêu cầu thanh toán",
               description = "Khởi tạo giao dịch thanh toán với MoMo, ZaloPay hoặc thanh toán tiền mặt (BE-16)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo yêu cầu thanh toán thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Đơn hàng hoặc người dùng không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse result = paymentService.createPayment(request);
        return ResponseEntity.ok(buildResponse(true, "Tạo yêu cầu thanh toán thành công!", result));
    }

    /**
     * BE-16: Xác nhận thanh toán hoàn tất
     * Được gọi sau khi user hoàn tất thanh toán trên MoMo/ZaloPay
     * hoặc được gọi từ webhook của gateway
     * Quyền hạn: Admin / System
     */
    @PostMapping("/{orderId}/confirm")
    @Operation(summary = "Xác nhận thanh toán hoàn tất",
               description = "Xác nhận rằng thanh toán đã được hoàn tất (BE-16)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xác nhận thành công"),
            @ApiResponse(responseCode = "404", description = "Đơn hàng không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> confirmPayment(
            @Parameter(description = "ID đơn hàng") @PathVariable Long orderId) {
        PaymentResponse result = paymentService.confirmPayment(orderId);
        return ResponseEntity.ok(buildResponse(true, "Thanh toán đã được xác nhận!", result));
    }

    /**
     * BE-16: Hủy thanh toán / Hoàn lại tiền
     * Quyền hạn: User, Admin
     */
    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Hủy thanh toán",
               description = "Hủy giao dịch thanh toán hoặc hoàn lại tiền (BE-16)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hủy thanh toán thành công"),
            @ApiResponse(responseCode = "404", description = "Đơn hàng không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> cancelPayment(
            @Parameter(description = "ID đơn hàng") @PathVariable Long orderId) {
        PaymentResponse result = paymentService.cancelPayment(orderId);
        return ResponseEntity.ok(buildResponse(true, "Thanh toán đã bị hủy!", result));
    }

    /**
     * BE-16: Kiểm tra trạng thái thanh toán
     * Quyền hạn: User, Admin
     */
    @GetMapping("/{orderId}/status")
    @Operation(summary = "Kiểm tra trạng thái thanh toán",
               description = "Lấy trạng thái thanh toán hiện tại của một đơn hàng (BE-16)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy trạng thái thành công"),
            @ApiResponse(responseCode = "404", description = "Đơn hàng không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> getPaymentStatus(
            @Parameter(description = "ID đơn hàng") @PathVariable Long orderId) {
        PaymentResponse result = paymentService.getPaymentStatus(orderId);
        return ResponseEntity.ok(buildResponse(true, "Lấy trạng thái thanh toán thành công!", result));
    }

    /**
     * Webhook callback từ MoMo (khi thanh toán hoàn tất)
     * TODO: Cần xác thực signature từ MoMo trước khi xử lý
     */
    @PostMapping("/webhook/momo")
    @Operation(summary = "Webhook MoMo callback",
               description = "Endpoint nhận callback từ MoMo khi thanh toán hoàn tất")
    public ResponseEntity<Map<String, Object>> momoCallback(@RequestBody Map<String, Object> payload) {
        // TODO: Xác thực signature
        // TODO: Cập nhật trạng thái thanh toán
        return ResponseEntity.ok(buildResponse(true, "Đã nhận webhook MoMo", null));
    }

    /**
     * Webhook callback từ ZaloPay (khi thanh toán hoàn tất)
     * TODO: Cần xác thực signature từ ZaloPay trước khi xử lý
     */
    @PostMapping("/webhook/zalopay")
    @Operation(summary = "Webhook ZaloPay callback",
               description = "Endpoint nhận callback từ ZaloPay khi thanh toán hoàn tất")
    public ResponseEntity<Map<String, Object>> zaloPayCallback(@RequestBody Map<String, Object> payload) {
        // TODO: Xác thực signature
        // TODO: Cập nhật trạng thái thanh toán
        return ResponseEntity.ok(buildResponse(true, "Đã nhận webhook ZaloPay", null));
    }

    /**
     * Helper method để build response chung
     */
    private Map<String, Object> buildResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return response;
    }
}

