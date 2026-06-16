# 🤖 NightBite – Agent Coding Instructions for Nhã (BE)

> **Dự án:** NightBite – Zalo Mini App bán thực phẩm cuối ngày  
> **Thành viên:** Nguyễn Thanh Nhã · CE181298 · CEO/Leader & Backend  
> **Deadline review lần 1:** 25/06/2026  
> **Tech stack:** Java 17 · Spring Boot 3.x · MySQL 8.x · JPA Code First · Lombok · Maven · Swagger · Spring Security + JWT

---

## 📐 Kiến trúc & Quy ước bắt buộc

### Package structure (Feature Base)
```
src/main/java/com/nightbite/
├── domain/          ← @Entity toàn cục
├── features/
│   ├── shop/        ← ShopController, ShopService, ShopDto
│   ├── order/       ← OrderController, OrderService, OrderDto
│   ├── review/      ← ReviewController, ReviewService, ReviewDto
│   └── admin/       ← AdminController, AdminService
├── shared/
│   ├── config/      ← SecurityConfig, SwaggerConfig, CorsConfig
│   ├── exception/   ← GlobalExceptionHandler
│   ├── jwt/         ← JwtUtil, JwtFilter
│   └── utils/       ← ResponseWrapper, DateUtils
└── infrastructure/
    └── persistence/ ← JpaRepository interfaces
```

### Naming conventions
- **Package:** `com.nightbite.features.{featureName}`
- **Class:** PascalCase (`ShopController`, `OrderService`)
- **Method:** camelCase (`createShop`, `getOrdersByUser`)
- **DTO:** suffix `Dto` hoặc `Request/Response` (`CreateOrderRequest`, `OrderResponse`)
- **Exception:** suffix `Exception` (`OrderNotFoundException`, `ShopNotFoundException`)

### Response wrapper – dùng cho MỌI API
```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
```

### Swagger annotation – bắt buộc trên mỗi endpoint
```java
@Operation(summary = "...", description = "...")
@ApiResponse(responseCode = "200", description = "...")
```

### Lombok – dùng trên DTO và Entity
```java
@Data @Builder @NoArgsConstructor @AllArgsConstructor
```

---

## 🗄️ Sprint 1 – Database Schema & Shop API

### BE-01 · Thiết kế schema DB (1 ngày) · Ưu tiên: CAO

**Tạo các JPA Entity sau trong `com.nightbite.domain`:**

Database name: `nightbite_db` · charset: `utf8mb4` · collation: `utf8mb4_unicode_ci`

---

#### Entity: `User.java`
| Cột | Kiểu Java | Annotation JPA | Ghi chú |
|-----|-----------|----------------|---------|
| id | Long | `@Id @GeneratedValue(AUTO)` | PK |
| zaloId | String(100) | `@Column(unique=true)` | Zalo OpenID |
| fullName | String(150) | `@Column(nullable=false)` | |
| phone | String(20) | `@Column(unique=true)` | |
| email | String(150) | `@Column(unique=true)` | |
| passwordHash | String(255) | `@Column(nullable=false)` | BCrypt |
| avatarUrl | String(500) | | |
| trustScore | Integer | `DEFAULT 100, NOT NULL` | Chống boom hàng |
| isActive | Boolean | `DEFAULT true` | false = bị Admin khoá |
| role | Enum(USER) | `DEFAULT 'USER'` | |
| createdAt | LocalDateTime | `@CreationTimestamp` | |
| updatedAt | LocalDateTime | `@UpdateTimestamp` | |

---

