# Backlog MVP

**NightBite – Product Backlog MVP**

**Dự án EXE101 – FPT University Can Tho  |  \~4 Tuần  |  Nhã · Ngân · Khôi (BE) \+ Thắng (FE)**

 **PHẢI HOÀN THÀNH CODE TRƯỚC NGÀY 25 THÁNG 6 ( ĐỂ REVIEW LẦN 1\)**

**Tech Stack & Công Cụ**

| Vai trò / Tool | Ngôn ngữ & Framework | Database / Platform | Thư viện & Ghi chú |
| :---- | :---- | :---- | :---- |
| **BE – IntelliJ IDEA** | **Java 17 \+ Spring Boot 3.x** | **MySQL 8.x (Code First JPA)** | **Spring Security \+ JWT · Lombok · Maven · Swagger · HikariCP** |
| **FE – VS Code** | **ReactJS 18 (pnpm)** | **Zalo Mini App SDK** | **Axios · TailwindCSS · React Router · ZaloPay/MoMo integration** |
| **Database – MySQL** | **MySQL Workbench** | **Code First (JPA Entities)** | **Nhã thiết kế schema, seed data mẫu; Khôi cấu hình kết nối Spring** |
| **DevOps** | **Git \+ GitHub** | **Render / Railway (BE free)** | **Zalo Cloud (FE) · Postman test API · Docker (tuỳ chọn)** |

 

**Giới Thiệu Phân Quyền**

**Hệ thống NightBite có 4 nhóm phân quyền. Mỗi API / màn hình sẽ được gắn một role cụ thể – Khôi (BE) sẽ cấu hình Spring Security @PreAuthorize dựa trên bảng này:**

 

| Role | Là ai? | Màu nhận diện | Quyền hạn chính |
| :---- | :---- | :---- | :---- |
| **User** | **Khách hàng – sinh viên, người mua đồ ăn cuối ngày** | **Xem cột Phân quyền trong backlog bên dưới** | **Xem Flash Sale, đặt hàng, thanh toán, đánh giá, xem lịch sử đơn** |
| **Shop** | **Chủ quán – tiệm bánh, trà sữa, quán ăn đối tác** | **Xem cột Phân quyền trong backlog bên dưới** | **Đăng sản phẩm sale, tạo NightBite Box, xác nhận / huỷ đơn, xem doanh thu** |
| **Admin** | **Quản trị viên – nhóm NightBite quản lý hệ thống** | **Xem cột Phân quyền trong backlog bên dưới** | **Khoá / mở khoá shop hoặc user vi phạm, giám sát chất lượng** |
| **Hệ thống** | **Internal – logic nội bộ, không lộ ra UI người dùng** | **Xem cột Phân quyền trong backlog bên dưới** | **DB schema, JWT filter, trust score, push notification, deploy** |
| **Cả hai** | **Cả User lẫn Shop đều truy cập được endpoint này** | **Xem cột Phân quyền trong backlog bên dưới** | **Xem lịch sử đơn, xem review – tuỳ token sẽ lọc đúng dữ liệu của mình** |

**Lưu ý: Auth (JWT) được làm ở Sprint 2, KHÔNG làm trước. Sprint 1 test API tự do qua Postman, không cần token. Cuối Sprint 2 mới @PreAuthorize vào các route.**

 

**Phân Công Tổng Quan**

| Giai Đoạn | Nhã (BE) | Ngân (BE) | Khôi (BE) | Thắng (FE) |
| :---- | :---- | :---- | :---- | :---- |
| **Sprint 1 – Nền tảng core (Tuần 1–2)** | **Nhã: DB schema \+ data mẫu \+ Shop API** | **Ngân: Product Flash Sale API** | **Khôi: Spring Boot setup \+ Swagger \+ Deploy** | **Thắng: Setup FE \+ Home \+ Chi tiết \+ Shop Dashboard** |
| **Sprint 2 – Order Flow \+ Auth (Tuần 2–3)** | **Nhã: Order API (tạo/xem/cập nhật trạng thái)** | **Ngân: NightBite Box \+ Thanh toán** | **Khôi: Auth JWT \+ Spring Security \+ Trust Score** | **Thắng: Checkout \+ Thanh toán \+ Auth FE** |
| **Sprint 3 – Polish & Deploy (Tuần 3–4)** | **Nhã: Review/Rating \+ Admin lock** | **Ngân: Filter/Search \+ Thống kê** | **Khôi: Notification \+ Unit test \+ Fix bug** | **Thắng: Box FE \+ Lịch sử \+ Profile \+ Deploy** |

 

**Chú Thích Màu Sắc**

**Sprint:  Sprint 1 – xanh dương nhạt  |  Sprint 2 – vàng nhạt  |  Sprint 3 – xanh lá nhạt**

**Phân quyền:  User – xanh dương  |  Shop – cam nhạt  |  Admin – tím nhạt  |  Hệ thống – xám  |  Cả hai – xanh lá**

**Ưu tiên:  Cao – đỏ nhạt  |  Trung bình – vàng nhạt  |  Thấp – xanh lá nhạt**

 

**Chi Tiết Backlog**

