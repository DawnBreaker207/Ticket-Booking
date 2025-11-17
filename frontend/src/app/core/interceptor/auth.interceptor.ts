import {
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';
import { catchError, switchMap, throwError } from 'rxjs';
import { SKIP_AUTH } from '@/app/core/constants/context-token.model';
import { Store } from '@ngrx/store';
import { AuthActions } from '@/app/core/store/state/auth/auth.actions';
import { StorageService } from '@/app/shared/services/storage/storage.service';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const storageService = inject(StorageService);
  const store = inject(Store);
  const token = storageService.getItem('accessToken');
  const skipAuth = req.context.get(SKIP_AUTH);
  let modifiedReq = req.clone({ withCredentials: true });

  if (!skipAuth) {
    if (token) {
      modifiedReq = modifiedReq.clone({
        setHeaders: { Authorization: `Bearer ${token}` },
      });
    }

    return next(modifiedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !req.url.includes('/refreshToken')) {
          return authService.callRefreshToken().pipe(
            switchMap((token) => {
              storageService.setItem('accessToken', token.accessToken);
              store.dispatch(AuthActions.loadRefreshTokenSuccess({ token }));

              const retryReq: HttpRequest<any> = req.clone({
                setHeaders: { Authorization: `Bearer ${token.accessToken}` },
                withCredentials: true,
              });
              return next(retryReq);
            }),
            catchError((err) => {
              storageService.removeItem('accessToken');
              store.dispatch(AuthActions.loadLogout());
              return throwError(() => err);
            }),
          );
        }

        return throwError(() => error);
      }),
    );
  }

  return next(req);
};
