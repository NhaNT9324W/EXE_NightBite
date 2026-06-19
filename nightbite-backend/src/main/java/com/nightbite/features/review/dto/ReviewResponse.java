package com.nightbite.features.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Đối tượng cấu trúc dữ liệu phản hồi (Response) trả về thông tin bài đánh giá
 * sau khi lọc sạch để hiển thị lên màn hình người dùng.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long orderId;
    private String reviewerName; // Hiển thị tên người đánh giá trực quan cho FE
    private Integer rating;
    private String comment;
    private String imageUrls;
    private LocalDateTime createdAt;
}