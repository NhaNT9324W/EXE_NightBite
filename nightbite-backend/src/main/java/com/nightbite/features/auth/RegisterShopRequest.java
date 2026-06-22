package com.nightbite.features.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterShopRequest {

    @NotBlank(message = "Tên cửa hàng không được để trống")
    @Size(max = 200, message = "Tên cửa hàng tối đa 200 ký tự")
    private String shopName;

    @NotBlank(message = "Tên chủ cửa hàng không được để trống")
    @Size(max = 150)
    private String ownerName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[3-9]\\d{8})$", message = "Số điện thoại không hợp lệ (VD: 0901234567)")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6–100 ký tự")
    private String password;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @Size(max = 100)
    private String district;

    private String description;
}
