package com.nightbite.features.auth;

import com.nightbite.domain.Admin;
import com.nightbite.domain.Shop;
import com.nightbite.domain.User;
import com.nightbite.infrastructure.persistence.AdminRepository;
import com.nightbite.infrastructure.persistence.ShopRepository;
import com.nightbite.infrastructure.persistence.UserRepository;
import com.nightbite.shared.exception.DuplicateResourceException;
import com.nightbite.shared.exception.UnauthorizedException;
import com.nightbite.shared.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService – Unit Tests")
class AuthServiceTest {

    @Mock private UserRepository  userRepository;
    @Mock private ShopRepository  shopRepository;
    @Mock private AdminRepository adminRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil         jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "accessTokenExpMs", 86_400_000L);
        when(jwtUtil.generateAccessToken(anyLong(), anyString())).thenReturn("mock-access-token");
        when(jwtUtil.generateRefreshToken(anyLong(), anyString())).thenReturn("mock-refresh-token");
    }

    // =========================================================================
    // Register User
    // =========================================================================
    @Nested
    @DisplayName("registerUser")
    class RegisterUser {

        @Test
        @DisplayName("Đăng ký thành công – trả về AuthResponse với ROLE_USER")
        void success() {
            RegisterUserRequest req = new RegisterUserRequest();
            req.setFullName("Nguyễn Văn A");
            req.setEmail("a@test.com");
            req.setPhone("0901234567");
            req.setPassword("password123");

            when(userRepository.existsByEmail("a@test.com")).thenReturn(false);
            when(userRepository.existsByPhone("0901234567")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("hashed");

            User savedUser = User.builder().id(1L).fullName("Nguyễn Văn A")
                    .email("a@test.com").build();
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            AuthResponse response = authService.registerUser(req);

            assertThat(response.getRole()).isEqualTo("ROLE_USER");
            assertThat(response.getAccessToken()).isEqualTo("mock-access-token");
            assertThat(response.getEmail()).isEqualTo("a@test.com");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Email đã tồn tại – ném DuplicateResourceException")
        void duplicateEmail_throwsException() {
            RegisterUserRequest req = new RegisterUserRequest();
            req.setEmail("exists@test.com");
            req.setPassword("pass");
            req.setFullName("Test");

            when(userRepository.existsByEmail("exists@test.com")).thenReturn(true);

            assertThatThrownBy(() -> authService.registerUser(req))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("exists@test.com");
        }

        @Test
        @DisplayName("Số điện thoại đã tồn tại – ném DuplicateResourceException")
        void duplicatePhone_throwsException() {
            RegisterUserRequest req = new RegisterUserRequest();
            req.setEmail("new@test.com");
            req.setPhone("0901111111");
            req.setPassword("pass");
            req.setFullName("Test");

            when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
            when(userRepository.existsByPhone("0901111111")).thenReturn(true);

            assertThatThrownBy(() -> authService.registerUser(req))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("0901111111");
        }
    }

    // =========================================================================
    // Register Shop
    // =========================================================================
    @Nested
    @DisplayName("registerShop")
    class RegisterShop {

        @Test
        @DisplayName("Đăng ký shop thành công – trả về ROLE_SHOP")
        void success() {
            RegisterShopRequest req = new RegisterShopRequest();
            req.setShopName("Tiệm Bánh ABC");
            req.setOwnerName("Chủ ABC");
            req.setEmail("shop@test.com");
            req.setPhone("0901234568");
            req.setPassword("shoppass");
            req.setAddress("123 Đường ABC, Cần Thơ");

            when(shopRepository.existsByEmail("shop@test.com")).thenReturn(false);
            when(shopRepository.existsByPhone("0901234568")).thenReturn(false);
            when(passwordEncoder.encode("shoppass")).thenReturn("hashed");

            Shop saved = Shop.builder().id(2L).shopName("Tiệm Bánh ABC")
                    .email("shop@test.com").build();
            when(shopRepository.save(any(Shop.class))).thenReturn(saved);

            AuthResponse response = authService.registerShop(req);

            assertThat(response.getRole()).isEqualTo("ROLE_SHOP");
            assertThat(response.getAccessToken()).isEqualTo("mock-access-token");
        }
    }

    // =========================================================================
    // Login
    // =========================================================================
    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("User đăng nhập bằng email thành công")
        void user_loginByEmail_success() {
            User user = User.builder().id(1L).fullName("A").email("a@test.com")
                    .passwordHash("hashed").isActive(true).build();

            when(userRepository.findByEmail("a@test.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);

            LoginRequest req = new LoginRequest();
            req.setIdentifier("a@test.com");
            req.setPassword("pass");

            AuthResponse response = authService.login(req);
            assertThat(response.getRole()).isEqualTo("ROLE_USER");
            assertThat(response.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Mật khẩu sai – ném UnauthorizedException")
        void wrongPassword_throwsException() {
            User user = User.builder().id(1L).email("a@test.com")
                    .passwordHash("hashed").isActive(true).build();

            when(userRepository.findByEmail("a@test.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

            LoginRequest req = new LoginRequest();
            req.setIdentifier("a@test.com");
            req.setPassword("wrong");

            assertThatThrownBy(() -> authService.login(req))
                    .isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("User bị khoá – ném UnauthorizedException")
        void lockedUser_throwsException() {
            User user = User.builder().id(1L).email("a@test.com")
                    .passwordHash("hashed").isActive(false).build();

            when(userRepository.findByEmail("a@test.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);

            LoginRequest req = new LoginRequest();
            req.setIdentifier("a@test.com");
            req.setPassword("pass");

            assertThatThrownBy(() -> authService.login(req))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("khoá");
        }

        @Test
        @DisplayName("Shop đăng nhập thành công")
        void shop_login_success() {
            Shop shop = Shop.builder().id(5L).shopName("Quán ABC").email("shop@test.com")
                    .passwordHash("hashed").isActive(true).build();

            when(userRepository.findByEmail("shop@test.com")).thenReturn(Optional.empty());
            when(shopRepository.findByEmail("shop@test.com")).thenReturn(Optional.of(shop));
            when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);

            LoginRequest req = new LoginRequest();
            req.setIdentifier("shop@test.com");
            req.setPassword("pass");

            AuthResponse response = authService.login(req);
            assertThat(response.getRole()).isEqualTo("ROLE_SHOP");
            assertThat(response.getId()).isEqualTo(5L);
        }

        @Test
        @DisplayName("Admin đăng nhập bằng username thành công")
        void admin_loginByUsername_success() {
            Admin admin = Admin.builder().id(1L).username("admin").fullName("Admin")
                    .email("admin@nightbite.vn").passwordHash("hashed").build();

            when(userRepository.findByPhone("admin")).thenReturn(Optional.empty());
            when(shopRepository.findByPhone("admin")).thenReturn(Optional.empty());
            when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
            when(passwordEncoder.matches("adminpass", "hashed")).thenReturn(true);

            LoginRequest req = new LoginRequest();
            req.setIdentifier("admin");
            req.setPassword("adminpass");

            AuthResponse response = authService.login(req);
            assertThat(response.getRole()).isEqualTo("ROLE_ADMIN");
        }

        @Test
        @DisplayName("Không tìm thấy account – ném UnauthorizedException")
        void notFound_throwsException() {
            when(userRepository.findByEmail("nobody@test.com")).thenReturn(Optional.empty());
            when(shopRepository.findByEmail("nobody@test.com")).thenReturn(Optional.empty());
            when(adminRepository.findByEmail("nobody@test.com")).thenReturn(Optional.empty());

            LoginRequest req = new LoginRequest();
            req.setIdentifier("nobody@test.com");
            req.setPassword("pass");

            assertThatThrownBy(() -> authService.login(req))
                    .isInstanceOf(UnauthorizedException.class);
        }
    }

    // =========================================================================
    // Refresh Token
    // =========================================================================
    @Nested
    @DisplayName("refreshToken")
    class RefreshTokenTests {

        @Test
        @DisplayName("Refresh token không hợp lệ – ném BadRequestException")
        void invalidRefreshToken_throwsException() {
            when(jwtUtil.validateToken("bad-token")).thenReturn(false);

            RefreshTokenRequest req = new RefreshTokenRequest();
            req.setRefreshToken("bad-token");

            assertThatThrownBy(() -> authService.refreshToken(req))
                    .isInstanceOf(com.nightbite.shared.exception.BadRequestException.class);
        }

        @Test
        @DisplayName("Refresh token hợp lệ cho User – trả về AuthResponse mới")
        void validUserRefreshToken_returnsNewTokens() {
            User user = User.builder().id(1L).fullName("A").email("a@test.com")
                    .isActive(true).build();

            when(jwtUtil.validateToken("valid-refresh")).thenReturn(true);
            when(jwtUtil.extractId("valid-refresh")).thenReturn(1L);
            when(jwtUtil.extractRole("valid-refresh")).thenReturn("ROLE_USER");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            RefreshTokenRequest req = new RefreshTokenRequest();
            req.setRefreshToken("valid-refresh");

            AuthResponse response = authService.refreshToken(req);
            assertThat(response.getRole()).isEqualTo("ROLE_USER");
            assertThat(response.getAccessToken()).isEqualTo("mock-access-token");
        }
    }
}
