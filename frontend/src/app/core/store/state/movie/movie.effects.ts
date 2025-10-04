import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, of, switchMap} from 'rxjs';
import {MovieService} from '@/app/core/services/movie/movie.service';
import {MovieActions} from '@/app/core/store/state/movie/movie.actions';

@Injectable()
export class ReservationEffects {
  private actions$ = inject(Actions);
  private movieService = inject(MovieService);

  movies$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MovieActions.loadMovies),
      switchMap(({movies}) =>
        this.movieService.getMovieLists()
          .pipe(
            map((order) => MovieActions.loadMovies({movies})),
            catchError((err) => of(MovieActions.loadMoviesFailed({error: err})))
          )
      )
    )
  })

  movie$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MovieActions.loadMovie),
      switchMap(({movie}) =>
        this.movieService.getMovieDetails(movie.id)
          .pipe(
            map((order) => MovieActions.loadMovie({movie})),
            catchError((err) => of(MovieActions.loadMovieFailed({error: err})))
          )
      )
    )
  })

}
