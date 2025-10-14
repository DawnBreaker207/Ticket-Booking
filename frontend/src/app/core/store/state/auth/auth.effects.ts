import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AuthService } from '@/app/core/services/auth/auth.service';
import { AuthActions } from '@/app/core/store/state/auth/auth.actions';
import { catchError, map, of, switchMap } from 'rxjs';
import { StorageService } from '@/app/shared/services/storage/storage.service';

@Injectable()
export class AuthEffects {
  private actions$ = inject(Actions);
  private authService = inject(AuthService);
  private storageService = inject(StorageService);

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
          map((jwt) => {
            this.storageService.setJWT(jwt);
            return AuthActions.loadLoginSuccess({ jwt });
          }),
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
          map(() => {
            this.storageService.clearAuth();
            this.authService.accessToken = null;
            this.authService.refreshToken = null;
            return AuthActions.loadLogoutSuccess();
          }),
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
