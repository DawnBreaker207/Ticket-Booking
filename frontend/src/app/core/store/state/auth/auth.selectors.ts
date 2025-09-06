import {createFeatureSelector, createSelector} from '@ngrx/store';
import {authFeatureKey, AuthState} from '@/app/core/store/state/auth/auth.reducers';

export const selectAuthState = createFeatureSelector<AuthState>(authFeatureKey)

export const selectUser = createSelector(
  selectAuthState,
  (state: AuthState) => state?.jwt ?? undefined,
)
