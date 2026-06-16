package com.nightbite.features.shop;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopReviewDto {

    private Long id;
    private Long orderId;
    private Long userId;
    private String userName;
    private Long shopId;
    private Integer rating;
    private String comment;
    private String imageUrls;
    private Boolean isVisible;
    private LocalDateTime createdAt;
}

