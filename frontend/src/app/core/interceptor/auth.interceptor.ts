import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth/auth.service';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.accessToken;

  let modifiedReq = req.clone({withCredentials: true})

  if (token) {
    modifiedReq = modifiedReq.clone({
      setHeaders: {'Authorization': `Bearer ${token}`},
    })
  }
  return next(modifiedReq);
};
