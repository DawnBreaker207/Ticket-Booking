import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  showtimeFeatureKey,
  ShowtimeState,
} from '@/app/core/store/state/showtime/showtime.reducers';

export const selectShowtimeState =
  createFeatureSelector<ShowtimeState>(showtimeFeatureKey);

export const selectAllShowtimes = createSelector(
  selectShowtimeState,
  (state) => state.showtimes,
);

export const selectSelectedShowtime = createSelector(
  selectShowtimeState,
  (state) => state.selectedShowtime,
);

export const selectShowtimeById = (id: number) =>
  createSelector(
    selectAllShowtimes,
    (showtimes) => showtimes.find((m) => m.id === id) || null,
  );

// Loading state selector
export const selectShowtimeLoading = createSelector(
  selectShowtimeState,
  (state) => state.loading,
);

export const selectShowtimeLoadingDetails = createSelector(
  selectShowtimeState,
  (state) => state.loadingDetails,
);

export const selectShowtimeSaving = createSelector(
  selectShowtimeState,
  (state) => state.saving,
);

// Error Selector
export const selectShowtimeError = createSelector(
  selectShowtimeState,
  (state) => state.error,
);
export const selectShowtimeCount = createSelector(
  selectAllShowtimes,
  (movies) => movies.length,
);

export const selectHasShowtime = createSelector(
  selectAllShowtimes,
  (movies) => movies.length > 0,
);
