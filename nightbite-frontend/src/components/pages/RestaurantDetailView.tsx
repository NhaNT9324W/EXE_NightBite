import React, { useState, useRef, useEffect } from 'react';
import { Product, Restaurant, CartItem } from '../../types';
import { PRODUCTS } from '../../demoData';
import { ChevronLeft, Star, MapPin, Clock, Search, Heart, Share, Info, ChevronRight, Plus, Minus, Store, Sparkles, CheckCircle, ShoppingBag } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

interface RestaurantDetailViewProps {
  restaurant: Restaurant;
  onBack: () => void;
  onSelectProduct: (product: Product) => void;
  cart?: CartItem[];
  onUpdateQuantity?: (index: number, delta: number) => void;
  onRemoveItem?: (index: number) => void;
  onGoToCart?: () => void;
}

export function RestaurantDetailView({ restaurant, onBack, onSelectProduct, cart = [], onUpdateQuantity, onRemoveItem, onGoToCart }: RestaurantDetailViewProps) {
  const restaurantProducts = PRODUCTS.filter(p => p.restaurantId === restaurant.id);
  const scrollRef = useRef<HTMLDivElement>(null);
  const [isScrolled, setIsScrolled] = useState(false);
  const [activeTab, setActiveTab] = useState('Món chính');

  useEffect(() => {
    const handleScroll = () => {
      if (scrollRef.current) {
        setIsScrolled(scrollRef.current.scrollTop > 100);
      }
    };
    const ref = scrollRef.current;
    if (ref) {
      ref.addEventListener('scroll', handleScroll);
    }
    return () => {
      if (ref) ref.removeEventListener('scroll', handleScroll);
    };
  }, []);

  const getProductQuantity = (productId: string) => {
    return cart.filter(item => item.product.id === productId).reduce((sum, item) => sum + item.quantity, 0);
  };

  const handleDecreaseProduct = (productId: string) => {
    if (!onUpdateQuantity || !onRemoveItem) return;
    const itemIndex = cart.findIndex(item => item.product.id === productId);
    if (itemIndex > -1) {
       const newQuantity = cart[itemIndex].quantity - 1;
       if (newQuantity <= 0) {
           onRemoveItem(itemIndex);
       } else {
           onUpdateQuantity(itemIndex, -1);
       }
    }
  };

  const handleIncreaseProduct = (productId: string, product: Product) => {
    if (!onUpdateQuantity) return;
    const itemIndex = cart.findIndex(item => item.product.id === productId);
    if (itemIndex > -1) {
       onUpdateQuantity(itemIndex, 1);
    } else {
       onSelectProduct(product);
    }
  };

  const totalCartItems = cart.reduce((sum, item) => sum + item.quantity, 0);
  const totalCartPrice = cart.reduce((sum, item) => sum + item.product.price * item.quantity, 0);

  return (
    <motion.div 
      initial={{ x: '100%' }}
      animate={{ x: 0 }}
      exit={{ x: '100%' }}
      transition={{ type: 'spring', damping: 25, stiffness: 200 }}
      className="absolute inset-0 bg-[#F4F4F4] z-[40] flex flex-col overflow-hidden"
    >
      {/* Dynamic Header */}
      <div className={`absolute top-0 left-0 right-0 z-50 transition-all duration-300 ${isScrolled ? 'bg-white shadow-sm' : 'bg-transparent'}`}>
        <div className="h-14 px-4 flex items-center justify-between gap-3">
          <button 
            onClick={onBack} 
            className={`w-8 h-8 rounded-full flex items-center justify-center transition-colors ${isScrolled ? 'text-stone-800' : 'bg-black/40 text-white backdrop-blur-sm'}`}
          >
            <ChevronLeft size={24} />
          </button>
          
          {isScrolled ? (
            <div className="flex-1 bg-stone-100 h-9 rounded-full px-3 flex items-center gap-2">
              <Search size={16} className="text-stone-400" />
              <input 
                type="text" 
                placeholder={`Tìm món tại ${restaurant.name}`}
                className="bg-transparent border-none outline-none text-xs w-full text-stone-800 placeholder:text-stone-400"
              />
            </div>
          ) : (
            <div className="flex-1 flex justify-end gap-2">
              <button className="w-8 h-8 rounded-full bg-black/40 flex items-center justify-center text-white backdrop-blur-sm">
                <Search size={16} />
              </button>
            </div>
          )}
          
          <div className="flex gap-2">
             <button className={`w-8 h-8 rounded-full flex items-center justify-center transition-colors ${isScrolled ? 'text-rose-600' : 'bg-black/40 text-white backdrop-blur-sm'}`}>
                <Heart size={20} className={isScrolled ? 'fill-current' : ''} />
             </button>
          </div>
        </div>
      </div>

      <div ref={scrollRef} className="flex-1 overflow-y-auto no-scrollbar flex flex-col min-h-0">
        {/* Banner */}
        <div className="relative h-60 shrink-0 w-full bg-stone-200">
          <img src={restaurant.image} className="w-full h-full object-cover" referrerPolicy="no-referrer" />
          <div className="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent pointer-events-none" />
          <div className="absolute bottom-2 right-2">
            <button className="w-6 h-6 bg-white/80 backdrop-blur-sm rounded-full flex items-center justify-center text-stone-600">
              <Info size={14} />
            </button>
          </div>
        </div>

        {/* Info */}
        <div className="bg-white px-4 pt-4 pb-3 mb-2 shrink-0">
          <div className="flex items-start gap-2 mb-2">
            <span className="bg-rose-500 text-white text-[10px] font-bold px-1.5 py-0.5 rounded shrink-0 mt-1">Yêu thích</span>
            <h2 className="text-xl font-bold text-stone-900 leading-tight flex-1 flex items-center gap-1.5 flex-wrap">
              <div className="bg-amber-500 text-white p-0.5 rounded-full shrink-0">
                <CheckCircle size={12} className="fill-current" />
              </div>
              {restaurant.name}
            </h2>
          </div>
          
          <div className="flex items-center gap-2 text-xs text-stone-600 mb-3">
            <div className="flex items-center gap-1 text-amber-500 font-bold text-sm">
              <Star size={14} className="fill-current" />
              <span>{restaurant.rating}</span>
            </div>
            <span className="text-stone-500 text-[11px]">(999+ Bình luận) <ChevronRight size={12} className="inline -ml-0.5"/></span>
            <div className="ml-auto">
               <Heart size={18} className="text-stone-400"/>
            </div>
          </div>

          <div className="space-y-2.5 pt-3 border-t border-stone-100">
            <div className="flex items-center justify-between text-[11px]">
              <div className="flex items-center gap-2 font-medium text-stone-800">
                <Store size={14} className="text-emerald-500"/>
                <span>Nhận tại quán</span>
              </div>
            </div>
            <div className="flex items-center justify-between text-[11px]">
              <div className="flex items-center gap-2 font-medium text-stone-800">
                <Sparkles size={14} className="text-rose-500"/>
                <span>Ưu đãi dành cho bạn</span>
              </div>
              <span className="text-stone-400">Xem thêm <ChevronRight size={12} className="inline -ml-0.5"/></span>
            </div>
            
          </div>
        </div>

        {/* Popular */}
        <div className="bg-white pt-4 pb-4 mb-2 shrink-0">
          <h3 className="text-[15px] font-medium text-rose-600 px-4 mb-3">Món phổ biến</h3>
          <div className="flex gap-3 overflow-x-auto no-scrollbar px-4">
            {restaurantProducts.slice(0, 3).map((p, i) => (
              <div key={p.id} className="w-[140px] shrink-0 border border-stone-100 rounded-lg overflow-hidden relative cursor-pointer" onClick={() => onSelectProduct(p)}>
                <div className="h-[100px] w-full relative">
                  <img src={p.image} className="w-full h-full object-cover" referrerPolicy="no-referrer" />
                  <div className="absolute top-0 left-0 bg-amber-500 text-white text-[9px] font-bold px-1.5 py-0.5 rounded-br-lg z-10">
                    {3 * (i+1)}K+ đã bán
                  </div>
                  <div className="absolute bottom-1 right-1">
                      {getProductQuantity(p.id) > 0 ? (
                        <div className="w-6 h-6 bg-white border border-[#C57A44] text-[#C57A44] rounded-full flex items-center justify-center font-bold text-[10px] shadow-xs">
                          {getProductQuantity(p.id)}
                        </div>
                      ) : (
                        <div className="w-6 h-6 bg-[#C57A44] text-white rounded-full flex items-center justify-center shadow-xs">
                          <Plus size={14} className="stroke-[3]" />
                        </div>
                      )}
                  </div>
                </div>
                <div className="px-2 pt-2 pb-2">
                  <h4 className="text-[11px] text-stone-800 line-clamp-2 min-h-[32px] leading-snug">{p.name}</h4>
                  <div className="mt-1.5">
                    <span className="text-rose-600 font-medium text-sm">{p.price.toLocaleString('vi-VN')}đ</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* List */}
        <div className="bg-white pb-24 grow min-h-screen">
          <div className="p-4">
            <h3 className="text-sm font-bold text-stone-800 mb-4">Tất cả món ({restaurantProducts.length})</h3>
            <div className="space-y-5">
              {restaurantProducts.map(p => (
                <div key={p.id} className="flex gap-3 cursor-pointer group" onClick={() => onSelectProduct(p)}>
                  <div className="w-24 h-24 shrink-0 rounded-lg overflow-hidden bg-stone-100 relative">
                     <img src={p.image} className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" referrerPolicy="no-referrer" />
                  </div>
                  <div className="flex-1 min-w-0 py-0.5 flex flex-col">
                    <h4 className="text-sm text-stone-800 mb-1 leading-snug font-medium group-hover:text-rose-600 transition-colors">{p.name}</h4>
                    <div className="text-[10px] text-stone-500 mb-auto flex items-center flex-wrap gap-x-1.5 gap-y-0.5">
                      <span>{p.salesCount || 100}+ đã bán</span>
                      {p.rating && <span className="text-stone-300">|</span>}
                      {p.rating && <span>{p.salesCount ? Math.floor((p.salesCount)/100) : 5} lượt thích</span>}
                    </div>
                    <div className="flex justify-between items-end mt-2">
                       <span className="text-rose-600 font-medium text-[15px]">{p.price.toLocaleString('vi-VN')}đ</span>
                       
                       {getProductQuantity(p.id) > 0 ? (
                         <div className="flex items-center gap-3">
                           <button onClick={(e) => { e.stopPropagation(); handleDecreaseProduct(p.id); }} className="w-7 h-7 flex items-center justify-center border border-[#C57A44] text-[#C57A44] rounded-full bg-white active:bg-amber-50 transition-colors shadow-xs">
                              <Minus size={16} className="stroke-[3]" />
                           </button>
                           <span className="text-[15px] font-medium w-4 text-center text-stone-800">
                             {getProductQuantity(p.id)}
                           </span>
                           <button onClick={(e) => { e.stopPropagation(); handleIncreaseProduct(p.id, p); }} className="w-7 h-7 flex items-center justify-center bg-[#C57A44] text-white rounded-full active:bg-amber-800 transition-colors shadow-xs">
                              <Plus size={16} className="stroke-[3]" />
                           </button>
                         </div>
                       ) : (
                         <button className="w-7 h-7 bg-[#C57A44] text-white rounded-full flex items-center justify-center transition-transform hover:scale-110 active:scale-95 shadow-xs" onClick={(e) => { e.stopPropagation(); onSelectProduct(p); }}>
                            <Plus size={16} className="stroke-[3]" />
                         </button>
                       )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Sticky Bottom Cart Bar */}
      {cart.length > 0 && (
        <div className="absolute bottom-0 left-0 right-0 max-w-[480px] mx-auto bg-white border-t border-stone-200 p-2.5 flex items-center justify-between z-50 shadow-[0_-4px_10px_rgba(0,0,0,0.05)] h-[60px]">
          <div className="flex items-center gap-3">
            <div className="relative ml-2">
               <ShoppingBag size={24} className="text-[#f24e1e]" />
               <span className="absolute -top-1.5 -right-2 bg-white text-[#f24e1e] border border-[#f24e1e] text-[9px] font-bold w-4 h-4 flex items-center justify-center rounded-full">
                 {totalCartItems}
               </span>
            </div>
            <span className="text-[15px] font-bold text-[#f24e1e] ml-1">
              {totalCartPrice.toLocaleString('vi-VN')}đ
            </span>
          </div>
          <button onClick={onGoToCart} className="bg-[#f24e1e] text-white px-6 py-2 rounded-lg text-sm font-medium active:bg-[#e04316] transition-colors">
            Tiếp tục
          </button>
        </div>
      )}
    </motion.div>
  );
}