| Sprint | ID | Task | User Story | Phụ trách | Phân quyền | Ưu tiên | Ước tính |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **Sprint 1** | **BE-01** | **Thiết kế schema DB: User, Shop, Product, Order, OrderItem, Review (MySQL Workbench)** | **Database & Data** | **Nhã (BE)** | **Hệ thống** | **Cao** | **1 ngày** |
| **Sprint 1** | **BE-02** | **Tạo data mẫu (seed): 3–5 shop, 10–15 sản phẩm, 5 user test** | **Database & Data** | **Nhã (BE)** | **Hệ thống** | **Cao** | **0.5 ngày** |
| **Sprint 1** | **BE-03** | **API tạo / cập nhật thông tin Shop (không cần token)** | **Quản lý Shop** | **Nhã (BE)** | **Shop** | **Cao** | **1 ngày** |
| **Sprint 1** | **BE-04** | **API lấy danh sách Shop (filter theo khu vực)** | **Quản lý Shop** | **Nhã (BE)** | **User** | **Cao** | **1 ngày** |
| **Sprint 1** | **BE-05** | **API tạo sản phẩm Flash Sale (tên, ảnh, giá gốc, giá sale, SL, tag dị ứng)** | **Quản lý Menu & Sản phẩm** | **Ngân (BE)** | **Shop** | **Cao** | **2 ngày** |
| **Sprint 1** | **BE-06** | **API cập nhật / xóa sản phẩm Flash Sale** | **Quản lý Menu & Sản phẩm** | **Ngân (BE)** | **Shop** | **Cao** | **1 ngày** |
| **Sprint 1** | **BE-07** | **API lấy danh sách sản phẩm đang sale (theo giờ, theo shop)** | **Quản lý Menu & Sản phẩm** | **Ngân (BE)** | **User** | **Cao** | **1 ngày** |
| **Sprint 1** | **BE-08** | **Setup Spring Boot 3.x, cấu hình JPA Code First, Lombok, Maven, kết nối MySQL** | **Project Setup** | **Khôi (BE)** | **Hệ thống** | **Cao** | **1 ngày** |
| **Sprint 1** | **BE-09** | **Cấu hình Swagger (OpenAPI) – tất cả API phải có docs để FE dễ tích hợp** | **Project Setup** | **Khôi (BE)** | **Hệ thống** | **Cao** | **0.5 ngày** |
| **Sprint 1** | **BE-10** | **Deploy backend lên Render/Railway (free tier)** | **Project Setup** | **Khôi (BE)** | **Hệ thống** | **Trung bình** | **1 ngày** |
| **Sprint 1** | **FE-01** | **Setup ReactJS (pnpm), cấu hình Axios, routing, folder structure** | **Setup FE** | **Thắng (FE)** | **Hệ thống** | **Cao** | **1 ngày** |
| **Sprint 1** | **FE-02** | **Màn hình trang chủ: danh sách món Flash Sale theo giờ (gọi API không cần token)** | **Trang chủ User** | **Thắng (FE)** | **User** | **Cao** | **2 ngày** |
| **Sprint 1** | **FE-03** | **Màn hình chi tiết sản phẩm (ảnh, giá, tag dị ứng, số lượng còn)** | **Chi tiết sản phẩm** | **Thắng (FE)** | **User** | **Cao** | **1 ngày** |
| **Sprint 1** | **FE-04** | **Màn hình quản lý sản phẩm Flash Sale cho Shop (thêm/sửa/xóa – chưa cần login)** | **Shop Dashboard** | **Thắng (FE)** | **Shop** | **Cao** | **2 ngày** |
| **Sprint 2** | **BE-11** | **API tạo đơn hàng (User đặt món Flash Sale)** | **Đặt hàng** | **Nhã (BE)** | **User** | **Cao** | **2 ngày** |
| **Sprint 2** | **BE-12** | **API xem lịch sử đơn hàng (theo User & Shop)** | **Đặt hàng** | **Nhã (BE)** | **Cả hai** | **Cao** | **1 ngày** |
| **Sprint 2** | **BE-13** | **API cập nhật trạng thái đơn: PENDING → CONFIRMED → DONE / CANCELLED (Shop thao tác)** | **Đặt hàng** | **Nhã (BE)** | **Shop** | **Cao** | **1 ngày** |
| **Sprint 2** | **BE-14** | **API tạo NightBite Box (loại hộp, giá, cảnh báo dị ứng)** | **Night Bite Box** | **Ngân (BE)** | **Shop** | **Trung bình** | **2 ngày** |
| **Sprint 2** | **BE-15** | **API đặt NightBite Box** | **Night Bite Box** | **Ngân (BE)** | **User** | **Trung bình** | **1 ngày** |
| **Sprint 2** | **BE-16** | **Tích hợp cổng thanh toán MoMo hoặc ZaloPay (mock nếu chưa kịp, đánh dấu TODO)** | **Thanh toán** | **Ngân (BE)** | **User** | **Cao** | **2 ngày** |
| **Sprint 2** | **BE-17** | **API đăng ký / đăng nhập User \+ Shop (JWT) – gắn @PreAuthorize vào các API đã có** | **Xác thực & Phân quyền** | **Khôi (BE)** | **Cả hai** | **Cao** | **2 ngày** |
| **Sprint 2** | **BE-18** | **Spring Security filter: phân quyền ROLE\_USER, ROLE\_SHOP, ROLE\_ADMIN** | **Xác thực & Phân quyền** | **Khôi (BE)** | **Hệ thống** | **Cao** | **1 ngày** |
| **Sprint 2** | **BE-19** | **Logic tính trust score User (boom hàng → giảm điểm → hạn chế đặt)** | **Trust Score** | **Khôi (BE)** | **Hệ thống** | **Trung bình** | **1 ngày** |
| **Sprint 2** | **FE-05** | **Màn hình giỏ hàng \+ checkout \+ chọn giờ nhận** | **Đặt hàng FE** | **Thắng (FE)** | **User** | **Cao** | **2 ngày** |
| **Sprint 2** | **FE-06** | **Màn hình thanh toán (tích hợp cổng thanh toán)** | **Thanh toán FE** | **Thắng (FE)** | **User** | **Cao** | **1 ngày** |
| **Sprint 2** | **FE-07** | **Màn hình đăng ký / đăng nhập User – gắn JWT vào Axios interceptor** | **Auth FE** | **Thắng (FE)** | **User** | **Cao** | **1 ngày** |
| **Sprint 2** | **FE-08** | **Màn hình đăng ký / đăng nhập Shop – bảo vệ route Shop Dashboard bằng token** | **Auth FE** | **Thắng (FE)** | **Shop** | **Cao** | **1 ngày** |
| **Sprint 3** | **BE-20** | **API gửi đánh giá sau khi nhận hàng** | **Review & Rating** | **Nhã (BE)** | **User** | **Trung bình** | **1 ngày** |
| **Sprint 3** | **BE-21** | **API lấy danh sách review theo Shop** | **Review & Rating** | **Nhã (BE)** | **Cả hai** | **Trung bình** | **0.5 ngày** |
| **Sprint 3** | **BE-22** | **API khoá / mở khoá quán hoặc người dùng vi phạm** | **Admin cơ bản** | **Nhã (BE)** | **Admin** | **Thấp** | **1 ngày** |
| **Sprint 3** | **BE-23** | **API tìm kiếm / lọc sản phẩm (theo loại, tag dị ứng)** | **Filter & Search** | **Ngân (BE)** | **User** | **Trung bình** | **1 ngày** |
| **Sprint 3** | **BE-24** | **API thống kê đơn hàng / doanh thu theo ngày cho Shop** | **Thống kê Shop** | **Ngân (BE)** | **Shop** | **Thấp** | **1.5 ngày** |
| **Sprint 3** | **BE-25** | **Push notification khi shop đăng Flash Sale mới (Firebase hoặc Zalo OA)** | **Thông báo** | **Khôi (BE)** | **User** | **Trung bình** | **2 ngày** |
| **Sprint 3** | **BE-26** | **Viết unit test các API core (Auth, Order, Product), fix bug Sprint 1-2** | **Fix & Test** | **Khôi (BE)** | **Hệ thống** | **Cao** | **2 ngày** |
| **Sprint 3** | **FE-09** | **Màn hình NightBite Box (chọn loại hộp, hiển thị cảnh báo dị ứng)** | **Night Bite Box FE** | **Thắng (FE)** | **User** | **Trung bình** | **1.5 ngày** |
| **Sprint 3** | **FE-10** | **Màn hình lịch sử đơn hàng \+ form đánh giá shop** | **Lịch sử & Đánh giá** | **Thắng (FE)** | **User** | **Trung bình** | **1.5 ngày** |
| **Sprint 3** | **FE-11** | **Màn hình profile User, filter món theo tag dị ứng** | **Profile & Filter** | **Thắng (FE)** | **User** | **Trung bình** | **1 ngày** |
| **Sprint 3** | **FE-12** | **Responsive UI, loading states, tích hợp Zalo Mini App SDK, đóng gói deploy Zalo Cloud** | **Polish & Deploy** | **Thắng (FE)** | **Hệ thống** | **Cao** | **2 ngày** |

 

