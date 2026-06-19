package com.nightbite.features.order;

import com.nightbite.features.order.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp điều phối REST API phục vụ cho nghiệp vụ quản lý luồng đơn hàng (Order API).
 * Định tuyến Base URL: /savibite/orders[cite: 173].
 */
@RestController
@RequestMapping("/savibite/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Endpoint xử lý tạo mới đơn hàng khi khách hàng mua hàng (Task BE-11)[cite: 23, 173, 196].
     * Phân quyền: USER[cite: 23, 173, 191].
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createNewOrder(@RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(buildResponse(true, "Khởi tạo đơn hàng thành công!", response));
    }

    /**
     * Endpoint truy xuất xem lịch sử giao dịch đơn hàng của hệ thống (Task BE-12)[cite: 23, 173, 196].
     * Phân quyền: Cả hai đối tượng (User / Shop)[cite: 23, 173].
     * * @param id Mã định danh đối tượng cần lấy lịch sử (UserId hoặc ShopId)
     * @param role Vai trò đối tượng lọc hệ thống (USER hoặc SHOP)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrderHistory(
            @RequestParam Long id,
            @RequestParam String role) {
        List<OrderResponse> response = orderService.getOrderHistory(id, role);
        return ResponseEntity.ok(buildResponse(true, "Truy xuất danh sách lịch sử đơn hàng thành công!", response));
    }

    /**
     * Endpoint cho phép chủ quán thay đổi cập nhật trạng thái xử lý đơn hàng (Task BE-13)[cite: 23, 173, 196].
     * Phân quyền: SHOP[cite: 23, 173, 191].
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> changeOrderStatusState(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String cancelReason) {
        OrderResponse response = orderService.updateOrderStatus(id, status, cancelReason);
        return ResponseEntity.ok(buildResponse(true, "Cập nhật trạng thái tiến trình đơn hàng thành công!", response));
    }

    /**
     * Gói bọc dữ liệu (Response Wrapper) cấu trúc đầu ra chuẩn hóa của hệ thống[cite: 163, 208].
     */
    private Map<String, Object> buildResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}