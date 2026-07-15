import React, { useState } from 'react';
import { Heart, Star } from 'lucide-react';
import { Product } from '../../types';
import { RatingBadge } from '@/src/components/atoms/RatingBadge';
import { Badge } from '../atoms/Badge';

interface ProductCardProps {
  product: Product;
  onSelect: (product: Product) => void;
  isFavoritedDefault?: boolean;
}

export function ProductCard({ product, onSelect, isFavoritedDefault = false }: ProductCardProps) {
  const [isFavorite, setIsFavorite] = useState(isFavoritedDefault);

  const handleFavoriteClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    setIsFavorite(!isFavorite);
  };

  return (
    <div
      onClick={() => onSelect(product)}
      className="bg-white rounded-2xl border border-stone-200/60 overflow-hidden shadow-xs hover:border-[#C57A44]/40 hover:shadow-xs transition-all duration-300 cursor-pointer flex flex-col group"
    >
      <div className="relative aspect-[4/3] w-full bg-stone-100 overflow-hidden">
        <img
          src={product.image}
          alt={product.name}
          className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
          referrerPolicy="no-referrer"
        />
        
        {/* Favorite Icon Overlay */}
        <button
          onClick={handleFavoriteClick}
          className="absolute top-2.5 right-2.5 w-7.5 h-7.5 bg-white/85 hover:bg-white backdrop-blur-xs rounded-full flex items-center justify-center shadow-xs text-[#E95C8F] transition-all"
        >
          <Heart size={14} fill={isFavorite ? 'currentColor' : 'transparent'} className={isFavorite ? 'scale-110' : ''} />
        </button>

        {product.originalPrice && (
          <div className="absolute top-2.5 left-2.5">
            <Badge variant="primary" className="bg-[#FAF1E6] text-[#C57A44]">
              -{Math.round(((product.originalPrice - product.price) / product.originalPrice) * 100)}%
            </Badge>
          </div>
        )}
      </div>

      <div className="p-3 flex-1 flex flex-col justify-between">
        <div>
          <h4 className="text-xs font-black text-stone-850 line-clamp-1 tracking-tight group-hover:text-[#C57A44] transition-colors leading-tight">
            {product.name}
          </h4>
          <p className="text-[10px] text-stone-400 font-serif italic line-clamp-1 mt-0.5 mb-1.5">
            {product.description}
          </p>
        </div>

        <div className="flex items-center justify-between border-t border-dashed border-stone-100 pt-2 mt-auto">
          <span className="text-xs font-black text-[#C57A44]">
            {product.price.toLocaleString('vi-VN')}đ
          </span>
          <RatingBadge rating={product.rating} />
        </div>
      </div>
    </div>
  );
}
