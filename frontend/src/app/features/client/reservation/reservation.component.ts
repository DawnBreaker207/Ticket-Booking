import { Component, inject, OnInit, signal } from '@angular/core';
import { NzStepsModule } from 'ng-zorro-antd/steps';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { Store } from '@ngrx/store';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs';
import { CommonModule } from '@angular/common';
import { PaymentService } from '@domain/payment/data-access/payment.service';
import { StorageService } from '@core/services/storage/storage.service';
import { selectSelectedShowtime } from '@domain/showtime/data-access/showtime.selectors';
import { selectJwt } from '@core/auth/auth.selectors';
import { TheaterActions } from '@domain/theater/data-access/theater.actions';
import { ShowtimeActions } from '@domain/showtime/data-access/showtime.actions';
import { ReservationRequest } from '@domain/reservation/models/reservation.model';
import { SeatComponent } from '@features/client/reservation/components/seat/seat.component';
import { DetailFilmComponent } from '@features/client/reservation/components/detail-film/detail-film.component';
import { ConfirmComponent } from '@features/client/reservation/components/confirm/confirm.component';
import { TranslatePipe } from '@ngx-translate/core';
import { SeatStore } from '@features/client/reservation/components/seat/seat.store';
import { ReservationStore } from '@features/client/reservation/reservation.store';
import { toSignal } from '@angular/core/rxjs-interop';
import { selectSelectedTheater } from '@domain/theater/data-access/theater.selectors';

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
    TranslatePipe,
  ],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.css',
})
export class ReservationComponent implements OnInit {
  private store = inject(Store);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  readonly seatStore = inject(SeatStore);
  readonly reservationStore = inject(ReservationStore);
  private paymentService = inject(PaymentService);
  private storageService = inject(StorageService);

  readonly showtime = this.store.selectSignal(selectSelectedShowtime);
  readonly user = this.store.selectSignal(selectJwt);
  readonly theater = this.store.selectSignal(selectSelectedTheater);
  readonly reservationRoute = toSignal(
    this.route.paramMap.pipe(map((params) => params.get('reservationId'))),
  );
  index = signal(0);
  steps = signal([0, 1]);

  ngOnInit() {
    const state = this.storageService.getItem<any>('reservationState');
    const routeId = this.reservationRoute();
    if (!state || (!state.reservationId && !routeId)) {
      this.router.navigate(['/home']);
      return;
    }

    const reservationId = routeId || state.reservationId;

    if (!this.reservationStore.reservationId() && reservationId) {
      this.reservationStore.restore({ reservationId: reservationId });
    }

    this.store.dispatch(TheaterActions.loadTheater({ id: state.theaterId }));
    this.store.dispatch(ShowtimeActions.loadShowtime({ id: state.showtimeId }));
    this.seatStore.loadSeats({ showtimeId: state.showtimeId });
  }

  onStepChange(newIndex: number) {
    const currentIndex = this.index();
    console.log(currentIndex, newIndex);

    if (newIndex < currentIndex) {
      this.index.set(newIndex);
      return;
    }

    if (newIndex === 1 && currentIndex === 0) {
      const selectedSeats = this.seatStore.selectedSeats();
      const user = this.user();
      const showtime = this.showtime();
      const theater = this.theater();
      const resId = this.reservationStore.reservationId();

      if (!selectedSeats.length || !user || !showtime || !resId || !theater) {
        return;
      }

      const payload: ReservationRequest = {
        reservationId: resId,
        userId: user.userId,
        showtimeId: showtime.id,
        seatIds: selectedSeats.map((s) => s.id),
      };

      this.storageService.setItem('reservationState', {
        ...payload,
        theaterId: theater.id,
      });
      this.reservationStore.holdSeat({ reservation: payload });

      this.index.set(newIndex);
    }
    if (newIndex === 2) {
      const resId = this.reservationStore.reservationId();
      if (!resId) return;

      this.paymentService
        .createPayment({
          reservationId: resId,
          amount: this.seatStore.totalPrice(),
          paymentType: 'VNPAY',
        })
        .subscribe((res) => {
          window.open(res, '_self');
          this.index.set(newIndex);
        });
    }
  }
}
