package com.nightbite.features.auth;

import com.nightbite.shared.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Auth", description = "Đăng ký / Đăng nhập / Refresh Token")
@RestController
@RequestMapping("/savibite/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // -------------------------------------------------------------------------
    // POST /auth/register/user
    // -------------------------------------------------------------------------
    @Operation(summary = "Đăng ký tài khoản User")
    @PostMapping("/register/user")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(
            @Valid @RequestBody RegisterUserRequest req) {

        AuthResponse response = authService.registerUser(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký thành công", response));
    }

    // -------------------------------------------------------------------------
    // POST /auth/register/shop
    // -------------------------------------------------------------------------
    @Operation(summary = "Đăng ký tài khoản Shop")
    @PostMapping("/register/shop")
    public ResponseEntity<ApiResponse<AuthResponse>> registerShop(
            @Valid @RequestBody RegisterShopRequest req) {

        AuthResponse response = authService.registerShop(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký shop thành công", response));
    }

    // -------------------------------------------------------------------------
    // POST /auth/login
    // -------------------------------------------------------------------------
    @Operation(summary = "Đăng nhập (User / Shop / Admin) – trả về JWT")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest req) {

        AuthResponse response = authService.login(req);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }

    // -------------------------------------------------------------------------
    // POST /auth/refresh
    // -------------------------------------------------------------------------
    @Operation(summary = "Làm mới Access Token bằng Refresh Token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest req) {

        AuthResponse response = authService.refreshToken(req);
        return ResponseEntity.ok(ApiResponse.success("Token đã được làm mới", response));
    }

    // -------------------------------------------------------------------------
    // GET /auth/me
    // -------------------------------------------------------------------------
    @Operation(
        summary = "Lấy thông tin tài khoản hiện tại",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponse>> getMe() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        Long id = (Long) auth.getPrincipal();
        String role = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElseThrow();

        MeResponse me = authService.getMe(id, role);
        return ResponseEntity.ok(ApiResponse.success(me));
    }
}
