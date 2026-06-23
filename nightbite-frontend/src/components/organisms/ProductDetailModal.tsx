import React, { useState } from 'react';
import { Product, CartItem } from '../../types';
import { X, ShoppingBag, Info } from 'lucide-react';
import { motion } from 'motion/react';
import { Badge } from '../atoms/Badge';
import { RatingBadge } from '../atoms/RatingBadge';
import { QuantitySelector } from '../atoms/QuantitySelector';

interface ProductDetailModalProps {
  product: Product | null;
  onClose: () => void;
  onAddToCart: (cartItem: CartItem) => void;
}

export function ProductDetailModal({ product, onClose, onAddToCart }: ProductDetailModalProps) {
  if (!product) return null;

  const [quantity, setQuantity] = useState(1);
  const [selectedSize, setSelectedSize] = useState(product.sizes ? product.sizes[0] : 'Standard');
  const [selectedSweetness, setSelectedSweetness] = useState(product.sweetnessLevels ? product.sweetnessLevels[0] : 'Standard');

  const handleDecrease = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  const handleIncrease = () => {
    setQuantity(quantity + 1);
  };

  const handleAdd = () => {
    onAddToCart({
      product,
      quantity,
      selectedSize,
      selectedSweetness,
    });
    onClose();
  };

  const priceMultiplier = selectedSize.includes('Ổ lớn') ? 4.9 : selectedSize.includes('Ổ nhỏ') ? 3.5 : 1;
  const currentPrice = Math.round(product.price * priceMultiplier);

  return (
    <div className="absolute inset-0 bg-black/60 z-50 flex flex-col justify-end">
      {/* Semi-transparent overlay to close */}
      <div className="absolute inset-0 -z-10" onClick={onClose} />

      {/* Sheet Content */}
      <motion.div
        initial={{ y: '100%' }}
        animate={{ y: 0 }}
        exit={{ y: '100%' }}
        transition={{ type: 'spring', damping: 25, stiffness: 250 }}
        className="bg-[#FAF8F5] rounded-t-3xl max-h-[85%] overflow-y-auto shadow-2xl flex flex-col pb-6 text-[#2C2520]"
      >
        {/* Header Drag Handle/Bar */}
        <div className="flex justify-center py-3">
          <div className="w-12 h-1.5 bg-[#E8E2D9] rounded-full" />
        </div>

        {/* Product Image & Main details */}
        <div className="px-5 pb-4 flex justify-between items-start">
          <div className="flex-1 pr-4">
            <Badge variant="primary" className="mb-1">
              SaviBite Premium
            </Badge>
            <h3 className="text-lg font-bold font-sans text-[#2C2520] leading-tight">
              {product.name}
            </h3>
            <div className="flex items-center gap-4 mt-1.5 text-xs text-stone-500">
              <RatingBadge rating={product.rating} />
              <span>Đã bán {product.salesCount}+</span>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 bg-[#E8E2D9]/60 hover:bg-[#E8E2D9] rounded-full text-stone-600 transition-colors"
          >
            <X size={18} />
          </button>
        </div>

        {/* Product Image */}
        <div className="px-5 mb-4">
          <div className="relative aspect-[16:10] rounded-xl overflow-hidden shadow-inner-md">
            <img
              src={product.image}
              alt={product.name}
              referrerPolicy="no-referrer"
              className="w-full h-full object-cover"
            />
            {product.originalPrice && (
              <div className="absolute top-3 left-3">
                <Badge variant="danger">
                  GIẢM {Math.round(((product.originalPrice - product.price) / product.originalPrice) * 105)}%
                </Badge>
              </div>
            )}
          </div>
        </div>

        {/* Scrollable Customization Content */}
        <div className="px-5 space-y-5 pb-4 flex-1">
          {/* Description */}
          <div>
            <p className="text-sm text-stone-600 leading-relaxed font-serif italic text-balance">
              "{product.description}"
            </p>
          </div>

          {/* Size Selections */}
          {product.sizes && product.sizes.length > 0 && (
            <div className="space-y-2">
              <h4 className="text-xs font-bold uppercase tracking-wider text-stone-500">Kích Cỡ Bánh:</h4>
              <div className="flex flex-wrap gap-2">
                {product.sizes.map((size) => (
                  <button
                    key={size}
                    onClick={() => setSelectedSize(size)}
                    className={`px-4 py-2 rounded-xl text-xs font-medium border transition-all ${
                      selectedSize === size
                        ? 'border-[#C57A44] bg-[#C57A44] text-white shadow-sm'
                        : 'border-[#E8E2D9] bg-white text-[#2C2520] hover:bg-stone-50'
                    }`}
                  >
                    {size} {size.includes('Ổ nhỏ') && '(+25k)'} {size.includes('Ổ lớn') && '(+115k)'}
                  </button>
                ))}
              </div>
            </div>
          )}

          {/* Sweetness Selections */}
          {product.sweetnessLevels && product.sweetnessLevels.length > 0 && (
            <div className="space-y-2">
              <h4 className="text-xs font-bold uppercase tracking-wider text-stone-500">Mức Độ Ngọt (Kem):</h4>
              <div className="flex flex-wrap gap-1.5">
                {product.sweetnessLevels.map((lvl) => (
                  <button
                    key={lvl}
                    onClick={() => setSelectedSweetness(lvl)}
                    className={`px-3 py-1.5 rounded-xl text-xs font-medium border transition-all ${
                      selectedSweetness === lvl
                        ? 'border-[#C57A44] bg-[#F7ECE1] text-[#C57A44]'
                        : 'border-[#E8E2D9] bg-white text-[#2C2520] hover:bg-stone-50'
                    }`}
                  >
                    {lvl}
                  </button>
                ))}
              </div>
            </div>
          )}

          {/* Health/Note Banner */}
          <div className="bg-stone-100/80 rounded-xl p-3 flex gap-2.5 items-start text-xs text-stone-500">
            <Info size={16} className="text-[#C57A44] shrink-0 mt-0.5" />
            <p>Bánh ngọt SaviBite được làm tại quầy trong ngày từ bơ tự nhiên Pháp và đường ăn kiêng tự nhiên tốt cho sức khỏe.</p>
          </div>

          {/* Quantity customizer */}
          <div className="flex items-center justify-between pt-3 border-t border-[#E8E2D9]">
            <span className="text-xs font-bold uppercase tracking-wider text-stone-500">Số lượng đặt hàng:</span>
            <QuantitySelector
              quantity={quantity}
              onDecrease={handleDecrease}
              onIncrease={handleIncrease}
            />
          </div>
        </div>

        {/* Footer Add Button */}
        <div className="px-5 pt-3 border-t border-[#E8E2D9] flex items-center justify-between gap-4 bg-[#FAF8F5]">
          <div className="flex flex-col">
            <span className="text-[10px] uppercase font-bold text-stone-400">Tạm tính:</span>
            <span className="text-lg font-extrabold text-[#C57A44]">
              {(currentPrice * quantity).toLocaleString('vi-VN')}đ
            </span>
          </div>
          <button
            onClick={handleAdd}
            className="flex-1 flex items-center justify-center gap-2 py-3.5 px-4 bg-[#C57A44] hover:bg-[#B46A36] text-white font-bold text-sm rounded-xl transition-all shadow-md shadow-[#C57A44]/20 active:scale-[0.98]"
          >
            <ShoppingBag size={16} />
            Thêm vào giỏ hàng
          </button>
        </div>
      </motion.div>
    </div>
  );
}
