import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {SocketService} from '@/app/core/services/socket/socket.service';
import {interval, Subject, Subscription, takeUntil, withLatestFrom} from 'rxjs';
import {Store} from '@ngrx/store';
import {selectedRemainingTime} from '@/app/core/store/state/countdown/countdown.selectors';
import {AsyncPipe} from '@angular/common';
import {FormattedCountdownPipe} from '@/app/core/pipes/formatted-countdown.pipe';
import {CountdownActions} from '@/app/core/store/state/countdown/countdown.actions';
import {StorageService} from '@/app/shared/services/storage/storage.service';
import {ReservationRequest} from '@/app/core/models/reservation.model';

@Component({
  selector: 'app-countdown',
  imports: [AsyncPipe, FormattedCountdownPipe],
  templateUrl: './countdown.component.html',
  styleUrl: './countdown.component.css',
})
export class CountdownComponent implements OnInit, OnDestroy {
  private store = inject(Store);
  private socketService = inject(SocketService);
  private subscription: Subscription = new Subscription();
  private storageService = inject(StorageService);
  private destroy$ = new Subject<void>();

  remainingTime$ = this.store.select(selectedRemainingTime);

  ngOnInit() {
    const reservationState = this.storageService.getItem<ReservationRequest>("reservationState");
    const reservationId = reservationState?.reservationId;
    if (!reservationId) return;
    this.socketService.watchReservation(
      `/topic/reservation/${reservationId}`,
    ).pipe(takeUntil(this.destroy$)).subscribe((data) => {
        const body = JSON.parse(data.body);
        if (body.event === 'TTL_SYNC') {
          this.store.dispatch(
            CountdownActions.updateCountdownTTL({ttl: body.ttl}),
          );
        }
      }
    );


    interval(1000).pipe(
      withLatestFrom(this.remainingTime$),
      takeUntil(this.destroy$)
    ).subscribe(([_, ttl]) => {
      if (ttl > 0) {
        this.store.dispatch(
          CountdownActions.updateCountdownTTL({ttl: ttl - 1}),
        );
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    this.destroy$.next();
    this.destroy$.complete();
  }
}
