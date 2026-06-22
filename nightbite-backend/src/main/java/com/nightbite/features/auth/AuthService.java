package com.nightbite.features.auth;

import com.nightbite.domain.Admin;
import com.nightbite.domain.Shop;
import com.nightbite.domain.User;
import com.nightbite.infrastructure.persistence.AdminRepository;
import com.nightbite.infrastructure.persistence.ShopRepository;
import com.nightbite.infrastructure.persistence.UserRepository;
import com.nightbite.shared.exception.BadRequestException;
import com.nightbite.shared.exception.DuplicateResourceException;
import com.nightbite.shared.exception.ResourceNotFoundException;
import com.nightbite.shared.exception.UnauthorizedException;
import com.nightbite.shared.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.jwt.access-token-expiration:86400000}")
    private long accessTokenExpMs;

    // -------------------------------------------------------------------------
    // Register User
    // -------------------------------------------------------------------------

    @Transactional
    public AuthResponse registerUser(RegisterUserRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("Email", req.getEmail());
        }
        if (req.getPhone() != null && userRepository.existsByPhone(req.getPhone())) {
            throw new DuplicateResourceException("Số điện thoại", req.getPhone());
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .trustScore(100)
                .isActive(true)
                .role(User.UserRole.USER)
                .build();

        user = userRepository.save(user);
        log.info("Đăng ký User mới: id={}, email={}", user.getId(), user.getEmail());

        return buildAuthResponse(user.getId(), user.getFullName(), user.getEmail(), "ROLE_USER");
    }

    // -------------------------------------------------------------------------
    // Register Shop
    // -------------------------------------------------------------------------

    @Transactional
    public AuthResponse registerShop(RegisterShopRequest req) {
        if (shopRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("Email", req.getEmail());
        }
        if (shopRepository.existsByPhone(req.getPhone())) {
            throw new DuplicateResourceException("Số điện thoại", req.getPhone());
        }

        Shop shop = Shop.builder()
                .shopName(req.getShopName())
                .ownerName(req.getOwnerName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .address(req.getAddress())
                .district(req.getDistrict())
                .description(req.getDescription())
                .isActive(true)
                .role(Shop.ShopRole.SHOP)
                .build();

        shop = shopRepository.save(shop);
        log.info("Đăng ký Shop mới: id={}, email={}", shop.getId(), shop.getEmail());

        return buildAuthResponse(shop.getId(), shop.getShopName(), shop.getEmail(), "ROLE_SHOP");
    }

    // -------------------------------------------------------------------------
    // Login (User / Shop / Admin)
    // -------------------------------------------------------------------------

    public AuthResponse login(LoginRequest req) {
        String identifier = req.getIdentifier().trim();

        // Thử User trước
        var userOpt = identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                : userRepository.findByPhone(identifier);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            validatePassword(req.getPassword(), user.getPasswordHash());
            if (!user.getIsActive()) {
                throw new UnauthorizedException("Tài khoản của bạn đã bị khoá. Vui lòng liên hệ hỗ trợ.");
            }
            return buildAuthResponse(user.getId(), user.getFullName(), user.getEmail(), "ROLE_USER");
        }

        // Thử Shop
        var shopOpt = identifier.contains("@")
                ? shopRepository.findByEmail(identifier)
                : shopRepository.findByPhone(identifier);

        if (shopOpt.isPresent()) {
            Shop shop = shopOpt.get();
            validatePassword(req.getPassword(), shop.getPasswordHash());
            if (!shop.getIsActive()) {
                throw new UnauthorizedException("Tài khoản shop đã bị khoá. Vui lòng liên hệ Admin.");
            }
            return buildAuthResponse(shop.getId(), shop.getShopName(), shop.getEmail(), "ROLE_SHOP");
        }

        // Thử Admin (chỉ login bằng username hoặc email)
        var adminOpt = identifier.contains("@")
                ? adminRepository.findByEmail(identifier)
                : adminRepository.findByUsername(identifier);

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            validatePassword(req.getPassword(), admin.getPasswordHash());
            return buildAuthResponse(admin.getId(), admin.getFullName(), admin.getEmail(), "ROLE_ADMIN");
        }

        throw new UnauthorizedException("Email/số điện thoại hoặc mật khẩu không đúng");
    }

    // -------------------------------------------------------------------------
    // Refresh Token
    // -------------------------------------------------------------------------

    public AuthResponse refreshToken(RefreshTokenRequest req) {
        String refreshToken = req.getRefreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BadRequestException("Refresh token không hợp lệ hoặc đã hết hạn");
        }

        Long id = jwtUtil.extractId(refreshToken);
        String role = jwtUtil.extractRole(refreshToken);

        // Verify entity vẫn tồn tại và active
        String name = switch (role) {
            case "ROLE_USER" -> {
                User u = userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User", id));
                if (!u.getIsActive()) throw new UnauthorizedException("Tài khoản đã bị khoá");
                yield u.getFullName();
            }
            case "ROLE_SHOP" -> {
                Shop s = shopRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Shop", id));
                if (!s.getIsActive()) throw new UnauthorizedException("Tài khoản shop đã bị khoá");
                yield s.getShopName();
            }
            case "ROLE_ADMIN" -> {
                Admin a = adminRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Admin", id));
                yield a.getFullName();
            }
            default -> throw new BadRequestException("Role không hợp lệ trong token");
        };

        String email = switch (role) {
            case "ROLE_USER" -> userRepository.findById(id).map(User::getEmail).orElse("");
            case "ROLE_SHOP" -> shopRepository.findById(id).map(Shop::getEmail).orElse("");
            case "ROLE_ADMIN" -> adminRepository.findById(id).map(Admin::getEmail).orElse("");
            default -> "";
        };

        return buildAuthResponse(id, name, email, role);
    }

    // -------------------------------------------------------------------------
    // Get /me
    // -------------------------------------------------------------------------

    public MeResponse getMe(Long id, String role) {
        return switch (role) {
            case "ROLE_USER" -> {
                User u = userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User", id));
                yield MeResponse.builder()
                        .id(u.getId())
                        .fullName(u.getFullName())
                        .email(u.getEmail())
                        .phone(u.getPhone())
                        .avatarUrl(u.getAvatarUrl())
                        .role("ROLE_USER")
                        .trustScore(u.getTrustScore())
                        .isActive(u.getIsActive())
                        .build();
            }
            case "ROLE_SHOP" -> {
                Shop s = shopRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Shop", id));
                yield MeResponse.builder()
                        .id(s.getId())
                        .fullName(s.getOwnerName())
                        .email(s.getEmail())
                        .phone(s.getPhone())
                        .role("ROLE_SHOP")
                        .isActive(s.getIsActive())
                        .shopName(s.getShopName())
                        .district(s.getDistrict())
                        .build();
            }
            case "ROLE_ADMIN" -> {
                Admin a = adminRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Admin", id));
                yield MeResponse.builder()
                        .id(a.getId())
                        .fullName(a.getFullName())
                        .email(a.getEmail())
                        .role("ROLE_ADMIN")
                        .build();
            }
            default -> throw new UnauthorizedException();
        };
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void validatePassword(String raw, String encoded) {
        if (!passwordEncoder.matches(raw, encoded)) {
            throw new UnauthorizedException("Email/số điện thoại hoặc mật khẩu không đúng");
        }
    }

    private AuthResponse buildAuthResponse(Long id, String name, String email, String role) {
        return AuthResponse.builder()
                .id(id)
                .fullName(name)
                .email(email)
                .role(role)
                .accessToken(jwtUtil.generateAccessToken(id, role))
                .refreshToken(jwtUtil.generateRefreshToken(id, role))
                .expiresIn(accessTokenExpMs)
                .build();
    }
}
