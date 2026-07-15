import React from 'react';

interface BadgeProps {
  children: React.ReactNode;
  variant?: 'primary' | 'danger' | 'success' | 'warning' | 'teal' | 'yellow';
  className?: string;
}

export function Badge({ children, variant = 'primary', className = '' }: BadgeProps) {
  const getColors = () => {
    switch (variant) {
      case 'danger':
        return 'bg-red-50 text-red-700 border-red-100';
      case 'success':
        return 'bg-emerald-50 text-emerald-700 border-emerald-100';
      case 'warning':
        return 'bg-amber-50 text-amber-700 border-amber-100';
      case 'teal':
        return 'bg-teal-50 text-teal-700 border-teal-100';
      case 'yellow':
        return 'bg-yellow-50 text-yellow-800 border-yellow-105';
      case 'primary':
      default:
        return 'bg-[#F7ECE1] text-[#D48C56] border-[#E8E2D9]';
    }
  };

  return (
    <span className={`inline-block text-[10px] px-2 py-0.5 rounded-full border font-bold uppercase tracking-wider ${getColors()} ${className}`}>
      {children}
    </span>
  );
}
