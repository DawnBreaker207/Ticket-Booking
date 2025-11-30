import { CanActivateChildFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '@/app/core/services/auth/auth.service';
import { Role } from '@/app/core/constants/enum';

export const RedirectGuard: CanActivateChildFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  console.log(authService.hasRole(Role.ADMIN.toString()));
  if (authService.hasRole(Role.ADMIN.toString())) {
    return router.parseUrl('/admin');
  }
  return true;
};
