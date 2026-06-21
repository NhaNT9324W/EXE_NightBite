package com.nightbite.features.statistics;

import com.nightbite.domain.*;
import com.nightbite.features.statistics.dto.RevenueStatisticsResponse;
import com.nightbite.infrastructure.persistence.OrderItemRepository;
import com.nightbite.infrastructure.persistence.OrderRepository;
import com.nightbite.infrastructure.persistence.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dịch vụ xử lý thống kê doanh thu
 * Phục vụ task: BE-24 (thống kê doanh thu)
 * Phụ trách: Ngân (BE)
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShopRepository shopRepository;

    /**
     * BE-24: Lấy thống kê doanh thu theo shop
     * 
     * @param shopId ID của shop
     * @param period Kỳ thống kê: TODAY, THIS_WEEK, THIS_MONTH, ALL_TIME
     * @return RevenueStatisticsResponse chứa các thông tin thống kê
     */
    @Transactional(readOnly = true)
    public RevenueStatisticsResponse getRevenueStatistics(Long shopId, String period) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại với ID: " + shopId));

        // Lấy danh sách đơn hàng theo kỳ thống kê
        List<Order> orders = getOrdersByPeriod(shopId, period);

        // Lọc chỉ các đơn hàng đã hoàn thành
        List<Order> completedOrders = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DONE)
                .collect(Collectors.toList());

        // Tính toán doanh thu tổng
        BigDecimal totalRevenue = completedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính toán doanh thu theo phương thức thanh toán
        BigDecimal cashRevenue = completedOrders.stream()
                .filter(o -> o.getPaymentMethod() == PaymentMethod.CASH)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal momoRevenue = completedOrders.stream()
                .filter(o -> o.getPaymentMethod() == PaymentMethod.MOMO)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal zaloPayRevenue = completedOrders.stream()
                .filter(o -> o.getPaymentMethod() == PaymentMethod.ZALOPAY)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính toán doanh thu theo loại sản phẩm
        BigDecimal productRevenue = BigDecimal.ZERO;
        BigDecimal boxRevenue = BigDecimal.ZERO;

        for (Order order : completedOrders) {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            for (OrderItem item : items) {
                if (item.getItemType() == ItemType.PRODUCT) {
                    productRevenue = productRevenue.add(item.getSubtotal());
                } else if (item.getItemType() == ItemType.BOX) {
                    boxRevenue = boxRevenue.add(item.getSubtotal());
                }
            }
        }

        // Tính toán doanh thu theo ngày
        Map<String, BigDecimal> revenueByDate = calculateRevenueByDate(completedOrders);

        // Xác định ngày bắt đầu và kết thúc
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();

        switch (period.toUpperCase()) {
            case "TODAY":
                fromDate = LocalDate.now();
                toDate = LocalDate.now();
                break;
            case "THIS_WEEK":
                fromDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                toDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                break;
            case "THIS_MONTH":
                fromDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                toDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                break;
            case "ALL_TIME":
                fromDate = LocalDate.of(2020, 1, 1);
                toDate = LocalDate.now();
                break;
        }

        return RevenueStatisticsResponse.builder()
                .shopId(shopId)
                .shopName(shop.getShopName())
                .totalRevenue(totalRevenue)
                .totalOrders(orders.size())
                .completedOrders(completedOrders.size())
                .cancelledOrders((int) orders.stream()
                        .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
                        .count())
                .productRevenue(productRevenue)
                .boxRevenue(boxRevenue)
                .cashRevenue(cashRevenue)
                .momoRevenue(momoRevenue)
                .zaloPayRevenue(zaloPayRevenue)
                .revenueByDate(revenueByDate)
                .period(period.toUpperCase())
                .fromDate(fromDate.toString())
                .toDate(toDate.toString())
                .generatedAt(System.currentTimeMillis())
                .build();
    }

    /**
     * BE-24: Lấy thống kê doanh thu toàn hệ thống (Admin)
     * 
     * @param period Kỳ thống kê: TODAY, THIS_WEEK, THIS_MONTH, ALL_TIME
     * @return RevenueStatisticsResponse cho toàn hệ thống
     */
    @Transactional(readOnly = true)
    public RevenueStatisticsResponse getSystemRevenueStatistics(String period) {
        List<Order> orders = getSystemOrdersByPeriod(period);

        List<Order> completedOrders = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DONE)
                .collect(Collectors.toList());

        BigDecimal totalRevenue = completedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cashRevenue = completedOrders.stream()
                .filter(o -> o.getPaymentMethod() == PaymentMethod.CASH)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal momoRevenue = completedOrders.stream()
                .filter(o -> o.getPaymentMethod() == PaymentMethod.MOMO)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal zaloPayRevenue = completedOrders.stream()
                .filter(o -> o.getPaymentMethod() == PaymentMethod.ZALOPAY)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal productRevenue = BigDecimal.ZERO;
        BigDecimal boxRevenue = BigDecimal.ZERO;

        for (Order order : completedOrders) {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            for (OrderItem item : items) {
                if (item.getItemType() == ItemType.PRODUCT) {
                    productRevenue = productRevenue.add(item.getSubtotal());
                } else if (item.getItemType() == ItemType.BOX) {
                    boxRevenue = boxRevenue.add(item.getSubtotal());
                }
            }
        }

        Map<String, BigDecimal> revenueByDate = calculateRevenueByDate(completedOrders);

        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();

        switch (period.toUpperCase()) {
            case "TODAY":
                fromDate = LocalDate.now();
                toDate = LocalDate.now();
                break;
            case "THIS_WEEK":
                fromDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                toDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                break;
            case "THIS_MONTH":
                fromDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                toDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                break;
            case "ALL_TIME":
                fromDate = LocalDate.of(2020, 1, 1);
                toDate = LocalDate.now();
                break;
        }

        return RevenueStatisticsResponse.builder()
                .shopId(null)
                .shopName("Toàn Hệ Thống")
                .totalRevenue(totalRevenue)
                .totalOrders(orders.size())
                .completedOrders(completedOrders.size())
                .cancelledOrders((int) orders.stream()
                        .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
                        .count())
                .productRevenue(productRevenue)
                .boxRevenue(boxRevenue)
                .cashRevenue(cashRevenue)
                .momoRevenue(momoRevenue)
                .zaloPayRevenue(zaloPayRevenue)
                .revenueByDate(revenueByDate)
                .period(period.toUpperCase())
                .fromDate(fromDate.toString())
                .toDate(toDate.toString())
                .generatedAt(System.currentTimeMillis())
                .build();
    }

    // ===================== HELPER METHODS =====================

    /**
     * Lấy danh sách đơn hàng theo kỳ thống kê cho một shop cụ thể
     */
    private List<Order> getOrdersByPeriod(Long shopId, String period) {
        List<Order> allOrders = orderRepository.findByShopId(shopId);
        LocalDateTime now = LocalDateTime.now();

        switch (period.toUpperCase()) {
            case "TODAY":
                return allOrders.stream()
                        .filter(o -> o.getCreatedAt().toLocalDate().equals(LocalDate.now()))
                        .collect(Collectors.toList());

            case "THIS_WEEK":
                LocalDate mondayOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                LocalDate sundayOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                return allOrders.stream()
                        .filter(o -> {
                            LocalDate orderDate = o.getCreatedAt().toLocalDate();
                            return !orderDate.isBefore(mondayOfWeek) && !orderDate.isAfter(sundayOfWeek);
                        })
                        .collect(Collectors.toList());

            case "THIS_MONTH":
                LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                return allOrders.stream()
                        .filter(o -> {
                            LocalDate orderDate = o.getCreatedAt().toLocalDate();
                            return !orderDate.isBefore(firstDayOfMonth) && !orderDate.isAfter(lastDayOfMonth);
                        })
                        .collect(Collectors.toList());

            case "ALL_TIME":
            default:
                return allOrders;
        }
    }

    /**
     * Lấy danh sách đơn hàng theo kỳ thống kê cho toàn hệ thống
     */
    private List<Order> getSystemOrdersByPeriod(String period) {
        List<Order> allOrders = orderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        switch (period.toUpperCase()) {
            case "TODAY":
                return allOrders.stream()
                        .filter(o -> o.getCreatedAt().toLocalDate().equals(LocalDate.now()))
                        .collect(Collectors.toList());

            case "THIS_WEEK":
                LocalDate mondayOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                LocalDate sundayOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                return allOrders.stream()
                        .filter(o -> {
                            LocalDate orderDate = o.getCreatedAt().toLocalDate();
                            return !orderDate.isBefore(mondayOfWeek) && !orderDate.isAfter(sundayOfWeek);
                        })
                        .collect(Collectors.toList());

            case "THIS_MONTH":
                LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                return allOrders.stream()
                        .filter(o -> {
                            LocalDate orderDate = o.getCreatedAt().toLocalDate();
                            return !orderDate.isBefore(firstDayOfMonth) && !orderDate.isAfter(lastDayOfMonth);
                        })
                        .collect(Collectors.toList());

            case "ALL_TIME":
            default:
                return allOrders;
        }
    }

    /**
     * Tính toán doanh thu theo từng ngày
     */
    private Map<String, BigDecimal> calculateRevenueByDate(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getCreatedAt().toLocalDate().toString(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Order::getTotalAmount,
                                BigDecimal::add
                        )
                ));
    }
}

