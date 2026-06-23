package com.nightbite.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nightbite.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByShopIdAndIsActiveTrueOrderByCreatedAtDesc(Long shopId);

    // Tìm sản phẩm theo category (BE-23)
    List<Product> findByCategoryAndIsActiveTrue(String category);

    // Tìm sản phẩm theo ngày sale (BE-07)
    List<Product> findBySaleDateAndIsActiveTrueOrderByCreatedAtDesc(LocalDate saleDate);

    // Tìm sản phẩm của shop theo ngày sale (BE-07)
    @Query("SELECT p FROM Product p WHERE p.shop.id = :shopId AND p.saleDate = :saleDate AND p.isActive = true ORDER BY p.createdAt DESC")
    List<Product> findByShopIdAndSaleDate(@Param("shopId") Long shopId, @Param("saleDate") LocalDate saleDate);
}

