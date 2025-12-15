import { CanActivateFn, Router } from '@angular/router';

import { inject } from '@angular/core';
import { ToastService } from '@core/services/toast/toast.service';
import { AuthService } from '@core/auth/auth.service';

export const AuthGuard: CanActivateFn = (route, state) => {
  const toast = inject(ToastService);
  const router = inject(Router);
  const authService = inject(AuthService);
  if (!authService.isAuthenticated()) {
    toast.createNotification({
      type: 'error',
      title: 'Authentication Required',
      message: 'Please login to access this page',
    });
    return router.parseUrl('/login');
  }

  return true;
};
