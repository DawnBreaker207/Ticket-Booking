import {Component, input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CountdownComponent} from '@/app/modules/home/components/reservation/countdown/countdown.component';
import {CinemaSeats} from '@/app/core/models/cinemaHall.model';

@Component({
  selector: 'app-seat',
  imports: [CommonModule, CountdownComponent],
  templateUrl: './seat.component.html',
  styleUrl: './seat.component.css'
})
export class SeatComponent implements OnInit {
  seats = input<CinemaSeats[]>([]);
  groupedSeats: CinemaSeats[][] = []

  ngOnInit(): void {
    console.log(this.seats())
    this.groupSeats();
  }

  private groupSeats() {
    const groups: { [row: string]: CinemaSeats[] } = {};
    for (const seat of this.seats()) {
      const row = seat.seatNumber.charAt(0); // "A1" -> "A"
      if (!groups[row]) groups[row] = [];
      groups[row].push(seat);
    }

    // sắp xếp ghế theo số
    Object.keys(groups).forEach(row => {
      groups[row].sort((a, b) => {
        const numA = parseInt(a.seatNumber.slice(1), 10);
        const numB = parseInt(b.seatNumber.slice(1), 10);
        return numA - numB;
      });
    });

    this.groupedSeats = Object.values(groups);
  }

  toggleSeat(seat: CinemaSeats) {
    if (seat.status === "BOOKED") return;
    seat.status = seat.status === "SELECTED" ? "AVAILABLE" : "SELECTED";
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
