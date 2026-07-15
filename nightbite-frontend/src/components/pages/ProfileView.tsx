import React, { useState, useEffect } from 'react';
import { UserProfile, Order } from '../../types';
import { Award, Bookmark, BadgeCheck, Phone, Calendar, MessageSquareShare, LogOut } from 'lucide-react';
import { Badge } from '../atoms/Badge';
import { useAuth } from '../../context/AuthContext';
import { orderService } from '../../api/orderService';
import { mapOrderResponseToOrder } from '../../api/mappers';

interface ProfileViewProps {
  user: UserProfile;
  orders: Order[];
  onTriggerVoucherCopy: (code: string) => void;
}

export default function ProfileView({ user, orders: propOrders, onTriggerVoucherCopy }: ProfileViewProps) {
  const { user: authUser, logout } = useAuth();
  const [copiedCode, setCopiedCode] = useState<string | null>(null);
  const [orderList, setOrderList] = useState<Order[]>(propOrders);
  const [loadingOrders, setLoadingOrders] = useState(false);

  useEffect(() => {
    const fetchOrderHistory = async () => {
      if (!authUser) return;
      setLoadingOrders(true);
      try {
        const roleParam = authUser.role === 'ROLE_SHOP' ? 'SHOP' : 'USER';
        const response = await orderService.getOrderHistory(authUser.id, roleParam);
        if (response.success && response.data) {
          const mappedOrders = response.data.map(mapOrderResponseToOrder);
          setOrderList(mappedOrders);
        }
      } catch (error) {
        console.warn('Lỗi khi tải lịch sử đơn hàng từ API:', error);
      } finally {
        setLoadingOrders(false);
      }
    };
    fetchOrderHistory();
  }, [authUser]);

  const handleCopyCode = (code: string) => {
    onTriggerVoucherCopy(code);
    setCopiedCode(code);
    setTimeout(() => setCopiedCode(null), 2000);
  };

  const getOrderStatusBadge = (status: Order['status']) => {
    switch (status) {
      case 'pending':
        return (
          <Badge variant="warning">
            Đang chuẩn bị món
          </Badge>
        );
      case 'shipping':
        return (
          <Badge variant="teal">
            Đang giao hàng
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

  const pointPercent = Math.min(100, (user.points / 3000) * 100);

  return (
    <div className="flex-1 overflow-y-auto bg-[#FAF8F5] pb-6 text-[#2C2520]">
      {/* 1. Profile header banner card */}
      <div className="bg-gradient-to-b from-[#FAF1E6] to-[#FAF8F5] p-5 pb-2 text-center flex flex-col items-center">
        <div className="relative">
          <img
            src={user.avatar}
            alt={user.name}
            className="w-16 h-16 rounded-full border-4 border-white shadow-md object-cover"
            referrerPolicy="no-referrer"
          />
          <div className="absolute -bottom-1 -right-1 bg-amber-500 text-white rounded-full p-1 border-2 border-white shadow-xs">
            <Award size={12} className="fill-current" />
          </div>
        </div>

        <h2 className="text-sm font-black font-sans text-stone-800 tracking-tight mt-2.5 leading-tight flex items-center justify-center gap-1">
          {user.name}
          <BadgeCheck size={14} className="text-[#C57A44] inline" />
        </h2>
        <p className="text-[10px] text-stone-500 font-sans">{user.phone}</p>

        {/* Membership box card */}
        <div className="mt-4 bg-white p-4 rounded-2xl w-full border border-[#E8E2D9] shadow-inner-xs text-left max-w-sm">
          <div className="flex justify-between items-center mb-1">
            <div className="flex items-center gap-1.5 text-xs font-bold text-stone-800">
              <span className="text-yellow-600 font-black">★</span>
              <span>Hạng thành viên: <span className="text-[#C57A44] font-extrabold uppercase">{user.tier}</span></span>
            </div>
            <span className="text-[10.5px] text-stone-400 font-sans font-semibold">{user.points} / 3000 pts</span>
          </div>

          {/* progress bar */}
          <div className="w-full bg-stone-100 h-2.5 rounded-full overflow-hidden border border-stone-200/50 mb-2">
            <div
              className="bg-gradient-to-r from-amber-500 to-amber-600 h-full rounded-full transition-all duration-700"
              style={{ width: `${pointPercent}%` }}
            />
          </div>

          <p className="text-[9px] text-[#C57A44] italic">
            Tích lũy thêm <strong>{(3000 - user.points).toLocaleString()} điểm</strong> để thăng cấp Diamond.
          </p>
        </div>
      </div>

      {/* 2. Wallet metrics shortcuts */}
      <div className="px-5 py-2">
        <div className="grid grid-cols-2 gap-3 max-w-sm mx-auto">
          <div className="bg-white p-3.5 rounded-2xl border border-stone-200/60 shadow-xs text-center flex flex-col justify-center items-center">
            <span className="text-2xl font-black text-[#C57A44] tracking-tight">{user.points.toLocaleString()}</span>
            <span className="text-[10px] uppercase font-bold text-stone-400 tracking-wider mt-0.5">SaviPoints</span>
          </div>
          <div className="bg-white p-3.5 rounded-2xl border border-stone-200/60 shadow-xs text-center flex flex-col justify-center items-center">
            <span className="text-2xl font-black text-[#C57A44] tracking-tight">{user.savedVouchers.length}</span>
            <span className="text-[10px] uppercase font-bold text-stone-400 tracking-wider mt-0.5">Mã ưu đãi</span>
          </div>
        </div>
      </div>

      {/* 3. Voucher Codes List */}
      <div className="px-5 py-3">
        <div className="flex items-center gap-1.5 pb-2 mb-3 border-b border-[#E8E2D9] max-w-sm mx-auto">
          <Bookmark size={14} className="text-[#C57A44]" />
          <h3 className="text-xs font-black uppercase text-stone-800 tracking-wider">Mã ưu đãi Savi của bạn</h3>
        </div>

        <div className="space-y-2 max-w-sm mx-auto font-sans">
          {user.savedVouchers.map((voucher) => {
            const isCopied = copiedCode === voucher;
            return (
              <div
                key={voucher}
                className="bg-white rounded-xl p-3 border border-stone-200/55 flex justify-between items-center shadow-xs"
              >
                <div className="flex items-center gap-2.5">
                  <div className="w-8 h-8 rounded-lg bg-orange-50 flex items-center justify-center text-[#C57A44]">
                    <Award size={16} />
                  </div>
                  <div>
                    <h4 className="text-xs font-bold text-stone-800 tracking-wider uppercase font-sans">
                      {voucher}
                    </h4>
                    <p className="text-[10px] text-stone-400">
                      {voucher === 'SAVIBITE15' ? 'Giảm 15% tất cả bánh kem' : voucher === 'FREESHIP' ? 'Freeship đơn bánh từ 100k' : 'Ưu đãi mua 1 tặng 1 đặc biệt'}
                    </p>
                  </div>
                </div>

                <button
                  onClick={() => handleCopyCode(voucher)}
                  className={`px-3 py-1.5 rounded-lg text-[10px] font-bold transition-all ${
                    isCopied
                      ? 'bg-emerald-50 text-emerald-600 border border-emerald-100'
                      : 'bg-stone-100 text-stone-600 hover:bg-stone-200'
                  }`}
                >
                  {isCopied ? 'Đã Sao' : 'Dùng mã'}
                </button>
              </div>
            );
          })}
        </div>
      </div>

      {/* 4. Active Orders History List */}
      <div className="px-5 py-3 font-sans">
        <div className="flex items-center gap-1.5 pb-2 mb-3 border-b border-[#E8E2D9] max-w-sm mx-auto">
          <Calendar size={14} className="text-[#C57A44]" />
          <h3 className="text-xs font-black uppercase text-stone-800 tracking-wider font-sans">Lịch sử rước bánh ngọt</h3>
        </div>

        <div className="space-y-3.5 max-w-sm mx-auto">
          {loadingOrders ? (
            <div className="text-center py-8 bg-white rounded-2xl border border-stone-200">
              <span className="w-5 h-5 border-2 border-[#C57A44] border-t-transparent rounded-full animate-spin inline-block" />
              <p className="text-stone-400 text-[10px] mt-2 font-sans">Đang tải lịch sử đơn hàng...</p>
            </div>
          ) : orderList.length === 0 ? (
            <div className="text-center py-8 bg-white rounded-2xl border border-dotted border-stone-200">
              <p className="text-stone-400 text-xs text-center font-sans">Bạn chưa có giao dịch đặt món nào.</p>
            </div>
          ) : (
            orderList.map((ord) => (
              <div
                key={ord.id}
                className="bg-white p-3.5 rounded-2xl border border-stone-200 space-y-3 shadow-inner-xs"
              >
                <div className="flex justify-between items-center text-xs pb-2 border-b border-stone-100 font-sans">
                  <span className="font-extrabold tracking-wider">Đơn hàng #{ord.id}</span>
                  {getOrderStatusBadge(ord.status)}
                </div>

                {/* Items loop */}
                <div className="space-y-1.5 font-sans">
                  {ord.items.map((it, idx) => {
                    const priceMultiplier = it.selectedSize.includes('lớn') ? 1.5 : it.selectedSize.includes('đặc biệt') ? 1.2 : 1;
                    const itemPrice = Math.round(it.product.price * priceMultiplier);
                    return (
                      <div key={idx} className="flex justify-between items-center text-[11px] text-stone-600 font-sans">
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

                <div className="flex justify-between pt-2 border-t border-dotted border-stone-200 text-xs items-center font-sans">
                  <span className="text-[10px] text-stone-400">Thời gian: {ord.createdAt ? new Date(ord.createdAt).toLocaleString('vi-VN') : 'Đang xử lý'}</span>
                  <div className="flex flex-col items-end font-sans">
                    <span className="text-[10px] text-stone-400 uppercase font-bold">Tổng thanh toán:</span>
                    <span className="font-bold text-[#C57A44]">
                      {ord.finalAmount.toLocaleString('vi-VN')}đ
                    </span>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* 5. Support / Contact footer details */}
      <div className="px-5 pt-1 pb-1 mt-1 font-sans">
        <div className="bg-[#FAF1E6]/40 rounded-2xl p-3 border border-[#E8E2D9] text-[#2C2520] space-y-2 max-w-sm mx-auto">
          <div className="flex items-center gap-1.5 text-xs font-bold uppercase tracking-wider text-stone-700">
            <MessageSquareShare size={14} className="text-[#C57A44]" />
            <span>Liên hệ SaviBite:</span>
          </div>

          <div className="space-y-1.5 text-xs text-stone-600 font-sans">
            <div className="flex justify-between">
              <span>Đường dây nóng:</span>
              <span className="font-semibold text-stone-900 flex items-center gap-0.5">
                <Phone size={10} className="text-[#C57A44]" /> 1900-1234
              </span>
            </div>
            <div className="flex justify-between">
              <span>Văn phòng:</span>
              <span className="font-semibold text-stone-900 max-w-[60%] text-right text-[10.5px]">
                132 Nguyễn Huệ, P. Bến Nghé, Quận 1, Tp. Hồ Chí Minh
              </span>
            </div>
            <div className="flex justify-between">
              <span>Hỗ trợ Kỹ thuật:</span>
              <span className="font-semibold text-stone-600 text-xs">
                Zalo OA SaviBite Bakery
              </span>
            </div>
          </div>
        </div>
      </div>

      {/* 6. Logout action button */}
      <div className="px-5 pt-4 max-w-sm mx-auto">
        <button
          onClick={logout}
          className="w-full py-2.5 bg-red-50 hover:bg-red-100 text-red-600 hover:text-red-700 border border-red-200/50 rounded-xl text-xs font-black uppercase tracking-wider flex items-center justify-center gap-2 transition-all shadow-xs"
        >
          <LogOut size={14} />
          Đăng xuất tài khoản
        </button>
      </div>
    </div>
  );
}
