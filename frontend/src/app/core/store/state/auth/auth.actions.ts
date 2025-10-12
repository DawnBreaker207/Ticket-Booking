import {createActionGroup, emptyProps, props} from '@ngrx/store';
import {Jwt, RefreshToken} from '@/app/core/models/jwt.model';
import {LoginRequest, RegisterRequest} from '@/app/core/models/user.model';


export const AuthActions = createActionGroup(
  {
    source: 'Auth',
    events: {
      'Load Register': props<{ user: RegisterRequest }>(),
      'Load Register Success': props<{ token: string }>(),
      'Load Register Failure': props<{ error: any }>(),

      'Load Login': props<{ user: LoginRequest }>(),
      'Load Login Success': props<{ jwt: Jwt }>(),
      'Load Login Failure': props<{ error: any }>(),

      'Load Logout': emptyProps(),
      'Load Logout Success': emptyProps(),
      'Load Logout Failure': props<{ error: any }>(),

      'Load Refresh Token': props<{ refreshToken: string }>(),
      'Load Refresh Token Success': props<{ token: RefreshToken }>(),
      'Load Refresh Token Failure': props<{ error: any }>(),
    }

  }
)