#### Entity: `Shop.java`
| Cột | Kiểu Java | Annotation JPA | Ghi chú |
|-----|-----------|----------------|---------|
| id | Long | `@Id @GeneratedValue(AUTO)` | PK |
| shopName | String(200) | `@Column(nullable=false)` | |
| ownerName | String(150) | `@Column(nullable=false)` | |
| phone | String(20) | `@Column(unique=true, nullable=false)` | |
| email | String(150) | `@Column(unique=true)` | |
| passwordHash | String(255) | `@Column(nullable=false)` | |
| address | String (TEXT) | `@Column(nullable=false)` | |
| district | String(100) | | Dùng để filter |
| description | String (TEXT) | | |
| logoUrl | String(500) | | |
| bannerUrl | String(500) | | |
| isActive | Boolean | `DEFAULT true` | false = bị Admin khoá |
| ratingAvg | BigDecimal(3,2) | `DEFAULT 0.00` | |
| reviewCount | Integer | `DEFAULT 0` | |
| role | Enum(SHOP) | `DEFAULT 'SHOP'` | |
| createdAt | LocalDateTime | `@CreationTimestamp` | |
| updatedAt | LocalDateTime | `@UpdateTimestamp` | |

---

#### Entity: `Admin.java`
| Cột | Kiểu Java | Annotation JPA |
|-----|-----------|----------------|
| id | Long | `@Id @GeneratedValue(AUTO)` |
| username | String(100) | `@Column(unique=true, nullable=false)` |
| passwordHash | String(255) | `@Column(nullable=false)` |
| fullName | String(150) | `@Column(nullable=false)` |
| email | String(150) | `@Column(unique=true, nullable=false)` |
| role | Enum(ADMIN) | `DEFAULT 'ADMIN'` |
| createdAt | LocalDateTime | `@CreationTimestamp` |

---

#### Entity: `Product.java` (Flash Sale)
| Cột | Kiểu Java | Annotation JPA | Ghi chú |
|-----|-----------|----------------|---------|
| id | Long | `@Id @GeneratedValue(AUTO)` | |
| shop | Shop | `@ManyToOne @JoinColumn(name="shop_id")` | FK → shops |
| name | String(200) | `@Column(nullable=false)` | |
| description | String (TEXT) | | |
| imageUrl | String(500) | | |
| originalPrice | BigDecimal(12,2) | `@Column(nullable=false)` | Giá gốc VNĐ |
| salePrice | BigDecimal(12,2) | `@Column(nullable=false)` | Giá sale |
| quantityAvailable | Integer | `DEFAULT 0, NOT NULL` | |
| quantitySold | Integer | `DEFAULT 0` | |
| saleStartTime | LocalTime | | Giờ bắt đầu |
| saleEndTime | LocalTime | | Giờ kết thúc |
| saleDate | LocalDate | | |
| allergenTags | String(500) | | JSON array CSV |
| category | String(100) | | bánh/trà sữa/đồ ăn |
| isActive | Boolean | `DEFAULT true` | |
| createdAt | LocalDateTime | `@CreationTimestamp` | |
| updatedAt | LocalDateTime | `@UpdateTimestamp` | |

---

#### Entity: `NightBiteBox.java`
| Cột | Kiểu Java | Annotation JPA | Ghi chú |
|-----|-----------|----------------|---------|
| id | Long | `@Id @GeneratedValue(AUTO)` | |
| shop | Shop | `@ManyToOne @JoinColumn(name="shop_id")` | |
| boxName | String(200) | `@Column(nullable=false)` | |
| boxType | Enum(SMALL,MEDIUM,LARGE) | `@Column(nullable=false)` | |
| description | String (TEXT) | | |
| imageUrl | String(500) | | |
| price | BigDecimal(12,2) | `@Column(nullable=false)` | |
| quantityAvailable | Integer | `DEFAULT 0, NOT NULL` | |
| allergenWarning | String (TEXT) | | |
| saleDate | LocalDate | | |
| isActive | Boolean | `DEFAULT true` | |
| createdAt | LocalDateTime | `@CreationTimestamp` | |

---

