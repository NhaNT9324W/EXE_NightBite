import axiosClient from './axiosClient';
import { ApiResponse } from './authService';

export interface ShopRequest {
  id?: number;
  shopName: string;
  ownerName: string;
  phone: string;
  email: string;
  password?: string;
  address: string;
  district: string;
  description: string;
  logoUrl?: string;
  bannerUrl?: string;
}

export interface ShopResponse {
  id: number;
  shopName: string;
  ownerName: string;
  phone: string;
  email: string;
  address: string;
  district: string;
  description: string;
  logoUrl?: string;
  bannerUrl?: string;
  ratingAvg: number;
  reviewCount: number;
}

export const shopService = {
  saveShop: async (req: ShopRequest): Promise<ApiResponse<ShopResponse>> => {
    return axiosClient.post('/shops', req);
  },

  getShopById: async (id: number): Promise<ApiResponse<ShopResponse>> => {
    return axiosClient.get(`/shops/${id}`);
  },

  getAllShops: async (district?: string): Promise<ApiResponse<ShopResponse[]>> => {
    return axiosClient.get('/shops', { params: district ? { district } : {} });
  }
};
