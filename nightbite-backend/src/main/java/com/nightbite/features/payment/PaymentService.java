package com.nightbite.features.payment;

import com.nightbite.domain.Order;
import com.nightbite.domain.PaymentMethod;
import com.nightbite.domain.PaymentStatus;
import com.nightbite.domain.User;
import com.nightbite.features.payment.dto.PaymentRequest;
import com.nightbite.features.payment.dto.PaymentResponse;
import com.nightbite.infrastructure.persistence.OrderRepository;
import com.nightbite.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dịch vụ xử lý logic thanh toán
 * Phục vụ task: BE-16 (tích hợp thanh toán MoMo/ZaloPay)
 * 
 * Hiện tại hỗ trợ:
 * - MoMo (Dự kiến Sprint 2)
 * - ZaloPay (Dự kiến Sprint 2)
 * - CASH (Thanh toán tiền mặt)
 * 
 * Phụ trách: Ngân (BE)
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    // TODO: Thêm các dependency cho MoMo SDK và ZaloPay SDK trong pom.xml khi sẵn sàng

    /**
     * BE-16: Tạo yêu cầu thanh toán
     * Hỗ trợ các phương thức: CASH, MOMO, ZALOPAY
     * 
     * @param request Dữ liệu thanh toán từ Frontend
     * @return PaymentResponse chứa paymentUrl (nếu là MOMO/ZALOPAY) hoặc trạng thái thanh toán
     */
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        // Kiểm tra Order tồn tại
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với ID: " + request.getOrderId()));

        // Kiểm tra User tồn tại
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại với ID: " + request.getUserId()));

        // Cập nhật thông tin thanh toán cho order
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);

        String transactionId = UUID.randomUUID().toString();

        String paymentUrl = null;
        PaymentStatus status = PaymentStatus.PENDING;

        // Xử lý theo phương thức thanh toán
        switch (request.getPaymentMethod()) {
            case CASH:
                // Thanh toán tiền mặt - không cần redirect
                status = PaymentStatus.PENDING;  // Chờ xác nhận từ shop
                break;

            case MOMO:
                // TODO: Tích hợp MoMo Payment Gateway
                // Ví dụ: paymentUrl = createMomoPayment(order, transactionId);
                paymentUrl = createMomoPaymentUrl(order, transactionId, request.getReturnUrl());
                break;

            case ZALOPAY:
                // TODO: Tích hợp ZaloPay Gateway
                // Ví dụ: paymentUrl = createZaloPayPayment(order, transactionId);
                paymentUrl = createZaloPayUrl(order, transactionId, request.getReturnUrl());
                break;

            default:
                throw new RuntimeException("Phương thức thanh toán không hỗ trợ: " + request.getPaymentMethod());
        }

        order.setPaymentStatus(status);
        orderRepository.save(order);

        return PaymentResponse.builder()
                .id(order.getId())
                .orderId(order.getId())
                .userId(user.getId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(status)
                .transactionId(transactionId)
                .paymentUrl(paymentUrl)
                .createdAt(LocalDateTime.now())
                .message(status == PaymentStatus.PENDING 
                    ? "Yêu cầu thanh toán được tạo. Vui lòng hoàn tất thanh toán." 
                    : "Thanh toán thành công!")
                .build();
    }

    /**
     * BE-16: Xác nhận thanh toán hoàn tất (callback từ MoMo/ZaloPay hoặc Shop)
     * 
     * @param orderId ID của đơn hàng
     * @return PaymentResponse với trạng thái COMPLETED
     */
    @Transactional
    public PaymentResponse confirmPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với ID: " + orderId));

        order.setPaymentStatus(PaymentStatus.PAID);
        Order savedOrder = orderRepository.save(order);

        return PaymentResponse.builder()
                .id(savedOrder.getId())
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUser().getId())
                .amount(savedOrder.getTotalAmount())
                .paymentMethod(savedOrder.getPaymentMethod())
                .status(PaymentStatus.PAID)
                .transactionId(savedOrder.getId().toString())
                .createdAt(savedOrder.getCreatedAt())
                .updatedAt(savedOrder.getUpdatedAt())
                .message("Thanh toán đã được xác nhận thành công!")
                .build();
    }

    /**
     * BE-16: Hủy thanh toán / Hoàn lại
     * 
     * @param orderId ID của đơn hàng
     * @return PaymentResponse với trạng thái CANCELLED
     */
    @Transactional
    public PaymentResponse cancelPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với ID: " + orderId));

        order.setPaymentStatus(PaymentStatus.REFUNDED);
        Order savedOrder = orderRepository.save(order);

        return PaymentResponse.builder()
                .id(savedOrder.getId())
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUser().getId())
                .amount(savedOrder.getTotalAmount())
                .paymentMethod(savedOrder.getPaymentMethod())
                .status(PaymentStatus.REFUNDED)
                .transactionId(savedOrder.getId().toString())
                .createdAt(savedOrder.getCreatedAt())
                .updatedAt(savedOrder.getUpdatedAt())
                .message("Thanh toán đã bị hủy!")
                .build();
    }

    /**
     * Lấy trạng thái thanh toán của một đơn hàng
     * 
     * @param orderId ID của đơn hàng
     * @return PaymentResponse chứa trạng thái hiện tại
     */
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với ID: " + orderId));

        return PaymentResponse.builder()
                .id(order.getId())
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .amount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getPaymentStatus())
                .transactionId(order.getId().toString())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    // ===================== HELPER METHODS =====================

    /**
     * Tạo URL thanh toán cho MoMo (placeholder - cần tích hợp SDK thực tế)
     * TODO: Thay thế bằng code thực tế từ MoMo SDK
     */
    private String createMomoPaymentUrl(Order order, String transactionId, String returnUrl) {
        // Placeholder URL - thực tế phải gọi MoMo API
        return "https://test-payment.momo.vn/sandbox/pay?transId=" + transactionId + "&amount=" + order.getTotalAmount();
    }

    /**
     * Tạo URL thanh toán cho ZaloPay (placeholder - cần tích hợp SDK thực tế)
     * TODO: Thay thế bằng code thực tế từ ZaloPay SDK
     */
    private String createZaloPayUrl(Order order, String transactionId, String returnUrl) {
        // Placeholder URL - thực tế phải gọi ZaloPay API
        return "https://sandbox.zalopay.com/api/qr/generate?transId=" + transactionId + "&amount=" + order.getTotalAmount();
    }
}

