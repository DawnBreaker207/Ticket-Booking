import {createReducer, on} from '@ngrx/store';
import {AuthActions} from '@/app/core/store/state/auth/auth.actions';
import {Jwt} from '@/app/core/models/jwt.model';

export const authFeatureKey = 'authKey';

export interface AuthState {
  jwt: Jwt | null,
  token: any | null,
  loading: boolean,
  error: any
}


export const initialState: AuthState = {
  jwt: JSON.parse(localStorage.getItem('jwt') || 'null'),
  token: JSON.parse(localStorage.getItem('token') || 'null'),
  loading: false,
  error: null
}

export const authReducer = createReducer(
  initialState,
  on(AuthActions.loadLogin, AuthActions.loadRegister, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(AuthActions.loginSuccess, (state, {jwt}) => {
    localStorage.setItem('jwt', JSON.stringify(jwt));
    return {
      ...state,
      jwt: jwt,
      userId: jwt.userId,
      loading: false
    }
  }),
  on(AuthActions.registerSuccess, (state, {token}) => ({
    ...state,
    token: token,
    loading: false
  })),
  on(AuthActions.loginFailure, AuthActions.registerFailure, (state, {error}) => ({
    ...state,
    error,
    loading: false
  })),
  on(AuthActions.logoutSuccess, () => {
    localStorage.removeItem('jwt');
    localStorage.removeItem('token');
    return initialState
  })
)
