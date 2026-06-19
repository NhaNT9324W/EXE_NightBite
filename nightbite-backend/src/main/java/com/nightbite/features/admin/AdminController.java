package com.nightbite.features.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp kiểm soát điều hướng cung cấp các API quản trị đặc quyền cao cho Admin[cite: 1].
 * Định vị Base URL chuẩn phân hệ: /savibite/admin[cite: 1].
 */
@RestController
@RequestMapping("/savibite/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * Endpoint xử lý khóa hoặc mở khóa tài khoản khách hàng (Task BE-22)
     * Phân quyền truy cập: ADMIN.
     * @param active giá trị true là mở khóa, false là khóa
     */
    @PatchMapping("/users/{id}/lock")
    public ResponseEntity<Map<String, Object>> toggleUserLock(
            @PathVariable Long id,
            @RequestParam boolean active) {
        adminService.updateUserLockState(id, active);
        String msg = active ? "Đã mở khóa tài khoản người dùng thành công!" : "Đã khóa tài khoản người dùng vi phạm!";
        return ResponseEntity.ok(buildResponse(true, msg, null));
    }

    /**
     * Endpoint xử lý đình chỉ hoạt động hoặc mở lại quyền bán hàng cho Shop đối tác (Task BE-22).
     * Phân quyền truy cập: ADMIN
     * @param active giá trị true là mở bán lại, false là đình chỉ quán
     */
    @PatchMapping("/shops/{id}/lock")
    public ResponseEntity<Map<String, Object>> toggleShopLock(
            @PathVariable Long id,
            @RequestParam boolean active) {
        adminService.updateShopLockState(id, active);
        String msg = active ? "Đã mở khóa hoạt động kinh doanh cho Shop!" : "Đã đình chỉ hoạt động Shop vi phạm!";
        return ResponseEntity.ok(buildResponse(true, msg, null));
    }

    private Map<String, Object> buildResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}