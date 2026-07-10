import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { ChevronRight } from 'lucide-react';

interface BannerItem {
  id: string;
  title: string;
  subtitle: string;
  desc: string;
  bg: string;
  img: string;
}

interface BannerCarouselProps {
  banners: BannerItem[];
}

export function BannerCarousel({ banners }: BannerCarouselProps) {
  const [activeBanner, setActiveBanner] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setActiveBanner((prev) => (prev + 1) % banners.length);
    }, 4000);
    return () => clearInterval(interval);
  }, [banners.length]);

  return (
    <div className="relative overflow-hidden rounded-3xl h-36 shadow-sm border border-stone-200/50">
      <AnimatePresence mode="wait">
        <motion.div
          key={activeBanner}
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          exit={{ opacity: 0, x: -20 }}
          transition={{ duration: 0.35 }}
          className={`absolute inset-0 bg-gradient-to-r ${banners[activeBanner].bg} p-5 flex justify-between items-center text-white`}
        >
          <div className="space-y-1 flex-1 pr-4">
            <span className="text-[9px] uppercase font-bold tracking-widest bg-white/20 px-2 py-0.5 rounded-full">
              {banners[activeBanner].subtitle}
            </span>
            <h3 className="text-base font-black uppercase tracking-tight leading-none mt-1">
              {banners[activeBanner].title}
            </h3>
            <p className="text-[10px] text-white/90 leading-tight line-clamp-2 max-w-[200px] font-sans">
              {banners[activeBanner].desc}
            </p>
          </div>
          <div className="w-20 h-20 rounded-2xl overflow-hidden shrink-0 border border-white/20 shadow-lg">
            <img
              src={banners[activeBanner].img}
              alt="Promo banner artwork"
              className="w-full h-full object-cover"
              referrerPolicy="no-referrer"
            />
          </div>
        </motion.div>
      </AnimatePresence>

      {/* Manual pager indicator bars */}
      <div className="absolute bottom-3 left-5 flex gap-1.5 google-fonts-mono z-10">
        {banners.map((_, idx) => (
          <button
            key={idx}
            onClick={() => setActiveBanner(idx)}
            className={`h-1.5 rounded-full transition-all duration-300 ${
              activeBanner === idx ? 'w-4 bg-white' : 'w-1.5 bg-white/50'
            }`}
          />
        ))}
      </div>
    </div>
  );
}
