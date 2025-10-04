import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {SocketService} from '@/app/core/services/socket/socket.service';
import {EMPTY, interval, Subscription, switchMap, take} from 'rxjs';
import {Store} from '@ngrx/store';
import {selectedOrder} from '@/app/core/store/state/reservation/reservation.selectors';
import {selectedRemainingTime} from '@/app/core/store/state/countdown/countdown.selectors';
import {CountdownActions} from '@/app/core/store/state/countdown/countdown.action';
import {AsyncPipe} from '@angular/common';
import {FormattedCountdownPipe} from '@/app/core/pipes/formatted-countdown.pipe';

@Component({
  selector: 'app-countdown',
  imports: [
    AsyncPipe,
    FormattedCountdownPipe
  ],
  templateUrl: './countdown.component.html',
  styleUrl: './countdown.component.css'
})
export class CountdownComponent implements OnInit, OnDestroy {
  private store = inject(Store);

  remainingTime = 0;
  remainingTime$ = this.store.select(selectedRemainingTime);

  private socketService = inject(SocketService);
  private subscription: Subscription = new Subscription();

  ngOnInit() {
    const orderSub = this.store
      .select(selectedOrder)
      .pipe(
        switchMap(order => {
            if (!order?.orderId) return EMPTY;
            return this.socketService.watchOrder(`/topic/order/${order.orderId}`)
          }
        )
      ).subscribe(data => {
        const body = JSON.parse(data.body);
        if (body.event === 'TTL_SYNC') {
          // this.remainingTime = body.ttl;
          this.store.dispatch(CountdownActions.updateCountdownTTL({ttl: body.ttl}))
        }
      })

    const countDownSub = interval(1000).subscribe(() => {
      this.remainingTime$.pipe(take(1)).subscribe(ttl => {
        if (ttl > 0) {
          this.store.dispatch(CountdownActions.updateCountdownTTL({ttl: ttl - 1}))
        }
      })
      // if (this.remainingTime > 0) {
      //   this.remainingTime--;
      // }
    })

    this.subscription.add(orderSub);
    this.subscription.add(countDownSub);
  }


  ngOnDestroy() {
    this.subscription.unsubscribe()
  }
}
