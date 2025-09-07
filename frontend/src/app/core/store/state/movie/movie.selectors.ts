import {createFeatureSelector, createSelector} from '@ngrx/store';
import {movieFeatureKey, MovieState} from '@/app/core/store/state/movie/movie.reducers';

export const selectMovieState = createFeatureSelector<MovieState>(movieFeatureKey);


// export const selectedAllSeats = createSelector(
//   selectMovieState,
//   (state) => state.seats
// )


