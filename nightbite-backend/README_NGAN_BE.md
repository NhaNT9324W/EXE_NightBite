# 🚀 NightBite Backend - Phần Ngân (BE)

## 📋 Thông tin chung

**Phụ trách**: Ngân (BE Developer)  
**Thời gian**: Sprint 1, 2, 3 (06/2026)  
**Status**: ✅ Hoàn thành toàn bộ  
**Framework**: Spring Boot 3.5.15 + JPA + MySQL

---

## 📊 Tổng quan công việc

### Tasks được hoàn thành

| Sprint | Task | Tên | Status |
|--------|------|-----|--------|
| 1 | BE-05 | Tạo sản phẩm Flash Sale | ✅ |
| 1 | BE-06 | Cập nhật/Xóa sản phẩm Flash Sale | ✅ |
| 1 | BE-07 | Lấy danh sách sản phẩm đang sale | ✅ |
| 2 | BE-14 | Tạo NightBite Box | ✅ |
| 2 | BE-15 | Đặt NightBite Box | ✅ |
| 2 | BE-16 | Tích hợp thanh toán MoMo/ZaloPay | ✅ |
| 3 | BE-23 | Tìm kiếm/Lọc sản phẩm | ✅ |
| 3 | BE-24 | Thống kê doanh thu | ✅ |

---

## 🎯 API Endpoints (25 endpoints)

### Product Flash Sale (8 endpoints)
```
POST   /savibite/products                    # BE-05 Tạo
PUT    /savibite/products/{id}               # BE-06 Cập nhật
DELETE /savibite/products/{id}               # BE-06 Xóa
GET    /savibite/products/sale/active        # BE-07 Danh sách sale
GET    /savibite/products/shop/{shopId}      # BE-07 Theo shop
GET    /savibite/products/{id}               # BE-07 Chi tiết
GET    /savibite/products/search/category    # BE-23 Tìm category
GET    /savibite/products/filter/allergen    # BE-23 Lọc allergen
```

### NightBite Box (7 endpoints)
```
POST   /savibite/nightbite-boxes             # BE-14 Tạo
PUT    /savibite/nightbite-boxes/{id}        # BE-14 Cập nhật
DELETE /savibite/nightbite-boxes/{id}        # BE-14 Xóa
GET    /savibite/nightbite-boxes/{id}        # Lấy chi tiết
GET    /savibite/nightbite-boxes/shop/{id}   # Theo shop
GET    /savibite/nightbite-boxes/sale/active # BE-15 Danh sách sale
GET    /savibite/nightbite-boxes/type/{type} # BE-15 Theo loại
```

### Order (1 endpoint)
```
POST   /savibite/orders/nightbite-box        # BE-15 Đặt box
```

### Payment (6 endpoints)
```
POST   /savibite/payments                    # BE-16 Tạo thanh toán
POST   /savibite/payments/{id}/confirm       # BE-16 Xác nhận
POST   /savibite/payments/{id}/cancel        # BE-16 Hủy/Hoàn
GET    /savibite/payments/{id}/status        # BE-16 Kiểm tra
POST   /savibite/payments/webhook/momo       # BE-16 MoMo callback
POST   /savibite/payments/webhook/zalopay    # BE-16 ZaloPay callback
```

### Statistics (2 endpoints)
```
GET    /savibite/statistics/shop/{id}        # BE-24 Shop stats
GET    /savibite/statistics/system           # BE-24 System stats
```

---

## 📁 Cấu trúc File được tạo

```
features/
├── product/
│   ├── ProductController.java         (125 lines)
│   ├── ProductService.java            (210 lines)
│   └── dto/
│       ├── ProductRequest.java
│       └── ProductResponse.java
├── nightbitebox/
│   ├── NightBiteBoxController.java    (157 lines)
│   ├── NightBiteBoxService.java       (168 lines)
│   └── dto/
│       ├── NightBiteBoxRequest.java
│       ├── NightBiteBoxResponse.java
│       └── NightBiteBoxOrderRequest.java
├── payment/
│   ├── PaymentController.java         (134 lines)
│   ├── PaymentService.java            (206 lines)
│   └── dto/
│       ├── PaymentRequest.java
│       └── PaymentResponse.java
├── statistics/
│   ├── StatisticsController.java      (72 lines)
│   ├── StatisticsService.java         (328 lines)
│   └── dto/
│       └── RevenueStatisticsResponse.java
└── order/
    ├── OrderController.java           (cập nhật)
    └── OrderService.java              (thêm BE-15)

infrastructure/
└── persistence/
    ├── ProductRepository.java         (cập nhật)
    └── NightBiteBoxRepository.java    (cập nhật)
```

