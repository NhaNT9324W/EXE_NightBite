import React from 'react';
import { Trash2, Minus, Plus } from 'lucide-react';
import { CartItem } from '../../types';

interface CartItemRowProps {
  item: CartItem;
  index: number;
  onUpdateQuantity: (index: number, delta: number) => void;
  onRemoveItem: (index: number) => void;
}

export function CartItemRow({ item, index, onUpdateQuantity, onRemoveItem }: CartItemRowProps) {
  const sizeMultiplier = item.selectedSize.includes('Ổ lớn') ? 4.9 : item.selectedSize.includes('Ổ nhỏ') ? 3.5 : 1;
  const itemPrice = Math.round(item.product.price * sizeMultiplier);

  return (
    <div className="bg-white p-3 rounded-2xl border border-stone-200/50 flex gap-3 shadow-inner-xs hover:border-stone-200 transition-all">
      <img
        src={item.product.image}
        alt={item.product.name}
        className="w-16 h-16 rounded-xl object-cover border border-stone-100 shrink-0"
        referrerPolicy="no-referrer"
      />
      <div className="flex-1 flex flex-col justify-between">
        <div>
          <div className="flex justify-between items-start">
            <h4 className="text-xs font-bold text-stone-800 line-clamp-1 pr-1">{item.product.name}</h4>
            <button
              onClick={() => onRemoveItem(index)}
              className="p-1 text-stone-400 hover:text-rose-500 rounded-md transition-colors"
            >
              <Trash2 size={12} />
            </button>
          </div>
          <p className="text-[10px] text-stone-400 font-medium leading-tight">
            Cỡ: {item.selectedSize} | Ngọt: {item.selectedSweetness}
          </p>
        </div>

        <div className="flex items-center justify-between mt-2 pt-1.5 border-t border-dotted border-stone-100">
          <span className="text-xs font-black text-[#C57A44]">
            {(itemPrice * item.quantity).toLocaleString('vi-VN')}đ
          </span>

          {/* Quantity tools */}
          <div className="flex items-center gap-3.5 bg-stone-50 rounded-lg px-2 py-0.5 border border-stone-200/50">
            <button
              onClick={() => onUpdateQuantity(index, -1)}
              className="text-stone-500 hover:text-[#C57A44] transition-all"
            >
              <Minus size={12} />
            </button>
            <span className="text-xs font-bold text-stone-800">{item.quantity}</span>
            <button
              onClick={() => onUpdateQuantity(index, 1)}
              className="text-stone-500 hover:text-[#C57A44] transition-all"
            >
              <Plus size={12} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
