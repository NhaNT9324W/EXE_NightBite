import React from 'react';
import { Minus, Plus } from 'lucide-react';

interface QuantitySelectorProps {
  quantity: number;
  onDecrease: () => void;
  onIncrease: () => void;
}

export function QuantitySelector({ quantity, onDecrease, onIncrease }: QuantitySelectorProps) {
  return (
    <div className="flex items-center gap-3.5 bg-white rounded-xl border border-[#E8E2D9] px-2.5 py-1 shadow-xs">
      <button
        onClick={onDecrease}
        className="p-1 text-stone-500 hover:text-[#C57A44] hover:bg-stone-50 rounded-lg transition-all"
      >
        <Minus size={14} className="stroke-[2.5]" />
      </button>
      <span className="text-sm font-black w-5 text-center text-stone-800">{quantity}</span>
      <button
        onClick={onIncrease}
        className="p-1 text-stone-500 hover:text-[#C57A44] hover:bg-stone-50 rounded-lg transition-all"
      >
        <Plus size={14} className="stroke-[2.5]" />
      </button>
    </div>
  );
}
