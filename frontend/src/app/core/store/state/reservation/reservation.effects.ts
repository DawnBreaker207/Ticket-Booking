import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { ReservationService } from '@/app/core/services/reservation/reservation.service';
import { ReservationActions } from '@/app/core/store/state/reservation/reservation.actions';
import {
  catchError,
  concatMap,
  map,
  of,
  switchMap,
  withLatestFrom,
} from 'rxjs';
import { Store } from '@ngrx/store';
import { selectSelectedSeats } from '@/app/core/store/state/seat/seat.selectors';
import { selectSelectedShowtime } from '@/app/core/store/state/showtime/showtime.selectors';

@Injectable()
export class ReservationEffects {
  private actions$ = inject(Actions);
  private store = inject(Store);
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

  createReservationInit$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ReservationActions.createReservationInit),
      switchMap(({ reservation }) =>
        this.reservationService.initReservation(reservation).pipe(
          map((response) => {
            return [
              ReservationActions.createReservationInitSuccess({
                reservationId: response.reservationId,
              }),
              ReservationActions.updateReservationTTL({
                reservationId: response.reservationId,
                ttl: response.ttl,
                expiredAt: response.expiredAt,
              }),
            ];
          }),
          concatMap((actions) => actions),
          catchError((err) =>
            of(ReservationActions.createReservationInitFailure({ error: err })),
          ),
        ),
      ),
    );
  });

  createReservationHoldSeat$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ReservationActions.createReservationHoldSeat),
      switchMap(({ reservation }) =>
        this.reservationService.holdReservationSeat(reservation).pipe(
          map(() => ReservationActions.createReservationHoldSeatSuccess()),
          catchError((err) =>
            of(
              ReservationActions.createReservationHoldSeatFailure({
                error: err,
              }),
            ),
          ),
        ),
      ),
    );
  });

  createReservation$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ReservationActions.createReservation),
      withLatestFrom(
        this.store.select(selectSelectedSeats),
        this.store.select(selectSelectedShowtime),
      ),
      switchMap(([{ reservation }, selectedSeats, selectShowtime]) => {
        const seatIds = selectedSeats.map((s) => s.id);
        const totalPrice = (selectShowtime?.price ?? 0) * selectedSeats.length;

        const request = {
          ...reservation,
          seatIds,
          totalAmount: totalPrice,
        };

        return this.reservationService.confirmReservation(request).pipe(
          map((reservation) =>
            ReservationActions.createReservationSuccess({ reservation }),
          ),
          catchError((err) =>
            of(ReservationActions.createReservationFailure({ error: err })),
          ),
        );
      }),
    );
  });

  cancelReservation$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ReservationActions.cancelReservation),
      switchMap(({ reservationId, userId }) =>
        this.reservationService.cancelReservation(reservationId, userId).pipe(
          map(() => ReservationActions.cancelReservationSuccess()),
          catchError((err) =>
            of(
              ReservationActions.cancelReservationFailure({
                error: err,
              }),
            ),
          ),
        ),
      ),
    );
  });
}