**Ghi Chú MVP**

**1\.  Auth trước hay sau?  Auth (JWT) làm ở Sprint 2 – Sprint 1 test Postman không cần token, nhanh hơn và ít bị block nhau.**

**2\.  Nhã setup DB:  Nhã dùng MySQL Workbench thiết kế schema và tạo data mẫu (seed) ngay đầu Sprint 1 để Ngân, Khôi, Thắng đều có data để test.**

**3\.  Swagger bắt buộc:  Khôi setup Swagger từ sớm – Thắng (FE) dựa vào đó để biết endpoint, request/response mà không cần hỏi BE liên tục.**

**4\.  Thanh toán:  Ưu tiên MoMo hoặc ZaloPay. Nếu không kịp thì mock flow (trả về status SUCCESS giả), đánh dấu TODO rõ ràng.**

**5\.  NightBite Box:  Tính năng phụ – chỉ làm sau khi Flash Sale core (đặt hàng, checkout) xong và ổn định.**

**6\.  Deploy:  BE lên Render/Railway free. FE đóng gói lên Zalo Cloud. Khôi lo phần deploy BE, Thắng lo FE.**

# System Design

**🌙 Savibite**

**System Design Document**

Phiên bản MVP  |  EXE101 – FPT University Cần Thơ  |  2026

| Nhóm | EXE101\_G03\_MF |
| :---- | :---- |
| **Tên dự án** | Savibite |
| **Tech Stack** | Spring Boot 3 \+ ReactJS 18 \+ MySQL 8 |
| **Nền tảng** | Zalo Mini App (webview) |

# **1\. Tổng Quan Dự Án**

## **1.1 Mô Tả Sản Phẩm**

NightBite là một Zalo Mini App kết nối các cửa hàng thực phẩm (tiệm bánh, trà sữa, quán ăn) tại Cần Thơ với người tiêu dùng để bán các sản phẩm dư thừa cuối ngày với giá chiết khấu 50–60%, nhằm giảm lãng phí thực phẩm và mang lại giá trị cho cả hai phía.

## **1.2 Mục Tiêu MVP**

* Người dùng có thể xem danh sách Flash Sale từ các shop đối tác

* Người dùng đặt hàng, chọn giờ nhận, thanh toán online

* Shop quản lý sản phẩm, đơn hàng, doanh thu

* Admin giám sát và kiểm soát chất lượng hệ thống

* Trust Score chống boom hàng

## **1.3 Phạm Vi Kỹ Thuật**

| Thành phần | Công nghệ | Ghi chú |
| :---- | :---- | :---- |
| Backend | Java 17 \+ Spring Boot 3.x | REST API, JPA Code First |
| Frontend | ReactJS 18 (pnpm) | Đóng gói lên Zalo Cloud |
| Database | MySQL 8.x | HikariCP connection pool |
| Auth | Spring Security \+ JWT | ROLE\_USER, ROLE\_SHOP, ROLE\_ADMIN |
| Payment | MoMo / ZaloPay (mock) | Fallback mock nếu chưa kịp |
| Notification | Firebase / Zalo OA | Sprint 3 |
| Deploy BE | Render / Railway (free) | Docker tuỳ chọn |
| Deploy FE | Zalo Cloud | Build pnpm → upload |

# **2\. Kiến Trúc Hệ Thống**

## **2.1 Tổng Quan Kiến Trúc**

NightBite theo kiến trúc Client–Server 3 tầng:

* Tầng Presentation: ReactJS chạy trong Zalo Mini App (webview)

* Tầng Application: Spring Boot REST API, xử lý nghiệp vụ

* Tầng Data: MySQL 8 với JPA Code First

## **2.2 Luồng Request Tổng Quan**

`Người dùng (Zalo App)`

   `↓  HTTPS / Axios`

ReactJS (Zalo Cloud)

   `↓  REST API calls`

Spring Boot API Server (Render/Railway)

   ├── Spring Security \+ JWT Filter

   ├── Feature Controllers (auth, shop, product, order, review, admin)

   ├── Feature Services (business logic)

   `└── JPA Repositories → MySQL 8`

## **2.3 Feature Base Architecture (BE)**

Backend áp dụng Feature Base pattern – tổ chức theo nghiệp vụ, không theo lớp kỹ thuật:

nightbite-backend/

├── src/main/java/com/nightbite/

`│   ├── domain/                     # Entity toàn cục (JPA @Entity)`

│   │   ├── User.java

│   │   ├── Shop.java

│   │   ├── Product.java

│   │   ├── NightBiteBox.java

│   │   ├── Order.java

│   │   ├── OrderItem.java

│   │   └── Review.java

│   ├── features/

│   │   ├── auth/                   \# Đăng ký / đăng nhập / JWT

│   │   │   ├── AuthController.java

│   │   │   ├── AuthService.java

│   │   │   ├── LoginRequest.java

│   │   │   ├── RegisterRequest.java

│   │   │   └── AuthResponse.java

`│   │   ├── shop/                   # Quản lý Shop`

│   │   │   ├── ShopController.java

│   │   │   ├── ShopService.java

│   │   │   └── ShopDto.java

│   │   ├── product/                \# Flash Sale & NightBite Box

│   │   │   ├── ProductController.java

│   │   │   ├── ProductService.java

│   │   │   └── ProductDto.java

