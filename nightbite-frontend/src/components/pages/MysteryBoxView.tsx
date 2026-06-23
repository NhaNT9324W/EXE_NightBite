import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { Gift, Sparkles, Check, ShoppingBag, HelpCircle, Clock, Lock, Unlock, AlertCircle } from 'lucide-react';
import { Product, CartItem, UserProfile } from '../../types';
import { Badge } from '../atoms/Badge';
import { MYSTERY_BOXES } from '../../demoData';

interface MysteryBoxViewProps {
  onAddToCart: (item: CartItem) => void;
  user: UserProfile;
}

export function MysteryBoxView({ onAddToCart, user }: MysteryBoxViewProps) {
  const [selectedBoxId, setSelectedBoxId] = useState<string | null>(null);
  const [selectedRibbon, setSelectedRibbon] = useState<string>('');
  const [selectedGreeting, setSelectedGreeting] = useState<string>('');
  const [showInquiryModal, setShowInquiryModal] = useState<boolean>(false);
  const [animationBoxId, setAnimationBoxId] = useState<string | null>(null);

  // Time-locked rules (21:00 - 22:00)
  const [currentTime, setCurrentTime] = useState<Date>(new Date());
  const [bypassTime, setBypassTime] = useState<boolean>(false);

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  const currentHour = currentTime.getHours();
  const realIsOpen = currentHour === 21; // 21:00 to 21:59 (inclusive 21:00 - 22:00)
  const isOpen = realIsOpen || bypassTime;

  const getCountdownMessage = () => {
    if (isOpen) {
      if (bypassTime) return 'Đang chạy mô phỏng khung giờ 21:30 (Cổng mở)';
      
      // Countdown to 22:00
      const target = new Date(currentTime);
      target.setHours(22, 0, 0, 0);
      const diffMs = target.getTime() - currentTime.getTime();
      if (diffMs <= 0) return 'Đang đóng cổng...';
      const mins = Math.floor(diffMs / 60000);
      const secs = Math.floor((diffMs % 60000) / 1000);
      return `Còn lại ${mins} phút ${secs} giây để đặt bánh!`;
    } else {
      // Countdown to 21:00 tonight or tomorrow
      const target = new Date(currentTime);
      if (currentHour >= 22) {
        target.setDate(target.getDate() + 1);
      }
      target.setHours(21, 0, 0, 0);
      const diffMs = target.getTime() - currentTime.getTime();
      const hrs = Math.floor(diffMs / 3600000);
      const mins = Math.floor((diffMs % 3600000) / 60000);
      const secs = Math.floor((diffMs % 60000) / 1000);
      return `Giỏ mở lại sau: ${hrs}g ${mins}ph ${secs}gi`;
    }
  };

  const handleOpenCustomize = (box: typeof MYSTERY_BOXES[0]) => {
    if (!isOpen) {
      alert("⚠️ Mystery Box chỉ mở bán từ 21:00 - 22:00 hàng ngày. Bạn có thể sử dụng nút 'Mở khoá Demo' phía trên để kiểm thử!");
      return;
    }
    setSelectedBoxId(box.id);
    setSelectedRibbon(box.options.ribbons[0]);
    setSelectedGreeting(box.options.greetings[0]);
  };

  const handleAddToCartSubmit = (box: typeof MYSTERY_BOXES[0]) => {
    setAnimationBoxId(box.id);
    
    setTimeout(() => {
      const productObj: Product = {
        id: box.id,
        name: `${box.name} (${box.badge})`,
        price: box.price,
        originalPrice: box.originalPrice,
        description: box.description,
        category: 'pastry',
        image: box.image,
        rating: box.rating,
        salesCount: box.salesCount,
        sizes: [box.badge]
      };

      const ribbonChoice = selectedBoxId === box.id ? selectedRibbon : box.options.ribbons[0];
      const greetingChoice = selectedBoxId === box.id ? selectedGreeting : box.options.greetings[0];

      const cartItem: CartItem = {
        product: productObj,
        quantity: 1,
        selectedSize: box.badge,
        selectedSweetness: `${ribbonChoice} ✦ ${greetingChoice}`,
      };

      onAddToCart(cartItem);
      setAnimationBoxId(null);
      setSelectedBoxId(null);
    }, 900);
  };

  return (
    <div className="flex-1 overflow-y-auto bg-[#FAF8F5] pb-12 text-[#2C2520]">
      {/* Dynamic Header Banner */}
      <div className="bg-gradient-to-b from-[#FAF1E6] to-[#FAF8F5] p-5 pb-3 text-center flex flex-col items-center border-b border-stone-200/40">
        <motion.div 
          animate={{ rotate: [0, -5, 5, -5, 0], scale: [1, 1.05, 1.05, 1] }}
          transition={{ repeat: Infinity, repeatDelay: 6, duration: 1 }}
          className="w-12 h-12 bg-[#C57A44]/10 rounded-full flex items-center justify-center mb-2.5 text-[#C57A44]"
        >
          <Gift size={26} className="animate-pulse" />
        </motion.div>
        <h2 className="text-base font-black uppercase text-stone-800 tracking-tight flex items-center gap-1.5 justify-center">
          SaviBite Mystery Box
          <Sparkles size={15} className="text-[#C57A44]" />
        </h2>
        <p className="text-[10px] text-stone-500 max-w-xs mt-0.5 leading-relaxed">
          Mở khóa hộp bánh diệu kỳ ngẫu nhiên với cực nhiều hương vị quyến rũ được làm mới mỗi ngày.
        </p>

        <button 
          onClick={() => setShowInquiryModal(true)}
          className="mt-2.5 inline-flex items-center gap-1 text-[9.5px] font-bold text-[#C57A44] hover:underline bg-[#FAF1E6]/70 px-2.5 py-1 rounded-full border border-[#C57A44]/15"
        >
          <HelpCircle size={11} />
          Mystery Box hoạt động như thế nào?
        </button>
      </div>

      {/* Schedule Banner Info Control Widget */}
      <div className="mx-4 mt-4 max-w-sm sm:mx-auto bg-white rounded-2xl border border-stone-200/80 p-3.5 flex flex-col gap-2.5 shadow-xs font-sans">
        <div className="flex justify-between items-center">
          <div className="flex items-center gap-1.5">
            <Clock size={15} className={isOpen ? 'text-[#C57A44]' : 'text-stone-400'} />
            <span className="text-xs font-black text-stone-800 uppercase tracking-wide">
              Khung giờ mở hộp
            </span>
          </div>
          <span className="text-[10px] text-stone-550 font-black bg-stone-100 px-2 py-0.5 rounded-md border border-stone-200/50">
            21:00 - 22:00
          </span>
        </div>

        <div className={`p-2.5 rounded-xl border flex items-center justify-between gap-3 ${
          isOpen 
            ? 'bg-emerald-50/70 border-emerald-100/70 text-emerald-800' 
            : 'bg-amber-50/50 border-amber-100/60 text-amber-900'
        }`}>
          <div className="flex items-center gap-2">
            <span className="relative flex h-2 w-2">
              <span className={`animate-ping absolute inline-flex h-full w-full rounded-full opacity-75 ${isOpen ? 'bg-emerald-400' : 'bg-amber-400'}`}></span>
              <span className={`relative inline-flex rounded-full h-2 w-2 ${isOpen ? 'bg-emerald-500' : 'bg-amber-550'}`}></span>
            </span>
            <div className="text-[11px] leading-tight">
              <p className="font-extrabold font-sans">
                {isOpen 
                  ? 'Cổng Mystery Box đang mở rộng!' 
                  : 'Bếp hẹn đón rước lúc 21:00'
                }
              </p>
              <p className="text-[10px] text-stone-500 mt-1 font-sans font-medium">
                {getCountdownMessage()}
              </p>
            </div>
          </div>
          
          <div className="shrink-0">
            {isOpen ? (
              <Unlock size={14} className="text-emerald-600 animate-pulse" />
            ) : (
              <Lock size={14} className="text-amber-600" />
            )}
          </div>
        </div>

        {/* Dynamic simulator helper tool for review/admin testing */}
        <div className="bg-[#FAF1E6]/50 rounded-xl p-2 border border-[#E8E2D9]/60 flex items-center justify-between text-[10px] text-stone-600 font-sans">
          <span className="flex items-center gap-1 font-bold text-stone-700">
            <Sparkles size={11} className="text-[#C57A44]" />
            Dành cho Thử nghiệm/Demo:
          </span>
          <button
            onClick={() => {
              setBypassTime(!bypassTime);
              setSelectedBoxId(null);
            }}
            className={`px-2.5 py-1 rounded-lg text-[9px] font-black tracking-wider transition-all uppercase flex items-center gap-1 shadow-xs ${
              bypassTime 
                ? 'bg-[#C57A44] text-white' 
                : 'bg-white hover:bg-stone-50 border border-stone-200 text-stone-600'
            }`}
          >
            {bypassTime ? 'Tắt mô phỏng' : 'Mở khoá Demo'}
          </button>
        </div>
      </div>

      {/* List of Mystery Boxes */}
      <div className="mx-4 mt-4 space-y-4 max-w-sm sm:mx-auto">
        {MYSTERY_BOXES.map((box) => {
          const isSelected = selectedBoxId === box.id;
          const isAnimating = animationBoxId === box.id;

          return (
            <div 
              key={box.id}
              className={`bg-white rounded-2xl border transition-all overflow-hidden ${
                isSelected 
                  ? 'border-[#C57A44] shadow-sm ring-1 ring-[#C57A44]/30' 
                  : 'border-stone-200/70 hover:border-stone-300 shadow-xs'
              }`}
            >
              {/* Product Info Layout */}
              <div className="flex p-3 gap-3">
                {/* Square Box Cover Artwork */}
                <div className="w-20 h-20 rounded-xl overflow-hidden bg-stone-100 flex-shrink-0 relative border border-stone-100">
                  <img 
                    src={box.image} 
                    alt={box.name}
                    className="w-full h-full object-cover transition-transform duration-500 hover:scale-105"
                    referrerPolicy="no-referrer"
                  />

                </div>

                {/* Text Content */}
                <div className="flex-1 min-w-0 flex flex-col justify-between">
                  <div>
                    <div className="flex items-center gap-1.5 flex-wrap">
                      <h4 className="text-sm font-black text-stone-900 tracking-tight">
                        {box.name}
                      </h4>
                      <span className={`text-[8.5px] px-1.5 py-0.5 rounded-full border font-black uppercase tracking-wider ${box.bgBadge}`}>
                        {box.badge}
                      </span>
                    </div>
                    <p className="text-[10px] text-stone-500 mt-1 line-clamp-2 leading-relaxed">
                      {box.description}
                    </p>
                  </div>

                  {/* Pricing and Action */}
                  <div className="flex items-center justify-between mt-1">
                    <div className="flex items-baseline gap-1.5">
                      <span className="text-sm font-black text-[#C57A44]">
                        {box.price.toLocaleString()}đ
                      </span>
                      {box.originalPrice && (
                        <span className="text-[9px] text-stone-400 line-through">
                          {box.originalPrice.toLocaleString()}đ
                        </span>
                      )}
                    </div>

                    {!isOpen ? (
                      <button
                        onClick={() => handleOpenCustomize(box)}
                        className="text-[10px] font-bold bg-stone-200/80 text-stone-550 border border-stone-300/30 px-3 py-1.5 rounded-xl flex items-center gap-1 shadow-xs transition-all active:scale-[0.98]"
                      >
                        <Lock size={11} />
                        Khóa 21:00
                      </button>
                    ) : !isSelected ? (
                      <button
                        onClick={() => handleOpenCustomize(box)}
                        className="text-[10px] font-black bg-[#C57A44] text-white hover:bg-amber-800 px-3 py-1.5 rounded-xl transition-all flex items-center gap-1 shadow-xs"
                      >
                        <ShoppingBag size={11} className="stroke-[2.5]" />
                        Mở cấu hình
                      </button>
                    ) : (
                      <span className="text-[10px] font-bold text-amber-600 flex items-center gap-1 bg-[#FAF1E6]/80 px-2 py-0.5 rounded-lg border border-amber-200/50">
                        Đang thiết lập
                      </span>
                    )}
                  </div>
                </div>
              </div>

              {/* Expansion Customizers Form */}
              <AnimatePresence>
                {isSelected && (
                  <motion.div
                    initial={{ height: 0, opacity: 0 }}
                    animate={{ height: 'auto', opacity: 1 }}
                    exit={{ height: 0, opacity: 0 }}
                    transition={{ duration: 0.25 }}
                    className="border-t border-stone-100 bg-[#FAFBFD] p-3 space-y-3 text-xs"
                  >
                    {/* Choose Ribbon Ribbon Decoration */}
                    <div className="space-y-1">
                      <span className="text-[9.5px] uppercase font-bold text-stone-400 tracking-wider">
                        1. Kiểu đóng gói tinh tế:
                      </span>
                      <div className="grid grid-cols-2 gap-1.5">
                        {box.options.ribbons.map((rib) => (
                          <button
                            key={rib}
                            onClick={() => setSelectedRibbon(rib)}
                            className={`py-1.5 px-2 text-[10px] rounded-lg text-left border transition-all flex items-center justify-between ${
                              selectedRibbon === rib 
                                ? 'border-[#C57A44] bg-[#C57A44]/5 text-[#C57A44] font-bold'
                                : 'border-stone-200 bg-white text-stone-600 hover:border-stone-300'
                            }`}
                          >
                            <span className="truncate">{rib}</span>
                            {selectedRibbon === rib && <Check size={10} className="stroke-[3]" />}
                          </button>
                        ))}
                      </div>
                    </div>

                    {/* Choose Message Card Ribbon */}
                    <div className="space-y-1">
                      <span className="text-[9.5px] uppercase font-bold text-stone-400 tracking-wider">
                        2. Lời nhắn đính kèm trên nắp hộp:
                      </span>
                      <div className="grid grid-cols-2 gap-1.5">
                        {box.options.greetings.map((greet) => (
                          <button
                            key={greet}
                            onClick={() => setSelectedGreeting(greet)}
                            className={`py-1.5 px-2 text-[10px] rounded-lg text-left border transition-all flex items-center justify-between ${
                              selectedGreeting === greet 
                                ? 'border-[#C57A44] bg-[#C57A44]/5 text-[#C57A44] font-bold'
                                : 'border-stone-200 bg-white text-stone-600 hover:border-stone-300'
                            }`}
                          >
                            <span className="truncate">{greet}</span>
                            {selectedGreeting === greet && <Check size={10} className="stroke-[3]" />}
                          </button>
                        ))}
                      </div>
                    </div>

                    {/* Control Submit Actions */}
                    <div className="flex gap-2 pt-2 border-t border-stone-100">
                      <button
                        onClick={() => setSelectedBoxId(null)}
                        className="flex-1 py-2 rounded-xl text-[10px] font-black border border-stone-200 hover:bg-stone-50 text-stone-500 tracking-wider bg-white transition-all uppercase"
                      >
                        Đóng lại
                      </button>
                      <button
                        onClick={() => handleAddToCartSubmit(box)}
                        disabled={isAnimating}
                        className="flex-2 py-2 rounded-xl bg-[#C57A44] hover:bg-amber-800 text-white font-black text-[10.1px] tracking-wider transition-all shadow-xs flex items-center justify-center gap-1.5 uppercase"
                      >
                        {isAnimating ? (
                          <span className="flex items-center gap-1">
                            <svg className="animate-spin h-3.5 w-3.5 text-white" fill="none" viewBox="0 0 24 24">
                              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
                            </svg>
                            Đang thắt nơ...
                          </span>
                        ) : (
                          <>
                            <ShoppingBag size={11} className="stroke-[2.5]" />
                            Xếp vào Giỏ {box.price.toLocaleString()}đ
                          </>
                        )}
                      </button>
                    </div>
                  </motion.div>
                )}
              </AnimatePresence>

              {/* Instant purchase feedback animation trigger wrapper */}
              {isAnimating && (
                <div className="bg-[#EAB308]/10 text-[#854D0E] text-[10px] font-bold p-2 text-center animate-pulse border-t border-[#EAB308]/20 flex items-center justify-center gap-1.5">
                  <Gift size={12} className="animate-bounce" />
                  Món quà Savi Mystery Box đã được niêm phong ruy-băng đỏ tươi tắn!
                </div>
              )}
            </div>
          );
        })}
      </div>

      {/* Decorative Savi Trust Badges Section */}
      <div className="mx-4 mt-4 max-w-sm sm:mx-auto">
        <div className="bg-[#FAF1E6]/40 rounded-2xl p-3 border border-[#E8E2D9] text-stone-700 space-y-2.5">
          <div className="flex items-center gap-1.5 text-xs font-bold uppercase tracking-wider text-stone-800">
            <Sparkles size={13} className="text-[#C57A44]" />
            <span>Đảm bảo từ cốt nhà SaviBite:</span>
          </div>
          <div className="space-y-2 text-[10px] leading-relaxed text-stone-500 font-sans">
            <div className="flex items-start gap-1">
              <span className="text-[#C57A44] font-bold">✓</span>
              <span><strong>Giá trị vượt mức:</strong> Bánh sừng bò, bánh gato, tiramisu bên trong luôn có giá gốc tối thiểu bằng hoặc cao hơn 25% - 40% so với giá bán Mystery Box.</span>
            </div>
            <div className="flex items-start gap-1">
              <span className="text-[#C57A44] font-bold">✓</span>
              <span><strong>Mới nướng 100%:</strong> Tuyệt đối không dùng bánh cũ. Tất cả Mystery Box đều được thợ bánh lành nghề bọc gói và giao trong ngày.</span>
            </div>
          </div>
        </div>
      </div>

      {/* Mystery Box Explanation Inquiry Modal Dialog */}
      <AnimatePresence>
        {showInquiryModal && (
          <div className="absolute inset-0 bg-black/40 z-[50] flex items-center justify-center p-5 animate-fade-in" onClick={() => setShowInquiryModal(false)}>
            <motion.div
              initial={{ scale: 0.95, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.95, opacity: 0 }}
              className="bg-white rounded-2xl p-5 space-y-4 max-w-sm shadow-xl text-left text-stone-800"
              onClick={(e) => e.stopPropagation()}
            >
              <div className="flex justify-between items-center pb-2 border-b border-stone-105">
                <span className="text-[11px] font-black uppercase text-stone-800 tracking-widest flex items-center gap-1">
                  <Gift size={13} className="text-[#C57A44]" />
                  Cách thức mua Mystery Box
                </span>
                <button onClick={() => setShowInquiryModal(false)} className="text-stone-400 hover:text-stone-700 font-black text-sm">
                  ×
                </button>
              </div>

              <div className="space-y-3 text-[11px] leading-relaxed text-stone-600 font-sans">
                <p>
                  <strong>Tên gọi chung duy nhất:</strong> Toàn bộ các gói quà ở màn hình này đều có tên hiển thị trong Đơn hàng là <strong>Mystery Box</strong>.
                </p>
                <p>
                  <strong>Cấu hình ngẫu nhiên:</strong> Bạn sẽ nhận được món quà bất ngờ khớp với khung giá trị (chắc chắn là các loại Gato thượng hạng, Croissant bơ Pháp ngàn lớp, hay trà mát cam sả đặc sản).
                </p>
                <p>
                  <strong>Niêm phong &amp; Đóng gói:</strong> Bạn có thể tự chọn ruy-băng thanh lịch nơ mộc mạc và chọn thiệp lời chúc để SaviBite in sẵn đặt lên trên nắp hộp gửi tặng bạn bè, người thương.
                </p>
              </div>

              <button
                onClick={() => setShowInquiryModal(false)}
                className="w-full py-2.5 bg-[#C57A44] hover:bg-amber-800 text-white font-black text-[11px] tracking-wider rounded-xl uppercase transition-all shadow-xs"
              >
                Đã hiểu, mở hộp ngọt ngay!
              </button>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </div>
  );
}
