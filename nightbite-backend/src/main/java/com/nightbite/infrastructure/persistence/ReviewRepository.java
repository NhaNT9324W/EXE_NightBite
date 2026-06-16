package com.nightbite.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightbite.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByShopIdAndIsVisibleTrueOrderByCreatedAtDesc(Long shopId);
}