│   │   ├── order/                  \# Đặt hàng & trạng thái

│   │   │   ├── OrderController.java

│   │   │   ├── OrderService.java

│   │   │   └── OrderDto.java

│   │   ├── review/                 \# Đánh giá

│   │   │   ├── ReviewController.java

│   │   │   ├── ReviewService.java

│   │   │   └── ReviewDto.java

`│   │   └── admin/                  # Khoá/mở khoá, thống kê`

│   │       ├── AdminController.java

│   │       └── AdminService.java

│   ├── shared/

│   │   ├── config/                 \# SecurityConfig, SwaggerConfig, CorsConfig

│   │   ├── exception/              \# GlobalExceptionHandler

│   │   ├── jwt/                    \# JwtUtil, JwtFilter

│   │   └── utils/                  \# ResponseWrapper, DateUtils

│   └── infrastructure/

│       └── persistence/            \# JpaRepository interfaces

## **2.4 Cấu Trúc Frontend (FE)**

nightbite-frontend/

├── public/

│   └── images/                     \# Static assets (logo, placeholder)

├── src/

│   ├── api/                        \# Axios instances & API calls

│   │   ├── axiosConfig.js          \# Base URL, interceptors (JWT)

│   │   ├── authApi.js

│   │   ├── shopApi.js

│   │   ├── productApi.js

│   │   ├── orderApi.js

│   │   └── reviewApi.js

│   ├── pages/

│   │   ├── user/                   \# Home, ProductDetail, Cart, Checkout, History, Profile

│   │   ├── shop/                   \# Dashboard, ProductMgmt, OrderMgmt, Revenue

│   │   ├── auth/                   \# Login, Register

│   │   └── admin/                  \# AdminPanel

│   ├── components/                 \# Reusable: FlashSaleCard, BoxCard, OrderCard...

│   ├── hooks/                      \# useAuth, useCart, useOrder

│   ├── context/                    \# AuthContext, CartContext

│   ├── router/                     \# React Router v6 \+ ProtectedRoute

│   └── styles/                     \# TailwindCSS config \+ global styles

# **3\. Thiết Kế Cơ Sở Dữ Liệu**

## **3.1 Thông Tin Database**

| Thuộc tính | Giá trị |
| :---- | :---- |
| Tên Database | nightbite\_db |
| Engine | MySQL 8.x |
| Character Set | utf8mb4 |
| Collation | utf8mb4\_unicode\_ci |
| ORM | Spring Data JPA (Hibernate) – Code First |
| Connection Pool | HikariCP |
| Tool thiết kế | MySQL Workbench |

## **3.2 Danh Sách Bảng**

| Tên bảng | Mô tả | Ghi chú |
| :---- | :---- | :---- |
| users | Tài khoản người dùng (khách hàng) | ROLE\_USER, trust\_score |
| shops | Tài khoản cửa hàng đối tác | ROLE\_SHOP, is\_active |
| admins | Tài khoản quản trị viên | ROLE\_ADMIN |
| products | Sản phẩm đồ ăn bình thường (Flash Sale) | Giá gốc, giá sale, số lượng |
| nightbite\_boxes | Sản phẩm hộp bí ẩn (Mystery Box) | Loại hộp, giá, cảnh báo dị ứng |
| orders | Đơn hàng của user | Trạng thái: PENDING→CONFIRMED→DONE/CANCELLED |
| order\_items | Chi tiết các item trong đơn hàng | FK → orders, products/boxes |
| reviews | Đánh giá của user sau khi nhận hàng | FK → orders, users, shops |
| payments | Bản ghi giao dịch thanh toán | MoMo/ZaloPay/MOCK |
| notifications | Thông báo push cho user | Sprint 3 |

## **3.3 Chi Tiết Từng Bảng**

### **Bảng: users**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| zalo\_id | VARCHAR(100) | UNIQUE, NOT NULL | Zalo OpenID (nếu dùng Zalo OAuth) |
| full\_name | VARCHAR(150) | NOT NULL | Họ tên người dùng |
| phone | VARCHAR(20) | UNIQUE | Số điện thoại |
| email | VARCHAR(150) | UNIQUE | Email (tuỳ chọn) |
| password\_hash | VARCHAR(255) | NOT NULL | Mật khẩu đã hash (BCrypt) |
| avatar\_url | VARCHAR(500) |  | Đường dẫn ảnh đại diện |
| trust\_score | INT | DEFAULT 100, NOT NULL | Điểm tin cậy (chống boom hàng) |
| is\_active | TINYINT(1) | DEFAULT 1, NOT NULL | 0 \= bị khoá bởi Admin |
| role | ENUM('USER') | DEFAULT 'USER', NOT NULL | Phân quyền |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Ngày tạo tài khoản |
| updated\_at | DATETIME | ON UPDATE CURRENT\_TIMESTAMP | Lần cập nhật cuối |

### **Bảng: shops**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| shop\_name | VARCHAR(200) | NOT NULL | Tên cửa hàng |
| owner\_name | VARCHAR(150) | NOT NULL | Tên chủ cửa hàng |
| phone | VARCHAR(20) | UNIQUE, NOT NULL | Số điện thoại liên hệ |
| email | VARCHAR(150) | UNIQUE | Email đăng nhập |
| password\_hash | VARCHAR(255) | NOT NULL | Mật khẩu đã hash |
| address | TEXT | NOT NULL | Địa chỉ cửa hàng |
| district | VARCHAR(100) |  | Quận/huyện (filter) |
| description | TEXT |  | Mô tả ngắn về shop |
| logo\_url | VARCHAR(500) |  | Ảnh logo shop |
| banner\_url | VARCHAR(500) |  | Ảnh banner shop |
| is\_active | TINYINT(1) | DEFAULT 1, NOT NULL | 0 \= bị khoá bởi Admin |
| rating\_avg | DECIMAL(3,2) | DEFAULT 0.00 | Điểm đánh giá trung bình |
| review\_count | INT | DEFAULT 0 | Tổng số lượt đánh giá |
| role | ENUM('SHOP') | DEFAULT 'SHOP', NOT NULL | Phân quyền |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Ngày tạo |
| updated\_at | DATETIME | ON UPDATE CURRENT\_TIMESTAMP | Cập nhật cuối |

### **Bảng: admins**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| username | VARCHAR(100) | UNIQUE, NOT NULL | Tên đăng nhập admin |
| password\_hash | VARCHAR(255) | NOT NULL | Mật khẩu đã hash |
| full\_name | VARCHAR(150) | NOT NULL | Họ tên admin |
| email | VARCHAR(150) | UNIQUE, NOT NULL | Email admin |
| role | ENUM('ADMIN') | DEFAULT 'ADMIN', NOT NULL | Phân quyền |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Ngày tạo |

