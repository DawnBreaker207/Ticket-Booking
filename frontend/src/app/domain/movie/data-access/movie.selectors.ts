import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  movieFeatureKey,
  MovieState,
} from '@domain/movie/data-access/movie.reducers';

export const selectMovieState =
  createFeatureSelector<MovieState>(movieFeatureKey);

export const selectAllMovies = createSelector(
  selectMovieState,
  (state) => state.movies,
);

export const selectPaginationMovie = createSelector(
  selectMovieState,
  (state) => state.pagination,
);
export const selectSelectedMovie = createSelector(
  selectMovieState,
  (state) => state.selectedMovie,
);

export const selectMovieById = (id: number) =>
  createSelector(
    selectAllMovies,
    (movies) => movies.find((m) => m.id === id) || null,
  );

// Loading state selector
export const selectMovieLoading = createSelector(
  selectMovieState,
  (state) => state.loading,
);

export const selectMovieLoadingDetails = createSelector(
  selectMovieState,
  (state) => state.loadingDetails,
);

export const selectMoviesSaving = createSelector(
  selectMovieState,
  (state) => state.saving,
);

// Error Selector
export const selectMoviesError = createSelector(
  selectMovieState,
  (state) => state.error,
);

// Search Selector
export const selectSearchQuery = createSelector(
  selectMovieState,
  (state) => state.searchQuery,
);

export const selectMoviesCount = createSelector(
  selectAllMovies,
  (movies) => movies.length,
);

export const selectHasMovies = createSelector(
  selectAllMovies,
  (movies) => movies.length > 0,
);
