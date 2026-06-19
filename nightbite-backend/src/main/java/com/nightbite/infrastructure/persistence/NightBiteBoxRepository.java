package com.nightbite.infrastructure.persistence;

import com.nightbite.domain.NightBiteBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Giao diện tương tác cơ sở dữ liệu (Repository) cho thực thể NightBiteBox (Hộp bí ẩn).
 * Phục vụ truy vấn thông tin tồn kho và lưu trữ hộp dữ liệu.
 */
@Repository
public interface NightBiteBoxRepository extends JpaRepository<NightBiteBox, Long> {
    // Hiện tại chỉ cần kế thừa JpaRepository để lấy các hàm basic như findById, save.
}