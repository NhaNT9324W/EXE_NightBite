import React, { useState } from 'react';
import { Order } from '../../types';
import { Badge } from '../atoms/Badge';

interface CartViewProps {
  orders?: Order[];
}

export default function CartView({
  orders = []
}: CartViewProps) {
  const [activeOrderTab, setActiveOrderTab] = useState<'pending' | 'completed'>('pending');

  const pendingOrders = orders.filter(o => o.status === 'pending');
  const completedOrders = orders.filter(o => o.status === 'completed');

  const displayedOrders = activeOrderTab === 'pending' ? pendingOrders : completedOrders;

  const getOrderStatusBadge = (status: Order['status']) => {
    switch (status) {
      case 'pending':
        return (
          <Badge variant="warning">
            Đang chuẩn bị món
          </Badge>
        );
      default:
        return (
          <Badge variant="success">
            Đã hoàn thành
          </Badge>
        );
    }
  };

  return (
    <div className="flex-1 flex flex-col bg-[#FAF8F5] pb-2 text-[#2C2520] h-full overflow-hidden">
      {/* Tab Header */}
      <div className="flex bg-white sticky top-0 z-10 border-b border-[#E8E2D9] shrink-0">
        <button
          onClick={() => setActiveOrderTab('pending')}
          className={`flex-1 py-3 text-sm font-bold transition-all ${
            activeOrderTab === 'pending'
              ? 'text-[#C57A44] border-b-2 border-[#C57A44]'
              : 'text-stone-500 hover:text-stone-700'
          }`}
        >
          Đơn đã đặt
        </button>
        <button
          onClick={() => setActiveOrderTab('completed')}
          className={`flex-1 py-3 text-sm font-bold transition-all ${
            activeOrderTab === 'completed'
              ? 'text-[#C57A44] border-b-2 border-[#C57A44]'
              : 'text-stone-500 hover:text-stone-700'
          }`}
        >
          Đã hoàn thành
        </button>
      </div>

      <div className="flex-1 overflow-y-auto">
        <div className="p-4 space-y-3.5 max-w-sm mx-auto">
          {displayedOrders.length === 0 ? (
            <div className="text-center py-16">
              <p className="text-stone-400 text-sm font-sans">Bạn chưa có đơn hàng nào.</p>
            </div>
          ) : (
            displayedOrders.map((ord) => (
              <div
                key={ord.id}
                className="bg-white p-4 rounded-2xl border border-stone-200 space-y-3 shadow-sm"
              >
                <div className="flex justify-between items-center text-xs pb-2 border-b border-stone-100 font-sans">
                  <span className="font-extrabold tracking-wider">{ord.id}</span>
                  {getOrderStatusBadge(ord.status)}
                </div>

                {/* Items loop */}
                <div className="space-y-1.5 font-sans">
                  {ord.items.map((it, idx) => {
                    const priceMultiplier = it.selectedSize.includes('lớn') ? 1.5 : it.selectedSize.includes('đặc biệt') ? 1.2 : 1;
                    const itemPrice = Math.round(it.product.price * priceMultiplier);
                    return (
                      <div key={idx} className="flex justify-between items-center text-xs text-stone-600 font-sans">
                        <span className="truncate max-w-[70%] font-medium">
                          {it.quantity}x {it.product.name} ({it.selectedSize})
                        </span>
                        <span className="font-semibold">
                          {(itemPrice * it.quantity).toLocaleString('vi-VN')}đ
                        </span>
                      </div>
                    );
                  })}
                </div>

                <div className="flex justify-between pt-3 border-t border-dotted border-stone-200 text-xs items-center font-sans">
                  <span className="text-[10px] text-stone-400">{ord.createdAt}</span>
                  <div className="flex flex-col items-end font-sans">
                    <span className="text-[10px] text-stone-400 uppercase font-bold">Tổng thanh toán:</span>
                    <span className="font-bold text-[#C57A44] text-sm">
                      {ord.finalAmount.toLocaleString('vi-VN')}đ
                    </span>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
