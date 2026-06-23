package com.nightbite.features.product;

import com.nightbite.domain.Product;
import com.nightbite.domain.Shop;
import com.nightbite.features.product.dto.ProductRequest;
import com.nightbite.features.product.dto.ProductResponse;
import com.nightbite.infrastructure.persistence.ProductRepository;
import com.nightbite.infrastructure.persistence.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dịch vụ xử lý logic nghiệp vụ liên quan đến sản phẩm Flash Sale
 * Phục vụ các task: BE-05 (tạo), BE-06 (cập nhật/xóa), BE-07 (lấy danh sách)
 * Phụ trách: Ngân (BE)
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    /**
     * BE-05: Tạo sản phẩm Flash Sale mới
     * 
     * @param request Dữ liệu sản phẩm từ Shop
     * @return ProductResponse chứa thông tin sản phẩm đã được lưu
     */
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // Kiểm tra Shop tồn tại
        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại với ID: " + request.getShopId()));

        // Tạo Product mới
        Product product = Product.builder()
                .shop(shop)
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .originalPrice(request.getOriginalPrice())
                .salePrice(request.getSalePrice())
                .quantityAvailable(request.getQuantityAvailable() != null ? request.getQuantityAvailable() : 0)
                .quantitySold(0)
                .saleStartTime(request.getSaleStartTime())
                .saleEndTime(request.getSaleEndTime())
                .saleDate(request.getSaleDate())
                .allergenTags(request.getAllergenTags())
                .category(request.getCategory())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    /**
     * BE-06: Cập nhật sản phẩm Flash Sale
     * 
     * @param id ID của sản phẩm cần cập nhật
     * @param request Dữ liệu cập nhật
     * @return ProductResponse chứa thông tin sản phẩm sau cập nhật
     */
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với ID: " + id));

        // Cập nhật các trường dữ liệu
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        if (request.getOriginalPrice() != null) {
            product.setOriginalPrice(request.getOriginalPrice());
        }
        if (request.getSalePrice() != null) {
            product.setSalePrice(request.getSalePrice());
        }
        if (request.getQuantityAvailable() != null) {
            product.setQuantityAvailable(request.getQuantityAvailable());
        }
        if (request.getSaleStartTime() != null) {
            product.setSaleStartTime(request.getSaleStartTime());
        }
        if (request.getSaleEndTime() != null) {
            product.setSaleEndTime(request.getSaleEndTime());
        }
        if (request.getSaleDate() != null) {
            product.setSaleDate(request.getSaleDate());
        }
        if (request.getAllergenTags() != null) {
            product.setAllergenTags(request.getAllergenTags());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    /**
     * BE-06: Xóa sản phẩm Flash Sale (soft delete - chỉ đánh dấu isActive = false)
     * 
     * @param id ID của sản phẩm cần xóa
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với ID: " + id));
        
        product.setIsActive(false);
        productRepository.save(product);
    }

    /**
     * BE-07: Lấy danh sách sản phẩm Flash Sale đang bán hôm nay theo giờ
     * Lọc những sản phẩm có saleDate = hôm nay và đang trong khoảng thời gian sale
     * 
     * @return Danh sách ProductResponse
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveSaleProducts() {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Lấy tất cả sản phẩm active, sau đó filter theo điều kiện thời gian
        return productRepository.findAll().stream()
                .filter(p -> p.getIsActive())
                .filter(p -> p.getSaleDate() != null && p.getSaleDate().equals(today))
                .filter(p -> p.getSaleStartTime() != null && p.getSaleEndTime() != null)
                .filter(p -> {
                    // Kiểm tra xem thời gian hiện tại có nằm trong khoảng sale không
                    return (currentTime.isAfter(p.getSaleStartTime()) || currentTime.equals(p.getSaleStartTime()))
                            && (currentTime.isBefore(p.getSaleEndTime()) || currentTime.equals(p.getSaleEndTime()));
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * BE-07: Lấy danh sách sản phẩm Flash Sale theo cửa hàng
     * 
     * @param shopId ID của cửa hàng
     * @return Danh sách ProductResponse của shop
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByShop(Long shopId) {
        return productRepository.findByShopIdAndIsActiveTrueOrderByCreatedAtDesc(shopId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * BE-23: Tìm kiếm sản phẩm theo loại (category)
     * 
     * @param category Loại sản phẩm
     * @return Danh sách ProductResponse theo category
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> searchByCategory(String category) {
        return productRepository.findAll().stream()
                .filter(p -> p.getIsActive())
                .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(category))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * BE-23: Lọc sản phẩm theo tag dị ứng
     * 
     * @param allergen Tag dị ứng cần lọc
     * @return Danh sách ProductResponse không chứa allergen
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> filterByAllergen(String allergen) {
        return productRepository.findAll().stream()
                .filter(p -> p.getIsActive())
                .filter(p -> p.getAllergenTags() == null 
                        || !p.getAllergenTags().toLowerCase().contains(allergen.toLowerCase()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin chi tiết sản phẩm
     * 
     * @param id ID của sản phẩm
     * @return ProductResponse
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với ID: " + id));
        return mapToResponse(product);
    }

    /**
     * Chuyển đổi từ Product Entity sang ProductResponse DTO
     */
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .shopId(product.getShop().getId())
                .shopName(product.getShop().getShopName())
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
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}

