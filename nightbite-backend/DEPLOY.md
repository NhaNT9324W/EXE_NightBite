# Deploy NightBite Backend – Hướng Dẫn (Render / Railway)

> Phụ trách: **Trần Huỳnh Khôi (BE-10)**

---

## 1. Build artifact

```bash
cd nightbite-backend
mvn clean package -DskipTests
```

JAR xuất ra: `target/nightbite-backend-0.0.1-SNAPSHOT.jar`

---

## 2. Deploy lên Render

### 2.1 Tạo Web Service

1. Truy cập [render.com](https://render.com) → **New** → **Web Service**
2. Kết nối GitHub repo, chọn branch `main`
3. **Runtime**: Java
4. **Build Command**: `mvn clean package -DskipTests`
5. **Start Command**: `java -Dserver.port=$PORT -jar target/nightbite-backend-0.0.1-SNAPSHOT.jar`

### 2.2 Environment Variables (bắt buộc)

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DB_URL` | `jdbc:mysql://<host>:3306/nightbite_db?useSSL=true&serverTimezone=Asia/Ho_Chi_Minh` |
| `DB_USER` | tên user MySQL |
| `DB_PASS` | mật khẩu MySQL |
| `JWT_SECRET` | chuỗi random >= 32 ký tự (dùng `openssl rand -base64 32`) |
| `CORS_ORIGINS` | URL frontend Zalo Cloud |
| `PORT` | (Render tự set) |

> **Lưu ý bảo mật**: Không bao giờ commit `JWT_SECRET`, `DB_PASS` vào Git.

### 2.3 Database – PlanetScale / Clever Cloud

Dùng MySQL free tier:
- **PlanetScale** (đề xuất): có free tier, hỗ trợ `serverTimezone=UTC`
- **Clever Cloud**: MySQL 8.x free 256MB

---

## 3. Deploy lên Railway

1. Truy cập [railway.app](https://railway.app) → **New Project** → **Deploy from GitHub**
2. Chọn repo → Railway tự detect Java
3. Thêm **MySQL plugin** (New → Database → MySQL)
4. Copy `DATABASE_URL` từ MySQL plugin → set `DB_URL` trong Variables
5. Set các biến môi trường tương tự Render (mục 2.2)

---

## 4. Kiểm tra sau deploy

```bash
# Health check
curl https://nightbite-api.onrender.com/actuator/health

# Swagger UI
open https://nightbite-api.onrender.com/swagger-ui.html

# Test login
curl -X POST https://nightbite-api.onrender.com/savibite/auth/login \
  -H "Content-Type: application/json" \
  -d '{"identifier":"admin@nightbite.vn","password":"admin123"}'
```

---

## 5. Checklist trước review 25/06

- [ ] Spring Boot start OK, kết nối MySQL thành công
- [ ] Swagger UI accessible: `/swagger-ui.html`
- [ ] `POST /savibite/auth/register/user` hoạt động
- [ ] `POST /savibite/auth/login` trả JWT
- [ ] `GET /savibite/shops` trả danh sách (không cần token)
- [ ] `GET /savibite/products` trả danh sách Flash Sale
- [ ] Không có lỗi 500 trên tất cả endpoint đã test
- [ ] CORS không bị block từ FE localhost:5173
