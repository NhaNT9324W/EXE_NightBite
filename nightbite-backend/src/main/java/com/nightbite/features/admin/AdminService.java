package com.nightbite.features.admin;

import com.nightbite.domain.Shop;
import com.nightbite.domain.User;
import com.nightbite.infrastructure.persistence.ShopRepository;
import com.nightbite.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lớp dịch vụ quản trị hệ thống xử lý các quyền hạn đặc biệt của Admin
 * Thực hiện chức năng: BE-22 (Khóa hoặc mở khóa tài khoản vi phạm)
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    /**
     * Thay đổi trạng thái hoạt động (Khóa / Mở khóa) của người dùng cuối (Task BE-22)
     */
    @Transactional
    public void updateUserLockState(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người dùng yêu cầu"));
        user.setIsActive(active);
        userRepository.save(user);
    }

    /**
     * Đình chỉ hoặc mở lại quyền hoạt động kinh doanh kinh tế cho Shop đối tác (Task BE-22)
     */
    @Transactional
    public void updateShopLockState(Long shopId, boolean active) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin shop yêu cầu"));
        shop.setIsActive(active);
        shopRepository.save(shop);
    }
}