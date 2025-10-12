export interface Jwt {
  accessToken: string;
  refreshToken: string;
  username: string;
  email: string;
}

export interface RefreshToken {
  accessToken: string;
  refreshToken: string;
}
