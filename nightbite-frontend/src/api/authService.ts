import axiosClient from './axiosClient';

export interface LoginRequest {
  identifier: string; // email hoặc số điện thoại
  password: string;
}

export interface RegisterUserRequest {
  fullName: string;
  email: string;
  phone: string;
  password: string;
}

export interface RegisterShopRequest {
  fullName: string; // Tên chủ shop
  email: string;
  phone: string;
  password: string;
  shopName: string;
  address: string;
  district: string;
}

export interface AuthResponse {
  id: number;
  fullName: string;
  email: string;
  role: 'ROLE_USER' | 'ROLE_SHOP' | 'ROLE_ADMIN';
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

export interface MeResponse {
  id: number;
  fullName: string;
  email: string;
  phone: string;
  avatarUrl?: string;
  role: string;
  trustScore?: number;
  isActive?: boolean;
  shopName?: string;
  district?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: number;
}

export const authService = {
  login: async (req: LoginRequest): Promise<ApiResponse<AuthResponse>> => {
    return axiosClient.post('/auth/login', req);
  },

  registerUser: async (req: RegisterUserRequest): Promise<ApiResponse<AuthResponse>> => {
    return axiosClient.post('/auth/register/user', req);
  },

  registerShop: async (req: RegisterShopRequest): Promise<ApiResponse<AuthResponse>> => {
    return axiosClient.post('/auth/register/shop', req);
  },

  getMe: async (): Promise<ApiResponse<MeResponse>> => {
    return axiosClient.get('/auth/me');
  },

  refreshToken: async (refreshToken: string): Promise<ApiResponse<AuthResponse>> => {
    return axiosClient.post('/auth/refresh', { refreshToken });
  }
};
