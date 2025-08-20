import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
export interface SeatModel {
  row: string;
  number: number;
  status: "available" | "booked" | "selected";
}
@Component({
  selector: "app-seat",
  imports: [CommonModule],
  templateUrl: "./seat.html",
  styleUrl: "./seat.css",
})
export class Seat {
  rows = ["A", "B", "C", "D", "E"];
  cols = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

  seats: SeatModel[][] = [];

  ngOnInit(): void {
    this.seats = this.rows.map((row) =>
      this.cols.map((number) => ({
        row,
        number,
        status: this.randomStatus(), // bạn có thể thay bằng từ API
      }))
    );
  }

  randomStatus(): SeatModel["status"] {
    const r = Math.random();
    if (r < 0.1) return "booked";
    return "available";
  }

  toggleSeat(seat: SeatModel) {
    if (seat.status === "booked") return;
    seat.status = seat.status === "selected" ? "available" : "selected";
  }

  getSeatClass(seat: SeatModel): string {
    const base = "w-6 h-6 rounded-sm cursor-pointer transition-all";
    switch (seat.status) {
      case "booked":
        return `${base} bg-red-500 cursor-not-allowed`;
      case "selected":
        return `${base} bg-blue-500 text-white`;
      default:
        return `${base} bg-gray-200 hover:bg-green-300`;
    }
  }
}
