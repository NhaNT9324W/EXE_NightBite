package com.nightbite.infrastructure.persistence;

import com.nightbite.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Giao diện tương tác cơ sở dữ liệu (Repository) cho thực thể Order.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Tìm kiếm danh sách đơn hàng theo mã định danh của người mua (User).
     */
    List<Order> findByUserId(Long userId);

    /**
     * Tìm kiếm danh sách đơn hàng theo mã định danh của cửa hàng đối tác (Shop).
     */
    List<Order> findByShopId(Long shopId);
}