export interface Product {
  id: string;
  name: string;
  price: number;
  originalPrice?: number;
  description: string;
  category: string;
  image: string;
  rating: number;
  salesCount: number;
  sizes?: string[];
  sweetnessLevels?: string[];
  hotDeals?: boolean;
}

export interface CartItem {
  product: Product;
  quantity: number;
  selectedSize: string;
  selectedSweetness: string;
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
