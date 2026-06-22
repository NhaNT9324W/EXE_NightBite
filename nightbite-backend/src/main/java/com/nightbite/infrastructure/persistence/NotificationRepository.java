package com.nightbite.infrastructure.persistence;

import com.nightbite.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Thông báo cá nhân hoặc broadcast (user_id = null) – sort mới nhất trước
    @Query("""
            SELECT n FROM Notification n
            WHERE n.user.id = :userId OR n.user IS NULL
            ORDER BY n.createdAt DESC
            """)
    List<Notification> findByUserIdOrBroadcast(@Param("userId") Long userId);

    long countByUserIdAndIsReadFalse(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId")
    void markAllReadByUserId(@Param("userId") Long userId);
}
