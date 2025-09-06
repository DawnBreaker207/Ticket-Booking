import {Component, computed, inject, input, OnInit, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CountdownComponent} from '@/app/modules/home/components/reservation/countdown/countdown.component';
import {CinemaSeats} from '@/app/core/models/cinemaHall.model';
import {Store} from '@ngrx/store';
import {ReservationActions} from '@/app/core/store/state/actions/reservation.actions';
import {selectedSeats, selectedTotalPrice} from '@/app/core/store/state/selectors/reservation.selectors';
import {SeatStatus} from '@/app/core/constants/enum';

@Component({
  selector: 'app-seat',
  imports: [CommonModule, CountdownComponent],
  templateUrl: './seat.component.html',
  styleUrl: './seat.component.css'
})
export class SeatComponent implements OnInit {
  seats = input<CinemaSeats[]>([]);
  localSeats = signal<CinemaSeats[]>([]);
  store = inject(Store);
  // groupedSeats: CinemaSeats[][] = []
  totalPrice$ = this.store.select(selectedTotalPrice);
  selectedSeats$ = this.store.select(selectedSeats);

  ngOnInit(): void {
    this.localSeats.set(this.seats());
  }


  groupedSeats = computed(() => {
    const groups: { [row: string]: CinemaSeats[] } = {};
    for (const seat of this.localSeats()) {
      const row = seat.seatNumber.charAt(0);
      if (!groups[row]) groups[row] = [];
      groups[row].push(seat);
    }

    Object.keys(groups).forEach(row => {
      groups[row].sort((a, b) => parseInt(a.seatNumber.slice(1)) - parseInt(b.seatNumber.slice(1)));
    });

    return Object.values(groups);
  });

  toggleSeat(seat: CinemaSeats) {
    if (seat.status === "BOOKED") return;
    const updatedSeat: CinemaSeats = {
      ...seat,
      status: seat.status === 'SELECTED' ? 'AVAILABLE' : 'SELECTED'
    };

    // update local signal để UI phản hồi ngay
    this.localSeats.set(
      this.localSeats().map(s => s.id === seat.id ? updatedSeat : s)
    );
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

  get totalPrice() {
    return this.seats().filter(s => s.status === "SELECTED").reduce((sum, s) => sum + s.price, 0);
  }

  get selectedSeats() {
    return this.seats().filter(s => s.status === "SELECTED");
  }
}