#### Entity: `Order.java`
| Cột | Kiểu Java | Annotation JPA | Ghi chú |
|-----|-----------|----------------|---------|
| id | Long | `@Id @GeneratedValue(AUTO)` | |
| user | User | `@ManyToOne @JoinColumn(name="user_id")` | |
| shop | Shop | `@ManyToOne @JoinColumn(name="shop_id")` | |
| orderCode | String(50) | `@Column(unique=true, nullable=false)` | NB-YYYYMMDD-XXXXX |
| status | Enum(PENDING,CONFIRMED,READY,DONE,CANCELLED) | `DEFAULT 'PENDING'` | |
| totalAmount | BigDecimal(12,2) | `@Column(nullable=false)` | |
| pickupTime | LocalDateTime | | |
| note | String (TEXT) | | |
| paymentMethod | Enum(MOMO,ZALOPAY,CASH,MOCK) | `DEFAULT 'MOCK'` | |
| paymentStatus | Enum(PENDING,PAID,FAILED,REFUNDED) | `DEFAULT 'PENDING'` | |
| cancelReason | String (TEXT) | | |
| createdAt | LocalDateTime | `@CreationTimestamp` | |
| updatedAt | LocalDateTime | `@UpdateTimestamp` | |

---

#### Entity: `OrderItem.java`
| Cột | Kiểu Java | Annotation JPA | Ghi chú |
|-----|-----------|----------------|---------|
| id | Long | `@Id @GeneratedValue(AUTO)` | |
| order | Order | `@ManyToOne @JoinColumn(name="order_id")` | |
| itemType | Enum(PRODUCT,BOX) | `@Column(nullable=false)` | |
| product | Product | `@ManyToOne @JoinColumn(name="product_id")` | nullable |
| box | NightBiteBox | `@ManyToOne @JoinColumn(name="box_id")` | nullable |
| itemName | String(200) | `@Column(nullable=false)` | Snapshot tên lúc đặt |
| unitPrice | BigDecimal(12,2) | `@Column(nullable=false)` | Snapshot giá lúc đặt |
| quantity | Integer | `DEFAULT 1, NOT NULL` | |
| subtotal | BigDecimal(12,2) | `@Column(nullable=false)` | unitPrice × quantity |

---

#### Entity: `Review.java`
| Cột | Kiểu Java | Annotation JPA | Ghi chú |
|-----|-----------|----------------|---------|
| id | Long | `@Id @GeneratedValue(AUTO)` | |
| order | Order | `@OneToOne @JoinColumn(name="order_id", unique=true)` | 1 đơn = 1 review |
| user | User | `@ManyToOne @JoinColumn(name="user_id")` | |
| shop | Shop | `@ManyToOne @JoinColumn(name="shop_id")` | |
| rating | Integer | `CHECK 1-5, NOT NULL` | |
| comment | String (TEXT) | | |
| imageUrls | String (TEXT) | | JSON array đường dẫn |
| isVisible | Boolean | `DEFAULT true` | false = Admin ẩn |
| createdAt | LocalDateTime | `@CreationTimestamp` | |

---

### BE-02 · Seed data (0.5 ngày) · Ưu tiên: CAO

Tạo class `DataSeeder.java` implements `ApplicationRunner` hoặc dùng `data.sql`:

```
Seed:
- 3–5 Shop (đa dạng: tiệm bánh, trà sữa, quán ăn)
- 10–15 Product (Flash Sale, giá gốc 30k–120k, giá sale giảm 50–60%)
- 5 User test (trustScore = 100)
- 1 Admin account
```

---

### BE-03 · API tạo / cập nhật Shop (1 ngày) · Ưu tiên: CAO

**File:** `features/shop/ShopController.java`, `ShopService.java`, `ShopDto.java`

```
POST   /savibite/shops        → tạo shop mới
PUT    /savibite/shops/{id}   → cập nhật thông tin shop
```

- Sprint 1: không cần token (public)
- Sprint 2: gắn `@PreAuthorize("hasRole('SHOP')")`
- Request body: shopName, ownerName, phone, email, password, address, district, description, logoUrl, bannerUrl
- Response: `ApiResponse<ShopDto>`

