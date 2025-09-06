import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {OrderService} from '@/app/core/services/order/order.service';
import {ReservationActions} from '@/app/core/store/state/actions/reservation.actions';
import {catchError, map, of, switchMap} from 'rxjs';
import {AuthActions} from '@/app/core/store/state/actions/auth.actions';

@Injectable()
export class ReservationEffects {
  private actions$ = inject(Actions);
  private reservationService = inject(OrderService);

  confirm$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ReservationActions.confirmOrder),
      switchMap(({order}) =>
        this.reservationService.confirm(order)
          .pipe(
            map((order) => ReservationActions.confirmOrderSuccess({order})),
            catchError((err) => of(ReservationActions.confirmOrderFailure({error: err})))
          )
      )
    )
  })

}
