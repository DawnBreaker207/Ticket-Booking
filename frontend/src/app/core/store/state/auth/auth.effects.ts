import { inject, Injectable } from '@angular/core';
import {
  Actions,
  createEffect,
  ofType,
  ROOT_EFFECTS_INIT,
} from '@ngrx/effects';
import { AuthService } from '@/app/core/services/auth/auth.service';
import { AuthActions } from '@/app/core/store/state/auth/auth.actions';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { StorageService } from '@/app/shared/services/storage/storage.service';
import { ToastService } from '@/app/shared/services/toast/toast.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthEffects {
  private actions$ = inject(Actions);
  private authService = inject(AuthService);
  private storageService = inject(StorageService);
  private toastService = inject(ToastService);
  private router = inject(Router);

  initAuth$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ROOT_EFFECTS_INIT),
      map(() => {
        const jwt = this.storageService.getJwt();
        if (jwt) {
          try {
            return AuthActions.loadLoginSuccess({ jwt });
          } catch (e) {
            console.error('Invalid JWT in storage');
            return AuthActions.loadLogoutSuccess();
          }
        } else {
          return AuthActions.loadLogoutSuccess();
        }
      }),
    );
  });

  register$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loadRegister),
      switchMap(({ user }) =>
        this.authService.register(user).pipe(
          tap(() => {
            this.toastService.createNotification({
              message: 'Đăng ký thành công',
              title: 'Thành công',
              type: 'success',
            });
          }),
          map((token) => AuthActions.loadRegisterSuccess({ token })),
          catchError((err) =>
            of(AuthActions.loadRegisterFailure({ error: err })),
          ),
        ),
      ),
    );
  });

  login$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loadLogin),
      switchMap(({ user }) =>
        this.authService.login(user).pipe(
          tap((jwt) => {
            this.storageService.setJWT(jwt);
            this.toastService.createNotification({
              message: 'Đăng nhập thành công',
              title: 'Thành công',
              type: 'success',
            });
          }),
          map((jwt) => AuthActions.loadLoginSuccess({ jwt })),
          catchError((err) => of(AuthActions.loadLoginFailure({ error: err }))),
        ),
      ),
    );
  });

  logout$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loadLogout),
      switchMap(() =>
        this.authService.logout().pipe(
          tap(() => {
            this.toastService.createNotification({
              type: 'success',
              title: 'Thành công',
              message: 'Tài khoản của bạn đã đăng xuất thành công',
            });
            this.storageService.clearAuth();
            this.router.navigate(['/home']);
          }),
          map(() => AuthActions.loadLogoutSuccess()),
          catchError((err) => {
            this.storageService.clearAuth();
            return of(AuthActions.loadLogoutFailure({ error: err }));
          }),
        ),
      ),
    );
  });

  refreshToken$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loadRefreshToken),
      switchMap(() =>
        this.authService.callRefreshToken().pipe(
          map((token) => {
            return AuthActions.loadRefreshTokenSuccess({ token });
          }),
          catchError((err) =>
            of(AuthActions.loadRefreshTokenFailure({ error: err })),
          ),
        ),
      ),
    );
  });
}
