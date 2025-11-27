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

export const selectPaginationReservation = createSelector(
  selectReservationState,
  (state) => state.pagination,
);

export const selectReservation = createSelector(
  selectReservationState,
  (state) => state.reservation,
);

export const selectCurrentTTL = createSelector(
  selectReservationState,
  (state) => state.currentTTL,
);

export const selectedRemainingTime = createSelector(selectCurrentTTL, (ttl) => {
  if (!ttl) return;
  return ttl.ttl;
});

export const selectReservationLoading = createSelector(
  selectReservationState,
  (state) => state.loading,
);
export const selectReservationError = createSelector(
  selectReservationState,
  (state) => state.error,
);
