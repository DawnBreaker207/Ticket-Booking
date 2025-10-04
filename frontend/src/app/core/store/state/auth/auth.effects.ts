import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {AuthService} from '@/app/core/services/auth/auth.service';
import {AuthActions} from '@/app/core/store/state/auth/auth.actions';
import {catchError, map, of, switchMap} from 'rxjs';

@Injectable()
export class AuthEffects {
  private actions$ = inject(Actions);
  private authService = inject(AuthService);

  register$ = createEffect(() => {
    return this.actions$
      .pipe(
        ofType(AuthActions.loadRegister),
        switchMap(({username, email, password}) =>
          this.authService.register(username, email, password)
            .pipe(
              map((token) =>
                AuthActions.registerSuccess({token})),
              catchError((err) =>
                of(AuthActions.registerFailure({error: err}))
              )),
        ))
  })
  login$ = createEffect(() => {
    return this.actions$
      .pipe(
        ofType(AuthActions.loadLogin),
        switchMap(({username, password}) =>
          this.authService.login(username, password)
            .pipe(
              map((jwt) =>
                AuthActions.loginSuccess({jwt}),
              ),
              catchError((err) =>
                of(AuthActions.loginFailure({error: err})
                )))
        )
      )
  })
}
