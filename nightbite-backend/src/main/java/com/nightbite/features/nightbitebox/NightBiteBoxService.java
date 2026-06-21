package com.nightbite.features.nightbitebox;

import com.nightbite.domain.NightBiteBox;
import com.nightbite.domain.Shop;
import com.nightbite.features.nightbitebox.dto.NightBiteBoxRequest;
import com.nightbite.features.nightbitebox.dto.NightBiteBoxResponse;
import com.nightbite.infrastructure.persistence.NightBiteBoxRepository;
import com.nightbite.infrastructure.persistence.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dịch vụ xử lý logic nghiệp vụ liên quan đến NightBite Box
 * Phục vụ các task: BE-14 (tạo), BE-15 (đặt)
 * Phụ trách: Ngân (BE)
 */
@Service
@RequiredArgsConstructor
public class NightBiteBoxService {

    private final NightBiteBoxRepository nightBiteBoxRepository;
    private final ShopRepository shopRepository;

    /**
     * BE-14: Tạo NightBite Box mới
     * 
     * @param request Dữ liệu hộp từ Shop
     * @return NightBiteBoxResponse chứa thông tin hộp đã được lưu
     */
    @Transactional
    public NightBiteBoxResponse createNightBiteBox(NightBiteBoxRequest request) {
        // Kiểm tra Shop tồn tại
        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại với ID: " + request.getShopId()));

        // Tạo NightBiteBox mới
        NightBiteBox box = NightBiteBox.builder()
                .shop(shop)
                .boxName(request.getBoxName())
                .boxType(request.getBoxType())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .price(request.getPrice())
                .quantityAvailable(request.getQuantityAvailable() != null ? request.getQuantityAvailable() : 0)
                .allergenWarning(request.getAllergenWarning())
                .saleDate(request.getSaleDate())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        NightBiteBox savedBox = nightBiteBoxRepository.save(box);
        return mapToResponse(savedBox);
    }

    /**
     * BE-14: Cập nhật NightBite Box
     * 
     * @param id ID của hộp cần cập nhật
     * @param request Dữ liệu cập nhật
     * @return NightBiteBoxResponse chứa thông tin hộp sau cập nhật
     */
    @Transactional
    public NightBiteBoxResponse updateNightBiteBox(Long id, NightBiteBoxRequest request) {
        NightBiteBox box = nightBiteBoxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NightBiteBox không tồn tại với ID: " + id));

        if (request.getBoxName() != null) {
            box.setBoxName(request.getBoxName());
        }
        if (request.getBoxType() != null) {
            box.setBoxType(request.getBoxType());
        }
        if (request.getDescription() != null) {
            box.setDescription(request.getDescription());
        }
        if (request.getImageUrl() != null) {
            box.setImageUrl(request.getImageUrl());
        }
        if (request.getPrice() != null) {
            box.setPrice(request.getPrice());
        }
        if (request.getQuantityAvailable() != null) {
            box.setQuantityAvailable(request.getQuantityAvailable());
        }
        if (request.getAllergenWarning() != null) {
            box.setAllergenWarning(request.getAllergenWarning());
        }
        if (request.getSaleDate() != null) {
            box.setSaleDate(request.getSaleDate());
        }
        if (request.getIsActive() != null) {
            box.setIsActive(request.getIsActive());
        }

        NightBiteBox updatedBox = nightBiteBoxRepository.save(box);
        return mapToResponse(updatedBox);
    }

    /**
     * BE-14: Xóa NightBite Box (soft delete)
     * 
     * @param id ID của hộp cần xóa
     */
    @Transactional
    public void deleteNightBiteBox(Long id) {
        NightBiteBox box = nightBiteBoxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NightBiteBox không tồn tại với ID: " + id));
        
        box.setIsActive(false);
        nightBiteBoxRepository.save(box);
    }

    /**
     * BE-15: Lấy chi tiết NightBite Box
     * 
     * @param id ID của hộp
     * @return NightBiteBoxResponse
     */
    @Transactional(readOnly = true)
    public NightBiteBoxResponse getNightBiteBoxById(Long id) {
        NightBiteBox box = nightBiteBoxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NightBiteBox không tồn tại với ID: " + id));
        return mapToResponse(box);
    }

    /**
     * Lấy danh sách NightBite Box của một shop
     * 
     * @param shopId ID của shop
     * @return Danh sách NightBiteBoxResponse
     */
    @Transactional(readOnly = true)
    public List<NightBiteBoxResponse> getNightBiteBoxesByShop(Long shopId) {
        return nightBiteBoxRepository.findAll().stream()
                .filter(b -> b.getShop().getId().equals(shopId))
                .filter(b -> b.getIsActive())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách NightBite Box đang bán hôm nay
     * 
     * @return Danh sách NightBiteBoxResponse
     */
    @Transactional(readOnly = true)
    public List<NightBiteBoxResponse> getActiveSaleBoxes() {
        LocalDate today = LocalDate.now();
        return nightBiteBoxRepository.findAll().stream()
                .filter(b -> b.getIsActive())
                .filter(b -> b.getSaleDate() != null && b.getSaleDate().equals(today))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách NightBite Box theo BoxType
     * 
     * @param boxType Loại hộp
     * @return Danh sách NightBiteBoxResponse
     */
    @Transactional(readOnly = true)
    public List<NightBiteBoxResponse> getBoxesByType(String boxType) {
        return nightBiteBoxRepository.findAll().stream()
                .filter(b -> b.getIsActive())
                .filter(b -> b.getBoxType().name().equalsIgnoreCase(boxType))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi từ NightBiteBox Entity sang NightBiteBoxResponse DTO
     */
    private NightBiteBoxResponse mapToResponse(NightBiteBox box) {
        return NightBiteBoxResponse.builder()
                .id(box.getId())
                .shopId(box.getShop().getId())
                .shopName(box.getShop().getShopName())
                .boxName(box.getBoxName())
                .boxType(box.getBoxType())
                .description(box.getDescription())
                .imageUrl(box.getImageUrl())
                .price(box.getPrice())
                .quantityAvailable(box.getQuantityAvailable())
                .allergenWarning(box.getAllergenWarning())
                .saleDate(box.getSaleDate())
                .isActive(box.getIsActive())
                .createdAt(box.getCreatedAt())
                .build();
    }
}
