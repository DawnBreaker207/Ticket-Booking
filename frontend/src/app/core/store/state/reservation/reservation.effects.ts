import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { ReservationService } from '@/app/core/services/reservation/reservation.service';
import { ReservationActions } from '@/app/core/store/state/reservation/reservation.actions';
import { catchError, map, of, switchMap } from 'rxjs';

@Injectable()
export class ReservationEffects {
  private actions$ = inject(Actions);
  private reservationService = inject(ReservationService);

  loadReservations$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ReservationActions.loadReservations),
      switchMap(({ filter }) =>
        this.reservationService.getReservations(filter).pipe(
          map((reservations) =>
            ReservationActions.loadReservationsSuccess({ reservations }),
          ),
          catchError((err) =>
            of(ReservationActions.loadReservationsFailure({ error: err })),
          ),
        ),
      ),
    );
  });

  loadReservation$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ReservationActions.loadReservation),
      switchMap(({ id }) =>
        this.reservationService.getReservation(id).pipe(
          map((reservation) =>
            ReservationActions.loadReservationSuccess({ reservation }),
          ),
          catchError((err) =>
            of(ReservationActions.loadReservationFailure({ error: err })),
          ),
        ),
      ),
    );
  });

  createReservation$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ReservationActions.createReservation),
      switchMap(({ reservation }) =>
        this.reservationService.confirmReservation(reservation).pipe(
          map((reservation) =>
            ReservationActions.createReservationSuccess({ reservation }),
          ),
          catchError((err) =>
            of(ReservationActions.createReservationFailure({ error: err })),
          ),
        ),
      ),
    );
  });
}
