# API Documentation - Phần Backend của Ngân (BE)

## Tổng Quan
Ngân (BE) chịu trách nhiệm phát triển các API chính cho các task từ Sprint 1, 2, 3:
- **Sprint 1**: BE-05, BE-06, BE-07 (Product Flash Sale)
- **Sprint 2**: BE-14, BE-15, BE-16 (NightBite Box + Payment)
- **Sprint 3**: BE-23, BE-24 (Search/Filter + Statistics)

---

## 📦 Sprint 1: Product Flash Sale API

### Base URL: `/savibite/products`

#### **BE-05: Tạo sản phẩm Flash Sale**
```http
POST /savibite/products
Content-Type: application/json

{
  "shopId": 1,
  "name": "Bánh mì nước sốt",
  "description": "Bánh mì tươi ngon",
  "imageUrl": "https://...",
  "originalPrice": 50000,
  "salePrice": 35000,
  "quantityAvailable": 50,
  "saleStartTime": "18:00:00",
  "saleEndTime": "21:00:00",
  "saleDate": "2026-06-20",
  "allergenTags": "Peanut, Gluten",
  "category": "Bakery"
}
```

#### **BE-06: Cập nhật sản phẩm Flash Sale**
```http
PUT /savibite/products/{id}
```

#### **BE-06: Xóa sản phẩm Flash Sale (Soft Delete)**
```http
DELETE /savibite/products/{id}
```

#### **BE-07: Lấy danh sách sản phẩm đang sale hôm nay**
```http
GET /savibite/products/sale/active
```

#### **BE-07: Lấy sản phẩm của shop**
```http
GET /savibite/products/shop/{shopId}
```

#### **BE-07: Lấy chi tiết sản phẩm**
```http
GET /savibite/products/{id}
```

#### **BE-23: Tìm kiếm theo category**
```http
GET /savibite/products/search/category?category=Bakery
```

#### **BE-23: Lọc theo dị ứng (allergen)**
```http
GET /savibite/products/filter/allergen?allergen=Peanut
```

---

## 🎁 Sprint 2: NightBite Box API

### Base URL: `/savibite/nightbite-boxes`

#### **BE-14: Tạo NightBite Box**
```http
POST /savibite/nightbite-boxes
Content-Type: application/json

{
  "shopId": 1,
  "boxName": "Box Bí Mật - Tối",
  "boxType": "LARGE",
  "description": "Hộp bí mật đầy sơ prises",
  "imageUrl": "https://...",
  "price": 150000,
  "quantityAvailable": 20,
  "allergenWarning": "Có chứa Dairy, Gluten",
  "saleDate": "2026-06-20"
}
```

#### **BE-14: Cập nhật NightBite Box**
```http
PUT /savibite/nightbite-boxes/{id}
```

#### **BE-14: Xóa NightBite Box (Soft Delete)**
```http
DELETE /savibite/nightbite-boxes/{id}
```

#### **BE-15: Lấy chi tiết NightBite Box**
```http
GET /savibite/nightbite-boxes/{id}
```

#### **BE-15: Lấy Box của shop**
```http
GET /savibite/nightbite-boxes/shop/{shopId}
```

#### **BE-15: Lấy Box đang bán hôm nay**
```http
GET /savibite/nightbite-boxes/sale/active
```

#### **BE-15: Lấy Box theo loại**
```http
GET /savibite/nightbite-boxes/type/{boxType}
// boxType: SMALL, MEDIUM, LARGE
```

#### **BE-15: Đặt NightBite Box**
```http
POST /savibite/orders/nightbite-box
Content-Type: application/json

{
  "userId": 1,
  "shopId": 1,
  "nightBiteBoxId": 1,
  "quantity": 2,
  "pickupTime": "2026-06-20T19:00:00",
  "note": "Ưu tiên hình dạng, ko quan tâm vị"
}
```

---

## 💳 Sprint 2: Payment Integration API

### Base URL: `/savibite/payments`

#### **BE-16: Tạo yêu cầu thanh toán**
```http
POST /savibite/payments
Content-Type: application/json

{
  "orderId": 1,
  "userId": 1,
  "amount": 300000,
  "paymentMethod": "MOMO",  // CASH, MOMO, ZALOPAY
  "returnUrl": "https://yourapp.com/payment-success",
  "description": "Thanh toán đơn hàng NB-20260620-ABC12"
}

Response:
{
  "success": true,
  "message": "Tạo yêu cầu thanh toán thành công!",
  "data": {
    "id": 1,
    "orderId": 1,
    "userId": 1,
    "amount": 300000,
    "paymentMethod": "MOMO",
    "status": "PENDING",
    "transactionId": "uuid-xxx",
    "paymentUrl": "https://test-payment.momo.vn/...",
    "message": "Vui lòng hoàn tất thanh toán"
  }
}
```

#### **BE-16: Xác nhận thanh toán hoàn tất**
```http
POST /savibite/payments/{orderId}/confirm
```

#### **BE-16: Hủy thanh toán / Hoàn lại**
```http
POST /savibite/payments/{orderId}/cancel
```

#### **BE-16: Kiểm tra trạng thái thanh toán**
```http
GET /savibite/payments/{orderId}/status
```

#### **BE-16: Webhook MoMo (Callback)**
```http
POST /savibite/payments/webhook/momo
```

