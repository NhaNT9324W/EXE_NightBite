package com.nightbite.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightbite.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByShopIdAndIsActiveTrueOrderByCreatedAtDesc(Long shopId);
}

