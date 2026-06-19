package com.nightbite.features.order;

import com.nightbite.domain.*;
import com.nightbite.features.order.dto.*;
import com.nightbite.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Lớp dịch vụ nghiệp vụ xử lý luồng logic Đặt hàng và Đơn hàng (Order Flow).
 * Thực hiện toàn bộ các chức năng: BE-11, BE-12 và BE-13.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository; // Giả định Ngân (BE) đã tạo ở Product Feature

    // Giả định repository này tồn tại để xử lý Box
    private final NightBiteBoxRepository nightBiteBoxRepository;

    /**
     * Nghiệp vụ xử lý tạo đơn hàng mới cho User đặt món Flash Sale (Task BE-11).
     * Bao gồm kiểm tra tồn kho, trừ kho, tính tổng tiền và sinh mã đơn tự động.
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người mua"));
        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin cửa hàng"));

        // 1. Khởi tạo đối tượng Đơn hàng cơ bản
        Order order = Order.builder()
                .user(user)
                .shop(shop)
                .orderCode(generateOrderCode())
                .status(OrderStatus.PENDING)
                .pickupTime(request.getPickupTime())
                .note(request.getNote())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()))
                .paymentStatus(PaymentStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        Order savedOrder = orderRepository.save(order);

        BigDecimal grandTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 2. Duyệt qua từng item để tính tiền và trừ kho dữ liệu
        for (OrderItemRequest itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setItemType(ItemType.valueOf(itemReq.getItemType().toUpperCase()));
            item.setQuantity(itemReq.getQuantity());

            if (item.getItemType() == ItemType.PRODUCT) {
                Product product = productRepository.findById(itemReq.getProductId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm Flash Sale"));

                // Kiểm tra số lượng sản phẩm xem có đủ bán không
                if (product.getQuantityAvailable() < itemReq.getQuantity()) {
                    throw new RuntimeException("Sản phẩm '" + product.getName() + "' đã hết hàng hoặc không đủ số lượng!");
                }

                // Cập nhật trừ số lượng kho và tăng số lượng đã bán
                product.setQuantityAvailable(product.getQuantityAvailable() - itemReq.getQuantity());
                product.setQuantitySold(product.getQuantitySold() + itemReq.getQuantity());
                productRepository.save(product);

                item.setProduct(product);
                item.setItemName(product.getName());
                item.setUnitPrice(product.getSalePrice());
            } else {
                // Xử lý luồng cho NightBite Box nghiệp vụ phụ phụ trách
                NightBiteBox box = nightBiteBoxRepository.findById(itemReq.getBoxId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Hộp bí ẩn"));

                if (box.getQuantityAvailable() < itemReq.getQuantity()) {
                    throw new RuntimeException("Hộp bí ẩn đã cháy hàng!");
                }

                box.setQuantityAvailable(box.getQuantityAvailable() - itemReq.getQuantity());
                nightBiteBoxRepository.save(box);

                item.setBox(box);
                item.setItemName(box.getBoxName());
                item.setUnitPrice(box.getPrice());
            }

            // Tính toán thành tiền của item
            BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setSubtotal(subtotal);
            grandTotal = grandTotal.add(subtotal);

            orderItems.add(orderItemRepository.save(item));
        }

        // 3. Cập nhật lại tổng số tiền chuẩn xác cuối cùng cho đơn hàng
        savedOrder.setTotalAmount(grandTotal);
        orderRepository.save(savedOrder);

        return mapToResponse(savedOrder, orderItems);
    }

    /**
     * Nghiệp vụ xem lịch sử đơn hàng linh hoạt phân lọc theo Role (Task BE-12).
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrderHistory(Long id, String role) {
        List<Order> orders;
        if ("SHOP".equalsIgnoreCase(role)) {
            orders = orderRepository.findByShopId(id);
        } else {
            orders = orderRepository.findByUserId(id);
        }

        return orders.stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return mapToResponse(order, items);
                })
                .collect(Collectors.toList());
    }

    /**
     * Nghiệp vụ cập nhật trạng thái đơn hàng dành riêng cho chủ cửa hàng xử lý (Task BE-13).
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String newStatus, String cancelReason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng yêu cầu"));

        OrderStatus status = OrderStatus.valueOf(newStatus.toUpperCase());
        order.setStatus(status);

        if (status == OrderStatus.CANCELLED && cancelReason != null) {
            order.setCancelReason(cancelReason);
        }

        Order updatedOrder = orderRepository.save(order);
        List<OrderItem> items = orderItemRepository.findByOrderId(updatedOrder.getId());
        return mapToResponse(updatedOrder, items);
    }

    /**
     * Hàm nội bộ tự động sinh mã đơn hàng hiển thị theo quy ước: NB-YYYYMMDD-XXXXX
     */
    private String generateOrderCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "NB-" + dateStr + "-" + uniqueId;
    }

    private OrderResponse mapToResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemDtos = items.stream().map(i -> OrderItemResponse.builder()
                .id(i.getId())
                .itemType(i.getItemType().name())
                .productId(i.getProduct() != null ? i.getProduct().getId() : null)
                .boxId(i.getBox() != null ? i.getBox().getId() : null)
                .itemName(i.getItemName())
                .unitPrice(i.getUnitPrice())
                .quantity(i.getQuantity())
                .subtotal(i.getSubtotal())
                .build()).collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .pickupTime(order.getPickupTime())
                .note(order.getNote())
                .paymentMethod(order.getPaymentMethod().name())
                .paymentStatus(order.getPaymentStatus().name())
                .cancelReason(order.getCancelReason())
                .createdAt(order.getCreatedAt())
                .items(itemDtos)
                .build();
    }
}