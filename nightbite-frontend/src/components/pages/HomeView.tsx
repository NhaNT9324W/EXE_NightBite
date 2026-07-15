import React from 'react';
import { Product, UserProfile, Restaurant } from '@/src/types';
import { PRODUCTS, CATEGORIES, RESTAURANTS } from '../../demoData';
import { Search, SlidersHorizontal, Sparkles, ChevronRight, MapPin, Store, Star, Clock } from 'lucide-react';
import { BannerCarousel } from '../organisms/BannerCarousel';
import { CategoryTab } from '../molecules/CategoryTab';
import { ProductCard } from '../molecules/ProductCard';
import { RestaurantCard } from '../molecules/RestaurantCard';

interface HomeViewProps {
  onSelectProduct: (product: Product) => void;
  onSelectRestaurant: (restaurant: Restaurant) => void;
  onSearchFocus: () => void;
  activeCategory: string;
  setActiveCategory: (cat: string) => void;
  user: UserProfile;
  onRequestLocation: () => void;
  locationStatus: 'idle' | 'allowed' | 'denied';
}

const BANNERS = [
  {
    id: 'b1',
    title: 'Giảm 50K Phí Ship',
    subtitle: 'SaviBite khao bạn mới',
    desc: 'Ưu đãi đặc quyền cho các đơn hàng đầu tiên',
    bg: 'from-[#F3839F] to-[#E95C8F]',
    img: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?auto=format&fit=crop&q=80&w=200',
  },
  {
    id: 'b2',
    title: 'Freeship Quán Quen',
    subtitle: 'Không lo giá ship',
    desc: 'Tặng ngay mã Freeship 15k cho quán dưới 3km',
    bg: 'from-[#829E65] to-[#5D7048]',
    img: 'https://images.unsplash.com/photo-1515003844-10981543aa01?auto=format&fit=crop&q=80&w=200',
  },
  {
    id: 'b3',
    title: 'Thứ Ba Sale Đậm',
    subtitle: 'Đồng giá 35.000đ',
    desc: 'Hàng ngàn món ngon đồng giá mỗi thứ ba hàng tuần',
    bg: 'from-[#D48C56] to-[#A46033]',
    img: 'https://images.unsplash.com/photo-1551024601-bec78aea704b?auto=format&fit=crop&q=80&w=200',
  }
];

