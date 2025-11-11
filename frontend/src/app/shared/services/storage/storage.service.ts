import { Injectable } from '@angular/core';
import { Jwt } from '@/app/core/models/jwt.model';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  private readonly JWT_KEY = 'jwt';
  private readonly ACCESS_TOKEN_KEY = 'accessToken';

  setJWT(jwt: Jwt) {
    try {
      localStorage.setItem(this.JWT_KEY, JSON.stringify(jwt));
      localStorage.setItem(this.ACCESS_TOKEN_KEY, jwt.accessToken);
    } catch (error) {
      console.error('Failed to set JWT', error);
    }
  }

  getJwt() {
    try {
      const item = localStorage.getItem(this.JWT_KEY);
      return item ? JSON.parse(item) : null;
    } catch (error) {
      console.error('Failed to parse Jwt', error);
      return null;
    }
  }

  clearAuth() {
    localStorage.removeItem(this.JWT_KEY);
    localStorage.removeItem(this.ACCESS_TOKEN_KEY);
  }

  setItem<T>(key: string, value: T) {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error(`Failed to store ${key}`, error);
    }
  }

  getItem<T>(key: string): T | null {
    try {
      const data = localStorage.getItem(key);
      return data ? JSON.parse(data) : null;
    } catch (error) {
      console.error(`Failed to parse ${key}`, error);
      return null;
    }
  }

  removeItem(key: string) {
    localStorage.removeItem(key);
  }
}
