import {createFeatureSelector, createSelector} from '@ngrx/store';
import {authFeatureKey, AuthState} from '@/app/core/store/state/reducers/auth.reducers';

export const selectAuthState = createFeatureSelector<AuthState>(authFeatureKey)

export const selectUser = createSelector(
  selectAuthState,
  (state: AuthState) => state?.jwt ?? undefined,
)
