import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { NzModalRef } from 'ng-zorro-antd/modal';
import { Subject } from 'rxjs';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { NzTagComponent } from 'ng-zorro-antd/tag';
import { Store } from '@ngrx/store';
import { StatusTagsPipe } from '@shared/pipes/status-tags.pipe';
import { Reservation } from '@domain/reservation/models/reservation.model';
import { ReservationActions } from '@domain/reservation/data-access/reservation.actions';
import { selectReservation } from '@domain/reservation/data-access/reservation.selectors';
@Component({
  selector: 'app-form',
  imports: [DatePipe, CurrencyPipe, NzTagComponent, StatusTagsPipe],
  templateUrl: './reservation-form.component.html',
  styleUrl: './reservation-form.component.css',
})
export class FormReservationComponent implements OnInit, OnDestroy {
  private modelRef = inject(NzModalRef);
  private store = inject(Store);
  destroy$: Subject<void> = new Subject();
  reservation!: Reservation;
  seats: string = '';

  ngOnInit() {
    const { reservationId } = this.modelRef.getConfig().nzData;
    this.store.dispatch(
      ReservationActions.loadReservation({ id: reservationId }),
    );
    this.store.select(selectReservation).subscribe((data) => {
      if (data) {
        this.reservation = data;
        this.seats = data.seats.map((seat) => seat.seatNumber).join(', ');
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
