import {createActionGroup, emptyProps, props} from '@ngrx/store';
import {Jwt, RefreshToken} from '@/app/core/models/jwt.model';


export const AuthActions = createActionGroup(
  {
    source: 'Auth',
    events: {
      'Load Register': props<{ username: string, email: string, password: string }>(),
      'Register Success': props<{ token: RefreshToken }>(),
      'Register Failure': props<{ error: any }>(),

      'Load Login': props<{ username: string; password: string }>(),
      'Login Success': props<{ jwt: Jwt }>(),
      'Login Failure': props<{ error: any }>(),

      'Load Logout': emptyProps(),
      'Logout Success': emptyProps(),
    }

  }
)

