package com.nightbite.infrastructure.persistence;

import com.nightbite.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Giao diện tương tác cơ sở dữ liệu (Repository) cho thực thể OrderItem.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Lấy danh sách tất cả các chi tiết mặt hàng thuộc một đơn hàng cụ thể.
     */
    List<OrderItem> findByOrderId(Long orderId);
}