import React, { useState, useEffect } from 'react';
import { Product, CartItem } from '../../types';
import { X, FileText, CheckSquare, Square, Minus, Plus } from 'lucide-react';
import { motion } from 'motion/react';

interface ProductDetailModalProps {
  product: Product | null;
  onClose: () => void;
  onAddToCart: (cartItem: CartItem) => void;
}

export function ProductDetailModal({ product, onClose, onAddToCart }: ProductDetailModalProps) {
  const [quantity, setQuantity] = useState(1);
  const [selectedOptions, setSelectedOptions] = useState<string[]>([]);
  const [note, setNote] = useState('');

  // Reset state when product changes
  useEffect(() => {
    if (product) {
      setQuantity(1);
      setSelectedOptions([]);
      setNote('');
    }
  }, [product]);

  if (!product) return null;

  const handleDecrease = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  const handleIncrease = () => {
    setQuantity(quantity + 1);
  };
  
  const toggleOption = (option: string) => {
    if (selectedOptions.includes(option)) {
      setSelectedOptions(selectedOptions.filter(o => o !== option));
    } else {
      if (selectedOptions.length < 3) {
        setSelectedOptions([...selectedOptions, option]);
      }
    }
  };

  // Assume options cost 10,000 extra just to match screenshot realism, or just 0
  const optionPrice = 10000;
  const unitPrice = product.price + (selectedOptions.length * optionPrice);
  const totalPrice = unitPrice * quantity;

  const handleAdd = () => {
    onAddToCart({
      product: {
        ...product,
        // Override price slightly to account for toppings if we want accurate total in cart
        // But cart expects product.price. Let's just leave it or pass selectedOption properly
        price: unitPrice
      },
      quantity,
      selectedSize: 'Standard',
      selectedOption: selectedOptions.length > 0 ? selectedOptions.join(', ') + (note ? ` - Ghi chú: ${note}` : '') : (note ? `Ghi chú: ${note}` : 'Không'),
    });
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/60 z-50 flex flex-col justify-end">
      {/* Semi-transparent overlay to close */}
      <div className="absolute inset-0 -z-10" onClick={onClose} />
      
      {/* Sheet Content */}
      <motion.div
        initial={{ y: '100%' }}
        animate={{ y: 0 }}
        exit={{ y: '100%' }}
        transition={{ type: 'spring', damping: 25, stiffness: 250 }}
        className="bg-white rounded-t-2xl max-h-[90vh] flex flex-col font-sans"
      >
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-stone-100 shrink-0">
          <div className="w-6" /> {/* Spacer for centering */}
          <h2 className="text-[17px] font-medium text-stone-800">Thêm món mới</h2>
          <button onClick={onClose} className="p-1 text-stone-400 hover:text-stone-600 transition-colors">
            <X size={24} className="stroke-[1.5]" />
          </button>
        </div>

        {/* Scrollable Content */}
        <div className="flex-1 overflow-y-auto pb-4">
          {/* Product Info Row */}
          <div className="flex p-4 gap-4">
            <div className="w-[100px] h-[100px] shrink-0 rounded-lg overflow-hidden border border-stone-100">
              <img src={product.image} alt={product.name} className="w-full h-full object-cover" />
            </div>
            
            <div className="flex flex-col flex-1">
              <h3 className="text-[17px] font-normal text-stone-800 leading-tight">
                {product.name}
              </h3>
              
              <div className="text-[13px] text-stone-500 mt-1">
                {product.salesCount}+ đã bán | {product.rating} lượt thích
              </div>
              
              <div className="mt-auto flex items-center justify-between">
                <span className="text-base font-medium text-[#f24e1e]">
                  {product.price.toLocaleString('vi-VN')}đ
                </span>
                
                <div className="flex items-center gap-3">
                  <button 
                    onClick={handleDecrease}
                    className="w-7 h-7 flex items-center justify-center border border-[#f24e1e] text-[#f24e1e] rounded-sm bg-white"
                  >
                    <Minus size={16} />
                  </button>
                  <span className="text-[15px] font-medium w-4 text-center text-stone-800">{quantity}</span>
                  <button 
                    onClick={handleIncrease}
                    className="w-7 h-7 flex items-center justify-center bg-[#f24e1e] text-white rounded-sm"
                  >
                    <Plus size={16} />
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* Toppings Section */}
          {(product.options && product.options.length > 0) && (
            <div>
              <div className="bg-[#f5f5f5] px-4 py-2 text-[13px] text-stone-500 uppercase">
                MÓN THÊM (Topping, tối đa 3)
              </div>
              <div className="divide-y divide-stone-100">
                {product.options.map(option => (
                  <label key={option} onClick={() => toggleOption(option)} className="flex items-center justify-between p-4 cursor-pointer hover:bg-stone-50 transition-colors">
                    <div className="flex flex-col">
                      <span className="text-[15px] text-stone-800">{option}</span>
                      <span className="text-[13px] text-stone-500">10.000đ</span>
                    </div>
                    
                    <div className="text-stone-300">
                       {selectedOptions.includes(option) ? (
                         <CheckSquare size={24} className="text-[#f24e1e] fill-current" />
                       ) : (
                         <Square size={24} className="stroke-[1.5]" />
                       )}
                    </div>
                  </label>
                ))}
              </div>
            </div>
          )}

          {/* Note Input */}
          <div className="flex items-center gap-3 p-4 border-t border-[#f5f5f5]">
            <FileText size={20} className="text-stone-400 shrink-0" />
            <input 
              type="text"
              placeholder="Ghi chú cho quán"
              value={note}
              onChange={(e) => setNote(e.target.value)}
              className="flex-1 bg-transparent outline-none text-[15px] text-stone-800 placeholder:text-stone-400"
            />
          </div>
        </div>

        {/* Footer Add Button */}
        <div className="p-4 border-t border-stone-100 shrink-0 bg-white">
          <button
            onClick={handleAdd}
            className="w-full py-3 bg-[#f24e1e] active:bg-[#e04316] text-white font-medium text-[15px] rounded transition-colors"
          >
            Thêm vào giỏ hàng - {totalPrice.toLocaleString('vi-VN')}đ
          </button>
        </div>
      </motion.div>
    </div>
  );
}
