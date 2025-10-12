import {createReducer, on} from '@ngrx/store';
import {AuthActions} from '@/app/core/store/state/auth/auth.actions';
import {Jwt, RefreshToken} from '@/app/core/models/jwt.model';

export const authFeatureKey = 'authKey';

export interface AuthState {
  jwt: Jwt | null,
  token: RefreshToken | null,
  loading: boolean,
  error: any
}


export const initialState: AuthState = {
  jwt: null,
  token: null,
  loading: false,
  error: null
}

export const authReducer = createReducer(
  initialState,
  // Register
  on(AuthActions.loadRegister, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(AuthActions.loadRegisterSuccess, (state, {token}) => ({
    ...state,
    token: token,
    loading: false,
    error: null
  })),
  on(AuthActions.loadRegisterFailure, (state, {error}) => ({
    ...state,
    error: error,
    loading: false
  })),
  // Login
  on(AuthActions.loadLogin, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(AuthActions.loadLoginSuccess, (state, {jwt}) => {
    return {
      ...state,
      jwt: jwt,
      userId: jwt.email,
      loading: false
    }
  }),
  on(AuthActions.loadLoginFailure, (state, {error}) => ({
    ...state,
    error: error,
    loading: false
  })),
  // Logout
  on(AuthActions.loadLogout, (state) => {
    return {
      ...state,
      loading: true,
    }
  }),
  on(AuthActions.loadLogoutSuccess, () => initialState),
  on(AuthActions.loadLogoutFailure, (state, {error}) => {
    return {
      ...state,
      error: error,
      loading: true,
    }
  }),
  //   Refresh Token
  on(AuthActions.loadRefreshToken, (state) => ({
    ...state,
    loading: false,
    error: null,
  })),
  on(AuthActions.loadRefreshTokenSuccess, (state, {token}) => ({
    ...state,
    token,
    jwt: state.jwt ? {...state.jwt, ...token} : null,
    loading: false,
    error: null,
  })),
  on(AuthActions.loadRefreshTokenFailure, (state, {error}) => ({
    ...state,
    loading: false,
    error: error,
  })),
)