#### **BE-16: Webhook ZaloPay (Callback)**
```http
POST /savibite/payments/webhook/zalopay
```

---

## 📊 Sprint 3: Statistics & Reporting API

### Base URL: `/savibite/statistics`

#### **BE-24: Lấy thống kê doanh thu của shop**
```http
GET /savibite/statistics/shop/{shopId}?period=THIS_MONTH

Query Parameters:
- period: TODAY, THIS_WEEK, THIS_MONTH, ALL_TIME

Response:
{
  "success": true,
  "message": "Lấy thống kê doanh thu shop thành công!",
  "data": {
    "shopId": 1,
    "shopName": "NightBite Saigon",
    "totalRevenue": 15000000,
    "totalOrders": 150,
    "completedOrders": 145,
    "cancelledOrders": 5,
    "productRevenue": 8000000,
    "boxRevenue": 7000000,
    "cashRevenue": 5000000,
    "momoRevenue": 6000000,
    "zaloPayRevenue": 4000000,
    "revenueByDate": {
      "2026-06-15": 500000,
      "2026-06-16": 800000,
      "2026-06-17": 700000,
      ...
    },
    "period": "THIS_MONTH",
    "fromDate": "2026-06-01",
    "toDate": "2026-06-30",
    "generatedAt": 1687000000000
  }
}
```

#### **BE-24: Lấy thống kê doanh thu toàn hệ thống (Admin)**
```http
GET /savibite/statistics/system?period=THIS_MONTH
```

---

## 📝 Các Enums được sử dụng

### PaymentMethod
- CASH (Tiền mặt)
- MOMO (Ví MoMo)
- ZALOPAY (Ví ZaloPay)

### PaymentStatus
- PENDING (Chờ thanh toán)
- PAID (Đã thanh toán)
- FAILED (Thất bại)
- REFUNDED (Đã hoàn tiền)

### OrderStatus
- PENDING (Chờ xác nhận)
- CONFIRMED (Đã xác nhận)
- READY (Đã sẵn sàng)
- DONE (Hoàn thành)
- CANCELLED (Đã hủy)

### ItemType
- PRODUCT (Sản phẩm Flash Sale)
- BOX (NightBite Box)

### BoxType
- SMALL (Hộp nhỏ)
- MEDIUM (Hộp vừa)
- LARGE (Hộp lớn)

---

## 📂 Cấu trúc File

```
src/main/java/com/nightbite/
├── features/
│   ├── product/
│   │   ├── ProductController.java
│   │   ├── ProductService.java
│   │   └── dto/
│   │       ├── ProductRequest.java
│   │       └── ProductResponse.java
│   ├── nightbitebox/
│   │   ├── NightBiteBoxController.java
│   │   ├── NightBiteBoxService.java
│   │   └── dto/
│   │       ├── NightBiteBoxRequest.java
│   │       ├── NightBiteBoxResponse.java
│   │       └── NightBiteBoxOrderRequest.java
│   ├── payment/
│   │   ├── PaymentController.java
│   │   ├── PaymentService.java
│   │   └── dto/
│   │       ├── PaymentRequest.java
│   │       └── PaymentResponse.java
│   ├── statistics/
│   │   ├── StatisticsController.java
│   │   ├── StatisticsService.java
│   │   └── dto/
│   │       └── RevenueStatisticsResponse.java
│   └── order/ (được Nhã phát triển, Ngân bổ sung)
│       ├── OrderController.java
│       ├── OrderService.java (thêm method BE-15)
│       └── dto/
│           ├── OrderResponse.java
│           └── NightBiteBoxOrderRequest.java
└── infrastructure/
    └── persistence/
        ├── ProductRepository.java (cập nhật query methods)
        ├── NightBiteBoxRepository.java (cập nhật query methods)
        └── OrderRepository.java
```

---

## 🔗 Database Relationships

```
User (1) -----> (N) Order
Shop (1) -----> (N) Product
Shop (1) -----> (N) NightBiteBox
Shop (1) -----> (N) Order
Order (1) ----> (N) OrderItem
OrderItem ---> Product (FK)
OrderItem ---> NightBiteBox (FK)
```

---

## ✅ Task Hoàn Thành

- [x] **Sprint 1**: BE-05, BE-06, BE-07 - Product Flash Sale APIs
- [x] **Sprint 2**: BE-14, BE-15, BE-16 - NightBite Box + Payment APIs
- [x] **Sprint 3**: BE-23, BE-24 - Search/Filter + Statistics APIs

---

## 🚀 Hướng Dẫn Chạy

1. Build project:
```bash
mvn clean install
```

2. Chạy application:
```bash
mvn spring-boot:run
```

3. Truy cập Swagger UI:
```
http://localhost:8080/savibite/swagger-ui.html
```

---

## ⚠️ Lưu ý

- Tất cả các Payment Gateway endpoints (MoMo, ZaloPay) hiện tại là placeholder
- Cần tích hợp các SDK thực tế khi có credentials
- Webhook xử lý chưa triển khai đầy đủ, cần bổ sung xác thực signature
- Soft delete được sử dụng cho DELETE operations (đánh dấu isActive = false)

---

**Phụ trách**: Ngân (BE)
**Ngày hoàn thành**: 20/06/2026

