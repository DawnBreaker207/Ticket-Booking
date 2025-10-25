import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { Seat, Showtime } from '@/app/core/models/theater.model';
import {
  selectSeats,
  selectSelectedSeats,
} from '@/app/core/store/state/seat/seat.selectors';
import { filter, map, Subject, switchMap, take, takeUntil } from 'rxjs';
import { SeatActions } from '@/app/core/store/state/seat/seat.actions';
import {
  selectPrice,
  selectSelectedShowtime,
  selectTotalPrice,
} from '@/app/core/store/state/showtime/showtime.selectors';
import { CountdownComponent } from '@/app/modules/home/components/reservation/countdown/countdown.component';
import { SseService } from '@/app/core/services/sse/sse.service';
import { SeatStatus } from '@/app/core/constants/enum';
import { StorageService } from '@/app/shared/services/storage/storage.service';
import { ReservationRequest } from '@/app/core/models/reservation.model';

@Component({
  selector: ' app-seat',
  imports: [CommonModule, CountdownComponent],
  templateUrl: './seat.component.html',
  styleUrl: './seat.component.css',
})
export class SeatComponent implements OnInit, OnDestroy {
  private store = inject(Store);
  private sseService = inject(SseService);
  private destroy$: Subject<void> = new Subject();
  private storageService = inject(StorageService);
  showtime$ = this.store.select(selectSelectedShowtime);
  seats$ = this.store.select(selectSeats);
  selectSeats$ = this.store.select(selectSelectedSeats);
  totalPrice$ = this.store.select(selectTotalPrice);
  price$ = this.store.select(selectPrice);

  ngOnInit() {
    const reservationState =
      this.storageService.getItem<ReservationRequest>('reservationState');
    const reservationId = reservationState?.reservationId;
    this.showtime$
      .pipe(
        filter((data): data is Showtime => !!data),
        switchMap((data) =>
          this.sseService.connect(data?.id, reservationState?.userId as number),
        ),
        takeUntil(this.destroy$),
      )
      .subscribe((res: any) => {
        this.seats$.pipe(take(1)).subscribe((seats) => {
          if (res.event === 'SEAT_STATE_INIT') {
            res.data.seatIds.forEach((s: any) => {
              const isMySeat = s.reservationId === reservationId;
              const seatId = Number(s.seatId);
              const existingSeat = seats.find((x) => x.id === seatId);
              const seat: Seat = {
                id: seatId,
                seatNumber: existingSeat?.seatNumber || seatId.toString(),
                status: isMySeat ? 'SELECTED' : ('HOLD' as SeatStatus),
              } as Seat;
              console.log(seat);
              this.store.dispatch(SeatActions.selectSeat({ seat: seat }));
            });
          } else if (res.event === 'SEAT_HOLD') {
            const uniqueSeats = res.data.seatIds.filter(
              (v: any, i: number, a: any[]) =>
                a.findIndex((t) => t.seatId === v.seatId) === i,
            );
            uniqueSeats.forEach((s: any) => {
              const isMySeat = s.reservationId === reservationId;
              const seatId = Number(s.seatId);
              const existingSeat = seats.find((x) => x.id === seatId);

              this.store.dispatch(SeatActions.deselectSeat({ seatId }));

              const seat: Seat = {
                id: s.seatId,
                seatNumber: existingSeat?.seatNumber || seatId.toString(),
                status: isMySeat ? 'SELECTED' : ('HOLD' as SeatStatus),
              } as Seat;
              this.store.dispatch(SeatActions.selectSeat({ seat: seat }));
            });
          } else if (res.event === 'SEAT_RELEASE') {
            res.data.seatIds.forEach((seatId: number) => {
              this.store.dispatch(SeatActions.deselectSeat({ seatId: seatId }));
            });
          }
        });
      });
  }

  toggleSeat(seat: Seat) {
    if (seat.status === 'BOOKED' || seat.status === 'HOLD') return;
    this.selectSeats$.pipe(take(1)).subscribe((selected) => {
      const isSelected = selected.some((s) => s.id === seat.id);
      this.store.dispatch(
        isSelected
          ? SeatActions.deselectSeat({ seatId: seat.id })
          : SeatActions.selectSeat({
              seat: { ...seat, status: 'SELECTED' as SeatStatus },
            }),
      );
    });
  }

  getSeatClass(seat: Seat): string {
    const base = 'w-10 h-10 rounded-sm cursor-pointer transition-all';
    switch (seat.status) {
      case 'BOOKED':
        return `${base} bg-red-500 cursor-not-allowed`;
      case 'HOLD':
        return `${base} bg-orange-500 cursor-not-allowed`;
      case 'SELECTED':
        return `${base} bg-blue-500 text-white`;
      default:
        return `${base} bg-gray-200 hover:bg-green-300`;
    }
  }

  groupedSeats$ = this.seats$.pipe(
    map((seats) => {
      const groups = seats.reduce(
        (acc, seat) => {
          const row = seat.seatNumber.charAt(0);
          acc[row] = acc[row] || [];
          acc[row].push(seat);
          return acc;
        },
        {} as Record<string, Seat[]>,
      );

      return Object.keys(groups)
        .sort()
        .map((row) =>
          groups[row].sort(
            (a, b) =>
              parseInt(a.seatNumber.slice(1)) - parseInt(b.seatNumber.slice(1)),
          ),
        );
    }),
  );

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
