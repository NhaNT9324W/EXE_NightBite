export interface Restaurant {
  id: string;
  name: string;
  image: string;
  rating: number;
  distance: number;
  tags: string[];
  promoBadge?: string;
}

export interface Product {
  id: string;
  restaurantId: string;
  name: string;
  price: number;
  originalPrice?: number;
  description: string;
  category: string;
  image: string;
  rating: number;
  salesCount: number;
  sizes?: string[];
  options?: string[]; // Renamed from sweetnessLevels for generic food options
  hotDeals?: boolean;
}

export interface CartItem {
  product: Product;
  quantity: number;
  selectedSize: string;
  selectedOption: string; // Renamed from selectedSweetness
}

export interface Order {
  id: string;
  customerName: string;
  phoneNumber: string;
  address: string;
  note: string;
  items: CartItem[];
  totalAmount: number;
  discount: number;
  finalAmount: number;
  status: 'pending' | 'shipping' | 'completed';
  createdAt: string;
  restaurantId?: string; // track which restaurant
}

export interface Notification {
  id: string;
  title: string;
  content: string;
  time: string;
  read: boolean;
  type: 'promo' | 'system' | 'order';
}

export interface UserProfile {
  name: string;
  phone: string;
  avatar: string;
  tier: 'Bronze' | 'Silver' | 'Gold' | 'Diamond';
  points: number;
  savedVouchers: string[];
}

export interface Review {
  id: string;
  userName: string;
  userAvatar: string;
  rating: number;
  comment: string;
  date: string;
}
