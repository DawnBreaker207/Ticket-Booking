import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  authFeatureKey,
  AuthState,
} from '@/app/core/store/state/auth/auth.reducers';

export const selectAuthState = createFeatureSelector<AuthState>(authFeatureKey);

export const selectJwt = createSelector(selectAuthState, (state) => state.jwt);

export const selectToken = createSelector(
  selectAuthState,
  (state) => state.token,
);

export const selectAccessToken = createSelector(
  selectJwt,
  (jwt) => jwt?.accessToken ?? null,
);

export const selectRefreshToken = createSelector(
  selectJwt,
  (jwt) => jwt?.refreshToken ?? null,
);

export const selectUsername = createSelector(selectJwt, (jwt) => jwt?.username);

export const selectEmail = createSelector(selectJwt, (jwt) => jwt?.email);

export const selectIsAuthenticated = createSelector(
  selectJwt,
  (jwt) => !!jwt && !!jwt.accessToken,
);

export const selectAuthLoading = createSelector(
  selectAuthState,
  (state) => state.loading ?? false,
);

export const selectAuthError = createSelector(
  selectAuthState,
  (state) => state.error,
);