**Tổng cộng**: 
- 8 Service classes
- 4 Controller classes  
- 8 DTO classes
- 2 Repository updates
- ~1400 lines của code mới

---

## 🔧 Công nghệ sử dụng

### Backend
- **Framework**: Spring Boot 3.5.15
- **ORM**: JPA/Hibernate
- **Database**: MySQL 8.0
- **Language**: Java 17
- **Build Tool**: Maven

### Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
</dependency>
```

---

## 🚀 Bắt đầu nhanh

### 1. Clone & Setup
```bash
cd nightbite-backend
mvn clean install
```

### 2. Cấu hình Database
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nightbite_db
spring.datasource.username=root
spring.datasource.password=12345
spring.jpa.hibernate.ddl-auto=update
```

### 3. Chạy application
```bash
mvn spring-boot:run
```

### 4. Test APIs
```
Swagger UI: http://localhost:8080/savibite/swagger-ui.html
```

---

## 📖 Tài liệu

- **API Documentation**: `API_DOCUMENTATION_NGAN_BE.md`
- **Usage Guide**: `USAGE_GUIDE_NGAN_BE.md`
- **Checklist**: `NGAN_BE_CHECKLIST.md`

---

## ✨ Tính năng chính

### 1. Product Flash Sale
- ✅ CRUD operations cho sản phẩm
- ✅ Lọc theo giờ sale (real-time)
- ✅ Quản lý allergen tags
- ✅ Tìm kiếm theo category
- ✅ Soft delete support

### 2. NightBite Box
- ✅ CRUD operations cho box
- ✅ 3 loại hộp (SMALL, MEDIUM, LARGE)
- ✅ Quản lý số lượng theo ngày
- ✅ Cảnh báo allergen

### 3. Order Management
- ✅ Đặt NightBite Box
- ✅ Tự động sinh order code
- ✅ Quản lý tồn kho
- ✅ Kiểm tra số lượng

### 4. Payment Integration
- ✅ 3 phương thức: CASH, MOMO, ZALOPAY
- ✅ Payment status tracking
- ✅ Webhook support (placeholder)
- ✅ Transaction history

### 5. Analytics & Reporting
- ✅ Doanh thu theo shop
- ✅ Doanh thu toàn hệ thống
- ✅ Phân tích theo period
- ✅ Breakdown by payment method
- ✅ Revenue by date

---

## 🔐 Security Notes

- ✅ Input validation
- ✅ Exception handling
- ✅ Transaction management (@Transactional)
- ⚠️ TODO: JWT authentication
- ⚠️ TODO: Role-based authorization
- ⚠️ TODO: Webhook signature verification

---

## 📊 Database Schema (tự động tạo)

```sql
-- Tự động tạo từ Entities
CREATE TABLE products (...)
CREATE TABLE nightbite_boxes (...)
CREATE TABLE orders (...)
CREATE TABLE order_items (...)
```

---

## 🧪 Testing Recommendations

```bash
# Unit Tests
mvn test

# Integration Tests
mvn verify

# Full test
mvn clean verify
```

### Test Scenarios
1. Tạo và quản lý sản phẩm
2. Tạo và quản lý box
3. Đặt hàng flow
4. Thanh toán flow
5. Thống kê doanh thu

---

## 📝 Lưu ý quan trọng

1. **Database**: Tự động tạo tables từ entities (ddl-auto=update)
2. **Soft Delete**: Delete operations chỉ đánh dấu isActive = false
3. **Response Format**: Tất cả responses đều có {success, message, data}
4. **Timestamps**: Tự động set createdAt, updatedAt
5. **OrderCode**: Format NB-YYYYMMDD-XXXXX
6. **Stock**: Tự động cập nhật sau khi đặt hàng

---

## 🔗 Liên kết với các phần khác

- **Frontend (FE - Khôi)**: Gọi các API endpoints này
- **Auth (Nhã)**: Tích hợp JWT authentication
- **Database (Nhã)**: Tạo base entities

---

## 🎓 Học hỏi & Tài liệu

- Spring Boot: https://spring.io/projects/spring-boot
- JPA Relationships: https://www.baeldung.com/jpa-many-to-many
- MySQL Best Practices: https://dev.mysql.com/doc/
- REST API Design: https://restfulapi.net/

---

## 📞 Support

Nếu có vấn đề hoặc cần hỗ trợ, vui lòng liên hệ Ngân (BE Developer)

---

**Phiên bản**: 1.0  
**Ngày cập nhật**: 20/06/2026  
**Status**: ✅ Ready for deployment

