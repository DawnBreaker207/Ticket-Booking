import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { NzResultModule } from 'ng-zorro-antd/result';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { ActivatedRoute, Router } from '@angular/router';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { Store } from '@ngrx/store';
import { StorageService } from '@core/services/storage/storage.service';
import { ReservationRequest } from '@domain/reservation/models/reservation.model';
import { ShowtimeActions } from '@domain/showtime/data-access/showtime.actions';
import { ReservationActions } from '@domain/reservation/data-access/reservation.actions';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-payment-result',
  imports: [NzResultModule, NzButtonModule, NzSpinModule, TranslatePipe],
  templateUrl: './payment-result.component.html',
  styleUrl: './payment-result.component.css',
})
export class PaymentResultComponent implements OnInit, OnDestroy {
  private store = inject(Store);
  private storageService = inject(StorageService);
  private intervalId: any;
  route = inject(ActivatedRoute);
  router = inject(Router);
  vnpTxnRef: any = 4;
  vnpResponseCode: any;
  vnpAmount: any;
  vnpOrderInfo: any;
  status: 'success' | 'error' | 'info' = 'info';
  errorMessage: string = '';
  countdown = 10;

  constructor() {
    this.route.queryParams.subscribe((params) => {
      this.vnpTxnRef = params['vnp_TxnRef'];
      this.vnpResponseCode = params['vnp_ResponseCode'];
      this.vnpAmount = params['vnp_Amount'];
      this.vnpOrderInfo = params['vnp_OrderInfo'];
    });
  }

  ngOnInit() {
    const state =
      this.storageService.getItem<ReservationRequest>('reservationState');
    if (!state) {
      this.status = 'error';
      this.errorMessage = `Thanh toán thất bại. Mã lỗi: ${this.vnpResponseCode}`;
      return;
    }
    this.store.dispatch(ShowtimeActions.loadShowtime({ id: state.showtimeId }));
    if (this.vnpTxnRef && this.vnpResponseCode === '00') {
      console.log(`This is success`);
      this.status = 'success';
      this.saveReservation(state);
    } else {
      this.status = 'error';
      this.cancelReservation(state.reservationId, state.userId);
      this.errorMessage = `Thanh toán thất bại. Mã lỗi: ${this.vnpResponseCode}`;
    }
    this.storageService.removeItem('reservationState');
    this.startCountDown();
  }

  saveReservation(reservation: ReservationRequest) {
    this.store.dispatch(
      ReservationActions.createReservation({
        reservation: {
          reservationId: this.vnpTxnRef,
          userId: reservation.userId,
          showtimeId: reservation.showtimeId,
          seatIds: reservation.seatIds,
        },
      }),
    );
  }

  cancelReservation(reservationId: string, userId: number) {
    this.store.dispatch(
      ReservationActions.cancelReservation({
        reservationId: reservationId,
        userId: userId,
      }),
    );
  }

  goHome() {
    this.router.navigate(['/home']);
  }

  startCountDown() {
    this.intervalId = setInterval(() => {
      this.countdown--;
      if (this.countdown === 0) {
        clearInterval(this.intervalId);
        this.goHome();
      }
    }, 1000);
  }

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }
}
