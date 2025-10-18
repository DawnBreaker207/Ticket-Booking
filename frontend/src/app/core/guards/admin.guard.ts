import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {ToastService} from '@/app/shared/services/toast/toast.service';
import {delay, of, tap} from 'rxjs';

export const AdminGuard: CanActivateFn = (route, state) => {
  const toast = inject(ToastService);
  const router = inject(Router);

  const user = localStorage.getItem('accessToken');
  if (!user) {
    toast.createNotification({
      type: 'error',
      title: 'Do not have permission',
      message: 'You do not have permission to go to this page',
      position: 'topRight',
    });

    return of(false).pipe(
      delay(3000),
      tap(() => router.navigateByUrl('/home/login')),
    );
  }

  return of(true);
};
