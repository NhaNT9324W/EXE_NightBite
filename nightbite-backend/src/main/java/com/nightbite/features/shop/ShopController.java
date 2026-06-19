package com.nightbite.features.shop;

import com.nightbite.features.shop.dto.ShopRequest;
import com.nightbite.features.shop.dto.ShopResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp điều hướng điều khiển (Controller) chịu trách nhiệm cấu hình các endpoint REST API cho Shop[cite: 1].
 * Định vị Base URL chuẩn của phân hệ: /savibite/shops[cite: 1].
 */
@RestController
@RequestMapping("/savibite/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    /**
     * Endpoint xử lý yêu cầu đăng ký thông tin hoặc cập nhật cấu hình cửa hàng (Task BE-03)[cite: 1].
     * Quyền hạn thực hiện: Shop đối tác[cite: 1].
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveShop(@RequestBody ShopRequest shopRequest) {
        ShopResponse result = shopService.createOrUpdateShop(shopRequest);
        return ResponseEntity.ok(buildResponse(true, "Lưu thông tin dữ liệu Shop thành công!", result));
    }

    /**
     * Endpoint lấy thông tin hiển thị chi tiết của một Shop cụ thể qua mã ID[cite: 1].
     * Quyền hạn thực hiện: Public[cite: 1].
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getShopById(@PathVariable Long id) {
        ShopResponse result = shopService.getShopById(id);
        return ResponseEntity.ok(buildResponse(true, "Lấy thông tin chi tiết Shop thành công!", result));
    }

    /**
     * Endpoint lấy danh sách toàn bộ Shop đi kèm tính năng lọc theo địa bàn hành chính (Task BE-04)[cite: 1].
     * Quyền hạn thực hiện: User / Public[cite: 1].
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllShops(@RequestParam(required = false) String district) {
        List<ShopResponse> result = shopService.getAllShops(district);
        return ResponseEntity.ok(buildResponse(true, "Lấy danh sách dữ liệu Shop thành công!", result));
    }

    /**
     * Cấu trúc Response Wrapper chuẩn hóa để đồng bộ hóa dữ liệu trả về với phía Frontend ReactJS[cite: 1].
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