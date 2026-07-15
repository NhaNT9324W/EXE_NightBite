package com.nightbite.shared.config;

import com.nightbite.shared.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // cho phép dùng @PreAuthorize trên Controller/Service
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CorsConfig corsConfig;

    // -------------------------------------------------------------------------
    // Password encoder – BCrypt strength 12
    // -------------------------------------------------------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // -------------------------------------------------------------------------
    // AuthenticationManager (dùng trong AuthService)
    // -------------------------------------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // -------------------------------------------------------------------------
    // Security filter chain
    // -------------------------------------------------------------------------
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Tắt CSRF – REST API stateless không cần
            .csrf(AbstractHttpConfigurer::disable)

            // CORS dùng bean CorsConfig đã định nghĩa
            .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))

            // Stateless – không dùng session
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // ── Swagger / OpenAPI ─────────────────────────────────────
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3/api-docs"
                ).permitAll()

                // ── Auth endpoints (public) ───────────────────────────────
                .requestMatchers(
                        "/savibite/auth/login",
                        "/savibite/auth/register/**",
                        "/savibite/auth/refresh"
                ).permitAll()

                // ── Public read endpoints ─────────────────────────────────
                .requestMatchers(HttpMethod.GET, "/savibite/shops/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/savibite/products/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/savibite/boxes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/savibite/reviews/**").permitAll()

                // ── Shop-only endpoints ───────────────────────────────────
                .requestMatchers(HttpMethod.POST,   "/savibite/shops/**").hasRole("SHOP")
                .requestMatchers(HttpMethod.PUT,    "/savibite/shops/**").hasRole("SHOP")
                .requestMatchers(HttpMethod.POST,   "/savibite/products/**").hasRole("SHOP")
                .requestMatchers(HttpMethod.PUT,    "/savibite/products/**").hasRole("SHOP")
                .requestMatchers(HttpMethod.DELETE, "/savibite/products/**").hasRole("SHOP")
                .requestMatchers(HttpMethod.POST,   "/savibite/boxes/**").hasRole("SHOP")
                .requestMatchers(HttpMethod.PUT,    "/savibite/boxes/**").hasRole("SHOP")
                .requestMatchers(HttpMethod.DELETE, "/savibite/boxes/**").hasRole("SHOP")
                .requestMatchers(HttpMethod.PATCH,  "/savibite/orders/*/status").hasRole("SHOP")

                // ── User-only endpoints ───────────────────────────────────
                .requestMatchers(HttpMethod.POST,  "/savibite/orders/**").hasRole("USER")
                .requestMatchers(HttpMethod.PATCH, "/savibite/orders/*/cancel").hasRole("USER")
                .requestMatchers(HttpMethod.POST,  "/savibite/reviews/**").hasRole("USER")
                .requestMatchers(HttpMethod.POST,  "/savibite/payments/**").hasRole("USER")

                // ── Admin-only endpoints ──────────────────────────────────
                .requestMatchers("/savibite/admin/**").hasRole("ADMIN")

                // ── Payment callbacks – system/internal (no role check) ───
                .requestMatchers("/savibite/payments/callback/**").permitAll()

                // ── Everything else requires authentication ───────────────
                .anyRequest().authenticated()
            )

            // Đặt JwtFilter trước UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
