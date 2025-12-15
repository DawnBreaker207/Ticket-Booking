import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  seatFeatureKey,
  SeatState,
} from '@domain/seat/data-access/seat.reducers';

export const selectSeatState = createFeatureSelector<SeatState>(seatFeatureKey);

export const selectSeats = createSelector(
  selectSeatState,
  (state) => state.seats,
);

export const selectSelectedSeats = createSelector(
  selectSeatState,
  (state) => state.selectedSeat,
);

export const selectSelectedTotalSeats = createSelector(
  selectSeatState,
  (state) => state.selectedSeat.length,
);
