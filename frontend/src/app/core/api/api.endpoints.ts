import { environment } from '@env/environment.development';

const API_BASE = environment.apiUrl;

export const ApiEndpoints = {
  AUTH: {
    LOGIN: `${API_BASE}/auth/login`,
    REGISTER: `${API_BASE}/auth/register`,
    LOGOUT: `${API_BASE}/auth/logout`,
    TOKEN: `${API_BASE}/auth/refresh-token`,
  },
  DASHBOARD: {},
  MOVIES: {},
  PAYMENT: {},
  SEAT: {},
  SHOWTIME: {},
  THEATER: {},
  USER: {},
  RESERVATION: {},
};
