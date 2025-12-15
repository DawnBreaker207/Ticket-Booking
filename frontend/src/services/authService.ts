import instance from '../config/axios';

export interface LoginPayload {
  username: string;
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

export interface AuthData {
  token?: string;
  refreshToken?: string;
  email?: string;
  roles?: string[];
  username?: string;
  userId?: number;
  type?: string; 
}


export interface AuthResponse {
  code?: number;
  message?: string;
  data?: AuthData;
}

export const setAuthToken = (token?: string | null) => {
  if (token) {
    instance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    delete instance.defaults.headers.common['Authorization'];
  }
};

export const login = async (payload: LoginPayload): Promise<AuthResponse> => {
  const { data } = await instance.post<AuthResponse>('/auth/login', payload);
  return data;
};


export const register = async (
  payload: RegisterPayload
): Promise<AuthResponse> => {
  const { data } = await instance.post<AuthResponse>('/auth/register', payload);
  return data;
};

export const logout = () => {
  try {

    sessionStorage.removeItem('user');
  } catch (err) {
    console.warn('Lỗi khi clear sessionStorage', err);
  }
  setAuthToken(null);
};
