import { createReducer, on } from '@ngrx/store';
import { CountdownActions } from '@/app/core/store/state/countdown/countdown.actions';

export const countdownFeatureKey = 'countdownKey';

export interface CountdownState {
  remainingTime: number;
}

export const initialState: CountdownState = {
  remainingTime: 0,
};

export const countdownReducer = createReducer(
  initialState,
  on(CountdownActions.updateCountdownTTL, (state, { ttl }) => ({
    ...state,
    remainingTime: ttl,
  })),
);
