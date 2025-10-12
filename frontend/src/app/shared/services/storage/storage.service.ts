import {Injectable} from '@angular/core';
import {Jwt} from '@/app/core/models/jwt.model';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private readonly JWT_KEY = 'jwt';
  private readonly ACCESS_TOKEN_KEY = 'accessToken';
  private readonly REFRESH_TOKEN_KEY = 'refreshToken';

  setJWT(jwt: Jwt) {
    try {
      localStorage.setItem(this.JWT_KEY, JSON.stringify(jwt));
      localStorage.setItem(this.ACCESS_TOKEN_KEY, jwt.accessToken);
      localStorage.setItem(this.REFRESH_TOKEN_KEY, jwt.refreshToken);
    } catch (error) {
      console.error("Failed to set JWT", error)
    }
  }


  getJwt() {
    try {
      const item = localStorage.getItem(this.JWT_KEY);
      return item ? JSON.parse(item) : null;
    } catch (error) {
      console.error("Failed to parse Jwt", error)
      return null;
    }
  }

  clearAuth() {
    localStorage.removeItem(this.JWT_KEY);
    localStorage.removeItem(this.ACCESS_TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
  }
}
