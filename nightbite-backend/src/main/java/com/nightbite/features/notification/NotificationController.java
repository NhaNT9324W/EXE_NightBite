package com.nightbite.features.notification;

import com.nightbite.shared.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Notifications", description = "Thông báo push – BE-25")
@RestController
@RequestMapping("/savibite/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // -------------------------------------------------------------------------
    // GET /notifications – lấy thông báo của user đang đăng nhập
    // -------------------------------------------------------------------------
    @Operation(
        summary = "Lấy danh sách thông báo của user hiện tại",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications() {
        Long userId = getCurrentUserId();
        List<NotificationResponse> list = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    // -------------------------------------------------------------------------
    // GET /notifications/unread-count
    // -------------------------------------------------------------------------
    @Operation(
        summary = "Đếm số thông báo chưa đọc",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/unread-count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> countUnread() {
        Long userId = getCurrentUserId();
        long count = notificationService.countUnread(userId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("unreadCount", count)));
    }

    // -------------------------------------------------------------------------
    // PATCH /notifications/read-all – đánh dấu tất cả đã đọc
    // -------------------------------------------------------------------------
    @Operation(
        summary = "Đánh dấu tất cả thông báo là đã đọc",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/read-all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> markAllRead() {
        Long userId = getCurrentUserId();
        notificationService.markAllRead(userId);
        return ResponseEntity.ok(ApiResponse.success("Đã đánh dấu tất cả thông báo là đã đọc"));
    }

    // -------------------------------------------------------------------------
    // POST /notifications/broadcast – Admin gửi thông báo hệ thống
    // -------------------------------------------------------------------------
    @Operation(
        summary = "Admin gửi thông báo hệ thống (broadcast)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> broadcast(
            @RequestBody Map<String, String> body) {

        String title   = body.getOrDefault("title", "Thông báo từ NightBite");
        String content = body.getOrDefault("body", "");
        notificationService.sendSystemNotification(title, content);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi thông báo tới tất cả người dùng"));
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
