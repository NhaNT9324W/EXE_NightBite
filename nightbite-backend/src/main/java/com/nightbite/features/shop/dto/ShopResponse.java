package com.nightbite.features.shop.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Đối tượng dữ liệu phản hồi (Response) gửi về cho Frontend hiển thị thông tin.
 * Đã lược bỏ hoàn toàn các trường nhạy cảm như Password để đảm bảo an toàn thông tin.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse {
    private Long id;
    private String shopName;
    private String ownerName;
    private String phone;
    private String email;
    private String address;
    private String district;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private BigDecimal ratingAvg;
    private Integer reviewCount;
}