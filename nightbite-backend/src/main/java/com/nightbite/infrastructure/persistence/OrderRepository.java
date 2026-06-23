package com.nightbite.infrastructure.persistence;

import com.nightbite.domain.Order;
import com.nightbite.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByShopId(Long shopId);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByShopIdOrderByCreatedAtDesc(Long shopId);

    Optional<Order> findByOrderCode(String orderCode);

    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    List<Order> findByShopIdAndStatus(Long shopId, OrderStatus status);

    /** Đếm số đơn bị huỷ của user trong khoảng thời gian – dùng cho TrustScore */
    @Query("""
            SELECT COUNT(o) FROM Order o
            WHERE o.user.id = :userId
              AND o.status = 'CANCELLED'
              AND o.updatedAt >= :since
            """)
    long countCancelledOrdersSince(@Param("userId") Long userId,
                                   @Param("since") LocalDateTime since);

    /** Thống kê doanh thu shop theo ngày (BE-24) */
    @Query("""
            SELECT CAST(o.createdAt AS date), COUNT(o), SUM(o.totalAmount)
            FROM Order o
            WHERE o.shop.id = :shopId
              AND o.status = 'DONE'
              AND o.createdAt BETWEEN :from AND :to
            GROUP BY CAST(o.createdAt AS date)
            ORDER BY CAST(o.createdAt AS date)
            """)
    List<Object[]> getRevenueStats(@Param("shopId") Long shopId,
                                   @Param("from") LocalDateTime from,
                                   @Param("to") LocalDateTime to);
}
