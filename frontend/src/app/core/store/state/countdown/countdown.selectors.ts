import {createFeatureSelector, createSelector} from '@ngrx/store';
import {countdownFeatureKey, CountdownState} from '@/app/core/store/state/countdown/countdown.reducers';

export const selectCountdownState = createFeatureSelector<CountdownState>(countdownFeatureKey);


export const selectedRemainingTime = createSelector(
  selectCountdownState,
  (state) => state.remainingTime
)
