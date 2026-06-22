package com.nightbite.features.auth;

import com.nightbite.domain.User;
import com.nightbite.infrastructure.persistence.OrderRepository;
import com.nightbite.infrastructure.persistence.UserRepository;
import com.nightbite.shared.exception.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrustScoreService – Unit Tests")
class TrustScoreServiceTest {

    @Mock private UserRepository  userRepository;
    @Mock private OrderRepository orderRepository;

    @InjectMocks
    private TrustScoreService trustScoreService;

    private User userWith(int score) {
        return User.builder().id(1L).fullName("Test").email("t@test.com")
                .trustScore(score).isActive(true).build();
    }

    @BeforeEach
    void setUpSave() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    // -------------------------------------------------------------------------
    // penalizeCancel
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("penalizeCancel – giảm 10 điểm khi số lần huỷ < 3")
    void penalizeCancel_normalPenalty() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(80)));
        when(orderRepository.countCancelledOrdersSince(eq(1L), any())).thenReturn(1L);

        var result = trustScoreService.penalizeCancel(1L);

        assertThat(result.getScoreAfter()).isEqualTo(70);
        assertThat(result.getStatus()).isEqualTo("GOOD");
    }

    @Test
    @DisplayName("penalizeCancel – giảm thêm 20 điểm khi huỷ >= 3 lần trong 30 ngày")
    void penalizeCancel_serialPenalty() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(80)));
        when(orderRepository.countCancelledOrdersSince(eq(1L), any())).thenReturn(3L);

        var result = trustScoreService.penalizeCancel(1L);

        // -10 (normal) - 20 (serial) = 50
        assertThat(result.getScoreAfter()).isEqualTo(50);
    }

    @Test
    @DisplayName("penalizeCancel – status WARNING khi score <= 30")
    void penalizeCancel_warningStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(35)));
        when(orderRepository.countCancelledOrdersSince(eq(1L), any())).thenReturn(0L);

        var result = trustScoreService.penalizeCancel(1L);

        assertThat(result.getScoreAfter()).isEqualTo(25);
        assertThat(result.getStatus()).isEqualTo("WARNING");
    }

    @Test
    @DisplayName("penalizeCancel – status BLOCKED khi score <= 0")
    void penalizeCancel_blockedStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(5)));
        when(orderRepository.countCancelledOrdersSince(eq(1L), any())).thenReturn(0L);

        var result = trustScoreService.penalizeCancel(1L);

        assertThat(result.getScoreAfter()).isLessThanOrEqualTo(0);
        assertThat(result.getStatus()).isEqualTo("BLOCKED");
    }

    @Test
    @DisplayName("penalizeCancel – score không xuống dưới -99")
    void penalizeCancel_scoreFloor() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(-90)));
        when(orderRepository.countCancelledOrdersSince(eq(1L), any())).thenReturn(0L);

        var result = trustScoreService.penalizeCancel(1L);

        assertThat(result.getScoreAfter()).isGreaterThanOrEqualTo(-99);
    }

    // -------------------------------------------------------------------------
    // rewardDone
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("rewardDone – tăng 5 điểm khi đơn DONE")
    void rewardDone_increasesScore() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(70)));

        var result = trustScoreService.rewardDone(1L);

        assertThat(result.getScoreAfter()).isEqualTo(75);
    }

    @Test
    @DisplayName("rewardDone – score không vượt quá 100")
    void rewardDone_cappedAt100() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(98)));

        var result = trustScoreService.rewardDone(1L);

        assertThat(result.getScoreAfter()).isEqualTo(100);
    }

    // -------------------------------------------------------------------------
    // checkCanOrder
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("checkCanOrder – không ném exception khi score > 0")
    void checkCanOrder_allowed() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(50)));
        assertThatNoException().isThrownBy(() -> trustScoreService.checkCanOrder(1L));
    }

    @Test
    @DisplayName("checkCanOrder – ném ForbiddenException khi score <= 0")
    void checkCanOrder_blocked_throwsForbidden() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWith(0)));

        assertThatThrownBy(() -> trustScoreService.checkCanOrder(1L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("hạn chế");
    }
}
