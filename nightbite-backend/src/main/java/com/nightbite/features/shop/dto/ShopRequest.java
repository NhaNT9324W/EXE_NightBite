package com.nightbite.features.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Đối tượng chứa dữ liệu yêu cầu (Request) truyền từ Frontend lên Server
 * phục vụ cho việc đăng ký, tạo mới hoặc cập nhật thông tin cửa hàng (Shop).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopRequest {

    /**
     * Mã định danh duy nhất của cửa hàng (Chỉ truyền lên khi thực hiện cập nhật)
     */
    private Long id;

    private String shopName;
    private String ownerName;
    private String phone;
    private String email;

    /**
     * Mật khẩu dạng thô (Sẽ được xử lý mã hóa bảo mật ở Sprint tiếp theo)
     */
    private String password;

    private String address;
    private String district;
    private String description;
    private String logoUrl;
    private String bannerUrl;
}