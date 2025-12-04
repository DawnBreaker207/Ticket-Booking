import { DateModel, IsDeleted } from '@/app/core/models/common.model';

export interface User extends DateModel, IsDeleted {
  userId: number;
  username: string;
  avatar: string;
  email: string;
  role: string;
}

export interface UserProfile {
  username: string;
  avatar: string;
  email: string;
  role: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  identifier: string;
  password: string;
}
