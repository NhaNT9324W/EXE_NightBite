package com.nightbite.infrastructure.persistence;

import com.nightbite.domain.NightBiteBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Giao diện tương tác cơ sở dữ liệu (Repository) cho thực thể NightBiteBox (Hộp bí ẩn).
 * Phục vụ truy vấn thông tin tồn kho và lưu trữ hộp dữ liệu.
 * Phục vụ các task: BE-14 (tạo), BE-15 (đặt)
 */
@Repository
public interface NightBiteBoxRepository extends JpaRepository<NightBiteBox, Long> {
    
    // Lấy danh sách NightBite Box của một shop (BE-14, BE-15)
    List<NightBiteBox> findByShopIdAndIsActiveTrueOrderByCreatedAtDesc(Long shopId);
    
    // Lấy danh sách NightBite Box theo ngày sale (BE-15)
    List<NightBiteBox> findBySaleDateAndIsActiveTrueOrderByCreatedAtDesc(LocalDate saleDate);
}