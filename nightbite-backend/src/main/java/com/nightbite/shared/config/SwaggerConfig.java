package com.nightbite.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI nightBiteOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NightBite API")
                        .description("REST API cho nền tảng Flash Sale thực phẩm cuối ngày – NightBite\n\n" +
                                "**Base URL:** /savibite\n\n" +
                                "**Auth:** Dùng nút 'Authorize' để nhập Bearer token (Sprint 2+). " +
                                "Sprint 1 không cần token.\n\n" +
                                "**Roles:** ROLE_USER | ROLE_SHOP | ROLE_ADMIN")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Trần Huỳnh Khôi (BE)")
                                .email("khoi@nightbite.vn")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập JWT token (không cần tiền tố 'Bearer ')")));
    }
}
