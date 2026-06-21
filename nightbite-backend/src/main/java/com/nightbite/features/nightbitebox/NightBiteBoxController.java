package com.nightbite.features.nightbitebox;

import com.nightbite.features.nightbitebox.dto.NightBiteBoxRequest;
import com.nightbite.features.nightbitebox.dto.NightBiteBoxResponse;
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
 * Controller xử lý các endpoint REST API cho NightBite Box
 * Base URL: /savibite/nightbite-boxes
 * 
 * Phục vụ các task:
 * - BE-14: Tạo NightBite Box
 * - BE-15: Đặt NightBite Box
 * 
 * Phụ trách: Ngân (BE)
 */
@RestController
@RequestMapping("/savibite/nightbite-boxes")
@RequiredArgsConstructor
@Tag(name = "NightBite Box", description = "API quản lý NightBite Box")
public class NightBiteBoxController {

    private final NightBiteBoxService nightBiteBoxService;

    /**
     * BE-14: Tạo NightBite Box mới
     * Quyền hạn: Shop
     */
    @PostMapping
    @Operation(summary = "Tạo NightBite Box mới",
               description = "Cho phép cửa hàng tạo NightBite Box mới (BE-14)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Shop không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> createNightBiteBox(@RequestBody NightBiteBoxRequest request) {
        NightBiteBoxResponse result = nightBiteBoxService.createNightBiteBox(request);
        return ResponseEntity.ok(buildResponse(true, "Tạo NightBite Box thành công!", result));
    }

    /**
     * BE-14: Cập nhật NightBite Box
     * Quyền hạn: Shop
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật NightBite Box",
               description = "Cửa hàng cập nhật thông tin NightBite Box (BE-14)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "NightBite Box không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> updateNightBiteBox(
            @Parameter(description = "ID NightBite Box") @PathVariable Long id,
            @RequestBody NightBiteBoxRequest request) {
        NightBiteBoxResponse result = nightBiteBoxService.updateNightBiteBox(id, request);
        return ResponseEntity.ok(buildResponse(true, "Cập nhật NightBite Box thành công!", result));
    }

    /**
     * BE-14: Xóa NightBite Box (soft delete)
     * Quyền hạn: Shop
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa NightBite Box",
               description = "Cửa hàng xóa NightBite Box (BE-14)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "NightBite Box không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> deleteNightBiteBox(
            @Parameter(description = "ID NightBite Box") @PathVariable Long id) {
        nightBiteBoxService.deleteNightBiteBox(id);
        return ResponseEntity.ok(buildResponse(true, "Xóa NightBite Box thành công!", null));
    }

    /**
     * BE-15: Lấy chi tiết NightBite Box
     * Quyền hạn: User/Public
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết NightBite Box",
               description = "Lấy thông tin chi tiết của một NightBite Box (BE-15)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy dữ liệu thành công"),
            @ApiResponse(responseCode = "404", description = "NightBite Box không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> getNightBiteBoxById(
            @Parameter(description = "ID NightBite Box") @PathVariable Long id) {
        NightBiteBoxResponse result = nightBiteBoxService.getNightBiteBoxById(id);
        return ResponseEntity.ok(buildResponse(true, "Lấy chi tiết NightBite Box thành công!", result));
    }

    /**
     * BE-15: Lấy danh sách NightBite Box của một shop
     * Quyền hạn: User/Public
     */
    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Lấy NightBite Box của shop",
               description = "Lấy danh sách toàn bộ NightBite Box của một cửa hàng (BE-15)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy dữ liệu thành công"),
            @ApiResponse(responseCode = "404", description = "Shop không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> getNightBiteBoxesByShop(
            @Parameter(description = "ID cửa hàng") @PathVariable Long shopId) {
        List<NightBiteBoxResponse> result = nightBiteBoxService.getNightBiteBoxesByShop(shopId);
        return ResponseEntity.ok(buildResponse(true, "Lấy danh sách NightBite Box của shop thành công!", result));
    }

    /**
     * BE-15: Lấy danh sách NightBite Box đang bán hôm nay
     * Quyền hạn: User/Public
     */
    @GetMapping("/sale/active")
    @Operation(summary = "Lấy danh sách NightBite Box đang bán",
               description = "Lấy các NightBite Box đang có sẵn hôm nay (BE-15)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy dữ liệu thành công")
    })
    public ResponseEntity<Map<String, Object>> getActiveSaleBoxes() {
        List<NightBiteBoxResponse> result = nightBiteBoxService.getActiveSaleBoxes();
        return ResponseEntity.ok(buildResponse(true, "Lấy danh sách NightBite Box đang bán thành công!", result));
    }

    /**
     * BE-15: Lấy NightBite Box theo loại (BoxType)
     * Quyền hạn: User/Public
     */
    @GetMapping("/type/{boxType}")
    @Operation(summary = "Lấy NightBite Box theo loại",
               description = "Lấy danh sách NightBite Box theo loại hộp (SMALL, MEDIUM, LARGE) (BE-15)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy dữ liệu thành công")
    })
    public ResponseEntity<Map<String, Object>> getBoxesByType(
            @Parameter(description = "Loại hộp (SMALL, MEDIUM, LARGE)") @PathVariable String boxType) {
        List<NightBiteBoxResponse> result = nightBiteBoxService.getBoxesByType(boxType);
        return ResponseEntity.ok(buildResponse(true, "Lấy NightBite Box theo loại thành công!", result));
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

