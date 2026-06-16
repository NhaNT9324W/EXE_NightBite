2.3 Feature Base Architecture (BE)
Backend áp dụng Feature Base pattern – tổ chức theo nghiệp vụ, không theo lớp kỹ thuật:
nightbite-backend/
├── src/main/java/com/nightbite/
│   ├── domain/                     # Entity toàn cục (JPA @Entity)
│   │   ├── User.java
│   │   ├── Shop.java
│   │   ├── Product.java
│   │   ├── NightBiteBox.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   └── Review.java
│   ├── features/
│   │   ├── auth/                   # Đăng ký / đăng nhập / JWT
│   │   │   ├── AuthController.java
│   │   │   ├── AuthService.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   └── AuthResponse.java
│   │   ├── shop/                   # Quản lý Shop
│   │   │   ├── ShopController.java
│   │   │   ├── ShopService.java
│   │   │   └── ShopDto.java
│   │   ├── product/                # Flash Sale & NightBite Box
│   │   │   ├── ProductController.java
│   │   │   ├── ProductService.java
│   │   │   └── ProductDto.java
│   │   ├── order/                  # Đặt hàng & trạng thái
│   │   │   ├── OrderController.java
│   │   │   ├── OrderService.java
│   │   │   └── OrderDto.java
│   │   ├── review/                 # Đánh giá
│   │   │   ├── ReviewController.java
│   │   │   ├── ReviewService.java
│   │   │   └── ReviewDto.java
│   │   └── admin/                  # Khoá/mở khoá, thống kê
│   │       ├── AdminController.java
│   │       └── AdminService.java
│   ├── shared/
│   │   ├── config/                 # SecurityConfig, SwaggerConfig, CorsConfig
│   │   ├── exception/              # GlobalExceptionHandler
│   │   ├── jwt/                    # JwtUtil, JwtFilter
│   │   └── utils/                  # ResponseWrapper, DateUtils
│   └── infrastructure/
│       └── persistence/            # JpaRepository interfaces

2.4 Cấu Trúc Frontend (FE)
nightbite-frontend/
├── public/
│   └── images/                     # Static assets (logo, placeholder)
├── src/
│   ├── api/                        # Axios instances & API calls
│   │   ├── axiosConfig.js          # Base URL, interceptors (JWT)
│   │   ├── authApi.js
│   │   ├── shopApi.js
│   │   ├── productApi.js
│   │   ├── orderApi.js
│   │   └── reviewApi.js
│   ├── pages/
│   │   ├── user/                   # Home, ProductDetail, Cart, Checkout, History, Profile
│   │   ├── shop/                   # Dashboard, ProductMgmt, OrderMgmt, Revenue
│   │   ├── auth/                   # Login, Register
│   │   └── admin/                  # AdminPanel
│   ├── components/                 # Reusable: FlashSaleCard, BoxCard, OrderCard...
│   ├── hooks/                      # useAuth, useCart, useOrder
│   ├── context/                    # AuthContext, CartContext
│   ├── router/                     # React Router v6 + ProtectedRoute
│   └── styles/                     # TailwindCSS config + global styles

