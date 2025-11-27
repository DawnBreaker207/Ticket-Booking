import {createFeatureSelector, createSelector} from '@ngrx/store';
import {
  theaterFeatureKey,
  TheaterState,
} from '@/app/core/store/state/theater/theater.reducers';

export const selectTheaterState =
  createFeatureSelector<TheaterState>(theaterFeatureKey);

export const selectAllTheaters =
  createSelector(
    selectTheaterState,
    (state) => state.theaters,
  );

export const selectSelectedTheater =
  createSelector(
    selectTheaterState,
    (state) => state.selectedTheater,
  );

export const selectTheaterById = (id: number) =>
  createSelector(
    selectAllTheaters,
    (theaters) => theaters.find((m) => m.id === id) || null,
  );

export const selectSelectedTheaterId =
  createSelector(selectTheaterState, (state) =>
    state.selectedTheaterId
  )

// Loading state selector
export const selectTheaterLoading =
  createSelector(
    selectTheaterState,
    (state) => state.loading,
  );

export const selectTheaterLoadingDetails =
  createSelector(
    selectTheaterState,
    (state) => state.loadingDetails,
  );

export const selectTheaterSaving =
  createSelector(
    selectTheaterState,
    (state) => state.saving,
  );

// Error Selector
export const selectTheaterError =
  createSelector(
    selectTheaterState,
    (state) => state.error,
  );

export const selectTheaterCount =
  createSelector(
    selectAllTheaters,
    (movies) => movies.length,
  );

export const selectHasTheater =
  createSelector(
    selectAllTheaters,
    (movies) => movies.length > 0,
  );
