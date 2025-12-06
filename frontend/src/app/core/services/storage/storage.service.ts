import { Injectable } from '@angular/core';
import { Jwt } from '@core/models/jwt.model';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  private readonly JWT_KEY = 'jwt';
  private readonly ACCESS_TOKEN_KEY = 'accessToken';
  private readonly SELECTED_THEATER_KEY = 'selectedTheaterId';

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
      if (typeof value === 'string') {
        localStorage.setItem(key, value);
      } else {
        localStorage.setItem(key, JSON.stringify(value));
      }
    } catch (error) {
      console.error(`Failed to store ${key}`, error);
    }
  }

  getItem<T>(key: string): T | null {
    try {
      const data = localStorage.getItem(key);
      if (!data) return null;
      try {
        return JSON.parse(data);
      } catch {
        return data as T;
      }
    } catch (error) {
      console.error(`Failed to parse ${key}`, error);
      return null;
    }
  }

  setSelectedTheaterId(theaterId: number) {
    try {
      localStorage.setItem(this.SELECTED_THEATER_KEY, theaterId.toString());
    } catch (error) {
      console.error(`Failed to set selected theater `, theaterId);
    }
  }

  getSelectedTheaterId() {
    try {
      const id = localStorage.getItem(this.SELECTED_THEATER_KEY);
      return id ? Number(id) : null;
    } catch (error) {
      console.error(`Failed to get selected theater `);
      return null;
    }
  }

  clearSelectedTheaterId() {
    localStorage.removeItem(this.SELECTED_THEATER_KEY);
  }

  removeItem(key: string) {
    localStorage.removeItem(key);
  }
}