---

### BE-04 · API lấy danh sách Shop (1 ngày) · Ưu tiên: CAO

```
GET /savibite/shops              → danh sách tất cả shop (isActive = true)
GET /savibite/shops?district=X   → filter theo quận/huyện
GET /savibite/shops/{id}         → chi tiết 1 shop
GET /savibite/shops/{id}/products → sản phẩm đang sale của shop
GET /savibite/shops/{id}/reviews  → review của shop
```

- Phân quyền: **Public** (permitAll)
- Response: `ApiResponse<List<ShopDto>>`

---

## 📦 Sprint 2 – Order API

### BE-11 · API tạo đơn hàng (2 ngày) · Ưu tiên: CAO

**File:** `features/order/OrderController.java`, `OrderService.java`, `OrderDto.java`

```
POST /savibite/orders
```

- Phân quyền: `ROLE_USER`
- Request body:
  ```json
  {
    "shopId": 1,
    "items": [
      { "itemType": "PRODUCT", "productId": 5, "quantity": 2 },
      { "itemType": "BOX", "boxId": 3, "quantity": 1 }
    ],
    "pickupTime": "2026-06-20T18:00:00",
    "note": "Không hành",
    "paymentMethod": "MOMO"
  }
  ```
- Logic service:
  1. Kiểm tra `quantity_available` còn đủ không → nếu thiếu throw `InsufficientStockException`
  2. Sinh `orderCode` format `NB-YYYYMMDD-{5 số random}`
  3. Tạo `Order` + `OrderItem` list (snapshot `itemName`, `unitPrice` tại thời điểm đặt)
  4. Trừ `quantity_available` của Product/Box
  5. Tính `totalAmount = sum(unitPrice × quantity)`
  6. Tạo bản ghi `Payment` với status `PENDING`
- Response: `ApiResponse<OrderDto>`

---

### BE-12 · API lịch sử đơn hàng (1 ngày) · Ưu tiên: CAO

```
GET /savibite/orders                → User xem đơn của mình / Shop xem đơn vào shop
GET /savibite/orders/{id}           → Chi tiết 1 đơn hàng
```

- Phân quyền: `ROLE_USER` hoặc `ROLE_SHOP`
- Logic: lấy `userId` hoặc `shopId` từ JWT, filter đúng dữ liệu của người đang login
- Response: `ApiResponse<List<OrderDto>>` / `ApiResponse<OrderDto>`

---

### BE-13 · API cập nhật trạng thái đơn (1 ngày) · Ưu tiên: CAO

```
PATCH /savibite/orders/{id}/status
PATCH /savibite/orders/{id}/cancel   ← User huỷ đơn
```

- **Shop** cập nhật: `PENDING → CONFIRMED → READY → DONE` hoặc `→ CANCELLED`
- **User** huỷ đơn (chỉ khi còn PENDING):
  - Hoàn lại `quantity_available`
  - Giảm `trustScore` của User đi 10 điểm
  - Nếu `trustScore ≤ 50`: set flag hạn chế (trả về warning trong response)
- Phân quyền: `ROLE_SHOP` cho `/status`, `ROLE_USER` cho `/cancel`
- Request body: `{ "status": "CONFIRMED", "cancelReason": "..." }`

---

## ⭐ Sprint 3 – Review & Admin API

### BE-20 · API gửi đánh giá (1 ngày) · Ưu tiên: TB

```
POST /savibite/reviews
```

- Phân quyền: `ROLE_USER`
- Điều kiện: Order phải có `status = DONE` và chưa có review (`order_id` UNIQUE)
- Request body: `{ "orderId": 10, "rating": 5, "comment": "...", "imageUrls": [] }`
- Sau khi tạo review: cập nhật lại `ratingAvg` và `reviewCount` trong bảng `shops`
  ```
  ratingAvg = AVG(rating) WHERE shop_id = X AND isVisible = true
  reviewCount = COUNT(*) WHERE shop_id = X AND isVisible = true
  ```

