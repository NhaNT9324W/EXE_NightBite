import { Product, Order, CartItem } from '../types';
import { ProductResponse } from './productService';
import { OrderResponse } from './orderService';

/**
 * Ánh xạ (Map) dữ liệu ProductResponse từ Backend sang kiểu Product ở Frontend
 */
export const mapProductResponseToProduct = (res: ProductResponse): Product => {
  // Tách tag dị ứng nếu có
  const allergenList = res.allergenTags 
    ? res.allergenTags.split(',').map(tag => tag.trim()) 
    : [];

  return {
    id: String(res.id),
    name: res.name,
    price: res.salePrice,
    originalPrice: res.originalPrice,
    description: `${res.description}${allergenList.length > 0 ? ` (Cảnh báo dị ứng: ${allergenList.join(', ')})` : ''}`,
    category: res.category || 'all',
    image: res.imageUrl || 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?auto=format&fit=crop&q=80&w=400',
    rating: 4.8, // Giá trị mặc định vì Backend chưa lưu rating của từng product riêng lẻ ở Sprint này
    salesCount: res.quantitySold || 0,
    // Các trường tùy biến mặc định
    sizes: ['S (Slice)', 'M (Vừa)', 'L (Lớn)'],
    sweetnessLevels: ['50% Ngọt', '70% Ngọt', '100% Ngọt Chuẩn'],
    hotDeals: res.originalPrice > res.salePrice
  };
};

/**
 * Ánh xạ (Map) dữ liệu OrderResponse từ Backend sang kiểu Order ở Frontend
 */
export const mapOrderResponseToOrder = (res: OrderResponse): Order => {
  const items: CartItem[] = res.items.map(item => {
    // Mock một sản phẩm tạm thời để đưa vào giỏ hàng hiển thị
    const mockProduct: Product = {
      id: String(item.productId || item.boxId || 0),
      name: item.productName || item.boxType || 'Sản phẩm',
      price: item.price,
      description: 'Đơn hàng từ hệ thống',
      category: item.itemType === 'BOX' ? 'mystery' : 'cake',
      image: item.itemType === 'BOX' 
        ? 'https://images.unsplash.com/photo-1549465220-1a8b9238cd48?auto=format&fit=crop&q=80&w=400'
        : 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?auto=format&fit=crop&q=80&w=400',
      rating: 5.0,
      salesCount: 1,
    };

    return {
      product: mockProduct,
      quantity: item.quantity,
      selectedSize: 'Standard',
      selectedSweetness: 'Standard'
    };
  });

  // Chuyển đổi trạng thái chữ hoa từ backend sang chữ thường tương thích frontend
  let uiStatus: 'pending' | 'shipping' | 'completed' = 'pending';
  if (res.status === 'CONFIRMED') {
    uiStatus = 'shipping';
  } else if (res.status === 'DONE') {
    uiStatus = 'completed';
  }

  return {
    id: String(res.id),
    customerName: 'Khách hàng', // Backend không trả về trực tiếp trong OrderResponse
    phoneNumber: '',
    address: 'Nhận tại cửa hàng',
    note: res.note || '',
    items: items,
    totalAmount: res.totalAmount.valueOf(),
    discount: 0,
    finalAmount: res.totalAmount.valueOf(),
    status: uiStatus,
    createdAt: res.createdAt
  };
};
