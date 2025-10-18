export interface Jwt {
  userId: number;
  username: string;
  email: string;
  accessToken: string;
  refreshToken: string;
}

export interface RefreshToken {
  accessToken: string;
  refreshToken: string;
}
