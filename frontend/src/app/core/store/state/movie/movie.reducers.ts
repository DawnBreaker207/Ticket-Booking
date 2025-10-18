import { createReducer, on } from '@ngrx/store';
import { MovieActions } from '@/app/core/store/state/movie/movie.actions';
import { Movie } from '@/app/core/models/movie.model';

export const movieFeatureKey = 'movieKey';

export interface MovieState {
  movies: Movie[];
  selectedMovie: Movie | null;
  // Loading states
  loading: boolean;
  loadingDetails: boolean;
  saving: boolean;
  searchQuery: string | null;

  error: string | null;
}

export const initialState: MovieState = {
  movies: [],
  selectedMovie: null,
  loading: false,
  loadingDetails: false,
  saving: false,
  error: null,
  searchQuery: null,
};

export const movieReducer = createReducer(
  initialState,
  // Load all
  on(MovieActions.loadMovies, (state) => {
    return {
      ...state,
      loading: true,
      error: null,
    };
  }),
  on(MovieActions.loadMoviesSuccess, (state, { movies }) => {
    return {
      ...state,
      movies: movies,
      loading: false,
      error: null,
    };
  }),
  on(MovieActions.loadMoviesFailed, (state, { error }) => {
    return {
      ...state,
      loading: false,
      error,
    };
  }),
  //   Search
  on(MovieActions.searchMovies, (state, { search }) => {
    return {
      ...state,
      loading: true,
      searchQuery: search,
      error: null,
    };
  }),
  on(MovieActions.searchMoviesSuccess, (state, { movies }) => {
    return {
      ...state,
      movies,
      loading: false,
      error: null,
    };
  }),
  on(MovieActions.searchMoviesFailed, (state, { error }) => {
    return {
      ...state,
      loading: false,
      error,
    };
  }),
  //  Get one
  on(MovieActions.loadMovie, (state) => {
    return {
      ...state,
      loadingDetails: true,
      error: null,
    };
  }),
  on(MovieActions.loadMovieSuccess, (state, { movie }) => {
    const existingIndex = state.movies.findIndex((m) => m.id === movie.id);
    const movies =
      existingIndex >= 0
        ? state.movies.map((m) => (m.id === movie.id ? movie : m))
        : [...state.movies, movie];
    return {
      ...state,
      movies,
      selectedMovie: movie,
      loadingDetails: false,
      error: null,
    };
  }),
  on(MovieActions.loadMovieFailed, (state, { error }) => {
    return {
      ...state,
      loadingDetails: false,
      error,
    };
  }),
  //  Create
  on(MovieActions.createMovie, (state) => {
    return {
      ...state,
      saving: true,
      error: null,
    };
  }),
  on(MovieActions.createMovieSuccess, (state, { movie }) => {
    return {
      ...state,
      movies: [...state.movies, movie],
      saving: false,
      error: null,
    };
  }),
  on(MovieActions.createMovieFailed, (state, { error }) => {
    return {
      ...state,
      saving: false,
      error,
    };
  }),
  //  Update
  on(MovieActions.updateMovie, (state) => {
    return {
      ...state,
      saving: true,
      error: null,
    };
  }),
  on(MovieActions.updateMovieSuccess, (state, { movie }) => {
    return {
      ...state,
      movies: state.movies.map((m) => (m.id === movie.id ? movie : m)),
      selectedMovieId:
        state.selectedMovie?.id === movie.id ? movie : state.selectedMovie,
      saving: false,
      error: null,
    };
  }),
  on(MovieActions.updateMovieFailed, (state, { error }) => {
    return {
      ...state,
      saving: false,
      error,
    };
  }),
  //  Delete
  on(MovieActions.deleteMovie, (state) => {
    return {
      ...state,
      loading: true,
      error: null,
    };
  }),
  on(MovieActions.deleteMovieSuccess, (state, { id }) => {
    return {
      ...state,
      movies: state.movies.filter((m) => m.id !== id),
      selectedMovie:
        state.selectedMovie?.id === id ? null : state.selectedMovie,
      loading: false,
      error: null,
    };
  }),
  on(MovieActions.deleteMovieFailed, (state, { error }) => {
    return {
      ...state,
      loading: false,
      error,
    };
  }),
  on(MovieActions.selectedMovie, (state, { movie }) => {
    return {
      ...state,
      selectedMovie: movie,
      error: null,
    };
  }),
  on(MovieActions.clearSelectedMovie, (state) => {
    return {
      ...state,
      selectedMovie: null,
      error: null,
    };
  }),
  on(MovieActions.clearError, (state) => {
    return {
      ...state,
      error: null,
    };
  }),
);
