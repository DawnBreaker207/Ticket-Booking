import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, of, switchMap } from 'rxjs';
import { TheaterService } from '@/app/core/services/theater/theater.service';
import { SeatActions } from '@/app/core/store/state/seat/seat.actions';

@Injectable()
export class SeatEffects {
  private actions$ = inject(Actions);
  private seatService = inject(TheaterService);

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
