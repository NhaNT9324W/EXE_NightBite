import axiosClient from './axiosClient';
import { ApiResponse } from './authService';

export interface ProductRequest {
  id?: number;
  shopId: number;
  name: string;
  description: string;
  imageUrl: string;
  originalPrice: number;
  salePrice: number;
  quantityAvailable: number;
  saleStartTime: string; // Định dạng "HH:mm:ss" hoặc "HH:mm"
  saleEndTime: string;   // Định dạng "HH:mm:ss" hoặc "HH:mm"
  saleDate: string;      // Định dạng "YYYY-MM-DD"
  allergenTags: string;  // Ví dụ: "Peanut, Dairy, Gluten"
  category: string;
  isActive: boolean;
}

export interface ProductResponse {
  id: number;
  shopId: number;
  shopName: string;
  name: string;
  description: string;
  imageUrl: string;
  originalPrice: number;
  salePrice: number;
  quantityAvailable: number;
  quantitySold: number;
  saleStartTime: string;
  saleEndTime: string;
  saleDate: string;
  allergenTags: string;
  category: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export const productService = {
  createProduct: async (req: ProductRequest): Promise<ApiResponse<ProductResponse>> => {
    return axiosClient.post('/products', req);
  },

  updateProduct: async (id: number, req: ProductRequest): Promise<ApiResponse<ProductResponse>> => {
    return axiosClient.put(`/products/${id}`, req);
  },

  deleteProduct: async (id: number): Promise<ApiResponse<null>> => {
    return axiosClient.delete(`/products/${id}`);
  },

  getActiveSaleProducts: async (): Promise<ApiResponse<ProductResponse[]>> => {
    return axiosClient.get('/products/sale/active');
  },

  getProductsByShop: async (shopId: number): Promise<ApiResponse<ProductResponse[]>> => {
    return axiosClient.get(`/products/shop/${shopId}`);
  },

  getProductById: async (id: number): Promise<ApiResponse<ProductResponse>> => {
    return axiosClient.get(`/products/${id}`);
  },

  searchByCategory: async (category: string): Promise<ApiResponse<ProductResponse[]>> => {
    return axiosClient.get('/products/search/category', { params: { category } });
  },

  filterByAllergen: async (allergen: string): Promise<ApiResponse<ProductResponse[]>> => {
    return axiosClient.get('/products/filter/allergen', { params: { allergen } });
  }
};
