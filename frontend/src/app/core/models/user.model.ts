import {Role} from '@/app/core/constants/enum';

export interface User {
  userId: string;
  username: string;
  email: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  role: Role[];
  password: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}


