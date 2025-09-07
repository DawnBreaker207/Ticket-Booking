import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {OrderService} from '@/app/core/services/order/order.service';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';
import {catchError, map, of, switchMap, tap} from 'rxjs';

@Injectable()
export class ReservationEffects {
  private actions$ = inject(Actions);
  private reservationService = inject(OrderService);

  createOrder$ = createEffect(() =>
      this.actions$.pipe(
        ofType(ReservationActions.createOrder),
        tap(({order}) => {
          localStorage.setItem('cinemaHallId', JSON.stringify(order.cinemaHallId));
          localStorage.setItem('orderStatus', JSON.stringify(order.orderStatus));
          localStorage.setItem('paymentMethod', JSON.stringify(order.paymentMethod));
          localStorage.setItem('paymentStatus', JSON.stringify(order.paymentStatus));
        })
      ),
    {dispatch: false}
  )

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
