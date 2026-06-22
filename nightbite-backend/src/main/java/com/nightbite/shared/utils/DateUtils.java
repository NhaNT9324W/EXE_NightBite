package com.nightbite.shared.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Tiện ích xử lý ngày/giờ cho NightBite.
 * Chuẩn múi giờ: Asia/Ho_Chi_Minh (UTC+7)
 */
public final class DateUtils {

    public static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private DateUtils() {}

    /** Lấy ngày hiện tại theo giờ Việt Nam */
    public static LocalDate nowDate() {
        return LocalDate.now(VN_ZONE);
    }

    /** Lấy ngày giờ hiện tại theo giờ Việt Nam */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(VN_ZONE);
    }

    /** Format LocalDate sang dd/MM/yyyy */
    public static String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMATTER);
    }

    /** Format LocalDateTime sang dd/MM/yyyy HH:mm:ss */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * Sinh mã đơn hàng theo pattern: NB-YYYYMMDD-XXXXX
     * XXXXX = 5 chữ số ngẫu nhiên
     */
    public static String generateOrderCode() {
        String date = LocalDate.now(VN_ZONE)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = (int) (Math.random() * 90000) + 10000;
        return "NB-" + date + "-" + random;
    }
}
