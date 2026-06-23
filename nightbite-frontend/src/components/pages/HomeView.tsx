import React from 'react';
import { Product, UserProfile } from '../../types';
import { PRODUCTS, CATEGORIES } from '../../demoData';
import { Search, SlidersHorizontal, Sparkles, ChevronRight, MapPin, Flame, Star } from 'lucide-react';
import { BannerCarousel } from '../organisms/BannerCarousel';
import { CategoryTab } from '../molecules/CategoryTab';
import { ProductCard } from '../molecules/ProductCard';

interface HomeViewProps {
  onSelectProduct: (product: Product) => void;
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
    title: 'Mua 1 Tặng 1',
    subtitle: 'Nạp năng lượng ngọt dịu',
    desc: 'Ưu đãi bánh sừng bò bơ Pháp khi mua kèm trà sữa',
    bg: 'from-[#F3839F] to-[#E95C8F]',
    img: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?auto=format&fit=crop&q=80&w=200',
  },
  {
    id: 'b2',
    title: 'Ngọt Từ Tim',
    subtitle: 'Nhận ngay SaviPoint x2',
    desc: 'Khi thử dòng bánh Mousse Matcha thượng hạng tuần này',
    bg: 'from-[#829E65] to-[#5D7048]',
    img: 'https://images.unsplash.com/photo-1515003844-10981543aa01?auto=format&fit=crop&q=80&w=200',
  },
  {
    id: 'b3',
    title: 'Thứ Ba Ngọt Ngào',
    subtitle: 'Đồng giá 29.000đ',
    desc: 'Cho tất cả bánh ngọt Donuts sô cô la hạnh nhân thơm ngon',
    bg: 'from-[#D48C56] to-[#A46033]',
    img: 'https://images.unsplash.com/photo-1551024601-bec78aea704b?auto=format&fit=crop&q=80&w=200',
  }
];

