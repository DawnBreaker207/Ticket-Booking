import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, of, switchMap} from 'rxjs';
import {SeatActions} from '@domain/seat/data-access/seat.actions';
import {SeatService} from '@domain/seat/data-access/seat.service';

@Injectable()
export class SeatEffects {
  private actions$ = inject(Actions);
  private seatService = inject(SeatService);

  register$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(SeatActions.loadAllSeats),
      switchMap(({ showtimeId }) =>
        this.seatService.getSeatByShowtime(showtimeId).pipe(
          map((seats) => SeatActions.loadAllSeatsSuccess({ seats: seats })),
          catchError((err) =>
            of(SeatActions.loadAllSeatsFailed({ error: err })),
          ),
        ),
      ),
    );
  });
}
