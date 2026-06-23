package com.nightbite.features.order;

import com.nightbite.features.order.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp điều phối REST API phục vụ cho nghiệp vụ quản lý luồng đơn hàng (Order API).
 * Định tuyến Base URL: /savibite/orders.
 *
 * Phục vụ các task: BE-11 (tạo đơn), BE-12 (lịch sử), BE-13 (cập nhật trạng thái), BE-15 (đặt box)
 */
@RestController
@RequestMapping("/savibite/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "API quản lý đơn hàng")
public class OrderController {

    private final OrderService orderService;

    /**
     * Endpoint xử lý tạo mới đơn hàng khi khách hàng mua hàng (Task BE-11)
     * Phân quyền: USER
     */
    @PostMapping
    @Operation(summary = "Tạo đơn hàng mới", description = "Khách hàng tạo đơn hàng mua sản phẩm Flash Sale hoặc NightBite Box (BE-11)")
    public ResponseEntity<Map<String, Object>> createNewOrder(@RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(buildResponse(true, "Khởi tạo đơn hàng thành công!", response));
    }

    /**
     * BE-15: Đặt NightBite Box
     * Endpoint để User đặt NightBite Box từ một shop
     * Quyền hạn: User
     */
    @PostMapping("/nightbite-box")
    @Operation(summary = "Đặt NightBite Box",
            description = "Khách hàng đặt NightBite Box từ cửa hàng (BE-15)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đặt box thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "User, Shop hoặc Box không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> orderNightBiteBox(@RequestBody NightBiteBoxOrderRequest request) {
        OrderResponse response = orderService.createNightBiteBoxOrder(request);
        return ResponseEntity.ok(buildResponse(true, "Đặt NightBite Box thành công!", response));
    }

    /**
     * Endpoint truy xuất xem lịch sử giao dịch đơn hàng của hệ thống (Task BE-12)
     * Phân quyền: Cả hai đối tượng (User / Shop)[cite: 23, 173].
     *
     * @param id   Mã định danh đối tượng cần lấy lịch sử (UserId hoặc ShopId)
     * @param role Vai trò đối tượng lọc hệ thống (USER hoặc SHOP)
     */
    @GetMapping
    @Operation(summary = "Lấy lịch sử đơn hàng", description = "Lấy danh sách đơn hàng của User hoặc Shop (BE-12)")
    public ResponseEntity<Map<String, Object>> getOrderHistory(
            @Parameter(description = "ID người dùng/cửa hàng") @RequestParam Long id,
            @Parameter(description = "Vai trò (USER hoặc SHOP)") @RequestParam String role) {
        List<OrderResponse> response = orderService.getOrderHistory(id, role);
        return ResponseEntity.ok(buildResponse(true, "Truy xuất danh sách lịch sử đơn hàng thành công!", response));
    }

    /**
     * Endpoint cho phép chủ quán thay đổi cập nhật trạng thái xử lý đơn hàng (Task BE-13)
     * Phân quyền: SHOP[cite: 23, 173, 191].
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái đơn hàng", description = "Cửa hàng cập nhật trạng thái đơn hàng (BE-13)")
    public ResponseEntity<Map<String, Object>> changeOrderStatusState(
            @Parameter(description = "ID đơn hàng") @PathVariable Long id,
            @Parameter(description = "Trạng thái mới") @RequestParam String status,
            @Parameter(description = "Lý do hủy (nếu hủy)") @RequestParam(required = false) String cancelReason) {
        OrderResponse response = orderService.updateOrderStatus(id, status, cancelReason);
        return ResponseEntity.ok(buildResponse(true, "Cập nhật trạng thái tiến trình đơn hàng thành công!", response));
    }

    /**
     * Gói bọc dữ liệu (Response Wrapper) cấu trúc đầu ra chuẩn hóa của hệ thống
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