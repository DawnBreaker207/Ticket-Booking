import {HttpInterceptorFn} from '@angular/common/http';

export const CredentialInterceptor: HttpInterceptorFn = (req, next) => {
  const modifierRed = req.clone({withCredentials: true});
  return next(modifierRed);
};
