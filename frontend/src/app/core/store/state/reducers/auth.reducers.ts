import {createReducer, on} from '@ngrx/store';
import {AuthState} from '@/app/core/models/user.model';
import {AuthActions} from '@/app/core/store/state/actions/auth.actions';

export const authFeatureKey = 'authKey';

export const initialState: AuthState = {
  jwt: null,
  token: null,
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
  on(AuthActions.loginSuccess, (state, {jwt}) => ({
    ...state,
    jwt: jwt,
    userId: jwt.userId,
    loading: false
  })),
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
  on(AuthActions.logoutSuccess, () => initialState)
)
