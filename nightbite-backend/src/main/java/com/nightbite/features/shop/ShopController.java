package com.nightbite.features.shop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nightbite.shared.utils.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    @Operation(summary = "Create shop", description = "Create a new shop account for NightBite Sprint 1.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Shop created successfully")
    public ResponseEntity<ApiResponse<ShopDto>> createShop(@Valid @RequestBody ShopRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(buildResponse("Shop created successfully", shopService.createShop(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shop", description = "Update an existing shop profile by id.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shop updated successfully")
    public ResponseEntity<ApiResponse<ShopDto>> updateShop(@PathVariable Long id, @Valid @RequestBody ShopRequest request) {
        return ResponseEntity.ok(buildResponse("Shop updated successfully", shopService.updateShop(id, request)));
    }

    @GetMapping
    @Operation(summary = "Get active shops", description = "Get all active shops with optional district filter.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shops retrieved successfully")
    public ResponseEntity<ApiResponse<List<ShopDto>>> getShops(@RequestParam(required = false) String district) {
        return ResponseEntity.ok(buildResponse("Shops retrieved successfully", shopService.getActiveShops(district)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shop detail", description = "Get the active shop detail by id.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shop retrieved successfully")
    public ResponseEntity<ApiResponse<ShopDto>> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(buildResponse("Shop retrieved successfully", shopService.getShopById(id)));
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "Get shop products", description = "Get active flash-sale products for a shop.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public ResponseEntity<ApiResponse<List<ShopProductDto>>> getShopProducts(@PathVariable Long id) {
        return ResponseEntity.ok(buildResponse("Products retrieved successfully", shopService.getShopProducts(id)));
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "Get shop reviews", description = "Get visible reviews for a shop.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    public ResponseEntity<ApiResponse<List<ShopReviewDto>>> getShopReviews(@PathVariable Long id) {
        return ResponseEntity.ok(buildResponse("Reviews retrieved successfully", shopService.getShopReviews(id)));
    }

    private <T> ApiResponse<T> buildResponse(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

