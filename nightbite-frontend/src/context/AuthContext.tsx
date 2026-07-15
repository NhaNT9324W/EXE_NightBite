import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { authService, MeResponse, LoginRequest, RegisterUserRequest, RegisterShopRequest } from '../api/authService';

interface AuthContextType {
  user: MeResponse | null;
  token: string | null;
  isLoading: boolean;
  login: (req: LoginRequest) => Promise<void>;
  registerUser: (req: RegisterUserRequest) => Promise<void>;
  registerShop: (req: RegisterShopRequest) => Promise<void>;
  logout: () => void;
  refreshUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<MeResponse | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // Khôi phục phiên đăng nhập từ localStorage khi app load
  useEffect(() => {
    const initializeAuth = async () => {
      const storedToken = localStorage.getItem('accessToken');
      if (storedToken) {
        setToken(storedToken);
        try {
          const response = await authService.getMe();
          if (response.success && response.data) {
            setUser(response.data);
          } else {
            // Token không hợp lệ hoặc hết hạn
            handleClearAuth();
          }
        } catch (error) {
          console.error('Lỗi khi tải thông tin người dùng:', error);
          // Gặp lỗi kết nối hoặc token hết hạn
          handleClearAuth();
        }
      }
      setIsLoading(false);
    };

    initializeAuth();
  }, []);

  const handleClearAuth = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  };

  const login = async (req: LoginRequest) => {
    setIsLoading(true);
    try {
      const response = await authService.login(req);
      if (response.success && response.data) {
        const { accessToken, refreshToken } = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        setToken(accessToken);
        
        // Gọi getMe để đồng bộ thông tin chi tiết
        const meRes = await authService.getMe();
        if (meRes.success && meRes.data) {
          setUser(meRes.data);
        }
      } else {
        throw new Error(response.message || 'Đăng nhập không thành công.');
      }
    } catch (error: any) {
      handleClearAuth();
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const registerUser = async (req: RegisterUserRequest) => {
    setIsLoading(true);
    try {
      const response = await authService.registerUser(req);
      if (response.success && response.data) {
        const { accessToken, refreshToken } = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        setToken(accessToken);

        const meRes = await authService.getMe();
        if (meRes.success && meRes.data) {
          setUser(meRes.data);
        }
      } else {
        throw new Error(response.message || 'Đăng ký không thành công.');
      }
    } catch (error: any) {
      handleClearAuth();
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const registerShop = async (req: RegisterShopRequest) => {
    setIsLoading(true);
    try {
      const response = await authService.registerShop(req);
      if (response.success && response.data) {
        const { accessToken, refreshToken } = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        setToken(accessToken);

        const meRes = await authService.getMe();
        if (meRes.success && meRes.data) {
          setUser(meRes.data);
        }
      } else {
        throw new Error(response.message || 'Đăng ký shop không thành công.');
      }
    } catch (error: any) {
      handleClearAuth();
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    handleClearAuth();
  };

  const refreshUser = async () => {
    try {
      const response = await authService.getMe();
      if (response.success && response.data) {
        setUser(response.data);
      }
    } catch (error) {
      console.error('Lỗi khi làm mới thông tin:', error);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isLoading,
        login,
        registerUser,
        registerShop,
        logout,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth phải được sử dụng bên trong AuthProvider');
  }
  return context;
};
