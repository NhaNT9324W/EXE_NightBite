import React, { useState } from 'react';
import { CartItem, Order, UserProfile } from '../../types';
import { PROMO_VOUCHERS } from '../../demoData';
import { ShoppingBag, Tag, CheckCircle2, ArrowRight, ShieldCheck, ChevronLeft } from 'lucide-react';
import { motion } from 'motion/react';
import { Badge } from '../atoms/Badge';
import { CartItemRow } from '../molecules/CartItemRow';

interface CheckoutViewProps {
  cart: CartItem[];
  onUpdateQuantity: (index: number, delta: number) => void;
  onRemoveItem: (index: number) => void;
  onPlaceOrder: (order: Order) => void;
  onClearCart: () => void;
  user: UserProfile;
  locationStatus: 'idle' | 'allowed' | 'denied';
  onGoToOrders: () => void;
  onBack: () => void;
}

export default function CheckoutView({
  cart,
  onUpdateQuantity,
  onRemoveItem,
  onPlaceOrder,
  onClearCart,
  user,
  locationStatus,
  onGoToOrders,
  onBack
}: CheckoutViewProps) {
  const [promoCode, setPromoCode] = useState('');
  const [appliedPromo, setAppliedPromo] = useState<typeof PROMO_VOUCHERS[0] | null>(null);
  const [promoError, setPromoError] = useState('');
  const [promoSuccessMsg, setPromoSuccessMsg] = useState('');

  // Shipping & Checkout form info
  const [customerName, setCustomerName] = useState(user.name);
  const [customerPhone, setCustomerPhone] = useState(user.phone.replace(' *** ', '888'));
  const [checkoutNote, setCheckoutNote] = useState('');
  const [isOrdered, setIsOrdered] = useState(false);
  const [placedOrderDetails, setPlacedOrderDetails] = useState<Order | null>(null);

  // Subtotal calculations
  const subtotal = cart.reduce((sum, item) => {
    // Check if item size multipliers apply
    const sizeMultiplier = item.selectedSize.includes('lớn') ? 1.5 : item.selectedSize.includes('đặc biệt') ? 1.2 : 1;
    const itemPrice = Math.round(item.product.price * sizeMultiplier);
    return sum + (itemPrice * item.quantity);
  }, 0);

  // Apply Voucher Promo calculations
  let discountAmount = 0;
  if (appliedPromo) {
    if ('discountPercent' in appliedPromo && appliedPromo.discountPercent) {
      discountAmount = Math.round((subtotal * (appliedPromo as any).discountPercent) / 100);
    } else if (appliedPromo.discountValue) {
      discountAmount = appliedPromo.discountValue;
    }
  }

  const finalTotal = Math.max(0, subtotal - discountAmount);

  const handleApplyPromo = (code: string) => {
    const cleanCode = code.toUpperCase().trim();
    if (!cleanCode) return;

    const voucher = PROMO_VOUCHERS.find(v => v.code === cleanCode);
    if (!voucher) {
      setPromoError('Mã giảm giá không tồn tại');
      setPromoSuccessMsg('');
      setAppliedPromo(null);
      return;
    }

    if (subtotal < voucher.minOrder) {
      setPromoError(`Đơn hàng tối thiểu ${voucher.minOrder.toLocaleString('vi-VN')}đ để sử dụng mã`);
      setPromoSuccessMsg('');
      setAppliedPromo(null);
      return;
    }

    setPromoError('');
    setAppliedPromo(voucher);
    setPromoSuccessMsg(`Áp dụng thành công: ${voucher.description}`);
  };

  const submitCheckout = (e?: React.SyntheticEvent) => {
    if (e) e.preventDefault();
    if (cart.length === 0) return;

    const newOrder: Order = {
      id: `SAVI-${Math.floor(1000 + Math.random() * 9000)}`,
      customerName,
      phoneNumber: customerPhone,
      address: 'Nhận trực tiếp tại cửa hàng',
      note: checkoutNote,
      items: [...cart],
      totalAmount: subtotal,
      discount: discountAmount,
      finalAmount: finalTotal,
      status: 'pending',
      createdAt: new Date().toLocaleTimeString('vi-VN') + ' ' + new Date().toLocaleDateString('vi-VN'),
    };

    onPlaceOrder(newOrder);
    setPlacedOrderDetails(newOrder);
    setIsOrdered(true);
    onClearCart();
  };

  const quickSelectVoucher = (code: string) => {
    setPromoCode(code);
    handleApplyPromo(code);
  };

  // If order is completed successfully
  if (isOrdered && placedOrderDetails) {
    return (
      <div className="flex-1 overflow-y-auto bg-[#FAF8F5] pb-2 px-5 text-[#2C2520] flex flex-col items-center justify-center py-10">
        <motion.div
          initial={{ scale: 0.8, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ type: 'spring', damping: 15 }}
          className="text-center space-y-4 bg-white rounded-3xl p-6 shadow-md border border-[#E8E2D9] max-w-sm w-full"
        >
          <div className="w-16 h-16 bg-emerald-50 text-emerald-500 rounded-full flex items-center justify-center mx-auto mb-2 border border-emerald-100">
            <CheckCircle2 size={36} className="stroke-[2.5] animate-pulse" />
          </div>

          <Badge variant="success">
            Đặt Đơn Thành Công Savi UI
          </Badge>

          <h3 className="text-lg font-black font-sans leading-tight">Cảm ơn bạn đã đặt món!</h3>
          <p className="text-xs text-stone-500 leading-relaxed text-balance">
            Mã đơn hàng: <strong className="text-stone-800">{placedOrderDetails.id}</strong>. Nhà hàng đang xử lý đơn hàng của bạn.
          </p>

          {/* Quick summary box */}
          <div className="bg-stone-50 rounded-2xl p-4 text-left border border-stone-200/50 space-y-1.5 text-xs">
            <div className="flex justify-between border-b border-stone-200/50 pb-1.5 mb-1.5 font-bold">
              <span>Nội dung đơn</span>
              <span>Tổng tiền đơn</span>
            </div>
            {placedOrderDetails.items.map((item, index) => (
              <div key={index} className="flex justify-between text-stone-600 font-sans">
                <span className="max-w-[70%] truncate">
                  {item.quantity}x {item.product.name} ({item.selectedSize})
                </span>
                <span>
                  {Math.round(item.product.price * (item.selectedSize.includes('lớn') ? 1.5 : item.selectedSize.includes('đặc biệt') ? 1.2 : 1) * item.quantity).toLocaleString('vi-VN')}đ
                </span>
              </div>
            ))}
            <div className="flex justify-between pt-2 border-t border-dotted border-stone-300 font-bold text-stone-800">
              <span>Đã thanh toán (COD):</span>
              <span className="text-[#C57A44] text-sm">
                {placedOrderDetails.finalAmount.toLocaleString('vi-VN')}đ
              </span>
            </div>
          </div>

          <div className="bg-[#FAF8F5] p-3 rounded-xl flex items-center gap-2 border border-[#E8E2D9]/60 text-stone-500 text-[10px] text-left">
            <ShieldCheck size={16} className="text-[#C57A44] shrink-0" />
            <span>Đơn hàng của bạn đã tích lũy thêm <strong>+{Math.round(placedOrderDetails.finalAmount / 1000)} SaviPoints</strong> vào tài khoản thành viên Gold.</span>
          </div>

          <div className="flex gap-2 w-full mt-4">
            <button
              onClick={() => {
                setIsOrdered(false);
                setPlacedOrderDetails(null);
                onGoToOrders();
              }}
              className="flex-1 py-3 bg-stone-100 hover:bg-stone-200 text-stone-700 font-bold text-xs rounded-xl transition-all active:scale-[0.98]"
            >
              Xem đơn hàng
            </button>
          </div>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="flex-1 flex flex-col bg-[#FAF8F5] pb-2 text-[#2C2520] h-full overflow-hidden">
      <div className="flex items-center gap-3 bg-white sticky top-0 z-10 border-b border-[#E8E2D9] shrink-0 p-4">
        <button onClick={onBack} className="p-1 rounded-full bg-stone-100 hover:bg-stone-200 transition-colors">
          <ChevronLeft size={20} className="text-stone-600" />
        </button>
        <h2 className="text-sm font-bold text-stone-800">Xác nhận đơn hàng</h2>
      </div>

      <div className="flex-1 overflow-y-auto">
        <>
          {cart.length === 0 ? (
            <div className="px-6 py-16 text-center space-y-4">
              <div className="w-16 h-16 bg-[#E8E2D9]/40 rounded-full flex items-center justify-center mx-auto text-stone-400">
                <ShoppingBag size={28} />
              </div>
              <div>
                <h3 className="text-sm font-bold text-stone-700">Giỏ hàng của bạn đang trống</h3>
                <p className="text-xs text-stone-400 max-w-xs mx-auto mt-1 leading-relaxed">
                  Hãy thêm món ăn vào giỏ hàng trước khi thanh toán nhé!
                </p>
              </div>
            </div>
          ) : (
            <div className="p-4 space-y-4">
        {/* Cart Item Cards list using CartItemRow molecule */}
        <div className="space-y-2.5">
            {cart.map((item, index) => (
              <div key={index}>
                <CartItemRow
                  item={item}
                  index={index}
                  onUpdateQuantity={onUpdateQuantity}
                  onRemoveItem={onRemoveItem}
                />
              </div>
            ))}
          </div>

          {/* Saved Voucher quick click section */}
          <div className="bg-white rounded-2xl p-3 border border-stone-200/50 space-y-2">
            <div className="flex items-center gap-1.5 text-xs font-bold text-stone-700">
              <Tag size={12} className="text-[#C57A44]" />
              <span>Ví voucher ưu đãi có sẵn:</span>
            </div>
            <div className="flex gap-1.5 overflow-x-auto pb-1 no-scrollbar font-sans">
              {PROMO_VOUCHERS.map((v) => (
                <button
                  key={v.code}
                  onClick={() => quickSelectVoucher(v.code)}
                  className={`px-2.5 py-1 rounded-lg text-[10px] border shrink-0 transition-all ${
                    appliedPromo?.code === v.code
                      ? 'bg-[#C57A44] text-white border-[#C57A44]'
                      : 'bg-[#FDFBF7] text-[#C57A44] border-[#E8E2D9] hover:bg-[#F7ECE1]'
                  }`}
                >
                  {v.code}
                </button>
              ))}
            </div>
          </div>

          {/* Manual Promo Code Form */}
          <div className="bg-white rounded-2xl p-3 border border-stone-200/50">
            <div className="flex items-center gap-2 font-sans">
              <input
                type="text"
                placeholder="Nhập mã giảm giá (MUA1TANG1...)"
                value={promoCode}
                onChange={(e) => setPromoCode(e.target.value)}
                className="flex-1 px-3 py-2 bg-stone-50 rounded-xl text-xs border border-stone-200 focus:outline-[#C57A44] placeholder-stone-400 font-sans tracking-wider"
              />
              <button
                onClick={() => handleApplyPromo(promoCode)}
                className="px-3.5 py-2 bg-[#C57A44] hover:bg-[#B46A36] text-white text-xs font-bold rounded-xl transition-all font-sans"
              >
                Áp Dụng
              </button>
            </div>
            {promoError && <p className="text-red-500 text-[10px] mt-1.5 font-sans">✕ {promoError}</p>}
            {promoSuccessMsg && <p className="text-emerald-600 text-[10px] mt-1.5 font-sans">✓ {promoSuccessMsg}</p>}
          </div>

          {/* Delivery Method Picker */}
          <div className="bg-white rounded-2xl p-3.5 border border-stone-200/50 space-y-3 font-sans">
            {/* Simulated Checkout Form */}
            <form onSubmit={submitCheckout} className="space-y-3 pt-2">
              <div className="space-y-3">
                <div className="space-y-1">
                  <label className="text-[10px] font-bold text-stone-400 uppercase">Người lấy</label>
                  <input
                    type="text"
                    required
                    value={customerName}
                    onChange={(e) => setCustomerName(e.target.value)}
                    className="w-full px-3 py-2 bg-stone-50 rounded-lg text-xs border border-stone-200 focus:outline-[#C57A44] font-medium"
                  />
                </div>
                <div className="space-y-1">
                  <label className="text-[10px] font-bold text-stone-400 uppercase">Điện thoại</label>
                  <input
                    type="text"
                    required
                    value={customerPhone}
                    onChange={(e) => setCustomerPhone(e.target.value)}
                    className="w-full px-3 py-2 bg-stone-50 rounded-lg text-xs border border-stone-200 focus:outline-[#C57A44] font-medium"
                  />
                </div>
              </div>

              <div className="space-y-1">
                <label className="text-[10px] font-bold text-stone-400 uppercase">Lời nhắn cho quán</label>
                <input
                  type="text"
                  placeholder="Không hành, nhiều ớt..."
                  value={checkoutNote}
                  onChange={(e) => setCheckoutNote(e.target.value)}
                  className="w-full px-3 py-2 bg-stone-50 rounded-lg text-xs border border-stone-200 focus:outline-[#C57A44]"
                />
              </div>

              {/* Order calculations block */}
              <div className="bg-[#FAF8F5] rounded-2xl p-4 border border-stone-200/50 pt-3 text-stone-600 font-medium space-y-2 mt-4 text-xs font-sans">
                <div className="flex justify-between">
                  <span>Tiền đồ ăn:</span>
                  <span className="text-stone-800 font-sans">{subtotal.toLocaleString('vi-VN')}đ</span>
                </div>
                {discountAmount > 0 && (
                  <div className="flex justify-between text-red-500 font-semibold">
                    <span>Giảm giá voucher:</span>
                    <span className="font-sans">-{discountAmount.toLocaleString('vi-VN')}đ</span>
                  </div>
                )}
                <div className="flex justify-between pt-2.5 border-t border-[#E8E2D9] font-black text-stone-800 text-sm">
                  <span>Tổng cộng thanh toán:</span>
                  <span className="text-[#C57A44] font-bold text-base font-sans">{finalTotal.toLocaleString('vi-VN')}đ</span>
                </div>
                <div className="flex items-center gap-1 text-[10px] text-stone-400 font-normal">
                  <span>Phương thức:</span>
                  <span className="font-bold text-stone-500">Thanh toán khi nhận hàng (COD)</span>
                </div>
              </div>

              {/* Submit checkout button */}
              <button
                type="submit"
                className="w-full py-4 bg-[#C57A44] hover:bg-[#B46A36] text-white font-bold text-sm rounded-xl transition-all shadow-md shadow-[#C57A44]/15 active:scale-[0.98] mt-2 flex items-center justify-center gap-2 uppercase"
              >
                Xác nhận &amp; Đặt món
                <ArrowRight size={16} />
              </button>
            </form>
          </div>
        </div>
          )}
        </>
      </div>
    </div>
  );
}
