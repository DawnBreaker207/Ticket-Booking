import { CanActivateFn, Router } from '@angular/router';
import { ToastService } from '@/app/shared/services/toast/toast.service';
import { inject } from '@angular/core';
import { AuthService } from '@/app/core/services/auth/auth.service';

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
