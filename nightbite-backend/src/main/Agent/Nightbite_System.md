# 🌙 Savibite

# System Design Document

**Phiên bản MVP** | **EXE101 – FPT University Cần Thơ** | **2026**

| Thuộc tính | Giá trị                              |
| ---------- | ------------------------------------ |
| Nhóm       | EXE101_G03_MF                        |
| Tên dự án  | Savibite                             |
| Tech Stack | Spring Boot 3 + ReactJS 18 + MySQL 8 |
| Nền tảng   | Zalo Mini App (webview)              |

# 1. Tổng Quan Dự Án

## 1.1 Mô Tả Sản Phẩm

NightBite là một Zalo Mini App kết nối các cửa hàng thực phẩm (tiệm bánh, trà sữa, quán ăn) tại Cần Thơ với người tiêu dùng để bán các sản phẩm dư thừa cuối ngày với giá chiết khấu 50–60%, nhằm giảm lãng phí thực phẩm và mang lại giá trị cho cả hai phía.

## 1.2 Mục Tiêu MVP

* Người dùng có thể xem danh sách Flash Sale từ các shop đối tác
* Người dùng đặt hàng, chọn giờ nhận, thanh toán online
* Shop quản lý sản phẩm, đơn hàng, doanh thu
* Admin giám sát và kiểm soát chất lượng hệ thống
* Trust Score chống boom hàng

## 1.3 Phạm Vi Kỹ Thuật

| Thành phần   | Công nghệ                 | Ghi chú                          |
| ------------ | ------------------------- | -------------------------------- |
| Backend      | Java 17 + Spring Boot 3.x | REST API, JPA Code First         |
| Frontend     | ReactJS 18 (pnpm)         | Đóng gói lên Zalo Cloud          |
| Database     | MySQL 8.x                 | HikariCP connection pool         |
| Auth         | Spring Security + JWT     | ROLE_USER, ROLE_SHOP, ROLE_ADMIN |
| Payment      | MoMo / ZaloPay (mock)     | Fallback mock nếu chưa kịp       |
| Notification | Firebase / Zalo OA        | Sprint 3                         |
| Deploy BE    | Render / Railway (free)   | Docker tuỳ chọn                  |
| Deploy FE    | Zalo Cloud                | Build pnpm → upload              |

# 2. Kiến Trúc Hệ Thống

## 2.1 Tổng Quan Kiến Trúc

NightBite theo kiến trúc Client–Server 3 tầng:

* Tầng Presentation: ReactJS chạy trong Zalo Mini App (webview)
* Tầng Application: Spring Boot REST API, xử lý nghiệp vụ
* Tầng Data: MySQL 8 với JPA Code First

## 2.2 Luồng Request Tổng Quan

```text
Người dùng (Zalo App)
   ↓ HTTPS / Axios
ReactJS (Zalo Cloud)
   ↓ REST API calls
Spring Boot API Server (Render/Railway)
   ├── Spring Security + JWT Filter
   ├── Feature Controllers (auth, shop, product, order, review, admin)
   ├── Feature Services (business logic)
   └── JPA Repositories → MySQL 8
```

## 2.3 Feature Base Architecture (BE)

```text
nightbite-backend/
├── src/main/java/com/nightbite/
│   ├── domain/
│   │   ├── User.java
│   │   ├── Shop.java
│   │   ├── Product.java
│   │   ├── NightBiteBox.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   └── Review.java
│   ├── features/
│   │   ├── auth/
│   │   │   ├── AuthController.java
│   │   │   ├── AuthService.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   └── AuthResponse.java
│   │   ├── shop/
│   │   │   ├── ShopController.java
│   │   │   ├── ShopService.java
│   │   │   └── ShopDto.java
│   │   ├── product/
│   │   │   ├── ProductController.java
│   │   │   ├── ProductService.java
│   │   │   └── ProductDto.java
│   │   ├── order/
│   │   │   ├── OrderController.java
│   │   │   ├── OrderService.java
│   │   │   └── OrderDto.java
│   │   ├── review/
│   │   │   ├── ReviewController.java
│   │   │   ├── ReviewService.java
│   │   │   └── ReviewDto.java
│   │   └── admin/
│   │       ├── AdminController.java
│   │       └── AdminService.java
│   ├── shared/
│   │   ├── config/
│   │   ├── exception/
│   │   ├── jwt/
│   │   └── utils/
│   └── infrastructure/
│       └── persistence/
```

## 2.4 Cấu Trúc Frontend (FE)

```text
nightbite-frontend/
├── public/
│   └── images/
├── src/
│   ├── api/
│   │   ├── axiosConfig.js
│   │   ├── authApi.js
│   │   ├── shopApi.js
│   │   ├── productApi.js
│   │   ├── orderApi.js
│   │   └── reviewApi.js
│   ├── pages/
│   │   ├── user/
│   │   ├── shop/
│   │   ├── auth/
│   │   └── admin/
│   ├── components/
│   ├── hooks/
│   ├── context/
│   ├── router/
│   └── styles/
```

# 3. Thiết Kế Cơ Sở Dữ Liệu

## 3.1 Thông Tin Database

| Thuộc tính      | Giá trị                                  |
| --------------- | ---------------------------------------- |
| Tên Database    | nightbite_db                             |
| Engine          | MySQL 8.x                                |
| Character Set   | utf8mb4                                  |
| Collation       | utf8mb4_unicode_ci                       |
| ORM             | Spring Data JPA (Hibernate) – Code First |
| Connection Pool | HikariCP                                 |
| Tool thiết kế   | MySQL Workbench                          |

## 3.2 Danh Sách Bảng

| Tên bảng        | Mô tả                                   | Ghi chú                                      |
| --------------- | --------------------------------------- | -------------------------------------------- |
| users           | Tài khoản người dùng (khách hàng)       | ROLE_USER, trust_score                       |
| shops           | Tài khoản cửa hàng đối tác              | ROLE_SHOP, is_active                         |
| admins          | Tài khoản quản trị viên                 | ROLE_ADMIN                                   |
| products        | Sản phẩm đồ ăn bình thường (Flash Sale) | Giá gốc, giá sale, số lượng                  |
| nightbite_boxes | Sản phẩm hộp bí ẩn (Mystery Box)        | Loại hộp, giá, cảnh báo dị ứng               |
| orders          | Đơn hàng của user                       | Trạng thái: PENDING→CONFIRMED→DONE/CANCELLED |
| order_items     | Chi tiết các item trong đơn hàng        | FK → orders, products/boxes                  |
| reviews         | Đánh giá của user sau khi nhận hàng     | FK → orders, users, shops                    |
| payments        | Bản ghi giao dịch thanh toán            | MoMo/ZaloPay/MOCK                            |
| notifications   | Thông báo push cho user                 | Sprint 3                                     |

```
```