---

### BE-21 · API danh sách review theo Shop (0.5 ngày) · Ưu tiên: TB

```
GET /savibite/reviews/shop/{shopId}
```

- Phân quyền: **Public**
- Chỉ trả về `isVisible = true`
- Response: `ApiResponse<List<ReviewDto>>`

---

### BE-22 · API khoá / mở khoá Shop hoặc User (Admin) (1 ngày) · Ưu tiên: THẤP

```
PATCH /savibite/admin/users/{id}/lock
PATCH /savibite/admin/shops/{id}/lock
```

- Phân quyền: `ROLE_ADMIN`
- Request body: `{ "locked": true, "reason": "Vi phạm điều khoản" }`
- Logic: set `isActive = false/true` trong bảng `users` hoặc `shops`
- Nếu locked = true: tất cả API của user/shop đó trả về 403 (kiểm tra trong service layer)

---

## 🗂️ Danh sách API Nhã phụ trách (tổng hợp)

| ID | Sprint | Method | Endpoint | Phân quyền |
|----|--------|--------|----------|-----------|
| BE-01 | 1 | — | DB Schema (9 entities) | System |
| BE-02 | 1 | — | Seed data | System |
| BE-03 | 1 | POST/PUT | `/savibite/shops` | Shop |
| BE-04 | 1 | GET | `/savibite/shops`, `/savibite/shops/{id}` | Public |
| BE-11 | 2 | POST | `/savibite/orders` | User |
| BE-12 | 2 | GET | `/savibite/orders`, `/savibite/orders/{id}` | User/Shop |
| BE-13 | 2 | PATCH | `/savibite/orders/{id}/status`, `/cancel` | Shop/User |
| BE-20 | 3 | POST | `/savibite/reviews` | User |
| BE-21 | 3 | GET | `/savibite/reviews/shop/{shopId}` | Public |
| BE-22 | 3 | PATCH | `/savibite/admin/users/{id}/lock`, `/shops/{id}/lock` | Admin |

---

## ⚙️ Cấu hình chung (tham khảo)

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nightbite_db?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

server.servlet.context-path=/savibite

springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### JWT config
```properties
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000       # 24 giờ (ms)
jwt.refresh-expiration=604800000  # 7 ngày (ms)
```

### Base URL
```
https://nightbite-api.onrender.com/savibite
```

---

## ✅ Checklist trước ngày 25/06

- [ ] BE-01: 9 Entity tạo đúng, JPA auto tạo đủ 9 bảng trong MySQL
- [ ] BE-02: Seed data chạy được, có sẵn shop + product + user để test
- [ ] BE-03: `POST /savibite/shops` tạo shop, `PUT /savibite/shops/{id}` cập nhật
- [ ] BE-04: `GET /savibite/shops` trả danh sách, filter `?district=` hoạt động
- [ ] BE-11: `POST /savibite/orders` tạo đơn, sinh orderCode, trừ tồn kho
- [ ] BE-12: `GET /savibite/orders` filter đúng theo role từ JWT
- [ ] BE-13: Shop cập nhật status, User huỷ đơn → trừ trustScore
- [ ] BE-20: `POST /savibite/reviews` chỉ cho order DONE, cập nhật ratingAvg shop
- [ ] BE-21: `GET /savibite/reviews/shop/{id}` public, chỉ trả isVisible=true
- [ ] BE-22: Admin lock/unlock user, shop → set isActive
- [ ] Tất cả API đã có Swagger docs (`@Operation`, `@ApiResponse`)
- [ ] Tất cả response dùng `ApiResponse<T>` wrapper
- [ ] Test qua Postman không lỗi 500

---

*File này được tạo tự động từ NightBite_SYSTEM.docx – dành cho AI agent sinh code trong IntelliJ IDEA.*