export default function HomeView({
  onSelectProduct,
  onSearchFocus,
  activeCategory,
  setActiveCategory,
  user,
  onRequestLocation,
  locationStatus
}: HomeViewProps) {
  // Filter products based on selected category (if not all)
  const filteredProducts = activeCategory === 'all'
    ? [] // If active is 'all' we can show default recommendations or let CategoryView show them
    : [];

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
            <span className="text-xs text-stone-400 flex-1 font-sans">Tìm nhanh bánh ngon, nước uống hảo hạng...</span>
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
            Danh mục món ăn
          </h3>
          <span className="text-[10px] text-stone-400 font-sans">SaviBite Bakery Zalo UI</span>
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

      {/* 4. Horizontal Hot Deals */}
      <div className="py-2.5">
        <div className="px-4 flex items-center justify-between mb-3">
          <div className="flex items-center gap-1.5">
            <span className="p-1 rounded-lg bg-[#F7ECE1] text-[#C57A44]">
              <Sparkles size={14} className="fill-current animate-pulse" />
            </span>
            <h3 className="text-sm font-extrabold uppercase tracking-tight font-sans text-stone-800">
              Gợi ý cho bạn hôm nay
            </h3>
          </div>
          <button
            onClick={() => handleCategorySelect('all')}
            className="text-[10.5px] font-black text-[#C57A44] uppercase tracking-wide flex items-center gap-0.5 hover:underline"
          >
            Tất cả <ChevronRight size={12} className="stroke-[2.5]" />
          </button>
        </div>

        {/* Reusing Product list from main data */}
        <div className="flex gap-3.5 overflow-x-auto px-4 py-1 no-scrollbar scroll-smooth">
          {PRODUCTS.filter((p: any) => p.hotDeals).map((p: any) => (
            <div key={p.id} className="w-[145px] shrink-0 font-sans">
              <ProductCard
                product={p}
                onSelect={onSelectProduct}
              />
            </div>
          ))}
        </div>
      </div>

      {/* 4.5 Vertical Most Purchased Section */}
      <div className="py-2.5">
        <div className="px-4 flex items-center justify-between mb-3">
          <div className="flex items-center gap-1.5">
            <span className="p-1 rounded-lg bg-[#FDF1EB] text-rose-500">
              <Flame size={14} className="fill-current animate-pulse text-rose-550" />
            </span>
            <h3 className="text-sm font-extrabold uppercase tracking-tight font-sans text-stone-800">
              Bánh được mua nhiều nhất
            </h3>
          </div>
          <span className="text-[9px] font-black text-rose-600 bg-rose-50 border border-rose-100/60 px-2 py-0.5 rounded-full uppercase tracking-wider font-sans">
            🔥 BÁN CHẠY
          </span>
        </div>

        <div className="px-4 space-y-3">
          {[...PRODUCTS]
            .sort((a, b) => (b.salesCount || 0) - (a.salesCount || 0))
            .slice(0, 5)
            .map((p) => (
              <div
                key={p.id}
                onClick={() => onSelectProduct(p)}
                className="bg-white rounded-2xl border border-stone-200/60 p-2.5 flex gap-3 items-center shadow-xs hover:border-[#C57A44]/40 hover:shadow-xs transition-all duration-300 cursor-pointer group"
              >
                {/* Product Image */}
                <div className="relative w-20 h-20 rounded-xl overflow-hidden bg-stone-100 shrink-0">
                  <img
                    src={p.image}
                    alt={p.name}
                    className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                    referrerPolicy="no-referrer"
                  />
                  {p.salesCount && (
                    <div className="absolute bottom-1 left-1 bg-rose-600/90 backdrop-blur-xs text-[8px] font-black text-white px-1.5 py-0.5 rounded-md pointer-events-none z-10 font-sans shadow-xs">
                      #{p.salesCount}+ ĐÃ BÁN
                    </div>
                  )}
                </div>

                {/* Product Text & Details */}
                <div className="flex-1 min-w-0 flex flex-col justify-between py-0.5">
                  <div>
                    <div className="flex items-start justify-between gap-2">
                      <h4 className="text-xs font-black text-stone-850 line-clamp-1 tracking-tight group-hover:text-[#C57A44] transition-colors leading-tight">
                        {p.name}
                      </h4>
                      {/* Rating info */}
                      <div className="flex items-center gap-0.5 text-amber-500 shrink-0 text-[10px] font-black bg-amber-50 px-1.5 py-0.5 rounded-md border border-amber-100/20">
                        <Star size={9} className="fill-current" />
                        <span>{p.rating}</span>
                      </div>
                    </div>
                    <p className="text-[10px] text-stone-400 font-serif italic line-clamp-1 mt-0.5 mb-1.5 leading-normal">
                      {p.description}
                    </p>
                  </div>

                  {/* Pricing row */}
                  <div className="flex items-baseline gap-1.5 mt-auto">
                    <span className="text-xs font-black text-[#C57A44]">
                      {p.price.toLocaleString('vi-VN')}đ
                    </span>
                    {p.originalPrice && (
                      <span className="text-[10px] text-stone-400 line-through">
                        {p.originalPrice.toLocaleString('vi-VN')}đ
                      </span>
                    )}
                  </div>
                </div>
              </div>
            ))}
        </div>
      </div>

      {/* 5. Location Delivery Banner Section */}
      <div className="px-4 py-3">
        <div className="bg-white rounded-2xl border border-stone-200/50 p-4 flex gap-3 items-center shadow-xs">
          <div className="w-10 h-10 bg-[#FAF1E6] text-[#C57A44] rounded-full flex items-center justify-center shrink-0">
            <MapPin size={18} />
          </div>
          <div className="flex-1 min-w-0">
            <h4 className="text-xs font-black text-stone-800 leading-tight">Giao bánh siêu tốc tận nơi?</h4>
            <p className="text-[10px] text-stone-400 truncate mt-0.5 font-sans">Cấp quyền định vị GPS để quét chi nhánh SaviBite gần bạn nhất.</p>
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
