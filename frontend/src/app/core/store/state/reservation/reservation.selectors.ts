import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  reservationFeatureKey,
  ReservationState,
} from '@/app/core/store/state/reservation/reservation.reducers';

export const selectReservationState = createFeatureSelector<ReservationState>(
  reservationFeatureKey,
);

export const selectReservations = createSelector(
  selectReservationState,
  (state) => state.reservations,
);

export const selectReservation = createSelector(
  selectReservationState,
  (state) => state.reservation,
);
export const selectReservationLoading = createSelector(
  selectReservationState,
  (state) => state.loading,
);
export const selectReservationError = createSelector(
  selectReservationState,
  (state) => state.error,
);
