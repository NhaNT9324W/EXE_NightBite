package com.nightbite.shared.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT Filter – chạy một lần mỗi request.
 * Lấy token từ header Authorization: Bearer <token>
 * Xác thực và set Authentication vào SecurityContext.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
            try {
                Long id = jwtUtil.extractId(token);
                String role = jwtUtil.extractRole(token); // VD: "ROLE_USER"

                // Dùng id làm principal để các service có thể lấy qua SecurityContext
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                id,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                log.error("Không thể set authentication từ JWT: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

    /** Trích xuất token từ header Authorization: Bearer <token> */
    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    /**
     * Bỏ qua filter cho các request không cần auth
     * (Spring Security permitAll() đã xử lý, nhưng skip giúp hiệu năng tốt hơn)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/savibite/auth/")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/actuator/health");
    }
}
