import React, { useState } from 'react';
import { Product } from '../../types';
import { PRODUCTS } from '../../demoData';
import { Search, X, Star, Sparkles, TrendingUp, ArrowLeft } from 'lucide-react';

interface SearchViewProps {
  onBack: () => void;
  onSelectProduct: (product: Product) => void;
}

const POPULAR_SEARCHES = [
  'Bánh Kem Dâu Tây',
  'Croissant',
  'Cà phê muối',
  'Donut Chocolate',
  'Matcha Mousse'
];

export default function SearchView({ onBack, onSelectProduct }: SearchViewProps) {
  const [query, setQuery] = useState('');

  const filteredProducts = PRODUCTS.filter((p) =>
    p.name.toLowerCase().includes(query.toLowerCase()) ||
    p.description.toLowerCase().includes(query.toLowerCase())
  );

  const handleQuickSearch = (keyword: string) => {
    setQuery(keyword);
  };

  return (
    <div className="absolute inset-0 bg-[#FAF8F5] z-[40] flex flex-col text-[#2C2520]">
      {/* Search Header Bar matching Zalo Style */}
      <div className="bg-white px-4 py-3 border-b border-[#E8E2D9] flex items-center gap-3 font-sans">
        <button
          onClick={onBack}
          className="p-1.5 hover:bg-stone-100 rounded-full text-stone-600 transition-colors shrink-0 font-sans"
        >
          <ArrowLeft size={18} />
        </button>

        {/* Input area */}
        <div className="flex-1 bg-stone-100 rounded-xl px-3 py-2 flex items-center gap-2 border border-stone-200 focus-within:border-[#C57A44] transition-all font-sans">
          <Search size={14} className="text-stone-400 shrink-0 font-sans" />
          <input
            type="text"
            placeholder="Tìm thơm lừng bánh ngon, nước uống..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            autoFocus
            className="flex-1 bg-transparent border-none text-xs text-[#2C2520] focus:outline-none placeholder-stone-400 font-sans font-medium"
          />
          {query && (
            <button
              onClick={() => setQuery('')}
              className="p-0.5 hover:bg-stone-200 rounded-full text-stone-500 font-sans"
            >
              <X size={12} />
            </button>
          )}
        </div>
      </div>

      {query === '' ? (
        // Search Guides and Suggestions
        <div className="p-5 space-y-6 flex-1 overflow-y-auto">
          {/* Trending Searches */}
          <div className="space-y-3 font-sans">
            <h4 className="text-[10px] font-bold text-stone-400 uppercase tracking-widest flex items-center gap-1.5 font-sans">
              <TrendingUp size={12} className="text-[#C57A44]" />
              Tìm kiếm phổ biến tại SaviBite
            </h4>
            <div className="flex flex-wrap gap-2 font-sans">
              {POPULAR_SEARCHES.map((keyword) => (
                <button
                  key={keyword}
                  onClick={() => handleQuickSearch(keyword)}
                  className="px-3.5 py-1.8 bg-white border border-stone-200 text-xs font-semibold rounded-full text-stone-700 hover:border-[#C57A44] hover:text-[#C57A44] transition-all font-sans"
                >
                  {keyword}
                </button>
              ))}
            </div>
          </div>

          {/* Quick tips */}
          <div className="bg-white p-4 rounded-2xl border border-stone-200/50 space-y-1 font-sans">
            <h5 className="text-xs font-bold text-stone-800 flex items-center gap-1 font-sans">
              <Sparkles size={12} className="text-[#C57A44] fill-current" />
              Bạn thèm ngọt vị gì hôm nay?
            </h5>
            <p className="text-[11px] text-stone-500 leading-relaxed font-serif italic">
              "Hãy thử nhập các từ khóa như 'Dâu Tây', 'Croissant', hoặc 'Matcha' để tìm thấy các chiếc bánh nướng tươi ngon nhất phục vụ trực tiếp từ quầy."
            </p>
          </div>
        </div>
      ) : (
        // Real-time Results matching search mockup
        <div className="flex-1 overflow-y-auto p-4 font-sans">
          <h4 className="text-[10px] font-extrabold text-stone-400 uppercase tracking-wider mb-3 px-1 font-sans">
            Kết quả tìm thấy ({filteredProducts.length})
          </h4>

          {filteredProducts.length === 0 ? (
            <div className="text-center py-16 bg-white rounded-2xl border border-dotted border-stone-200 font-sans">
              <p className="text-stone-400 text-xs font-sans">Không tìm thấy mảnh bánh nào khớp với từ khóa của bạn.</p>
              <p className="text-[10px] text-stone-400 mt-1 font-sans">Hâm nóng tìm kiếm bằng từ khoá khác nhé!</p>
            </div>
          ) : (
            <div className="space-y-2.5 font-sans">
              {filteredProducts.map((p) => (
                <div
                  key={p.id}
                  onClick={() => onSelectProduct(p)}
                  className="bg-white p-3 rounded-2xl border border-stone-200/50 flex gap-3 shadow-xs hover:border-[#C57A44]/40 transition-all cursor-pointer group"
                >
                  <img
                    src={p.image}
                    alt={p.name}
                    className="w-14 h-14 rounded-xl object-cover shrink-0 border border-stone-100"
                    referrerPolicy="no-referrer"
                  />
                  <div className="flex-1 flex flex-col justify-between font-sans">
                    <div>
                      <h5 className="text-xs font-bold text-stone-800 line-clamp-1 group-hover:text-[#C57A44] transition-colors font-sans">
                        {p.name}
                      </h5>
                      <p className="text-[10px] text-stone-400 line-clamp-1 italic font-serif">
                        {p.description}
                      </p>
                    </div>
                    <div className="flex justify-between items-end border-t border-dotted border-stone-100 pt-1 mt-1 font-sans">
                      <span className="text-xs font-extrabold text-[#C57A44]">
                        {p.price.toLocaleString('vi-VN')}đ
                      </span>
                      <span className="text-[9px] font-bold text-stone-500 flex items-center gap-0.5 font-sans">
                        <Star size={9} fill="currentColor" className="text-amber-500" /> {p.rating}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}
