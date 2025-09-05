export interface Jwt {
  token: string;
  type: string;
  refreshToken: string;
  userId: number;
  username: string;
  email: string;
  roles: string[];
}

export interface RefreshToken {
  accessToken: string;
  refreshToken: string;
  token: string;
}
