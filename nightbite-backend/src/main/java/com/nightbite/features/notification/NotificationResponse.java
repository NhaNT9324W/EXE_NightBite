package com.nightbite.features.notification;

import com.nightbite.domain.Notification;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {

    private Long id;
    private String type;
    private String title;
    private String body;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .type(n.getType().name())
                .title(n.getTitle())
                .body(n.getBody())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
