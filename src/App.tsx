import React, { useState, useEffect } from 'react';
import { Product, CartItem, Order, UserProfile, Restaurant } from './types';
import { PRODUCTS, INITIAL_USER } from './demoData';
import HomeView from './components/pages/HomeView';
import CategoryView from './components/pages/CategoryView';
import CartView from './components/pages/CartView';
import CheckoutView from './components/pages/CheckoutView';
import { MysteryBoxView } from './components/pages/MysteryBoxView';
import ProfileView from './components/pages/ProfileView';
import SearchView from './components/pages/SearchView';
import { RestaurantDetailView } from './components/pages/RestaurantDetailView';
import { ProductDetailModal } from './components/organisms/ProductDetailModal';
import {
  Wifi, Signal, Battery, Ellipsis, X, Home, Grid, ShoppingBag, Bell, User,
  Cake, Info, RefreshCw, Star, Sparkles, CheckCircle2, ChevronRight, Share2,
  Moon, Sun, Link2, Heart, Award, Copy, Gift
} from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

export default function App() {
  const [activeTab, setActiveTab] = useState<'home' | 'categories' | 'cart' | 'mystery' | 'profile' | 'checkout'>('home');
  const [cart, setCart] = useState<CartItem[]>([]);
  const [orders, setOrders] = useState<Order[]>([]);
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [selectedRestaurant, setSelectedRestaurant] = useState<Restaurant | null>(null);
  const [user, setUser] = useState<UserProfile>(INITIAL_USER);
  const [selectedCategory, setSelectedCategory] = useState<string>('all');

  // Theme skin preset state
  const [themeColor, setThemeColor] = useState<'honey' | 'strawberry' | 'chocolate'>('honey');

  // Location request states (Mockup 2 matching)
  const [locationStatus, setLocationStatus] = useState<'idle' | 'allowed' | 'denied'>('idle');
  const [showLocationDialog, setShowLocationDialog] = useState(false);

  // Custom toast notification popups
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Zalo Native Header Option Menu ... popup drawer
  const [showZaloMenu, setShowZaloMenu] = useState(false);

  // Dynamic ticking time simulation
  const [currentTime, setCurrentTime] = useState('08:38');

  useEffect(() => {
    // Clock ticker
    const clockTimer = setInterval(() => {
      const now = new Date();
      const hrs = String(now.getHours()).padStart(2, '0');
      const mins = String(now.getMinutes()).padStart(2, '0');
      setCurrentTime(`${hrs}:${mins}`);
    }, 30000);

    return () => {
      clearInterval(clockTimer);
    };
  }, []);

  const triggerToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  // 1. Location Dialog handlers
  const handleAllowLocation = () => {
    setLocationStatus('allowed');
    setShowLocationDialog(false);
    triggerToast('✓ Đã cho phép chia sẻ vị trí với SaviBite');
  };

  const handleDenyLocation = () => {
    setLocationStatus('denied');
    setShowLocationDialog(false);
    triggerToast('✕ Đã từ chối quyền truy cập vị trí');
  };

  // 2. Cart Operations
  const handleAddToCart = (item: CartItem) => {
    const existingIndex = cart.findIndex(
      (cartItem) =>
        cartItem.product.id === item.product.id &&
        cartItem.product.sizes === item.product.sizes &&
        cartItem.selectedSize === item.selectedSize &&
        cartItem.selectedOption === item.selectedOption
    );

    if (existingIndex > -1) {
      const updated = [...cart];
      updated[existingIndex].quantity += item.quantity;
      setCart(updated);
    } else {
      setCart([...cart, item]);
    }
    triggerToast(`🛒 Đã thêm ${item.quantity}x ${item.product.name} vào đơn hàng`);
  };

  const handleUpdateQuantity = (index: number, delta: number) => {
    const updated = [...cart];
    updated[index].quantity += delta;
    if (updated[index].quantity < 1) {
      updated.splice(index, 1);
    }
    setCart(updated);
  };

  const handleRemoveItem = (index: number) => {
    const updated = [...cart];
    const removedName = updated[index].product.name;
    updated.splice(index, 1);
    setCart(updated);
    triggerToast(`Đã bỏ ${removedName} khỏi giỏ`);
  };

  // 3. Placing orders
  const handlePlaceOrder = (order: Order) => {
    setOrders([order, ...orders]);
    // Award points to profile
    const pointsAwarded = Math.round(order.finalAmount / 1000);
    setUser((prev) => ({
      ...prev,
      points: prev.points + pointsAwarded,
      tier: (prev.points + pointsAwarded) > 2500 ? 'Diamond' : 'Gold',
    }));
  };


  // Quick setup script helper
  const loadPromoToCart = () => {
    // Add two products to cart instantly as diagnostic play
    const item1: CartItem = {
      product: PRODUCTS[0],
      quantity: 1,
      selectedSize: 'S (Slice)',
      selectedOption: '100% Ngọt',
    };
    const item2: CartItem = {
      product: PRODUCTS[2],
      quantity: 2,
      selectedSize: 'S (Slice)',
      selectedOption: 'Tiêu chuẩn',
    };
    setCart([item1, item2]);
    setActiveTab('checkout');
    triggerToast('🛒 Đã tải sẵn đơn Bánh kem + Tiramisu vào giỏ!');
  };

  const handleCopyVoucher = (code: string) => {
    triggerToast(`✓ Đã lưu mã: ${code} vào bộ nhớ tạm`);
  };


  // Selected theme colors values
  const getThemeColorClass = () => {
    if (themeColor === 'strawberry') return 'accent-[#F3839F] cursor-pointer';
    if (themeColor === 'chocolate') return 'accent-[#8B5A2B] cursor-pointer';
    return 'accent-[#C57A44] cursor-pointer';
  };

  const getThemeTextClass = () => {
    if (themeColor === 'strawberry') return 'text-[#F3839F]';
    if (themeColor === 'chocolate') return 'text-[#8B5A2B]';
    return 'text-[#C57A44]';
  };

  const getThemeBgClass = () => {
    if (themeColor === 'strawberry') return 'bg-[#F3839F]';
    if (themeColor === 'chocolate') return 'bg-[#8B5A2B]';
    return 'bg-[#C57A44]';
  };

  return (
    <div className="w-screen h-screen bg-[#FAF8F5] flex flex-col relative overflow-hidden select-none font-sans">
      
      {/* 1. Phone Top Status Bar (Carrier, Time, Wifi, Battery) */}
      <div className="h-9 bg-white px-5 flex items-center justify-between text-stone-700 text-[11px] font-bold select-none pt-1.5 font-sans shrink-0">
        <span>{currentTime}</span>
        <div className="flex items-center gap-1.5">
          <Signal size={12} className="stroke-[2.5]" />
          <span className="text-[10px]">VinaPhone</span>
          <Wifi size={12} className="stroke-[2.5]" />
          <div className="flex items-center gap-0.5">
            <Battery size={14} className="stroke-[2]" />
          </div>
        </div>
      </div>

      {/* 2. Zalo Mini App Navigation Header Bar */}
      <div className="h-12 bg-white border-b border-[#E8E2D9]/75 px-4 flex items-center justify-between relative select-none shrink-0">
        <div className="flex items-center gap-2">
          <div className={`w-6 h-6 rounded-full flex items-center justify-center font-bold text-[10.5px] text-white shadow-xs ${getThemeBgClass()}`}>
            S
          </div>
          <div>
            <h1 className="text-xs font-black font-sans tracking-tight text-stone-800 flex items-center gap-0.5">
              SaviBite
            </h1>
            <span className="text-[8.5px] font-medium text-stone-400 block -mt-0.5 leading-none">Welcome, {user.name}</span>
          </div>
        </div>

        {/* Simulated Zalo controls ('...' and 'X') */}
        <div className="flex items-center bg-stone-100 rounded-full py-1 px-2.5 border border-stone-200 shadow-inner-xs gap-3">
          <button
            onClick={() => setShowZaloMenu(!showZaloMenu)}
            className="p-0.5 hover:text-[#C57A44] text-stone-600 transition-colors"
            title="Menu tùy chọn"
          >
            <Ellipsis size={15} className="stroke-[2.5]" />
          </button>
          <div className="w-[1px] h-3 bg-stone-200" />
          <button
            onClick={() => {
              triggerToast('SaviBite Zalo: Đạt trạng thái toàn màn hình');
              setActiveTab('home');
            }}
            className="p-0.5 hover:text-amber-600 text-stone-600 transition-colors"
            title="Đặt lại trang chủ"
          >
            <X size={15} className="stroke-[2.5]" />
          </button>
        </div>
      </div>

      {/* 3. Screen Main Active Content Viewer */}
      <div className="flex-1 w-full overflow-hidden flex flex-col relative bg-[#FAF8F5]">
        <AnimatePresence mode="wait">
          {activeTab === 'home' && (
            <HomeView
              onSelectProduct={(product) => setSelectedProduct(product)}
              onSelectRestaurant={(restaurant) => setSelectedRestaurant(restaurant)}
              onSearchFocus={() => setIsSearchOpen(true)}
              activeCategory="all"
              setActiveCategory={(cat) => {
                setSelectedCategory(cat);
                setActiveTab('categories');
              }}
              user={user}
              onRequestLocation={() => setShowLocationDialog(true)}
              locationStatus={locationStatus}
            />
          )}

          {activeTab === 'categories' && (
            <CategoryView
              onSelectProduct={(product) => setSelectedProduct(product)}
              activeCategory={selectedCategory}
              setActiveCategory={setSelectedCategory}
            />
          )}

          {activeTab === 'cart' && (
            <CartView
              orders={orders}
            />
          )}

          {activeTab === 'checkout' && (
            <CheckoutView
              cart={cart}
              onUpdateQuantity={handleUpdateQuantity}
              onRemoveItem={handleRemoveItem}
              onPlaceOrder={handlePlaceOrder}
              onClearCart={() => setCart([])}
              user={user}
              locationStatus={locationStatus}
              onGoToOrders={() => setActiveTab('cart')}
              onBack={() => setActiveTab('home')}
            />
          )}

          {activeTab === 'mystery' && (
            <MysteryBoxView
              onAddToCart={handleAddToCart}
              user={user}
            />
          )}

          {activeTab === 'profile' && (
            <ProfileView
              user={user}
              orders={orders}
              onTriggerVoucherCopy={handleCopyVoucher}
            />
          )}
        </AnimatePresence>

        {/* 4. Overlay SearchView */}
        <AnimatePresence>
          {isSearchOpen && (
            <SearchView
              onBack={() => setIsSearchOpen(false)}
              onSelectProduct={(p) => {
                setSelectedProduct(p);
                setIsSearchOpen(false);
              }}
            />
          )}
        </AnimatePresence>

        {/* 4.5 Restaurant Detail Overlay */}
        <AnimatePresence>
          {selectedRestaurant && (
            <RestaurantDetailView
              restaurant={selectedRestaurant}
              onBack={() => setSelectedRestaurant(null)}
              onSelectProduct={(p) => {
                setSelectedProduct(p);
              }}
              cart={cart}
              onUpdateQuantity={handleUpdateQuantity}
              onRemoveItem={handleRemoveItem}
              onGoToCart={() => {
                setSelectedRestaurant(null);
                setActiveTab('checkout');
              }}
            />
          )}
        </AnimatePresence>

        {/* 5. Product Details Modal Customizer overlay */}
        <AnimatePresence>
          {selectedProduct && (
            <ProductDetailModal
              product={selectedProduct}
              onClose={() => setSelectedProduct(null)}
              onAddToCart={handleAddToCart}
            />
          )}
        </AnimatePresence>


        {/* 7. Simulated Zalo ... Header Options Menu Drawer overlay */}
        <AnimatePresence>
          {showZaloMenu && (
            <div className="absolute inset-0 bg-black/40 z-[45]" onClick={() => setShowZaloMenu(false)}>
              <motion.div
                initial={{ y: '100%' }}
                animate={{ y: 0 }}
                exit={{ y: '100%' }}
                className="absolute bottom-0 inset-x-0 bg-white rounded-t-3xl p-5 space-y-4 shadow-xl text-left text-stone-800"
                onClick={(e) => e.stopPropagation()}
              >
                <div className="flex justify-between items-center pb-2 border-b border-stone-100">
                  <span className="text-[10px] font-bold text-stone-400 uppercase tracking-widest">Tùy chọn Zalo Mini App</span>
                  <button onClick={() => setShowZaloMenu(false)}>
                    <X size={15} className="text-stone-400 hover:text-stone-700" />
                  </button>
                </div>

                {/* Integrated Demo Controls in Options Drawer for mobile/tablet screens */}
                <div className="space-y-3 bg-[#FAF8F5] p-3 rounded-2xl border border-[#E8E2D9]">
                  <span className="text-[9px] font-bold text-stone-400 uppercase tracking-widest block">Tính năng mô phỏng nhanh:</span>
                  <div>
                    <button
                      onClick={() => {
                        loadPromoToCart();
                        setShowZaloMenu(false);
                      }}
                      className="w-full py-2 px-3 bg-white hover:bg-stone-50 border border-stone-200 text-stone-700 font-bold text-[11px] rounded-xl transition-all shadow-xs"
                    >
                      🛒 Nạp ngẫu nhiên hàng vào giỏ
                    </button>
                  </div>
                  <div className="flex gap-2.5 pt-1.5 border-t border-stone-200">
                    <button
                      onClick={() => { setThemeColor('honey'); setShowZaloMenu(false); }}
                      className={`flex-1 py-1 px-1.5 rounded-md text-[9px] font-bold text-center border ${themeColor === 'honey' ? 'bg-[#C57A44] text-white border-[#C57A44]' : 'bg-white'}`}
                    >
                      Mật Ong
                    </button>
                    <button
                      onClick={() => { setThemeColor('strawberry'); setShowZaloMenu(false); }}
                      className={`flex-1 py-1 px-1.5 rounded-md text-[9px] font-bold text-center border ${themeColor === 'strawberry' ? 'bg-[#F3839F] text-white border-[#F3839F]' : 'bg-white'}`}
                    >
                      Dâu Tây
                    </button>
                    <button
                      onClick={() => { setThemeColor('chocolate'); setShowZaloMenu(false); }}
                      className={`flex-1 py-1 px-1.5 rounded-md text-[9px] font-bold text-center border ${themeColor === 'chocolate' ? 'bg-[#8B5A2B] text-white border-[#8B5A2B]' : 'bg-white'}`}
                    >
                      Sô Cô La
                    </button>
                  </div>
                </div>

                <div className="grid grid-cols-3 gap-2.5 pt-2">
                  <button
                    onClick={() => {
                      triggerToast('✓ Đã sao chép liên kết chia sẻ!');
                      setShowZaloMenu(false);
                    }}
                    className="bg-stone-50 hover:bg-stone-100 p-2.5 rounded-xl flex flex-col items-center justify-center text-center gap-1 transition-all text-stone-700"
                  >
                    <Share2 size={15} className="text-[#C57A44]" />
                    <span className="text-[10px] font-bold">Chia sẻ</span>
                  </button>
                  <button
                    onClick={() => {
                      triggerToast('✓ Đã sao chép URL applet!');
                      setShowZaloMenu(false);
                    }}
                    className="bg-stone-50 hover:bg-stone-100 p-2.5 rounded-xl flex flex-col items-center justify-center text-center gap-1 transition-all text-stone-700"
                  >
                    <Link2 size={15} className="text-[#C57A44]" />
                    <span className="text-[10px] font-bold">Sao chép Link</span>
                  </button>
                  <button
                    onClick={() => {
                      triggerToast('★ Cảm ơn bạn đã tặng chúng mình 5 sao!');
                      setShowZaloMenu(false);
                    }}
                    className="bg-stone-50 hover:bg-stone-100 p-2.5 rounded-xl flex flex-col items-center justify-center text-center gap-1 transition-all text-stone-700"
                  >
                    <Star size={15} className="text-[#C57A44] fill-current" />
                    <span className="text-[10px] font-bold">Đánh giá 5★</span>
                  </button>
                </div>

                <div className="bg-[#FAF8F5] p-3 rounded-xl border border-[#E8E2D9] text-[10px] text-stone-500 leading-normal flex items-start gap-2">
                  <Info size={14} className="text-[#C57A44] shrink-0 mt-0.5" />
                  <span>Đây là giao diện mô phỏng Zalo Mini App cho SaviBite Cake, phát triển dựa trên bộ thư viện ZaUI Component Kit hoàn chỉnh.</span>
                </div>
              </motion.div>
            </div>
          )}
        </AnimatePresence>

        {/* 8. Floating Toast alerts */}
        <AnimatePresence>
          {toastMessage && (
            <motion.div
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              exit={{ opacity: 0 }}
              className="absolute bottom-20 left-1/2 -translate-x-1/2 bg-stone-900/90 text-white text-[10.5px] font-medium py-2 px-4 rounded-xl shadow-lg z-[70] backdrop-blur-md w-[85%] text-center"
            >
              {toastMessage}
            </motion.div>
          )}
        </AnimatePresence>

      </div>

      {/* Global Sticky Cart Bar */}
      {cart.length > 0 && activeTab !== 'cart' && activeTab !== 'checkout' && !selectedRestaurant && (
        <div className="absolute bottom-16 left-0 right-0 max-w-[480px] mx-auto bg-white border-t border-stone-200 p-2.5 flex items-center justify-between z-40 shadow-[0_-4px_10px_rgba(0,0,0,0.05)] h-[60px]">
          <div className="flex items-center gap-3">
            <div className="relative ml-2">
               <ShoppingBag size={24} className="text-[#f24e1e]" />
               <span className="absolute -top-1.5 -right-2 bg-white text-[#f24e1e] border border-[#f24e1e] text-[9px] font-bold w-4 h-4 flex items-center justify-center rounded-full">
                 {cart.reduce((total, item) => total + item.quantity, 0)}
               </span>
            </div>
            <span className="text-[15px] font-bold text-[#f24e1e] ml-1">
              {cart.reduce((total, item) => {
                // Approximate unit price logic similar to MysteryBox
                // Since selectedOptions isn't fully detailed in cart item for price calc, just use product.price for now as fallback if needed, or actual price stored
                return total + (item.product.price * item.quantity);
              }, 0).toLocaleString('vi-VN')}đ
            </span>
          </div>
          <button onClick={() => setActiveTab('checkout')} className="bg-[#f24e1e] text-white px-6 py-2 rounded-lg text-sm font-medium active:bg-[#e04316] transition-colors">
            Tiếp tục
          </button>
        </div>
      )}

      {/* 5. Zalo Mini App bottom Navigation bar (Mockup 3/4) */}
      <div className="h-16 bg-white border-t border-[#E8E2D9] grid grid-cols-4 items-center select-none py-1.5 px-1 font-sans shrink-0">
        {/* Trang chủ tab */}
        <button
          onClick={() => setActiveTab('home')}
          className={`flex flex-col items-center gap-1 transition-all ${
            activeTab === 'home' ? getThemeTextClass() : 'text-stone-400 hover:text-stone-600'
          }`}
        >
          <Home size={15} className={activeTab === 'home' ? 'stroke-[2.5]' : 'stroke-1.5'} />
          <span className={`text-[9px] font-bold ${activeTab === 'home' ? 'font-black' : 'font-medium'}`}>Trang chủ</span>
        </button>

        {/* Mystery Box tab */}
        <button
          onClick={() => setActiveTab('mystery')}
          className={`flex flex-col items-center gap-1 transition-all relative ${
            activeTab === 'mystery' ? getThemeTextClass() : 'text-stone-400 hover:text-stone-600'
          }`}
        >
          <div className="relative">
            <Gift size={15} className={activeTab === 'mystery' ? 'stroke-[2.5]' : 'stroke-1.5'} />
            <span className="absolute -top-1 -right-1 bg-amber-500 w-2 h-2 rounded-full border border-white animate-pulse" />
          </div>
          <span className={`text-[9px] font-bold ${activeTab === 'mystery' ? 'font-black' : 'font-medium'}`}>Mystery Box</span>
        </button>

        {/* Đơn hàng tab */}
        <button
          onClick={() => setActiveTab('cart')}
          className={`flex flex-col items-center gap-1 transition-all relative ${
            activeTab === 'cart' ? getThemeTextClass() : 'text-stone-400 hover:text-stone-600'
          }`}
        >
          <div className="relative">
            <ShoppingBag size={15} className={activeTab === 'cart' ? 'stroke-[2.5]' : 'stroke-1.5'} />
          </div>
          <span className={`text-[9px] font-bold ${activeTab === 'cart' ? 'font-black' : 'font-medium'}`}>Đơn hàng</span>
        </button>

        {/* Cá nhân tab */}
        <button
          onClick={() => setActiveTab('profile')}
          className={`flex flex-col items-center gap-1 transition-all ${
            activeTab === 'profile' ? getThemeTextClass() : 'text-stone-400 hover:text-stone-600'
          }`}
        >
          <User size={15} className={activeTab === 'profile' ? 'stroke-[2.5]' : 'stroke-1.5'} />
          <span className={`text-[9px] font-bold ${activeTab === 'profile' ? 'font-black' : 'font-medium'}`}>Cá nhân</span>
        </button>
      </div>

    </div>
  );
}
