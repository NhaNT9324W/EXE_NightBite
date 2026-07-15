import axios from 'axios';

// Lấy Base URL từ biến môi trường của Vite (.env) hoặc fallback về URL chạy local mặc định
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/savibite';

const axiosClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

// Request Interceptor: Tự động đính kèm JWT Token vào Header Authorization
axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor: Unwrap dữ liệu từ wrapper ApiResponse của Spring Boot
axiosClient.interceptors.response.use(
  (response) => {
    // Trả về trực tiếp object ApiResponse { success, message, data, timestamp }
    return response.data;
  },
  (error) => {
    // Xử lý lỗi trả về từ Backend (nếu Backend trả về cấu trúc lỗi chuẩn) hoặc lỗi kết nối
    const responseError = error.response?.data || {
      success: false,
      message: error.message || 'Không thể kết nối đến máy chủ API.',
    };

    // Kiểm tra nếu token hết hạn / không hợp lệ (401 Unauthorized)
    if (error.response?.status === 401) {
      // Clean up token hết hạn nếu có
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    }

    return Promise.reject(responseError);
  }
);

export default axiosClient;
