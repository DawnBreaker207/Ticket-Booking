import {Jwt} from '@/app/core/models/jwt.model';

export interface User {
  userId: string;
  username: string;
  email: string;
  password: string;
}
