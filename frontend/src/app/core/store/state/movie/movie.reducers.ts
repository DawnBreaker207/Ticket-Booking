import {CinemaSeats} from '@/app/core/models/cinemaHall.model';
import {createReducer, on} from '@ngrx/store';
import {MovieActions} from '@/app/core/store/state/movie/movie.actions';

export const movieFeatureKey = 'movieKey';

export interface MovieState {
  seats: CinemaSeats[],
  cinemaHallId: number
  totalPrice: number
}

export const initialState: MovieState = {
  seats: [],
  totalPrice: 0,
  cinemaHallId: 0
}

export const movieReducer = createReducer(
  initialState,
  on(MovieActions.loadMovies, (state, {movies}) => {
    return {
      ...state,
    }
  }),
)
