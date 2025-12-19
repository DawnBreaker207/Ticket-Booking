import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import {
  BehaviorSubject,
  catchError,
  filter,
  switchMap,
  take,
  throwError,
} from 'rxjs';
import { Store } from '@ngrx/store';
import { StorageService } from '@core/services/storage/storage.service';
import { SKIP_AUTH } from '@core/constants/http-context.tokens';
import { AuthActions } from '@core/auth/auth.actions';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const storageService = inject(StorageService);
  const store = inject(Store);

  const token = storageService.getItem('accessToken');
  const skipAuth = req.context.get(SKIP_AUTH);

  let modifiedReq = req.clone({ withCredentials: true });
  if (token && !skipAuth) {
    modifiedReq = modifiedReq.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  if (skipAuth) {
    return next(modifiedReq);
  }

  return next(modifiedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !req.url.includes('/refresh-token')) {
        if (isRefreshing) {
          return refreshTokenSubject.pipe(
            filter((token) => token !== null),
            take(1),
            switchMap((newToken) => {
              return next(
                req.clone({
                  setHeaders: { Authorization: `Bearer ${newToken}` },
                  withCredentials: true,
                }),
              );
            }),
          );
        }

        isRefreshing = true;
        refreshTokenSubject.next(null);
        return authService.callRefreshToken().pipe(
          switchMap((token) => {
            const newToken = token.accessToken;
            isRefreshing = false;
            storageService.setItem('accessToken', newToken);
            store.dispatch(
              AuthActions.loadRefreshTokenSuccess({ token: token }),
            );

            refreshTokenSubject.next(newToken);

            return next(
              req.clone({
                setHeaders: { Authorization: `Bearer ${newToken}` },
                withCredentials: true,
              }),
            );
          }),
          catchError((err) => {
            isRefreshing = false;
            storageService.removeItem('accessToken');
            store.dispatch(AuthActions.loadLogout());
            return throwError(() => err);
          }),
        );
      }
      return throwError(() => error);
    }),
  );
};
