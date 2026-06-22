package com.nightbite.features.notification;

import com.nightbite.domain.Notification;
import com.nightbite.domain.User;
import com.nightbite.infrastructure.persistence.NotificationRepository;
import com.nightbite.infrastructure.persistence.UserRepository;
import com.nightbite.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * BE-25 – Push notification khi shop đăng Flash Sale mới.
 *
 * Sprint 3: tích hợp Firebase Admin SDK (FCM).
 * Hiện tại: lưu notification vào DB + log.
 * Khi có Firebase credentials, uncomment phần FirebaseMessaging.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Value("${app.firebase.credentials-file:firebase-service-account.json}")
    private String firebaseCredentialsFile;

    // -------------------------------------------------------------------------
    // Gửi thông báo Flash Sale mới tới tất cả user (broadcast)
    // -------------------------------------------------------------------------

    @Transactional
    public void broadcastFlashSale(Long shopId, String shopName, String productName) {
        String title = "Flash Sale mới từ " + shopName + "!";
        String body  = "'" + productName + "' đang giảm giá. Đặt ngay trước khi hết!";

        // Lưu notification broadcast (user = null) vào DB
        Notification notification = Notification.builder()
                .user(null)   // null = broadcast
                .type(Notification.NotificationType.FLASH_SALE)
                .title(title)
                .body(body)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        // TODO Sprint 3: gửi FCM broadcast
        // sendFcmToTopic("all-users", title, body);
        log.info("[Notification] Broadcast Flash Sale: shopId={}, product='{}'", shopId, productName);
    }

    // -------------------------------------------------------------------------
    // Gửi thông báo cập nhật trạng thái đơn hàng cho user cụ thể
    // -------------------------------------------------------------------------

    @Transactional
    public void sendOrderUpdate(Long userId, String orderCode, String newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        String title = "Đơn hàng " + orderCode + " đã được cập nhật";
        String body  = buildOrderStatusMessage(newStatus);

        Notification notification = Notification.builder()
                .user(user)
                .type(Notification.NotificationType.ORDER_UPDATE)
                .title(title)
                .body(body)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        // TODO Sprint 3: gửi FCM tới device token của user
        // sendFcmToUser(userId, title, body);
        log.info("[Notification] Order update: userId={}, order={}, status={}", userId, orderCode, newStatus);
    }

    // -------------------------------------------------------------------------
    // Gửi thông báo hệ thống (Admin broadcast)
    // -------------------------------------------------------------------------

    @Transactional
    public void sendSystemNotification(String title, String body) {
        Notification notification = Notification.builder()
                .user(null)
                .type(Notification.NotificationType.SYSTEM)
                .title(title)
                .body(body)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
        log.info("[Notification] System broadcast: title='{}'", title);
    }

    // -------------------------------------------------------------------------
    // Lấy danh sách thông báo của user (cá nhân + broadcast)
    // -------------------------------------------------------------------------

    public List<NotificationResponse> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdOrBroadcast(userId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    // -------------------------------------------------------------------------
    // Đếm thông báo chưa đọc
    // -------------------------------------------------------------------------

    public long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // -------------------------------------------------------------------------
    // Đánh dấu tất cả đã đọc
    // -------------------------------------------------------------------------

    @Transactional
    public void markAllRead(Long userId) {
        notificationRepository.markAllReadByUserId(userId);
    }

    // -------------------------------------------------------------------------
    // Firebase FCM helper – TODO Sprint 3
    // -------------------------------------------------------------------------
    //
    // private void sendFcmToTopic(String topic, String title, String body) {
    //     Message message = Message.builder()
    //             .setTopic(topic)
    //             .setNotification(com.google.firebase.messaging.Notification.builder()
    //                     .setTitle(title).setBody(body).build())
    //             .build();
    //     try {
    //         String response = FirebaseMessaging.getInstance().send(message);
    //         log.info("FCM sent to topic '{}': {}", topic, response);
    //     } catch (FirebaseMessagingException e) {
    //         log.error("FCM error: {}", e.getMessage());
    //     }
    // }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private String buildOrderStatusMessage(String status) {
        return switch (status) {
            case "CONFIRMED"  -> "Shop đã xác nhận đơn hàng của bạn. Chuẩn bị đến lấy nhé!";
            case "READY"      -> "Đơn hàng đã sẵn sàng. Hãy đến lấy!";
            case "DONE"       -> "Bạn đã nhận hàng thành công. Đừng quên đánh giá shop nhé!";
            case "CANCELLED"  -> "Đơn hàng đã bị huỷ. Liên hệ shop nếu cần hỗ trợ.";
            default           -> "Trạng thái đơn hàng đã được cập nhật: " + status;
        };
    }
}
