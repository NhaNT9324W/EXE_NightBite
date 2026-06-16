package com.nightbite.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightbite.domain.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Shop> findByIsActiveTrueOrderByCreatedAtDesc();

    List<Shop> findByIsActiveTrueAndDistrictContainingIgnoreCaseOrderByCreatedAtDesc(String district);

    java.util.Optional<Shop> findByIdAndIsActiveTrue(Long id);
}

