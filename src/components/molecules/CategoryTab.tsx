import React from 'react';
import { Sparkles, Cake, Croissant, Cookie, Coffee } from 'lucide-react';

interface CategoryTabProps {
  id: string;
  name: string;
  iconName: string;
  isActive: boolean;
  onClick: () => void;
}

export function CategoryTab({ id, name, iconName, isActive, onClick }: CategoryTabProps) {
  const getIcon = () => {
    switch (iconName) {
      case 'Sparkles': return <Sparkles className={`w-4 h-4 ${isActive ? 'text-white' : 'text-amber-600'}`} />;
      case 'Cake': return <Cake className={`w-4 h-4 ${isActive ? 'text-white' : 'text-rose-500'}`} />;
      case 'Croissant': return <Croissant className={`w-4 h-4 ${isActive ? 'text-white' : 'text-amber-700'}`} />;
      case 'Cookie': return <Cookie className={`w-4 h-4 ${isActive ? 'text-white' : 'text-[#8A5A36]'}`} />;
      case 'Coffee': return <Coffee className={`w-4 h-4 ${isActive ? 'text-white' : 'text-sky-600'}`} />;
      default: return <Sparkles className={`w-4 h-4 ${isActive ? 'text-white' : 'text-amber-600'}`} />;
    }
  };

  return (
    <button
      onClick={onClick}
      className={`px-3 py-2 rounded-xl text-xs font-black tracking-wide transition-all flex items-center gap-1.5 shrink-0 border uppercase ${
        isActive
          ? 'bg-[#C57A44] border-[#C57A44] text-white shadow-sm'
          : 'bg-white border-[#E8E2D9] text-[#2C2520] hover:bg-stone-50'
      }`}
    >
      {getIcon()}
      <span>{name}</span>
    </button>
  );
}
