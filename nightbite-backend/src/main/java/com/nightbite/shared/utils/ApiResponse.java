package com.nightbite.shared.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;

/**
 * Wrapper chuẩn cho tất cả response của NightBite API.
 * Format: { success, message, data, timestamp }
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final long timestamp;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
    }

    // -------------------------------------------------------------------------
    // Factory methods – dùng trong toàn bộ dự án
    // -------------------------------------------------------------------------

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }

    // -------------------------------------------------------------------------
    // Builder-style helper để tương thích với code Ngân dùng @Builder pattern
    // ApiResponse.builder().success(true).message("OK").data(x).build()
    // -------------------------------------------------------------------------

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private boolean success;
        private String message;
        private T data;

        public Builder<T> success(boolean success) { this.success = success; return this; }
        public Builder<T> message(String message)   { this.message = message; return this; }
        public Builder<T> data(T data)              { this.data = data; return this; }

        public ApiResponse<T> build() {
            return new ApiResponse<>(success, message, data);
        }
    }
}
