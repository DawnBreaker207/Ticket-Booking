import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, of, switchMap} from 'rxjs';
import {TheaterActions} from '@/app/core/store/state/theater/theater.actions';
import {TheaterService} from '@/app/core/services/theater/theater.service';

@Injectable()
export class TheaterEffects {
  private actions$ = inject(Actions);
  private theaterService = inject(TheaterService);

  loadTheaters$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(TheaterActions.loadTheaters),
      switchMap(() =>
        this.theaterService.getTheaters()
          .pipe(
            map((theaters) => TheaterActions.loadTheatersSuccess({theaters})),
            catchError((err) => of(TheaterActions.loadTheatersFailed({error: err})))
          )
      )
    )
  })

  loadTheater$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(TheaterActions.loadTheater),
      switchMap(({id}) =>
        this.theaterService.getTheater(id)
          .pipe(
            map((theater) => TheaterActions.loadTheaterSuccess({theater})),
            catchError((err) => of(TheaterActions.loadTheaterFailed({error: err})))
          )
      )
    )
  })
  createTheater$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(TheaterActions.createTheater),
      switchMap(({theater}) =>
        this.theaterService.createTheater(theater)
          .pipe(
            map((theater) => TheaterActions.createTheaterSuccess({theater})),
            catchError((err) => of(TheaterActions.createTheaterFailed({error: err})))
          )
      )
    )
  })
  updateTheater$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(TheaterActions.updateTheater),
      switchMap(({id, theater}) =>
        this.theaterService.updateTheater(id, theater)
          .pipe(
            map((theater) => TheaterActions.updateTheaterSuccess({theater})),
            catchError((err) => of(TheaterActions.updateTheaterFailed({error: err})))
          )
      )
    )
  })
  deleteTheater$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(TheaterActions.deleteTheater),
      switchMap(({id}) =>
        this.theaterService.deleteTheater(id)
          .pipe(
            map(() => TheaterActions.deleteTheaterSuccess({id})),
            catchError((err) => of(TheaterActions.deleteTheaterFailed({error: err})))
          )
      )
    )
  })

}
