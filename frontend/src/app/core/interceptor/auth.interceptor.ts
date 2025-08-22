import {HttpErrorResponse, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth/auth.service';
import {catchError, switchMap, throwError} from 'rxjs';
import {SKIP_AUTH} from '@/app/core/constants/context-token.model';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.accessToken;
  let modifiedReq = req.clone({withCredentials: true})

  if (!req.context.get(SKIP_AUTH)) {


    if (token) {
      modifiedReq = modifiedReq.clone({
        setHeaders: {'Authorization': `Bearer ${token}`},
      })
    }
    return next(modifiedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !req.url.includes('/refreshToken')) {
          const refreshToken = localStorage.getItem('refreshToken') ?? '';

          return authService.callRefreshToken(refreshToken).pipe(
            switchMap((res) => {
              authService.accessToken = res.accessToken;
              authService.refreshToken = res.refreshToken;


              const retryReq: HttpRequest<any> = req.clone({
                setHeaders: {Authorization: `Bearer ${res.accessToken}`},
                withCredentials: true
              });
              return next(retryReq);
            }),
            catchError((err) => {
              authService.logout();
              return throwError(() => err);
            })
          );
        }

        return throwError(() => error);
      })
    );
  }

  return next(req);
};