### **Bảng: products  (Sản phẩm đồ ăn bình thường – Flash Sale)**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| shop\_id | BIGINT | FK → shops.id, NOT NULL | Shop sở hữu sản phẩm |
| name | VARCHAR(200) | NOT NULL | Tên sản phẩm |
| description | TEXT |  | Mô tả chi tiết |
| image\_url | VARCHAR(500) |  | Ảnh sản phẩm (đường dẫn tương đối) |
| original\_price | DECIMAL(12,2) | NOT NULL | Giá gốc (VNĐ) |
| sale\_price | DECIMAL(12,2) | NOT NULL | Giá sale cuối ngày |
| quantity\_available | INT | NOT NULL, DEFAULT 0 | Số lượng còn lại |
| quantity\_sold | INT | DEFAULT 0 | Số lượng đã bán |
| sale\_start\_time | TIME |  | Giờ bắt đầu flash sale |
| sale\_end\_time | TIME |  | Giờ kết thúc flash sale |
| sale\_date | DATE |  | Ngày áp dụng sale |
| allergen\_tags | VARCHAR(500) |  | Tag dị ứng (JSON array hoặc CSV) |
| category | VARCHAR(100) |  | Loại: bánh, trà sữa, đồ ăn... |
| is\_active | TINYINT(1) | DEFAULT 1 | Đang bán hay ẩn |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Ngày tạo |
| updated\_at | DATETIME | ON UPDATE CURRENT\_TIMESTAMP | Cập nhật cuối |

### **Bảng: nightbite\_boxes  (Sản phẩm hộp bí ẩn – Mystery Box)**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| shop\_id | BIGINT | FK → shops.id, NOT NULL | Shop tạo hộp |
| box\_name | VARCHAR(200) | NOT NULL | Tên hộp (VD: 'Hộp Bánh Bí Ẩn') |
| box\_type | ENUM('SMALL','MEDIUM','LARGE') | NOT NULL | Loại hộp theo kích thước |
| description | TEXT |  | Mô tả nội dung hộp (chung chung) |
| image\_url | VARCHAR(500) |  | Ảnh minh hoạ |
| price | DECIMAL(12,2) | NOT NULL | Giá hộp |
| quantity\_available | INT | NOT NULL, DEFAULT 0 | Số hộp còn |
| allergen\_warning | TEXT |  | Cảnh báo dị ứng tổng quát |
| sale\_date | DATE |  | Ngày áp dụng |
| is\_active | TINYINT(1) | DEFAULT 1 | Đang bán hay ẩn |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Ngày tạo |

### **Bảng: orders**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| user\_id | BIGINT | FK → users.id, NOT NULL | Người đặt hàng |
| shop\_id | BIGINT | FK → shops.id, NOT NULL | Shop nhận đơn |
| order\_code | VARCHAR(50) | UNIQUE, NOT NULL | Mã đơn hàng hiển thị (NB-YYYYMMDD-XXXXX) |
| status | ENUM('PENDING','CONFIRMED','READY','DONE','CANCELLED') | NOT NULL, DEFAULT 'PENDING' | Trạng thái đơn |
| total\_amount | DECIMAL(12,2) | NOT NULL | Tổng tiền đơn hàng |
| pickup\_time | DATETIME |  | Giờ khách dự kiến đến lấy |
| note | TEXT |  | Ghi chú của khách |
| payment\_method | ENUM('MOMO','ZALOPAY','CASH','MOCK') | DEFAULT 'MOCK' | Phương thức thanh toán |
| payment\_status | ENUM('PENDING','PAID','FAILED','REFUNDED') | DEFAULT 'PENDING' | Trạng thái thanh toán |
| cancel\_reason | TEXT |  | Lý do huỷ (nếu bị huỷ) |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Ngày đặt |
| updated\_at | DATETIME | ON UPDATE CURRENT\_TIMESTAMP | Cập nhật cuối |

### **Bảng: order\_items**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| order\_id | BIGINT | FK → orders.id, NOT NULL | Thuộc đơn hàng nào |
| item\_type | ENUM('PRODUCT','BOX') | NOT NULL | Loại item |
| product\_id | BIGINT | FK → products.id, NULL | Nếu là Flash Sale product |
| box\_id | BIGINT | FK → nightbite\_boxes.id, NULL | Nếu là NightBite Box |
| item\_name | VARCHAR(200) | NOT NULL | Snapshot tên tại lúc đặt |
| unit\_price | DECIMAL(12,2) | NOT NULL | Snapshot giá tại lúc đặt |
| quantity | INT | NOT NULL, DEFAULT 1 | Số lượng |
| subtotal | DECIMAL(12,2) | NOT NULL | unit\_price × quantity |

### **Bảng: reviews**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| order\_id | BIGINT | FK → orders.id, UNIQUE | 1 đơn chỉ đánh giá 1 lần |
| user\_id | BIGINT | FK → users.id, NOT NULL | Người đánh giá |
| shop\_id | BIGINT | FK → shops.id, NOT NULL | Shop được đánh giá |
| rating | TINYINT | NOT NULL, CHECK 1-5 | Điểm sao (1–5) |
| comment | TEXT |  | Nội dung nhận xét |
| image\_urls | TEXT |  | Ảnh minh chứng (JSON array đường dẫn) |
| is\_visible | TINYINT(1) | DEFAULT 1 | 0 \= Admin ẩn vi phạm |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Ngày đánh giá |

### **Bảng: payments**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| order\_id | BIGINT | FK → orders.id, NOT NULL | Đơn hàng liên quan |
| payment\_method | ENUM('MOMO','ZALOPAY','CASH','MOCK') | NOT NULL | Cổng thanh toán |
| transaction\_id | VARCHAR(200) | UNIQUE | Mã giao dịch từ cổng thanh toán |
| amount | DECIMAL(12,2) | NOT NULL | Số tiền giao dịch |
| status | ENUM('PENDING','SUCCESS','FAILED','REFUNDED') | NOT NULL | Trạng thái giao dịch |
| raw\_response | TEXT |  | JSON response từ cổng (debug) |
| paid\_at | DATETIME |  | Thời điểm thanh toán thành công |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Ngày tạo bản ghi |

