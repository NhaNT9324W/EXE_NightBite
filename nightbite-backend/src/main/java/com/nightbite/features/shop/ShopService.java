package com.nightbite.features.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nightbite.domain.Product;
import com.nightbite.domain.Review;
import com.nightbite.domain.Shop;
import com.nightbite.infrastructure.persistence.ProductRepository;
import com.nightbite.infrastructure.persistence.ReviewRepository;
import com.nightbite.infrastructure.persistence.ShopRepository;
import com.nightbite.shared.exception.BadRequestException;
import com.nightbite.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopService {

    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ShopDto createShop(ShopRequest request) {
        validateUniqueFields(request, null);
        Shop shop = toEntity(request);
        return toDto(shopRepository.save(shop));
    }

    public ShopDto updateShop(Long id, ShopRequest request) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));

        validateUniqueFields(request, id);
        applyRequest(shop, request);
        return toDto(shopRepository.save(shop));
    }

    @Transactional(readOnly = true)
    public List<ShopDto> getActiveShops(String district) {
        List<Shop> shops;
        if (StringUtils.hasText(district)) {
            shops = shopRepository.findByIsActiveTrueAndDistrictContainingIgnoreCaseOrderByCreatedAtDesc(district.trim());
        } else {
            shops = shopRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        }
        return shops.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ShopDto getShopById(Long id) {
        Shop shop = shopRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
        return toDto(shop);
    }

    @Transactional(readOnly = true)
    public List<ShopProductDto> getShopProducts(Long shopId) {
        Shop shop = shopRepository.findByIdAndIsActiveTrue(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));

        return productRepository.findByShopIdAndIsActiveTrueOrderByCreatedAtDesc(shop.getId()).stream()
                .filter(this::isActiveSaleProduct)
                .map(this::toProductDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShopReviewDto> getShopReviews(Long shopId) {
        Shop shop = shopRepository.findByIdAndIsActiveTrue(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));

        return reviewRepository.findByShopIdAndIsVisibleTrueOrderByCreatedAtDesc(shop.getId()).stream()
                .map(this::toReviewDto)
                .toList();
    }

    private Shop toEntity(ShopRequest request) {
        Shop shop = new Shop();
        applyRequest(shop, request);
        return shop;
    }

    private void applyRequest(Shop shop, ShopRequest request) {
        shop.setShopName(request.getShopName().trim());
        shop.setOwnerName(request.getOwnerName().trim());
        shop.setPhone(request.getPhone().trim());
        shop.setEmail(normalizeOptionalValue(request.getEmail()));
        shop.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        shop.setAddress(request.getAddress().trim());
        shop.setDistrict(normalizeOptionalValue(request.getDistrict()));
        shop.setDescription(normalizeOptionalValue(request.getDescription()));
        shop.setLogoUrl(normalizeOptionalValue(request.getLogoUrl()));
        shop.setBannerUrl(normalizeOptionalValue(request.getBannerUrl()));
    }

    private void validateUniqueFields(ShopRequest request, Long shopId) {
        String phone = normalizeRequiredValue(request.getPhone());
        String email = normalizeOptionalValue(request.getEmail());

        boolean phoneExists = shopId == null
                ? shopRepository.existsByPhone(phone)
                : shopRepository.existsByPhoneAndIdNot(phone, shopId);
        if (phoneExists) {
            throw new BadRequestException("Phone already exists: " + phone);
        }

        if (StringUtils.hasText(email)) {
            boolean emailExists = shopId == null
                    ? shopRepository.existsByEmail(email)
                    : shopRepository.existsByEmailAndIdNot(email, shopId);
            if (emailExists) {
                throw new BadRequestException("Email already exists: " + email);
            }
        }
    }

    private String normalizeRequiredValue(String value) {
        if (!StringUtils.hasText(value)) {
            throw new BadRequestException("Required field is empty");
        }
        return value.trim();
    }

    private String normalizeOptionalValue(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private boolean isActiveSaleProduct(Product product) {
        if (!Boolean.TRUE.equals(product.getIsActive())) {
            return false;
        }

        LocalDate saleDate = product.getSaleDate();
        if (saleDate != null && !saleDate.isEqual(LocalDate.now())) {
            return false;
        }

        LocalTime now = LocalTime.now();
        if (product.getSaleStartTime() != null && now.isBefore(product.getSaleStartTime())) {
            return false;
        }
        return product.getSaleEndTime() == null || !now.isAfter(product.getSaleEndTime());
    }

    private ShopDto toDto(Shop shop) {
        return ShopDto.builder()
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
                .isActive(shop.getIsActive())
                .ratingAvg(Optional.ofNullable(shop.getRatingAvg()).orElse(BigDecimal.ZERO))
                .reviewCount(Optional.ofNullable(shop.getReviewCount()).orElse(0))
                .createdAt(shop.getCreatedAt())
                .updatedAt(shop.getUpdatedAt())
                .build();
    }

    private ShopProductDto toProductDto(Product product) {
        Shop shop = product.getShop();
        return ShopProductDto.builder()
                .id(product.getId())
                .shopId(shop != null ? shop.getId() : null)
                .shopName(shop != null ? shop.getShopName() : null)
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .originalPrice(product.getOriginalPrice())
                .salePrice(product.getSalePrice())
                .quantityAvailable(product.getQuantityAvailable())
                .quantitySold(product.getQuantitySold())
                .saleStartTime(product.getSaleStartTime())
                .saleEndTime(product.getSaleEndTime())
                .saleDate(product.getSaleDate())
                .allergenTags(product.getAllergenTags())
                .category(product.getCategory())
                .isActive(product.getIsActive())
                .build();
    }

    private ShopReviewDto toReviewDto(Review review) {
        return ShopReviewDto.builder()
                .id(review.getId())
                .orderId(review.getOrder() != null ? review.getOrder().getId() : null)
                .userId(review.getUser() != null ? review.getUser().getId() : null)
                .userName(review.getUser() != null ? review.getUser().getFullName() : null)
                .shopId(review.getShop() != null ? review.getShop().getId() : null)
                .rating(review.getRating())
                .comment(review.getComment())
                .imageUrls(review.getImageUrls())
                .isVisible(review.getIsVisible())
                .createdAt(review.getCreatedAt())
                .build();
    }
}

