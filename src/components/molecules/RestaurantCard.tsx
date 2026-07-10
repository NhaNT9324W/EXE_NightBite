import React from 'react';
import { Restaurant } from '../../types';
import { Star, Clock, MapPin } from 'lucide-react';

interface RestaurantCardProps {
  restaurant: Restaurant;
  onClick: (res: Restaurant) => void;
}

export const RestaurantCard: React.FC<RestaurantCardProps> = ({ restaurant, onClick }) => {
  return (
    <div 
      onClick={() => onClick(restaurant)}
      className="bg-white rounded-2xl border border-stone-200/60 p-2.5 flex flex-col gap-2 shadow-xs hover:border-[#C57A44]/40 hover:shadow-sm transition-all duration-300 cursor-pointer group"
    >
      <div className="relative w-full aspect-[4/3] rounded-xl overflow-hidden bg-stone-100">
        <img
          src={restaurant.image}
          alt={restaurant.name}
          className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
          referrerPolicy="no-referrer"
        />
      </div>

      <div className="flex flex-col gap-1 px-0.5">
        <h4 className="text-[13px] font-black text-stone-850 line-clamp-1 group-hover:text-[#C57A44] transition-colors leading-tight">
          {restaurant.name}
        </h4>
        
        <div className="flex flex-wrap items-center gap-2 text-[10px] text-stone-500 font-medium">
          <div className="flex items-center gap-0.5 text-amber-500 font-black">
            <Star size={10} className="fill-current" />
            <span>{restaurant.rating}</span>
          </div>
          <span className="w-0.5 h-0.5 rounded-full bg-stone-300" />
          <div className="flex items-center gap-0.5">
            <MapPin size={10} className="text-stone-400" />
            <span>{restaurant.distance} km</span>
          </div>
          <span className="w-0.5 h-0.5 rounded-full bg-stone-300" />
        </div>

        <div className="flex flex-wrap gap-1 mt-1">
          {restaurant.tags.map(tag => (
            <span key={tag} className="text-[9px] font-bold text-stone-500 bg-stone-100 px-1.5 py-0.5 rounded-md">
              {tag}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
};
