# Frontend Setup Summary - NightBite

## 📁 Thông Tin Tổng Quan

* **Thư mục dự án:** `...\EXE_NightBite\nightbite-frontend`
* **Package Manager:** `pnpm` (v11.6.0)
* **Build Tool & Development Server:** `Vite` (v8.0.x)

---

# 🛠️ Danh Sách Công Nghệ & Thư Viện

| Thành phần        | Phiên bản | Mục đích sử dụng                        | Ghi chú                              |
| ----------------- | --------- | --------------------------------------- | ------------------------------------ |
| React & React-DOM | v19.2.x   | Xây dựng giao diện người dùng           | Được khởi tạo mặc định bởi Vite      |
| axios             | v1.17.x   | Gọi API và trao đổi dữ liệu với Backend | Phục vụ tích hợp hệ thống            |
| react-router-dom  | v7.17.x   | Quản lý điều hướng giữa các trang       | Hỗ trợ Route Guard và phân quyền     |
| tailwindcss       | v4.3.x    | Xây dựng giao diện bằng Utility Classes | Sử dụng kiến trúc Tailwind CSS v4    |
| @tailwindcss/vite | Chuẩn v4  | Tích hợp Tailwind trực tiếp vào Vite    | Không cần cấu hình init truyền thống |

---

# ⚙️ Các File Cấu Hình Quan Trọng

## 1. vite.config.js

Tích hợp React và Tailwind CSS vào Vite.

```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
})
```

## 2. src/index.css

Kích hoạt Tailwind CSS theo chuẩn phiên bản v4.

```css
@import "tailwindcss";
```

---

# 🚀 Câu Lệnh Khởi Động Dự Án

Mở Terminal tại thư mục:

```bash
nightbite-frontend
```

Chạy lệnh:

```bash
pnpm dev
```

Sau khi khởi động thành công, Vite sẽ tạo Development Server tại:

```text
http://localhost:5173
```

Truy cập đường dẫn trên bằng trình duyệt để kiểm tra giao diện và theo dõi quá trình phát triển.

---

# 📂 Trạng Thái foder dự kiến

Frontend đã hoàn tất giai đoạn khởi tạo môi trường phát triển và tích hợp các thư viện nền tảng.

Bộ khung hiện tại đã sẵn sàng cho việc xây dựng kiến trúc dự án theo hướng Feature-Based Architecture, bao gồm các thư mục chính:

```text
src/
├── api/
├── components/
├── pages/
├── routes/
├── layouts/
├── hooks/
├── services/
├── utils/
└── assets/
```

Đây là nền tảng để triển khai các màn hình và chức năng thuộc Sprint 1 của dự án NightBite.
