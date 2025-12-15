import { Component, inject, OnInit, signal } from '@angular/core';
import { NzStepsModule } from 'ng-zorro-antd/steps';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { Store } from '@ngrx/store';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, filter, map, take, tap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { PaymentService } from '@domain/payment/data-access/payment.service';
import { StorageService } from '@core/services/storage/storage.service';
import {
  selectSeats,
  selectSelectedSeats,
} from '@domain/seat/data-access/seat.selectors';
import {
  selectSelectedShowtime,
  selectTotalPrice,
} from '@domain/showtime/data-access/showtime.selectors';
import { selectJwt } from '@core/auth/auth.selectors';
import { TheaterActions } from '@domain/theater/data-access/theater.actions';
import { ShowtimeActions } from '@domain/showtime/data-access/showtime.actions';
import { SeatActions } from '@domain/seat/data-access/seat.actions';
import { ReservationRequest } from '@domain/reservation/models/reservation.model';
import { ReservationActions } from '@domain/reservation/data-access/reservation.actions';
import { SeatComponent } from '@features/client/reservation/components/seat/seat.component';
import { DetailFilmComponent } from '@features/client/reservation/components/detail-film/detail-film.component';
import { ConfirmComponent } from '@features/client/reservation/components/confirm/confirm.component';

@Component({
  selector: 'app-reservation',
  imports: [
    NzStepsModule,
    NzIconModule,
    NzButtonModule,
    NzLayoutModule,
    SeatComponent,
    DetailFilmComponent,
    CommonModule,
    ConfirmComponent,
  ],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.css',
})
export class ReservationComponent implements OnInit {
  private store = inject(Store);
  private paymentService = inject(PaymentService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private storageService = inject(StorageService);
  selectedSeats$ = this.store.select(selectSelectedSeats);
  totalSeats$ = this.store.select(selectSeats);
  showtime$ = this.store.select(selectSelectedShowtime);
  user$ = this.store.select(selectJwt);

  totalPrice: number = 0;
  reservationId!: string;
  index = signal(0);
  steps = signal([0, 1]);

  ngOnInit() {
    const state = this.storageService.getItem<any>('reservationState');
    if (!state) {
      console.warn('No persisted reservation were found');
      this.router.navigate(['/home']);
      return;
    }
    this.reservationId = state.reservationId;
    this.route.paramMap
      .pipe(map((params) => params.get('reservationId') as string))
      .subscribe((params) => {
        this.reservationId = params;
      });
    this.store.dispatch(TheaterActions.loadTheater({ id: state.theaterId }));
    this.store.dispatch(ShowtimeActions.loadShowtime({ id: state.showtimeId }));
    this.store.dispatch(
      SeatActions.loadAllSeats({ showtimeId: state.showtimeId }),
    );
    this.store
      .select(selectTotalPrice)
      .subscribe((total) => (this.totalPrice = total));
  }

  onStepChange(newIndex: number) {
    console.log(newIndex, this.index());
    if (newIndex === 1 && this.index() === 0) {
      combineLatest([
        this.selectedSeats$.pipe(take(1)),
        this.showtime$.pipe(take(1)),
        this.user$.pipe(take(1)),
      ])
        .pipe(
          map(([seats, showtime, user]) => {
            console.log('Combine result:', { seats, showtime, user });
            if (!seats.length || !showtime || !user || !this.reservationId)
              return;
            const payload: ReservationRequest = {
              reservationId: this.reservationId,
              userId: user.userId,
              showtimeId: showtime.id,
              seatIds: seats.map((s) => s.id),
            };
            this.storageService.setItem('reservationState', payload);
            return payload;
          }),
          filter(
            (payload): payload is ReservationRequest => payload !== undefined,
          ),
          tap((payload) => {
            this.store.dispatch(
              ReservationActions.createReservationHoldSeat({
                reservation: payload,
              }),
            );
          }),
        )
        .subscribe(() => {
          this.index.set(newIndex);
        });
      return;
    }
    if (newIndex === 2 && this.index() === 1) {
      this.paymentService
        .createPayment({
          reservationId: this.reservationId,
          amount: this.totalPrice,
          paymentType: 'VNPAY',
        })
        .subscribe((res) => {
          window.open(res, '_self');
          this.index.set(newIndex);
        });
    }
  }
}
