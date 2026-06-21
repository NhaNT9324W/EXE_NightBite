package com.nightbite.features.statistics;

import com.nightbite.features.statistics.dto.RevenueStatisticsResponse;
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
 * Controller xử lý các endpoint REST API cho thống kê doanh thu
 * Base URL: /savibite/statistics
 * 
 * Phục vụ task:
 * - BE-24: Thống kê doanh thu theo shop hoặc toàn hệ thống
 * 
 * Phụ trách: Ngân (BE)
 */
@RestController
@RequestMapping("/savibite/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "API thống kê doanh thu")
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * BE-24: Lấy thống kê doanh thu theo shop
     * Quyền hạn: Shop, Admin
     */
    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Lấy thống kê doanh thu của shop",
               description = "Lấy thống kê doanh thu của một cửa hàng cụ thể (BE-24)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thống kê thành công"),
            @ApiResponse(responseCode = "404", description = "Shop không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> getShopRevenueStatistics(
            @Parameter(description = "ID cửa hàng") @PathVariable Long shopId,
            @Parameter(description = "Kỳ thống kê: TODAY, THIS_WEEK, THIS_MONTH, ALL_TIME") 
            @RequestParam(defaultValue = "THIS_MONTH") String period) {
        RevenueStatisticsResponse result = statisticsService.getRevenueStatistics(shopId, period);
        return ResponseEntity.ok(buildResponse(true, "Lấy thống kê doanh thu shop thành công!", result));
    }

    /**
     * BE-24: Lấy thống kê doanh thu toàn hệ thống
     * Quyền hạn: Admin
     */
    @GetMapping("/system")
    @Operation(summary = "Lấy thống kê doanh thu toàn hệ thống",
               description = "Lấy thống kê doanh thu toàn bộ hệ thống (BE-24)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thống kê thành công")
    })
    public ResponseEntity<Map<String, Object>> getSystemRevenueStatistics(
            @Parameter(description = "Kỳ thống kê: TODAY, THIS_WEEK, THIS_MONTH, ALL_TIME") 
            @RequestParam(defaultValue = "THIS_MONTH") String period) {
        RevenueStatisticsResponse result = statisticsService.getSystemRevenueStatistics(period);
        return ResponseEntity.ok(buildResponse(true, "Lấy thống kê doanh thu toàn hệ thống thành công!", result));
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

