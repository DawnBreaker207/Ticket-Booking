import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Store} from '@ngrx/store';
import {Seat} from '@/app/core/models/theater.model';
import {selectSeats, selectSelectedSeats,} from '@/app/core/store/state/seat/seat.selectors';
import {map, take} from 'rxjs';
import {SeatActions} from '@/app/core/store/state/seat/seat.actions';
import {selectPrice, selectTotalPrice,} from '@/app/core/store/state/showtime/showtime.selectors';
import {CountdownComponent} from '@/app/modules/home/components/reservation/countdown/countdown.component';

@Component({
  selector: ' app-seat',
  imports: [CommonModule, CountdownComponent],
  templateUrl: './seat.component.html',
  styleUrl: './seat.component.css',
})
export class SeatComponent {
  private store = inject(Store);
  seats$ = this.store.select(selectSeats);
  selectSeats$ = this.store.select(selectSelectedSeats);
  totalPrice$ = this.store.select(selectTotalPrice);
  price$ = this.store.select(selectPrice);

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

  toggleSeat(seat: Seat) {
    if (seat.status === 'BOOKED') return;
    this.selectSeats$.pipe(take(1)).subscribe((selected) => {
      const isSelected = selected.some((s) => s.id === seat.id);
      this.store.dispatch(
        isSelected
          ? SeatActions.deselectSeat({seatId: seat.id})
          : SeatActions.selectSeat({seat}),
      );
    });
  }

  getSeatClass(seat: Seat): string {
    const base = 'w-10 h-10 rounded-sm cursor-pointer transition-all';
    switch (seat.status) {
      case 'BOOKED':
        return `${base} bg-red-500 cursor-not-allowed`;
      case 'SELECTED':
        return `${base} bg-blue-500 text-white`;
      default:
        return `${base} bg-gray-200 hover:bg-green-300`;
    }
  }
}