### **Bảng: notifications**

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :---- | :---- | :---- | :---- |
| id | BIGINT | PK, AUTO\_INCREMENT | Khoá chính |
| user\_id | BIGINT | FK → users.id, NULL | Nếu gửi cho user cụ thể |
| type | ENUM('FLASH\_SALE','ORDER\_UPDATE','SYSTEM') | NOT NULL | Loại thông báo |
| title | VARCHAR(200) | NOT NULL | Tiêu đề thông báo |
| body | TEXT | NOT NULL | Nội dung |
| is\_read | TINYINT(1) | DEFAULT 0 | Đã đọc chưa |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | Thời điểm tạo |

# **4\. Thiết Kế API**

## **4.1 Base URL & Conventions**

* Base URL: https://nightbite-api.onrender.com/savibite

* Content-Type: application/json

* Authentication: Bearer Token (JWT) trong header Authorization

* Response wrapper: { success, message, data, timestamp }

## **4.2 Auth API  (/savibite/auth)**

| Method | Endpoint | Phân quyền | Mô tả |
| :---- | :---- | :---- | :---- |
| POST | /auth/register/user | Public | Đăng ký tài khoản User |
| POST | /auth/register/shop | Public | Đăng ký tài khoản Shop |
| POST | /auth/login | Public | Đăng nhập (User/Shop/Admin) |
| POST | /auth/refresh | Public | Làm mới Access Token |
| GET | /auth/me | User/Shop/Admin | Lấy thông tin tài khoản hiện tại |

## **4.3 Shop API  (/savibite/shops)**

| Method | Endpoint | Phân quyền | Mô tả |
| :---- | :---- | :---- | :---- |
| GET | /shops | Public | Danh sách shops (filter theo district) |
| GET | /shops/{id} | Public | Chi tiết 1 shop |
| POST | /shops | Shop | Tạo/cập nhật thông tin shop (BE-03) |
| PUT | /shops/{id} | Shop | Cập nhật shop |
| GET | /shops/{id}/products | Public | Danh sách sản phẩm đang sale của shop |
| GET | /shops/{id}/reviews | Public | Danh sách review của shop (BE-21) |
| GET | /shops/{id}/stats | Shop | Thống kê đơn hàng, doanh thu (BE-24) |

## **4.4 Product API  (/savibite/products)**

| Method | Endpoint | Phân quyền | Mô tả |
| :---- | :---- | :---- | :---- |
| GET | /products | Public | Danh sách Flash Sale (filter giờ, shop, category) |
| GET | /products/{id} | Public | Chi tiết sản phẩm |
| POST | /products | Shop | Tạo sản phẩm Flash Sale (BE-05) |
| PUT | /products/{id} | Shop | Cập nhật sản phẩm (BE-06) |
| DELETE | /products/{id} | Shop | Xoá sản phẩm (BE-06) |
| GET | /products/search | Public | Tìm kiếm theo tag, loại (BE-23) |

## **4.5 NightBite Box API  (/savibite/boxes)**

| Method | Endpoint | Phân quyền | Mô tả |
| :---- | :---- | :---- | :---- |
| GET | /boxes | Public | Danh sách hộp đang mở bán |
| GET | /boxes/{id} | Public | Chi tiết hộp |
| POST | /boxes | Shop | Tạo NightBite Box (BE-14) |
| PUT | /boxes/{id} | Shop | Cập nhật hộp |
| DELETE | /boxes/{id} | Shop | Xoá hộp |

## **4.6 Order API  (/savibite/orders)**

| Method | Endpoint | Phân quyền | Mô tả |
| :---- | :---- | :---- | :---- |
| POST | /orders | User | Tạo đơn hàng mới (BE-11) |
| GET | /orders | User/Shop | Lịch sử đơn hàng (filter theo role) |
| GET | /orders/{id} | User/Shop | Chi tiết 1 đơn hàng |
| PATCH | /orders/{id}/status | Shop | Cập nhật trạng thái đơn (BE-13) |
| PATCH | /orders/{id}/cancel | User | User huỷ đơn (giảm trust score) |

## **4.7 Payment API  (/savibite/payments)**

| Method | Endpoint | Phân quyền | Mô tả |
| :---- | :---- | :---- | :---- |
| POST | /payments/initiate | User | Khởi tạo giao dịch thanh toán (BE-16) |
| POST | /payments/callback/momo | System | Webhook nhận kết quả từ MoMo |
| POST | /payments/callback/zalopay | System | Webhook nhận kết quả từ ZaloPay |
| GET | /payments/{orderId} | User | Kiểm tra trạng thái thanh toán |

## **4.8 Review API  (/savibite/reviews)**

| Method | Endpoint | Phân quyền | Mô tả |
| :---- | :---- | :---- | :---- |
| POST | /reviews | User | Gửi đánh giá sau nhận hàng (BE-20) |
| GET | /reviews/shop/{shopId} | Public | Danh sách review của shop (BE-21) |
| DELETE | /reviews/{id} | Admin | Admin ẩn review vi phạm |

## **4.9 Admin API  (/savibite/admin)**

| Method | Endpoint | Phân quyền | Mô tả |
| :---- | :---- | :---- | :---- |
| GET | /admin/users | Admin | Danh sách tất cả users |
| GET | /admin/shops | Admin | Danh sách tất cả shops |
| PATCH | /admin/users/{id}/lock | Admin | Khoá/mở khoá user (BE-22) |
| PATCH | /admin/shops/{id}/lock | Admin | Khoá/mở khoá shop (BE-22) |
| GET | /admin/dashboard | Admin | Thống kê tổng quan hệ thống |

# **5\. Xác Thực & Bảo Mật**

## **5.1 Chiến Lược Auth (Theo Feature Base)**

Áp dụng hướng giải quyết tối ưu từ Feature Base: Auth tối giản song song với core, không làm trước hoàn toàn cũng không để quá muộn.

| Sprint | Hành động Auth | Mục đích |
| :---- | :---- | :---- |
| Sprint 1 | Test API tự do qua Postman, không cần token | Tránh block nhau, tập trung core |
| Sprint 2 – đầu | Tạo bảng users/shops, endpoint /login trả JWT | Có danh tính để phân cấp dữ liệu |
| Sprint 2 – giữa | Gắn userId từ JWT vào Service logic (filter dữ liệu) | Orders, cart đúng của ai |
| Sprint 2 – cuối | @PreAuthorize vào các route, ROLE\_USER/SHOP/ADMIN | Bảo vệ hoàn chỉnh |
| Sprint 3 | Refresh token, xử lý edge cases, unit test | Production-ready |

## **5.2 Cấu Hình JWT**

* Algorithm: HS256

* Access Token TTL: 24 giờ (có thể điều chỉnh)

* Refresh Token TTL: 7 ngày

