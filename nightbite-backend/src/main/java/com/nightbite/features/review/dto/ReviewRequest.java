package com.nightbite.features.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Đối tượng chứa dữ liệu gửi đánh giá (Request) từ Frontend truyền lên hệ thống.
 * Phục vụ cho tác vụ tạo đánh giá mới sau khi hoàn thành đơn hàng.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Long orderId;
    private Long userId;
    private Long shopId;
    private Integer rating; // Số sao chấm từ 1 đến 5
    private String comment;
    private String imageUrls; // Chuỗi chứa các link ảnh minh chứng (định dạng JSON hoặc CSV)
}