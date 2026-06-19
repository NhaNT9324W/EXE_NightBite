package com.nightbite.features.review;

import com.nightbite.features.review.dto.ReviewRequest;
import com.nightbite.features.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp điều phối xử lý luồng API liên quan đến đánh giá dịch vụ[cite: 1].
 * Định vị Base URL chuẩn phân hệ: /savibite/reviews[cite: 1].
 */
@RestController
@RequestMapping("/savibite/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Endpoint cho phép khách hàng thực hiện gửi đánh giá chất lượng đơn hàng (Task BE-20)[cite: 1].
     * Phân quyền truy cập: USER[cite: 1].
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> postNewReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewResponse result = reviewService.submitReview(reviewRequest);
        return ResponseEntity.ok(buildResponse(true, "Gửi bài đánh giá dịch vụ thành công!", result));
    }

    /**
     * Endpoint công khai lấy toàn bộ danh sách review của một shop cụ thể (Task BE-21)[cite: 1].
     * Phân quyền truy cập: Cả hai (User / Shop / Public)[cite: 1].
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<Map<String, Object>> getAllReviewsOfShop(@PathVariable Long shopId) {
        List<ReviewResponse> result = reviewService.getReviewsByShopId(shopId);
        return ResponseEntity.ok(buildResponse(true, "Tải danh sách bài đánh giá thành công!", result));
    }

    private Map<String, Object> buildResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}