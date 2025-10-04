import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CountdownComponent} from '@/app/modules/home/components/reservation/countdown/countdown.component';
import {CinemaSeats} from '@/app/core/models/cinemaHall.model';
import {Store} from '@ngrx/store';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';
import {selectedAllSeats, selectedSeats, selectedTotalPrice} from '@/app/core/store/state/schedule/schedule.selectors';
import {map} from 'rxjs';

@Component({
  selector: ' app-seat',
  imports: [CommonModule, CountdownComponent],
  templateUrl: './seat.component.html',
  styleUrl: './seat.component.css'
})
export class SeatComponent implements OnInit {

  store = inject(Store);
  // groupedSeats: CinemaSeats[][] = []
  seats$ = this.store.select(selectedAllSeats)
  totalPrice$ = this.store.select(selectedTotalPrice);
  selectedSeats$ = this.store.select(selectedSeats);

  ngOnInit(): void {
  }


  groupedSeats$ = this.seats$.pipe(
    map(seats => {
      const groups: { [row: string]: CinemaSeats[] } = {};
      seats.forEach(seat => {
        const row = seat.seatNumber.charAt(0);
        if (!groups[row]) groups[row] = [];
        groups[row].push(seat);
      })
      Object.keys(groups).forEach(row => {
        groups[row].sort((a, b) =>
          parseInt(a.seatNumber.slice(1)) -
          parseInt(b.seatNumber.slice(1)));
      });
      return Object.values(groups);
    }))


  toggleSeat(seat: CinemaSeats) {
    if (seat.status === "BOOKED") return;
    this.store.dispatch(ReservationActions.toggleSeats({seat}));
  }

  getSeatClass(seat: CinemaSeats): string {
    const base = "w-10 h-10 rounded-sm cursor-pointer transition-all";
    switch (seat.status) {
      case "BOOKED":
        return `${base} bg-red-500 cursor-not-allowed`;
      case "SELECTED":
        return `${base} bg-blue-500 text-white`;
      default:
        return `${base} bg-gray-200 hover:bg-green-300`;
    }
  }
}
