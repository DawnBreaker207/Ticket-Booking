import {HttpInterceptorFn} from '@angular/common/http';
import {USE_HEADER} from '@/app/core/constants/context-token.model';

export const CredentialInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.context.get(USE_HEADER)) {
    const modifierRed = req.clone({withCredentials: true});
    return next(modifierRed);
  }
  return next(req);
};
