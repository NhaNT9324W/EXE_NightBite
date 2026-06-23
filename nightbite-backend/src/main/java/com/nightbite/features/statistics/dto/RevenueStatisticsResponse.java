package com.nightbite.features.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO dùng để trả về thống kê doanh thu cho Frontend
 * Phục vụ cho task: BE-24 (thống kê doanh thu)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatisticsResponse {

    private Long shopId;

    private String shopName;

    // Thống kê toàn bộ
    private BigDecimal totalRevenue;

    private Integer totalOrders;

    private Integer completedOrders;

    private Integer cancelledOrders;

    // Thống kê theo từng loại sản phẩm
    private BigDecimal productRevenue;  // Doanh thu từ sản phẩm Flash Sale

    private BigDecimal boxRevenue;  // Doanh thu từ NightBite Box

    private BigDecimal cashRevenue;  // Doanh thu thanh toán tiền mặt

    private BigDecimal momoRevenue;  // Doanh thu thanh toán MoMo

    private BigDecimal zaloPayRevenue;  // Doanh thu thanh toán ZaloPay

    // Chi tiết theo ngày
    private Map<String, BigDecimal> revenueByDate;  // Doanh thu theo ngày

    // Thời gian thống kê
    private String period;  // "TODAY", "THIS_WEEK", "THIS_MONTH", "ALL_TIME"

    private String fromDate;

    private String toDate;

    private Long generatedAt;
}

