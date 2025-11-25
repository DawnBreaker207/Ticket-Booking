import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, of, switchMap } from 'rxjs';
import { MovieService } from '@/app/core/services/movie/movie.service';
import { MovieActions } from '@/app/core/store/state/movie/movie.actions';

@Injectable()
export class MovieEffects {
  private actions$ = inject(Actions);
  private movieService = inject(MovieService);

  loadMovies$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MovieActions.loadMovies),
      switchMap(({ page, size }) =>
        this.movieService.getMovieLists({ page, size }).pipe(
          map((res) =>
            MovieActions.loadMoviesSuccess({
              movies: res.content,
              pagination: res.pagination,
            }),
          ),
          catchError((err) =>
            of(MovieActions.loadMoviesFailed({ error: err })),
          ),
        ),
      ),
    );
  });

  searchMovies$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MovieActions.searchMovies),
      switchMap(({ search }) =>
        this.movieService.getMovieLists(search).pipe(
          map((page) => MovieActions.searchMoviesSuccess({ page: page })),
          catchError((err) =>
            of(MovieActions.searchMoviesFailed({ error: err })),
          ),
        ),
      ),
    );
  });

  loadMovie$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MovieActions.loadMovie),
      switchMap(({ id }) =>
        this.movieService.findOneMovie(id).pipe(
          map((movie) => MovieActions.loadMovieSuccess({ movie })),
          catchError((err) => of(MovieActions.loadMovieFailed({ error: err }))),
        ),
      ),
    );
  });

  createMovie$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MovieActions.createMovie),
      switchMap(({ movie }) =>
        this.movieService.saveMovie(movie).pipe(
          map((movie) => MovieActions.createMovieSuccess({ movie })),
          catchError((err) =>
            of(MovieActions.createMovieFailed({ error: err })),
          ),
        ),
      ),
    );
  });

  updateMovie$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MovieActions.updateMovie),
      switchMap(({ id, movie }) =>
        this.movieService.updateMovie(id, movie).pipe(
          map((movie) => MovieActions.updateMovieSuccess({ movie })),
          catchError((err) =>
            of(MovieActions.updateMovieFailed({ error: err })),
          ),
        ),
      ),
    );
  });

  deleteMovie$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MovieActions.deleteMovie),
      switchMap(({ id }) =>
        this.movieService.removeMovie(id).pipe(
          map(() => MovieActions.deleteMovieSuccess({ id })),
          catchError((err) =>
            of(MovieActions.deleteMovieFailed({ error: err })),
          ),
        ),
      ),
    );
  });
}
