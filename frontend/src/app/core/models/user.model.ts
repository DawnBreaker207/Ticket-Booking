import { DateModel, IsDeleted } from '@/app/core/models/common.model';

export interface User extends DateModel, IsDeleted {
  userId: number;
  username: string;
  email: string;
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
