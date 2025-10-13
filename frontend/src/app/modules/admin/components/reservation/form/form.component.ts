import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NzModalRef} from 'ng-zorro-antd/modal';
import {Subject} from 'rxjs';
import {CurrencyPipe, DatePipe} from '@angular/common';
import {NzTagComponent} from 'ng-zorro-antd/tag';
import {StatusTagsPipe} from '@/app/core/pipes/status-tags.pipe';
import {Store} from '@ngrx/store';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';
import {selectReservation} from '@/app/core/store/state/reservation/reservation.selectors';
import {Reservation} from '@/app/core/models/reservation.model';

@Component({
  selector: 'app-form',
  imports: [
    DatePipe,
    CurrencyPipe,
    NzTagComponent,
    StatusTagsPipe
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css',
})
export class FormReservationComponent implements OnInit, OnDestroy {
  private modelRef = inject(NzModalRef);
  private store = inject(Store);
  destroy$: Subject<void> = new Subject();
  reservation!: Reservation;

  ngOnInit() {
    const {reservationId} = this.modelRef.getConfig().nzData;
    this.store.dispatch(ReservationActions.loadReservation({id: reservationId}))
    this.store.select(selectReservation).subscribe(data => {
      if (data) {
        this.reservation = data;
      }
    })
  }


  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