* Payload: { sub: userId, role: ROLE\_X, iat, exp }

* Secret: lưu trong application.properties hoặc environment variable

## **5.3 Phân Quyền**

| Role | Endpoints được phép | Cấu hình Spring Security |
| :---- | :---- | :---- |
| ROLE\_USER | Xem Flash Sale, đặt hàng, thanh toán, đánh giá | @PreAuthorize("hasRole('USER')") |
| ROLE\_SHOP | CRUD sản phẩm, xác nhận đơn, xem doanh thu | @PreAuthorize("hasRole('SHOP')") |
| ROLE\_ADMIN | Khoá shop/user, xem toàn bộ dữ liệu | @PreAuthorize("hasRole('ADMIN')") |
| Public | Danh sách shops, danh sách Flash Sale | permitAll() |

# **6\. Kế Hoạch Sprint**

## **6.1 Sprint 1 – Nền Tảng Core (Tuần 1–2)**

| ID | Task | Người làm | Ưu tiên | Ước tính |
| :---- | :---- | :---- | :---- | :---- |
| BE-01 | Thiết kế schema DB (nightbite\_db) – 9 bảng MySQL Workbench | Nhã | Cao | 1 ngày |
| BE-02 | Seed data: 3–5 shop, 10–15 sản phẩm, 5 user test | Nhã | Cao | 0.5 ngày |
| BE-03 | API tạo/cập nhật Shop | Nhã | Cao | 1 ngày |
| BE-04 | API lấy danh sách Shop (filter district) | Nhã | Cao | 1 ngày |
| BE-05 | API tạo sản phẩm Flash Sale | Ngân | Cao | 2 ngày |
| BE-06 | API cập nhật/xoá sản phẩm Flash Sale | Ngân | Cao | 1 ngày |
| BE-07 | API danh sách sản phẩm đang sale | Ngân | Cao | 1 ngày |
| BE-08 | Setup Spring Boot 3.x, JPA Code First, MySQL | Khôi | Cao | 1 ngày |
| BE-09 | Cấu hình Swagger/OpenAPI | Khôi | Cao | 0.5 ngày |
| BE-10 | Deploy BE lên Render/Railway | Khôi | TB | 1 ngày |
| FE-01 | Setup ReactJS (pnpm), Axios, routing | Thắng | Cao | 1 ngày |
| FE-02 | Màn hình Home – Flash Sale list | Thắng | Cao | 2 ngày |
| FE-03 | Màn hình chi tiết sản phẩm | Thắng | Cao | 1 ngày |
| FE-04 | Màn hình quản lý Flash Sale cho Shop | Thắng | Cao | 2 ngày |

## **6.2 Sprint 2 – Order Flow \+ Auth (Tuần 2–3)**

| ID | Task | Người làm | Ưu tiên | Ước tính |
| :---- | :---- | :---- | :---- | :---- |
| BE-11 | API tạo đơn hàng (Flash Sale) | Nhã | Cao | 2 ngày |
| BE-12 | API lịch sử đơn hàng (User & Shop) | Nhã | Cao | 1 ngày |
| BE-13 | API cập nhật trạng thái đơn hàng | Nhã | Cao | 1 ngày |
| BE-14 | API tạo NightBite Box | Ngân | TB | 2 ngày |
| BE-15 | API đặt NightBite Box | Ngân | TB | 1 ngày |
| BE-16 | Tích hợp cổng thanh toán MoMo/ZaloPay | Ngân | Cao | 2 ngày |
| BE-17 | Auth JWT \+ Register/Login User & Shop | Khôi | Cao | 2 ngày |
| BE-18 | Spring Security filter \+ @PreAuthorize | Khôi | Cao | 1 ngày |
| BE-19 | Logic Trust Score (boom hàng → giảm điểm) | Khôi | TB | 1 ngày |
| FE-05 | Màn hình giỏ hàng \+ checkout \+ chọn giờ nhận | Thắng | Cao | 2 ngày |
| FE-06 | Màn hình thanh toán (tích hợp cổng) | Thắng | Cao | 1 ngày |
| FE-07 | Màn hình Login/Register User \+ JWT interceptor | Thắng | Cao | 1 ngày |
| FE-08 | Màn hình Login/Register Shop \+ route guard | Thắng | Cao | 1 ngày |

## **6.3 Sprint 3 – Polish & Deploy (Tuần 3–4)**

| ID | Task | Người làm | Ưu tiên | Ước tính |
| :---- | :---- | :---- | :---- | :---- |
| BE-20 | API gửi đánh giá sau nhận hàng | Nhã | TB | 1 ngày |
| BE-21 | API danh sách review theo Shop | Nhã | TB | 0.5 ngày |
| BE-22 | API khoá/mở khoá shop, user vi phạm (Admin) | Nhã | Thấp | 1 ngày |
| BE-23 | API tìm kiếm/lọc sản phẩm (tag dị ứng, loại) | Ngân | TB | 1 ngày |
| BE-24 | API thống kê đơn hàng/doanh thu theo ngày | Ngân | Thấp | 1.5 ngày |
| BE-25 | Push notification Flash Sale mới (Firebase/Zalo OA) | Khôi | TB | 2 ngày |
| BE-26 | Unit test API core \+ fix bug Sprint 1–2 | Khôi | Cao | 2 ngày |
| FE-09 | Màn hình NightBite Box (chọn hộp, cảnh báo dị ứng) | Thắng | TB | 1.5 ngày |
| FE-10 | Màn hình lịch sử đơn \+ form đánh giá shop | Thắng | TB | 1.5 ngày |
| FE-11 | Màn hình profile User, filter món theo tag dị ứng | Thắng | TB | 1 ngày |
| FE-12 | Responsive UI, Zalo Mini App SDK, đóng gói deploy Zalo Cloud | Thắng | Cao | 2 ngày |

# **7\. Quy Ước Code & Tiêu Chuẩn**

## **7.1 Backend (Java/Spring Boot)**

* Package naming: com.nightbite.features.{featureName}

* Class naming: PascalCase (OrderController, ProductService)

* Method naming: camelCase (createOrder, getProductsByShop)

* DTO: suffix Dto hoặc Request/Response (CreateOrderRequest, OrderResponse)

* Exception: suffix Exception (OrderNotFoundException, InsufficientStockException)

* Tất cả API phải có Swagger annotation (@Operation, @ApiResponse)

* Sử dụng Lombok (@Data, @Builder, @RequiredArgsConstructor)

* Response wrapper: dùng class ApiResponse\<T\> { boolean success; String message; T data; }

