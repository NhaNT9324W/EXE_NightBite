package com.nightbite.features.shop;

import com.nightbite.domain.Shop;
import com.nightbite.features.shop.dto.ShopRequest;
import com.nightbite.features.shop.dto.ShopResponse;
import com.nightbite.infrastructure.persistence.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lớp dịch vụ xử lý logic nghiệp vụ liên quan đến phân hệ cửa hàng (Shop).
 * Hiện thực hóa các nhiệm vụ cốt lõi: BE-03 và BE-04 của Sprint 1[cite: 1].
 */
@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    /**
     * Xử lý luồng nghiệp vụ tạo mới hoặc cập nhật thông tin cửa hàng (Task BE-03)[cite: 1].
     *
     * @param request Dữ liệu yêu cầu gửi lên từ Client dạng ShopRequest
     * @return Dữ liệu cửa hàng sau khi lưu trữ đã được chuẩn hóa thành ShopResponse
     */
    @Transactional
    public ShopResponse createOrUpdateShop(ShopRequest request) {
        Shop shop;
        // Nếu có ID truyền lên, thực hiện tìm kiếm để cập nhật dữ liệu
        if (request.getId() != null) {
            shop = shopRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng với ID: " + request.getId()));
        } else {
            // Ngược lại, khởi tạo đối tượng Shop mới cho hệ thống
            shop = new Shop();
        }

        shop.setShopName(request.getShopName());
        shop.setOwnerName(request.getOwnerName());
        shop.setPhone(request.getPhone());
        shop.setEmail(request.getEmail());

        // Kiểm tra và gán mật khẩu nếu có thay đổi
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            shop.setPasswordHash(request.getPassword());
        }

        shop.setAddress(request.getAddress());
        shop.setDistrict(request.getDistrict());
        shop.setDescription(request.getDescription());
        shop.setLogoUrl(request.getLogoUrl());
        shop.setBannerUrl(request.getBannerUrl());

        Shop savedShop = shopRepository.save(shop);
        return mapToResponse(savedShop);
    }

    /**
     * Tìm kiếm và trả về thông tin chi tiết của một cửa hàng đối tác theo mã định danh[cite: 1].
     *
     * @param id Khóa chính của thực thể Shop cần tìm
     * @return Đối tượng ShopResponse chứa thông tin dữ liệu sạch
     */
    @Transactional(readOnly = true)
    public ShopResponse getShopById(Long id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng với ID: " + id));
        return mapToResponse(shop);
    }

    /**
     * Truy xuất danh sách toàn bộ các cửa hàng đang hoạt động, có hỗ trợ lọc theo khu vực (Task BE-04)[cite: 1].
     *
     * @param district Tên quận/huyện cần áp dụng bộ lọc (Có thể null)
     * @return Danh sách các cửa hàng được ánh xạ sang định dạng ShopResponse
     */
    @Transactional(readOnly = true)
    public List<ShopResponse> getAllShops(String district) {
        List<Shop> shops;

        // Kiểm tra xem khách hàng có truyền tham số bộ lọc quận huyện hay không[cite: 1]
        if (district != null && !district.trim().isEmpty()) {
            shops = shopRepository.findAll().stream()
                    .filter(s -> s.getIsActive() && district.equalsIgnoreCase(s.getDistrict()))
                    .collect(Collectors.toList());
        } else {
            shops = shopRepository.findAll().stream()
                    .filter(Shop::getIsActive)
                    .collect(Collectors.toList());
        }

        return shops.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Hàm tiện ích nội bộ phục vụ chuyển đổi (Mapping) từ Entity sang DTO đầu ra an toàn.
     */
    private ShopResponse mapToResponse(Shop shop) {
        return ShopResponse.builder()
                .id(shop.getId())
                .shopName(shop.getShopName())
                .ownerName(shop.getOwnerName())
                .phone(shop.getPhone())
                .email(shop.getEmail())
                .address(shop.getAddress())
                .district(shop.getDistrict())
                .description(shop.getDescription())
                .logoUrl(shop.getLogoUrl())
                .bannerUrl(shop.getBannerUrl())
                .ratingAvg(shop.getRatingAvg())
                .reviewCount(shop.getReviewCount())
                .build();
    }
}