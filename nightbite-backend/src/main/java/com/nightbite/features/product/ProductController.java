package com.nightbite.features.product;

import com.nightbite.features.product.dto.ProductRequest;
import com.nightbite.features.product.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller xử lý các endpoint REST API cho sản phẩm Flash Sale
 * Base URL: /savibite/products
 * 
 * Phục vụ các task:
 * - BE-05: Tạo sản phẩm Flash Sale
 * - BE-06: Cập nhật/Xóa sản phẩm Flash Sale
 * - BE-07: Lấy danh sách sản phẩm đang sale
 * - BE-23: Tìm kiếm/lọc sản phẩm
 * 
 * Phụ trách: Ngân (BE)
 */
@RestController
@RequestMapping("/savibite/products")
@RequiredArgsConstructor
@Tag(name = "Product Flash Sale", description = "API quản lý sản phẩm Flash Sale")
public class ProductController {

    private final ProductService productService;

    /**
     * BE-05: Tạo sản phẩm Flash Sale mới
     * Quyền hạn: Shop
     */
    @PostMapping
    @Operation(summary = "Tạo sản phẩm Flash Sale mới", 
               description = "Cho phép cửa hàng đăng sản phẩm Flash Sale (BE-05)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Shop không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody ProductRequest request) {
        ProductResponse result = productService.createProduct(request);
        return ResponseEntity.ok(buildResponse(true, "Tạo sản phẩm Flash Sale thành công!", result));
    }

    /**
     * BE-06: Cập nhật sản phẩm Flash Sale
     * Quyền hạn: Shop
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật sản phẩm Flash Sale",
               description = "Cửa hàng cập nhật thông tin sản phẩm Flash Sale (BE-06)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> updateProduct(
            @Parameter(description = "ID sản phẩm") @PathVariable Long id,
            @RequestBody ProductRequest request) {
        ProductResponse result = productService.updateProduct(id, request);
        return ResponseEntity.ok(buildResponse(true, "Cập nhật sản phẩm Flash Sale thành công!", result));
    }

    /**
     * BE-06: Xóa sản phẩm Flash Sale (soft delete)
     * Quyền hạn: Shop
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa sản phẩm Flash Sale",
               description = "Cửa hàng xóa sản phẩm Flash Sale (BE-06)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> deleteProduct(
            @Parameter(description = "ID sản phẩm") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(buildResponse(true, "Xóa sản phẩm Flash Sale thành công!", null));
    }

    /**
     * BE-07: Lấy danh sách sản phẩm Flash Sale đang bán hôm nay theo giờ
     * Quyền hạn: User/Public
     */
    @GetMapping("/sale/active")
    @Operation(summary = "Lấy danh sách sản phẩm Flash Sale đang bán",
               description = "Lấy các sản phẩm Flash Sale đang trong khoảng thời gian sale hôm nay (BE-07)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy dữ liệu thành công")
    })
    public ResponseEntity<Map<String, Object>> getActiveSaleProducts() {
        List<ProductResponse> result = productService.getActiveSaleProducts();
        return ResponseEntity.ok(buildResponse(true, "Lấy danh sách sản phẩm đang sale thành công!", result));
    }

    /**
     * BE-07: Lấy danh sách sản phẩm Flash Sale của một shop cụ thể
     * Quyền hạn: User/Public
     */
    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Lấy sản phẩm Flash Sale của shop",
               description = "Lấy danh sách toàn bộ sản phẩm Flash Sale của một cửa hàng (BE-07)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy dữ liệu thành công"),
            @ApiResponse(responseCode = "404", description = "Shop không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> getProductsByShop(
            @Parameter(description = "ID cửa hàng") @PathVariable Long shopId) {
        List<ProductResponse> result = productService.getProductsByShop(shopId);
        return ResponseEntity.ok(buildResponse(true, "Lấy danh sách sản phẩm của shop thành công!", result));
    }

    /**
     * BE-07: Lấy chi tiết sản phẩm
     * Quyền hạn: User/Public
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết sản phẩm",
               description = "Lấy thông tin chi tiết của một sản phẩm Flash Sale (BE-07)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy dữ liệu thành công"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tìm thấy")
    })
    public ResponseEntity<Map<String, Object>> getProductById(
            @Parameter(description = "ID sản phẩm") @PathVariable Long id) {
        ProductResponse result = productService.getProductById(id);
        return ResponseEntity.ok(buildResponse(true, "Lấy chi tiết sản phẩm thành công!", result));
    }

    /**
     * BE-23: Tìm kiếm sản phẩm theo category (loại)
     * Quyền hạn: User/Public
     */
    @GetMapping("/search/category")
    @Operation(summary = "Tìm kiếm sản phẩm theo loại",
               description = "Tìm kiếm các sản phẩm theo category/loại (BE-23)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm kiếm thành công")
    })
    public ResponseEntity<Map<String, Object>> searchByCategory(
            @Parameter(description = "Loại sản phẩm") @RequestParam String category) {
        List<ProductResponse> result = productService.searchByCategory(category);
        return ResponseEntity.ok(buildResponse(true, "Tìm kiếm sản phẩm theo loại thành công!", result));
    }

    /**
     * BE-23: Lọc sản phẩm theo tag dị ứng (loại trừ những sản phẩm chứa allergen)
     * Quyền hạn: User/Public
     */
    @GetMapping("/filter/allergen")
    @Operation(summary = "Lọc sản phẩm theo dị ứng",
               description = "Lọc các sản phẩm loại trừ những chứa allergen/dị ứng cụ thể (BE-23)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lọc thành công")
    })
    public ResponseEntity<Map<String, Object>> filterByAllergen(
            @Parameter(description = "Tag dị ứng cần loại trừ") @RequestParam String allergen) {
        List<ProductResponse> result = productService.filterByAllergen(allergen);
        return ResponseEntity.ok(buildResponse(true, "Lọc sản phẩm theo dị ứng thành công!", result));
    }

    /**
     * Helper method để build response chung
     */
    private Map<String, Object> buildResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return response;
    }
}

