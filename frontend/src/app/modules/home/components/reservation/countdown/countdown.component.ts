import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { interval, Subject, takeUntil, withLatestFrom } from 'rxjs';
import { Store } from '@ngrx/store';
import { AsyncPipe } from '@angular/common';
import { FormattedCountdownPipe } from '@/app/core/pipes/formatted-countdown.pipe';
import { StorageService } from '@/app/shared/services/storage/storage.service';
import { ReservationRequest } from '@/app/core/models/reservation.model';
import { selectedRemainingTime } from '@/app/core/store/state/reservation/reservation.selectors';
import { ReservationActions } from '@/app/core/store/state/reservation/reservation.actions';

@Component({
  selector: 'app-countdown',
  imports: [AsyncPipe, FormattedCountdownPipe],
  templateUrl: './countdown.component.html',
  styleUrl: './countdown.component.css',
})
export class CountdownComponent implements OnInit, OnDestroy {
  private store = inject(Store);
  private storageService = inject(StorageService);
  private destroy$ = new Subject<void>();

  remainingTime$ = this.store.select(selectedRemainingTime);

  ngOnInit() {
    const reservationState =
      this.storageService.getItem<ReservationRequest>('reservationState');
    const reservationId = reservationState?.reservationId;
    const userId = reservationState?.userId;
    if (!reservationId || !userId) return;

    interval(1000)
      .pipe(withLatestFrom(this.remainingTime$), takeUntil(this.destroy$))
      .subscribe(([_, ttl]) => {
        if (!ttl || ttl < 0) return;
        if (ttl > 0) {
          this.store.dispatch(
            ReservationActions.updateReservationCountdownTTL({ ttl: ttl - 1 }),
          );
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
