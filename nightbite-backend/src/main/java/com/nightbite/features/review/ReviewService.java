package com.nightbite.features.review;

import com.nightbite.domain.*;
import com.nightbite.features.review.dto.ReviewRequest;
import com.nightbite.features.review.dto.ReviewResponse;
import com.nightbite.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lớp dịch vụ xử lý logic nghiệp vụ liên quan đến Đánh giá & Xếp hạng (Review & Rating).
 * Thực hiện các task: BE-20 (Gửi đánh giá) và BE-21 (Lấy danh sách review của quán).
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    /**
     * Xử lý lưu bài đánh giá mới của User cho đơn hàng đã hoàn tất (Task BE-20).
     * Đồng thời tự động cập nhật lại điểm số rating_avg và số lượng review_count của Shop[cite: 1].
     */
    @Transactional
    public ReviewResponse submitReview(ReviewRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng cần đánh giá"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản khách hàng"));
        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin cửa hàng"));

        // Kiểm tra điều kiện nghiệp vụ: Đơn hàng phải hoàn thành (DONE) mới được phép đánh giá[cite: 1]
        if (!"DONE".equalsIgnoreCase(order.getStatus().name())) {
            throw new RuntimeException("Bạn chỉ có thể đánh giá những đơn hàng đã được giao nhận thành công!");
        }

        // Tạo thực thể Review mới
        Review review = Review.builder()
                .order(order)
                .user(user)
                .shop(shop)
                .rating(request.getRating())
                .comment(request.getComment())
                .imageUrls(request.getImageUrls())
                .isVisible(true)
                .build();

        Review savedReview = reviewRepository.save(review);

        // Kích hoạt hàm cập nhật lại các chỉ số đánh giá tổng kết của Shop đối tác[cite: 1]
        updateShopRatingMetrics(shop);

        return mapToResponse(savedReview);
    }

    /**
     * Lấy danh sách toàn bộ bài đánh giá đang hiển thị công khai của một Shop theo ID (Task BE-21)
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByShopId(Long shopId) {
        return reviewRepository.findAll().stream()
                .filter(r -> r.getShop().getId().equals(shopId) && r.getIsVisible())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Hàm nội bộ tự động tính toán lại điểm số đánh giá trung bình (rating_avg) và tổng lượt đánh giá (review_count)
     */
    private void updateShopRatingMetrics(Shop shop) {
        List<Review> activeReviews = reviewRepository.findAll().stream()
                .filter(r -> r.getShop().getId().equals(shop.getId()) && r.getIsVisible())
                .collect(Collectors.toList());

        int totalCount = activeReviews.size();
        double sumStars = activeReviews.stream().mapToDouble(Review::getRating).sum();
        double averageResult = totalCount > 0 ? sumStars / totalCount : 0.0;

        shop.setReviewCount(totalCount);
        shop.setRatingAvg(BigDecimal.valueOf(averageResult).setScale(2, RoundingMode.HALF_UP));
        shopRepository.save(shop);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .orderId(review.getOrder().getId())
                .reviewerName(review.getUser().getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .imageUrls(review.getImageUrls())
                .createdAt(review.getCreatedAt())
                .build();
    }
}