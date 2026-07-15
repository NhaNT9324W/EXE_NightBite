import axiosClient from './axiosClient';
import { ApiResponse } from './authService';

export interface OrderItemRequest {
  itemType: 'PRODUCT' | 'BOX';
  productId?: number;
  boxId?: number;
  quantity: number;
}

export interface CreateOrderRequest {
  userId?: number; // Tạm thời dùng ở Sprint 1-2
  shopId: number;
  pickupTime: string; // ISO LocalDateTime string ví dụ: "2026-07-15T21:00:00"
  note: string;
  paymentMethod: 'MOMO' | 'ZALOPAY' | 'CASH' | 'MOCK';
  items: OrderItemRequest[];
}

export interface NightBiteBoxOrderRequest {
  userId?: number;
  shopId: number;
  nightBiteBoxId: number;
  quantity: number;
  pickupTime: string; // ISO string
  note: string;
}

export interface OrderItemResponse {
  id: number;
  itemType: 'PRODUCT' | 'BOX';
  productId?: number;
  productName?: string;
  boxId?: number;
  boxType?: string;
  quantity: number;
  price: number;
}

export interface OrderResponse {
  id: number;
  orderCode: string;
  status: 'PENDING' | 'CONFIRMED' | 'DONE' | 'CANCELLED';
  totalAmount: number;
  pickupTime: string;
  note: string;
  paymentMethod: string;
  paymentStatus: string;
  cancelReason?: string;
  createdAt: string;
  items: OrderItemResponse[];
}

export const orderService = {
  createOrder: async (req: CreateOrderRequest): Promise<ApiResponse<OrderResponse>> => {
    return axiosClient.post('/orders', req);
  },

  orderNightBiteBox: async (req: NightBiteBoxOrderRequest): Promise<ApiResponse<OrderResponse>> => {
    return axiosClient.post('/orders/nightbite-box', req);
  },

  getOrderHistory: async (id: number, role: 'USER' | 'SHOP'): Promise<ApiResponse<OrderResponse[]>> => {
    return axiosClient.get('/orders', { params: { id, role } });
  },

  changeOrderStatus: async (
    id: number,
    status: 'PENDING' | 'CONFIRMED' | 'DONE' | 'CANCELLED',
    cancelReason?: string
  ): Promise<ApiResponse<OrderResponse>> => {
    return axiosClient.patch(`/orders/${id}/status`, null, {
      params: { status, ...(cancelReason ? { cancelReason } : {}) },
    });
  }
};
