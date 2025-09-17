// src/services/authService.ts
import instance from '../config/axios';

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload {
  name: string;
  email: string;
  password: string;
  confirmPassword?: string;
  dob?: string;
  gender?: string;
  phone?: string;
}

export interface AuthResponse {
  token?: string;
  user?: any;
  message?: string;
}


export const setAuthToken = (token?: string | null) => {
  if (token) {
    instance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    delete instance.defaults.headers.common['Authorization'];
  }
};

// hàm đăng nhập
export const login = async (payload: LoginPayload): Promise<AuthResponse> => {
  const { data } = await instance.post<AuthResponse>('/auth/login', payload);
  if (data?.token) {
    localStorage.setItem('token', data.token);
    setAuthToken(data.token);
  }
  return data;
};

// hàm đăng ký
export const register = async (
  payload: RegisterPayload
): Promise<AuthResponse> => {
  const { data } = await instance.post<AuthResponse>('/auth/register', payload);
  return data;
};

export const logout = () => {
  localStorage.removeItem('token');
  setAuthToken(null);
};