export default function HomeView({
  onSelectProduct,
  onSelectRestaurant,
  onSearchFocus,
  activeCategory,
  setActiveCategory,
  user,
  onRequestLocation,
  locationStatus
}: HomeViewProps) {

  const handleCategorySelect = (catId: string) => {
    setActiveCategory(catId);
  };

  return (
    <div className="flex-1 overflow-y-auto bg-[#FAF8F5] pb-2 text-[#2C2520]">
      {/* 1. Header welcome */}
      <div className="bg-gradient-to-b from-[#F2ECE4] to-[#FAF8F5] px-4 pt-4 pb-2">
        {/* Search bar mockup */}
        <div className="mt-1" onClick={onSearchFocus}>
          <div className="flex items-center gap-2 bg-white rounded-xl px-3 py-2.5 shadow-sm border border-stone-200/60 cursor-pointer hover:border-stone-300 transition-colors">
            <Search size={16} className="text-stone-400 shrink-0" />
            <span className="text-xs text-stone-400 flex-1 font-sans">Tìm quán ngon, món ăn đỉnh...</span>
            <SlidersHorizontal size={14} className="text-[#C57A44] shrink-0" />
          </div>
        </div>
      </div>

      {/* 2. Banner Auto-Sliding */}
      <div className="px-4 py-2">
        <BannerCarousel banners={BANNERS} />
      </div>

      {/* 3. Horizontal Categories Filter Tab List */}
      <div className="px-4 py-4">
        <div className="flex justify-between items-center mb-3">
          <h3 className="text-xs font-black uppercase text-stone-400 tracking-widest font-sans">
            Khám phá danh mục
          </h3>
          <span className="text-[10px] text-stone-400 font-sans">SaviBite Cake</span>
        </div>
        <div className="flex gap-2 overflow-x-auto pb-1 no-scrollbar">
          {CATEGORIES.map((cat) => (
            <div key={cat.id} className="shrink-0">
              <CategoryTab
                id={cat.id}
                name={cat.label}
                iconName={cat.icon}
                isActive={activeCategory === cat.id}
                onClick={() => handleCategorySelect(cat.id)}
              />
            </div>
          ))}
        </div>
      </div>

      {/* 4. Top Restaurants / Quán ngon */}
      <div className="py-2.5">
        <div className="px-4 flex items-center justify-between mb-3">
          <div className="flex items-center gap-1.5">
            <span className="p-1 rounded-lg bg-[#F7ECE1] text-[#C57A44]">
              <Store size={14} className="fill-current" />
            </span>
            <h3 className="text-sm font-extrabold uppercase tracking-tight font-sans text-stone-800">
              Quán ngon quanh đây
            </h3>
          </div>
          <button
            className="text-[10.5px] font-black text-[#C57A44] uppercase tracking-wide flex items-center gap-0.5 hover:underline"
          >
            Tất cả <ChevronRight size={12} className="stroke-[2.5]" />
          </button>
        </div>

        <div className="flex gap-3.5 overflow-x-auto px-4 py-1 no-scrollbar scroll-smooth">
          {RESTAURANTS.map((res: any) => (
            <div key={res.id} className="w-[160px] shrink-0 font-sans">
              <RestaurantCard
                restaurant={res}
                onClick={onSelectRestaurant}
              />
            </div>
          ))}
        </div>
      </div>

      {/* 5. Gợi ý tiệm bánh */}
      <div className="py-2.5 mt-2">
        <div className="px-4 flex items-center justify-between mb-3">
          <div className="flex items-center gap-1.5">
            <span className="p-1 rounded-lg bg-[#FDF1EB] text-rose-500">
              <Sparkles size={14} className="fill-current animate-pulse text-rose-550" />
            </span>
            <h3 className="text-sm font-extrabold uppercase tracking-tight font-sans text-stone-800">
              Cửa hàng nổi bật
            </h3>
          </div>
        </div>

        <div className="px-4 space-y-3">
          {RESTAURANTS
            .sort((a, b) => b.rating - a.rating)
            .map((res) => (
              <div
                key={res.id}
                onClick={() => onSelectRestaurant(res)}
                className="bg-white rounded-2xl border border-stone-200/50 p-2.5 flex gap-3 items-center shadow-xs hover:border-[#C57A44]/30 hover:shadow-sm transition-all duration-300 cursor-pointer group"
              >
                {/* Image */}
                <div className="w-20 h-20 shrink-0 rounded-xl overflow-hidden bg-stone-100 relative">
                  <img
                    src={res.image}
                    alt={res.name}
                    className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                    referrerPolicy="no-referrer"
                  />
                </div>

                {/* Content */}
                <div className="flex-1 min-w-0 flex flex-col justify-center py-1">
                  <h4 className="text-xs font-black text-stone-800 line-clamp-1 mb-1 leading-tight group-hover:text-[#C57A44] transition-colors">
                    {res.name}
                  </h4>
                  <div className="flex flex-wrap items-center gap-2 text-[10px] text-stone-500 font-medium mb-1.5">
                    <div className="flex items-center gap-0.5 text-amber-500 font-black">
                      <Star size={10} className="fill-current" />
                      <span>{res.rating}</span>
                    </div>
                    <span className="w-0.5 h-0.5 rounded-full bg-stone-300" />
                    <div className="flex items-center gap-0.5">
                      <MapPin size={10} className="text-stone-400" />
                      <span>{res.distance} km</span>
                    </div>
                    <span className="w-0.5 h-0.5 rounded-full bg-stone-300" />
                  </div>
                  <div className="flex gap-1 flex-wrap">
                    {res.tags.map(tag => (
                      <span key={tag} className="text-[9px] font-bold text-[#C57A44] bg-[#F7ECE1] px-1.5 py-0.5 rounded-md">
                        {tag}
                      </span>
                    ))}
                  </div>
                </div>
              </div>
            ))}
        </div>
      </div>

      {/* 6. Location Delivery Banner Section */}
      <div className="px-4 py-3">
        <div className="bg-white rounded-2xl border border-stone-200/50 p-4 flex gap-3 items-center shadow-xs">
          <div className="w-10 h-10 bg-[#FAF1E6] text-[#C57A44] rounded-full flex items-center justify-center shrink-0">
            <MapPin size={18} />
          </div>
          <div className="flex-1 min-w-0">
            <h4 className="text-xs font-black text-stone-800 leading-tight">Tìm quán gần đây?</h4>
            <p className="text-[10px] text-stone-400 truncate mt-0.5 font-sans">Cấp quyền định vị GPS để tìm quán gần bạn nhất.</p>
          </div>
          <button
            onClick={onRequestLocation}
            disabled={locationStatus === 'allowed'}
            className="px-3.5 py-1.8 bg-[#C57A44] text-white hover:bg-amber-800 disabled:bg-emerald-50 disabled:text-emerald-700 disabled:border-emerald-100 rounded-xl text-[10px] font-black uppercase tracking-wider shadow-xs transition-all border border-transparent"
          >
            {locationStatus === 'allowed' ? 'Đã bật' : 'Bật GPS'}
          </button>
        </div>
      </div>
    </div>
  );
}
