package com.nightbite.shared.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtUtil – Unit Tests")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET =
            "NightBite_Test_SuperSecret_Key_2026_Must256Bits_EXE101FPT";
    private static final long ACCESS_TTL  = 3_600_000L;  // 1 giờ
    private static final long REFRESH_TTL = 86_400_000L; // 1 ngày

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET, ACCESS_TTL, REFRESH_TTL);
    }

    // -------------------------------------------------------------------------
    // generateAccessToken
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("generateAccessToken – tạo token không null và không rỗng")
    void generateAccessToken_shouldReturnNonBlankToken() {
        String token = jwtUtil.generateAccessToken(42L, "ROLE_USER");
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("generateAccessToken – token hợp lệ, extract id đúng")
    void generateAccessToken_extractId_shouldReturnCorrectId() {
        long userId = 99L;
        String token = jwtUtil.generateAccessToken(userId, "ROLE_USER");
        assertThat(jwtUtil.extractId(token)).isEqualTo(userId);
    }

    @Test
    @DisplayName("generateAccessToken – extract role đúng ROLE_USER")
    void generateAccessToken_extractRole_user() {
        String token = jwtUtil.generateAccessToken(1L, "ROLE_USER");
        assertThat(jwtUtil.extractRole(token)).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("generateAccessToken – extract role đúng ROLE_SHOP")
    void generateAccessToken_extractRole_shop() {
        String token = jwtUtil.generateAccessToken(5L, "ROLE_SHOP");
        assertThat(jwtUtil.extractRole(token)).isEqualTo("ROLE_SHOP");
    }

    @Test
    @DisplayName("generateAccessToken – extract role đúng ROLE_ADMIN")
    void generateAccessToken_extractRole_admin() {
        String token = jwtUtil.generateAccessToken(1L, "ROLE_ADMIN");
        assertThat(jwtUtil.extractRole(token)).isEqualTo("ROLE_ADMIN");
    }

    // -------------------------------------------------------------------------
    // validateToken
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("validateToken – token mới tạo phải hợp lệ")
    void validateToken_freshToken_shouldBeValid() {
        String token = jwtUtil.generateAccessToken(10L, "ROLE_USER");
        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("validateToken – token rác phải không hợp lệ")
    void validateToken_garbageToken_shouldBeInvalid() {
        assertThat(jwtUtil.validateToken("not.a.valid.jwt")).isFalse();
    }

    @Test
    @DisplayName("validateToken – token null phải không hợp lệ")
    void validateToken_nullToken_shouldBeInvalid() {
        assertThat(jwtUtil.validateToken(null)).isFalse();
    }

    @Test
    @DisplayName("validateToken – token rỗng phải không hợp lệ")
    void validateToken_emptyToken_shouldBeInvalid() {
        assertThat(jwtUtil.validateToken("")).isFalse();
    }

    @Test
    @DisplayName("validateToken – token ký bằng secret khác phải không hợp lệ")
    void validateToken_differentSecret_shouldBeInvalid() {
        JwtUtil otherJwt = new JwtUtil(
                "AnotherSecret_TotallyDifferent_Key_2026_EXE101ForTest",
                ACCESS_TTL, REFRESH_TTL
        );
        String tokenFromOther = otherJwt.generateAccessToken(1L, "ROLE_USER");
        assertThat(jwtUtil.validateToken(tokenFromOther)).isFalse();
    }

    // -------------------------------------------------------------------------
    // Refresh Token
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("generateRefreshToken – tạo token khác với access token")
    void generateRefreshToken_shouldDifferFromAccessToken() {
        String access  = jwtUtil.generateAccessToken(1L, "ROLE_USER");
        String refresh = jwtUtil.generateRefreshToken(1L, "ROLE_USER");
        assertThat(access).isNotEqualTo(refresh);
    }

    @Test
    @DisplayName("generateRefreshToken – extract id đúng")
    void generateRefreshToken_extractId_correct() {
        String refresh = jwtUtil.generateRefreshToken(77L, "ROLE_SHOP");
        assertThat(jwtUtil.extractId(refresh)).isEqualTo(77L);
    }

    // -------------------------------------------------------------------------
    // isTokenExpired
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("isTokenExpired – token mới tạo chưa hết hạn")
    void isTokenExpired_freshToken_shouldBeFalse() {
        String token = jwtUtil.generateAccessToken(1L, "ROLE_USER");
        assertThat(jwtUtil.isTokenExpired(token)).isFalse();
    }

    @Test
    @DisplayName("isTokenExpired – token hết hạn ngay lập tức (TTL=1ms) phải là true")
    void isTokenExpired_expiredToken_shouldBeTrue() throws InterruptedException {
        JwtUtil shortLived = new JwtUtil(SECRET, 1L, 1L);
        String token = shortLived.generateAccessToken(1L, "ROLE_USER");
        Thread.sleep(10); // đợi token expire
        assertThat(shortLived.isTokenExpired(token)).isTrue();
    }
}