## **7.2 Frontend (ReactJS)**

* Component naming: PascalCase (FlashSaleCard, OrderHistoryPage)

* Hook naming: camelCase với prefix use (useAuth, useCart)

* API call: tập trung trong /src/api/, không gọi trực tiếp trong component

* Ảnh sản phẩm: lưu đường dẫn tương đối trong DB, ghép BASE\_URL khi hiển thị

* Không commit ảnh lớn (\>500KB) trực tiếp lên Git; dùng link hoặc đường dẫn tĩnh

## **7.3 Database**

* Tên bảng: snake\_case số nhiều (users, order\_items, nightbite\_boxes)

* Tên cột: snake\_case (created\_at, shop\_id, trust\_score)

* FK: {table\_name}\_id (shop\_id, user\_id, order\_id)

* Enum: UPPER\_CASE cho giá trị (PENDING, CONFIRMED, DONE)

* Không dùng reserved words làm tên cột (status, name là ok; order, table là không)

## **7.4 Git Workflow**

* Branch: main (production) / develop / feature/{task-id}-{description}

* Ví dụ: feature/BE-05-flash-sale-api, feature/FE-02-home-screen

* Commit message: \[BE-05\] Add Flash Sale product creation API

* Pull Request bắt buộc review trước khi merge vào develop

# **8\. Quản Lý Tài Nguyên Tĩnh**

## **8.1 Chiến Lược Cho MVP**

Dự án NightBite MVP thuộc loại nhỏ (\< 100 ảnh tĩnh), áp dụng phương pháp lưu ảnh trong project FE \+ đường dẫn tương đối trong DB.

## **8.2 Cấu Trúc Thư Mục Ảnh**

nightbite-frontend/public/images/

├── logo/                  \# Logo NightBite, logo shop mặc định

`├── products/              # Ảnh sản phẩm (format: {shop_id}_{product_id}.jpg)`

`├── boxes/                 # Ảnh minh hoạ NightBite Box`

`├── shops/                 # Ảnh logo, banner shop`

`└── placeholders/          # Ảnh placeholder khi chưa có ảnh thật`

## **8.3 Quy Tắc**

* DB lưu đường dẫn tương đối: /images/products/1\_101.jpg

* FE ghép URL: process.env.REACT\_APP\_STATIC\_BASE \+ imagePath

* Tên file: không dấu, không space, dùng gạch dưới (san\_pham\_001.jpg)

* Kích thước tối đa: 500KB/ảnh sản phẩm, 200KB/logo

* Format ưu tiên: WebP (nhanh) \> JPEG (tương thích)

# **9\. Triển Khai (Deployment)**

## **9.1 Backend Deploy**

| Bước | Mô tả | Công cụ |
| :---- | :---- | :---- |
| 1 | Cấu hình application.properties (DB URL, JWT secret từ env vars) | Spring Boot |
| 2 | Build artifact: mvn clean package \-DskipTests | Maven |
| 3 | Tạo tài khoản Render.com hoặc Railway.app | Render / Railway |
| 4 | Kết nối GitHub repo, chọn branch deploy (main) | GitHub |
| 5 | Cấu hình environment variables (DB\_URL, DB\_USER, DB\_PASS, JWT\_SECRET) | Render dashboard |
| 6 | Render tự động build & deploy khi push code | Render CI/CD |
| 7 | Kiểm tra logs, test API qua Postman | Postman |

## **9.2 Frontend Deploy (Zalo Cloud)**

| Bước | Mô tả | Công cụ |
| :---- | :---- | :---- |
| 1 | Cấu hình .env: REACT\_APP\_API\_BASE\_URL=https://nightbite-api.onrender.com/savibite | .env file |
| 2 | Build: pnpm run build | pnpm |
| 3 | Đóng gói theo cấu trúc Zalo Mini App (app.json, pages config) | Zalo CLI / Zalo Cloud |
| 4 | Upload lên Zalo Developer Console | Zalo Developer Console |
| 5 | Zalo review & duyệt app | Quy trình Zalo |
| 6 | Người dùng tìm 'NightBite' trên Zalo hoặc quét QR | Zalo App |

# **10\. Phân Công Thành Viên**

| Thành viên | MSSV | Vai trò | Trách nhiệm chính |
| :---- | :---- | :---- | :---- |
| Nguyễn Thanh Nhã | CE181298 | CEO/Leader & BE | DB schema, Shop API, Order API, Review API, Admin API |
| Nguyễn Hoàng Khả Ngân | CE180540 | BE | Product Flash Sale API, NightBite Box API, Payment, Filter/Search, Stats |
| Trần Huỳnh Khôi | CE190890 | BE | Spring Boot setup, Swagger, Deploy BE, Auth JWT, Security, Trust Score, Notification, Test |
| Cao Toàn Thắng | CE180873 | FE | Toàn bộ ReactJS: Home, Detail, Cart, Checkout, Payment, Auth, Box, History, Profile, Deploy Zalo |
| Lý Thị Trâm | CS191044 | CMO | Marketing, content, onboard shop đối tác |
| Phạm Minh Hiếu | CS190027 | Media Lead | Demo video, slide, hình ảnh sản phẩm mẫu |

# **11\. Checklist Trước Review Ngày 25/06**

## **11.1 Backend**

* Spring Boot project chạy được, kết nối MySQL thành công

* Tất cả 9 bảng được tạo tự động bởi JPA (Code First)

* Seed data: 3–5 shop, 10–15 sản phẩm, 5 user test

* Swagger UI accessible tại /swagger-ui.html

* API Flash Sale: GET /products, GET /products/{id}

* API Shop: GET /shops, POST /shops, GET /shops/{id}

* API Order: POST /orders, GET /orders, PATCH /orders/{id}/status

* Auth JWT: POST /auth/login, POST /auth/register/user

* Deploy BE lên Render/Railway, URL public hoạt động

## **11.2 Frontend**

* ReactJS project chạy được: pnpm dev

* Màn hình Home hiển thị danh sách Flash Sale từ API thật

* Màn hình chi tiết sản phẩm: ảnh, giá, tag dị ứng

* Màn hình Shop Dashboard: thêm/sửa/xoá sản phẩm

* Màn hình Checkout: chọn sản phẩm, chọn giờ lấy

* Màn hình Login/Register hoạt động với JWT

## **11.3 Integration**

* FE gọi được API BE đã deploy (không bị CORS)

* JWT được lưu localStorage/Context và đính kèm Axios interceptor

* Tất cả API đã test qua Postman, không lỗi 500

*— Hết tài liệu —*

