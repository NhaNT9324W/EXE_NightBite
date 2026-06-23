import React from 'react';
import { Star } from 'lucide-react';

interface RatingBadgeProps {
  rating: number;
}

export function RatingBadge({ rating }: RatingBadgeProps) {
  return (
    <span className="flex items-center gap-0.5 font-bold text-[#E5A93B] text-xs">
      <Star size={12} fill="currentColor" className="text-amber-500" />
      <span>{rating.toFixed(1)}</span>
    </span>
  );
}
