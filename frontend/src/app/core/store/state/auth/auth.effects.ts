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

@Injectable()
export class AuthEffects {
  private actions$ = inject(Actions);
  private authService = inject(AuthService);
  private storageService = inject(StorageService);
  private toastService = inject(ToastService);

  initAuth$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ROOT_EFFECTS_INIT),
      map(() => {
        const jwt = this.storageService.getJwt();
        if (jwt) {
          try {
            this.authService.accessToken = jwt.accessToken;
            this.authService.refreshToken = jwt.refreshToken;
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
              message:
                'Tài khoản của bạn đã hết thời hạn, xin vui lòng đăng nhập lại',
              title: 'Lỗi',
              type: 'warning',
            });
            this.storageService.clearAuth();
            this.authService.accessToken = null;
            this.authService.refreshToken = null;
          }),
          map(() => AuthActions.loadLogoutSuccess()),
          catchError((err) => {
            this.storageService.clearAuth();
            this.authService.accessToken = null;
            this.authService.refreshToken = null;
            return of(AuthActions.loadLogoutFailure({ error: err }));
          }),
        ),
      ),
    );
  });

  refreshToken$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loadRefreshToken),
      switchMap(({ refreshToken }) =>
        this.authService.callRefreshToken(refreshToken).pipe(
          map((token) => {
            this.authService.accessToken = token.accessToken;
            this.authService.refreshToken = token.refreshToken;
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
