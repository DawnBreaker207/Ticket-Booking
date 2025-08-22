import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@/environments/environment';
import {ApiRes} from '../../models/common.model';
import {Jwt, RefreshToken} from '../../models/jwt.model';
import {catchError, map, Observable, of, tap} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _accessToken: string | null = null;
  private _refreshToken: string | null = null;

  get accessToken(): string | null {
    return this._accessToken;
  }

  set accessToken(token: string | null) {
    this._accessToken = token;
  }

  get refreshToken(): string | null {
    return this._refreshToken;
  }

  set refreshToken(token: string | null) {
    this._refreshToken = token;
  }

  URL = `${environment.apiUrl}/auth`;
  private http = inject(HttpClient);

  login(username: string, password: string) {
    return this.http.post<ApiRes<Jwt>>(`${this.URL}/login`, {username, password}).pipe(
      map((res) => res.data),
      tap(res => {
        this.accessToken = res.token;
        this.refreshToken = res.refreshToken;
        localStorage.setItem("accessToken", this._accessToken?.toString() ?? '');
        localStorage.setItem("refreshToken", this._refreshToken?.toString() ?? '');
      }),
      catchError(this.handleError<Jwt>('login')),
    )
  }

  register(username: string, email: string, password: string) {
    return this.http.post<ApiRes<RefreshToken>>(`${this.URL}/login`, {username, email, password}).pipe(
      map((res) => res.data),
      catchError(this.handleError<Jwt>('register')),
    )
  }

  logout() {
    return this.http.post<void>(`${this.URL}/logout`, {}).pipe(
      catchError(this.handleError<Jwt>('logout')),
    )
  }

  callRefreshToken(refreshToken: string) {
    return this.http.post<ApiRes<RefreshToken>>(`${this.URL}/refreshToken`, {refreshToken}, {withCredentials: true}).pipe(
      map((res) => res.data),
      catchError(this.handleError<RefreshToken>('refreshToken')),
    )
  }

  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    }
  }
}
