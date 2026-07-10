import React from 'react';
import { Product } from '../../types';
import { PRODUCTS, CATEGORIES } from '../../demoData';
import { Filter } from 'lucide-react';
import { CategoryTab } from '../molecules/CategoryTab';
import { ProductCard } from '../molecules/ProductCard';

interface CategoryViewProps {
  onSelectProduct: (product: Product) => void;
  activeCategory: string;
  setActiveCategory: (category: string) => void;
}

export default function CategoryView({
  onSelectProduct,
  activeCategory,
  setActiveCategory,
}: CategoryViewProps) {
  // Filter products based on active category selection
  const filteredProducts = activeCategory === 'all'
    ? PRODUCTS
    : PRODUCTS.filter((p) => p.category === activeCategory);

  return (
    <div className="flex-1 overflow-y-auto bg-[#FAF8F5] pb-2 text-[#2C2520]">
      {/* Horizontal categories list using tab screen */}
      <div className="flex gap-2 overflow-x-auto px-4 py-3 bg-white border-b border-stone-100 no-scrollbar sticky top-0 z-20 shadow-xs">
        {CATEGORIES.map((cat) => (
          <div key={cat.id} className="shrink-0">
            <CategoryTab
              id={cat.id}
              name={cat.label}
              iconName={cat.icon}
              isActive={activeCategory === cat.id}
              onClick={() => setActiveCategory(cat.id)}
            />
          </div>
        ))}
      </div>

      {/* Grid of Products list */}
      <div className="p-4">
        <div className="flex justify-between items-center mb-4.5 px-0.5">
          <span className="text-[10px] font-black uppercase text-stone-400 tracking-wider">
            Kết quả ({filteredProducts.length} món ngon)
          </span>
          <div className="flex items-center gap-1 text-[11px] font-bold text-[#C57A44]">
            <Filter size={11} />
            <span>Bộ lọc</span>
          </div>
        </div>

        {filteredProducts.length === 0 ? (
          <div className="text-center py-20 bg-white rounded-2xl border border-dotted border-stone-200">
            <p className="text-stone-400 text-xs font-serif italic">Đang cập nhật thêm các món ngon...</p>
          </div>
        ) : (
          <div className="grid grid-cols-2 gap-3.5">
            {filteredProducts.map((product) => (
              <div key={product.id}>
                <ProductCard
                  product={product}
                  onSelect={onSelectProduct}
                />
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
