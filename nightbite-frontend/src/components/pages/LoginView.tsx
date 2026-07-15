import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { LogIn, UserPlus, Shield, Eye, EyeOff, Sparkles } from 'lucide-react';

export default function LoginView() {
  const { login, registerUser, registerShop } = useAuth();
  
  const [isRegister, setIsRegister] = useState(false);
  const [role, setRole] = useState<'USER' | 'SHOP'>('USER');
  const [showPassword, setShowPassword] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  // Form states
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [shopName, setShopName] = useState('');
  const [address, setAddress] = useState('');
  const [district, setDistrict] = useState('Ninh Kiều');

  const districts = ['Ninh Kiều', 'Cái Răng', 'Bình Thủy', 'Phong Điền', 'Ô Môn'];

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMsg(null);
    setSuccessMsg(null);
    setLoading(true);

    try {
      if (!isRegister) {
        // Đăng nhập
        if (!identifier || !password) {
          throw new Error('Vui lòng nhập đầy đủ thông tin đăng nhập.');
        }
        await login({ identifier, password });
        setSuccessMsg('Đăng nhập thành công!');
      } else {
        // Đăng ký
        if (role === 'USER') {
          if (!fullName || !email || !phone || !password) {
            throw new Error('Vui lòng điền đầy đủ các trường thông tin bắt buộc.');
          }
          await registerUser({ fullName, email, phone, password });
          setSuccessMsg('Đăng ký tài khoản khách hàng thành công!');
        } else {
          if (!fullName || !email || !phone || !password || !shopName || !address) {
            throw new Error('Vui lòng điền đầy đủ thông tin chủ quán và thông tin shop.');
          }
          await registerShop({
            fullName,
            email,
            phone,
            password,
            shopName,
            address,
            district
          });
          setSuccessMsg('Đăng ký tài khoản đối tác Shop thành công!');
        }
      }
    } catch (err: any) {
      setErrorMsg(err.message || 'Thao tác thất bại, vui lòng kiểm tra lại thông tin.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex-1 overflow-y-auto bg-[#FAF8F5] pb-6 text-[#2C2520] font-sans flex flex-col justify-center px-6">
      <div className="w-full max-w-md mx-auto bg-white rounded-3xl border border-[#E8E2D9] p-6 shadow-xl relative overflow-hidden">
        
        {/* Decorative background glow */}
        <div className="absolute -top-12 -right-12 w-24 h-24 bg-orange-100 rounded-full blur-2xl opacity-70 pointer-events-none" />
        <div className="absolute -bottom-12 -left-12 w-24 h-24 bg-pink-100 rounded-full blur-2xl opacity-70 pointer-events-none" />

        {/* Logo and Greeting */}
        <div className="text-center mb-6">
          <div className="w-12 h-12 rounded-full bg-[#C57A44] text-white flex items-center justify-center font-bold text-lg mx-auto shadow-md mb-3">
            S
          </div>
          <h2 className="text-lg font-black text-stone-850 tracking-tight leading-tight flex items-center justify-center gap-1">
            Chào mừng bạn đến với SaviBite
            <Sparkles size={14} className="text-[#C57A44] animate-pulse" />
          </h2>
          <p className="text-xs text-stone-400 mt-1">
            {isRegister ? 'Đăng ký tài khoản để khám phá ưu đãi' : 'Đăng nhập để đặt bánh và quản lý tài khoản'}
          </p>
        </div>

        {/* Auth Tab Switching */}
        <div className="flex bg-stone-100 p-1 rounded-xl mb-5">
          <button
            type="button"
            onClick={() => { setIsRegister(false); setErrorMsg(null); }}
            className={`flex-1 py-2 text-center text-xs font-black rounded-lg transition-all ${
              !isRegister ? 'bg-white text-stone-800 shadow-sm' : 'text-stone-500 hover:text-stone-700'
            }`}
          >
            Đăng nhập
          </button>
          <button
            type="button"
            onClick={() => { setIsRegister(true); setErrorMsg(null); }}
            className={`flex-1 py-2 text-center text-xs font-black rounded-lg transition-all ${
              isRegister ? 'bg-white text-stone-800 shadow-sm' : 'text-stone-500 hover:text-stone-700'
            }`}
          >
            Đăng ký mới
          </button>
        </div>

        {/* Registration Role Switching */}
        {isRegister && (
          <div className="flex gap-3 mb-5 justify-center">
            <button
              type="button"
              onClick={() => setRole('USER')}
              className={`px-4 py-1.5 rounded-full text-[10.5px] font-bold border transition-all ${
                role === 'USER'
                  ? 'bg-amber-50 text-amber-700 border-amber-200'
                  : 'bg-white text-stone-500 border-stone-200 hover:bg-stone-50'
              }`}
            >
              Tôi là Khách hàng
            </button>
            <button
              type="button"
              onClick={() => setRole('SHOP')}
              className={`px-4 py-1.5 rounded-full text-[10.5px] font-bold border transition-all ${
                role === 'SHOP'
                  ? 'bg-rose-50 text-rose-700 border-rose-200'
                  : 'bg-white text-stone-500 border-stone-200 hover:bg-stone-50'
              }`}
            >
              Tôi là Chủ quán (Shop)
            </button>
          </div>
        )}

        {/* Display Alert Messages */}
        {errorMsg && (
          <div className="mb-4 p-3 bg-red-50 border border-red-100 text-red-600 rounded-xl text-[11px] font-medium leading-relaxed">
            ⚠ {errorMsg}
          </div>
        )}
        {successMsg && (
          <div className="mb-4 p-3 bg-emerald-50 border border-emerald-100 text-emerald-600 rounded-xl text-[11px] font-medium leading-relaxed">
            ✓ {successMsg}
          </div>
        )}

        {/* Main form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          
          {/* Login Fields */}
          {!isRegister ? (
            <>
              <div>
                <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                  Email hoặc Số điện thoại
                </label>
                <input
                  type="text"
                  required
                  placeholder="Ví dụ: mail@example.com hoặc 0901234567"
                  value={identifier}
                  onChange={(e) => setIdentifier(e.target.value)}
                  className="w-full px-4 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-[#C57A44] focus:border-[#C57A44] transition-all outline-none"
                />
              </div>

              <div>
                <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                  Mật khẩu
                </label>
                <div className="relative">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    required
                    placeholder="Nhập mật khẩu"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full pl-4 pr-10 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-[#C57A44] focus:border-[#C57A44] transition-all outline-none"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3.5 top-1/2 -translate-y-1/2 text-stone-400 hover:text-stone-600"
                  >
                    {showPassword ? <EyeOff size={15} /> : <Eye size={15} />}
                  </button>
                </div>
              </div>
            </>
          ) : (
            /* Register Fields */
            <>
              <div>
                <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                  Họ tên {role === 'SHOP' && '(Chủ quán)'}
                </label>
                <input
                  type="text"
                  required
                  placeholder="Nhập họ tên đầy đủ"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  className="w-full px-4 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-[#C57A44] focus:border-[#C57A44] transition-all outline-none"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                    Email
                  </label>
                  <input
                    type="email"
                    required
                    placeholder="mail@example.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full px-4 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-[#C57A44] focus:border-[#C57A44] transition-all outline-none"
                  />
                </div>
                <div>
                  <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                    Số điện thoại
                  </label>
                  <input
                    type="tel"
                    required
                    placeholder="Ví dụ: 0901234567"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    className="w-full px-4 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-[#C57A44] focus:border-[#C57A44] transition-all outline-none"
                  />
                </div>
              </div>

              {/* Shop Specific Fields */}
              {role === 'SHOP' && (
                <>
                  <div className="border-t border-dashed border-stone-200 pt-3 my-3">
                    <span className="text-[10px] font-black text-rose-500 uppercase tracking-widest block mb-2">Thông tin Cửa hàng</span>
                  </div>

                  <div>
                    <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                      Tên tiệm bánh / Quán ăn
                    </label>
                    <input
                      type="text"
                      required
                      placeholder="Ví dụ: Tiệm bánh ngọt Savi Trung Tâm"
                      value={shopName}
                      onChange={(e) => setShopName(e.target.value)}
                      className="w-full px-4 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-rose-450 focus:border-rose-450 transition-all outline-none"
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                        Địa chỉ chi tiết
                      </label>
                      <input
                        type="text"
                        required
                        placeholder="Số nhà, Tên đường"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        className="w-full px-4 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-rose-450 focus:border-rose-450 transition-all outline-none"
                      />
                    </div>
                    <div>
                      <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                        Quận / Huyện
                      </label>
                      <select
                        value={district}
                        onChange={(e) => setDistrict(e.target.value)}
                        className="w-full px-4 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-rose-450 focus:border-rose-450 transition-all outline-none bg-white"
                      >
                        {districts.map(d => (
                          <option key={d} value={d}>{d}</option>
                        ))}
                      </select>
                    </div>
                  </div>
                </>
              )}

              <div>
                <label className="block text-[11px] font-extrabold uppercase tracking-wide text-stone-400 mb-1.5">
                  Mật khẩu đăng ký
                </label>
                <div className="relative">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    required
                    placeholder="Mật khẩu tối thiểu 6 ký tự"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full pl-4 pr-10 py-2.5 rounded-xl border border-stone-200 text-xs focus:ring-1 focus:ring-[#C57A44] focus:border-[#C57A44] transition-all outline-none"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3.5 top-1/2 -translate-y-1/2 text-stone-400 hover:text-stone-600"
                  >
                    {showPassword ? <EyeOff size={15} /> : <Eye size={15} />}
                  </button>
                </div>
              </div>
            </>
          )}

          {/* Action Button */}
          <button
            type="submit"
            disabled={loading}
            className={`w-full py-3 rounded-xl text-white text-xs font-black uppercase tracking-wider transition-all flex items-center justify-center gap-2 shadow-md ${
              loading 
                ? 'bg-stone-300 cursor-not-allowed' 
                : isRegister 
                  ? role === 'SHOP' 
                    ? 'bg-rose-500 hover:bg-rose-600' 
                    : 'bg-amber-600 hover:bg-amber-700'
                  : 'bg-[#C57A44] hover:bg-amber-800'
            }`}
          >
            {loading ? (
              <span className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
            ) : isRegister ? (
              <>
                <UserPlus size={14} />
                Đăng ký ngay
              </>
            ) : (
              <>
                <LogIn size={14} />
                Đăng nhập hệ thống
              </>
            )}
          </button>

          {/* Security policy footnote */}
          <div className="text-center pt-2">
            <span className="text-[9.5px] text-stone-400 flex items-center justify-center gap-1 font-sans">
              <Shield size={10} className="text-[#C57A44]" />
              Hệ thống bảo mật chuẩn mã hóa AES-256
            </span>
          </div>
        </form>

      </div>
    </div>
  );
}
