package com.nightbite.features.auth;

import com.nightbite.domain.User;
import com.nightbite.infrastructure.persistence.OrderRepository;
import com.nightbite.infrastructure.persistence.UserRepository;
import com.nightbite.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * BE-19 – Trust Score chống boom hàng.
 *
 * Quy tắc:
 *  - Mỗi lần user tự huỷ đơn:  -10 điểm
 *  - Shop xác nhận đơn DONE:   +5 điểm (tối đa 100)
 *  - trust_score <= 0 : user bị hạn chế đặt hàng
 *  - trust_score <= 30: cảnh báo, vẫn đặt được nhưng FE hiển thị warning
 *  - Nếu trong 30 ngày user huỷ >= 3 đơn liên tiếp → giảm thêm 20 điểm
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrustScoreService {

    private static final int CANCEL_PENALTY       = 10;
    private static final int DONE_REWARD          = 5;
    private static final int MAX_SCORE            = 100;
    private static final int BLOCK_THRESHOLD      = 0;
    private static final int WARN_THRESHOLD       = 30;
    private static final int SERIAL_CANCEL_LIMIT  = 3;    // >= 3 lần huỷ / 30 ngày
    private static final int SERIAL_CANCEL_EXTRA  = 20;   // phạt thêm khi liên tục huỷ

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // -------------------------------------------------------------------------
    // Giảm điểm khi user huỷ đơn
    // -------------------------------------------------------------------------

    @Transactional
    public TrustScoreResult penalizeCancel(Long userId) {
        User user = findUser(userId);

        int before = user.getTrustScore();
        int after = Math.max(before - CANCEL_PENALTY, -99);

        // Kiểm tra nếu huỷ liên tục >= SERIAL_CANCEL_LIMIT lần trong 30 ngày
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        long cancelCount = orderRepository.countCancelledOrdersSince(userId, since);

        if (cancelCount >= SERIAL_CANCEL_LIMIT) {
            after = Math.max(after - SERIAL_CANCEL_EXTRA, -99);
            log.warn("User {} bị phạt nặng: {} lần huỷ đơn trong 30 ngày. Score: {} → {}",
                    userId, cancelCount, before, after);
        } else {
            log.info("User {} huỷ đơn. Score: {} → {}", userId, before, after);
        }

        user.setTrustScore(after);
        userRepository.save(user);

        return buildResult(userId, before, after);
    }

    // -------------------------------------------------------------------------
    // Tăng điểm khi đơn hoàn thành (DONE)
    // -------------------------------------------------------------------------

    @Transactional
    public TrustScoreResult rewardDone(Long userId) {
        User user = findUser(userId);

        int before = user.getTrustScore();
        int after = Math.min(before + DONE_REWARD, MAX_SCORE);

        user.setTrustScore(after);
        userRepository.save(user);

        log.info("User {} nhận thưởng trust score. Score: {} → {}", userId, before, after);
        return buildResult(userId, before, after);
    }

    // -------------------------------------------------------------------------
    // Kiểm tra user có đủ điều kiện đặt hàng không
    // -------------------------------------------------------------------------

    public void checkCanOrder(Long userId) {
        User user = findUser(userId);
        if (user.getTrustScore() <= BLOCK_THRESHOLD) {
            throw new com.nightbite.shared.exception.ForbiddenException(
                "Tài khoản của bạn đã bị hạn chế đặt hàng do huỷ đơn quá nhiều. " +
                "Vui lòng liên hệ hỗ trợ để mở lại."
            );
        }
    }

    // -------------------------------------------------------------------------
    // Lấy trạng thái trust score hiện tại
    // -------------------------------------------------------------------------

    public TrustScoreResult getScore(Long userId) {
        User user = findUser(userId);
        return buildResult(userId, user.getTrustScore(), user.getTrustScore());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    private TrustScoreResult buildResult(Long userId, int before, int after) {
        String status;
        if (after <= BLOCK_THRESHOLD) {
            status = "BLOCKED";
        } else if (after <= WARN_THRESHOLD) {
            status = "WARNING";
        } else {
            status = "GOOD";
        }

        return TrustScoreResult.builder()
                .userId(userId)
                .scoreBefore(before)
                .scoreAfter(after)
                .status(status)
                .build();
    }

    // -------------------------------------------------------------------------
    // Result DTO
    // -------------------------------------------------------------------------

    @lombok.Data
    @lombok.Builder
    public static class TrustScoreResult {
        private Long userId;
        private int scoreBefore;
        private int scoreAfter;
        /** GOOD | WARNING | BLOCKED */
        private String status;
    }
}
