import { Component, input } from '@angular/core';
import { Showtime } from '@/app/core/models/theater.model';

@Component({
  selector: 'app-theater',
  imports: [],
  templateUrl: './showtime.component.html',
  styleUrl: './showtime.component.css',
})
export class ShowtimeComponent {
  showtime = input<Showtime>();

  formatShowtime(date: Date) {
    return new Date(date).toLocaleDateString('vi-VN');
  }

  formatTime(date: Date) {
    return new Date(date).toLocaleDateString('vi-VN', {
      hour: '2-digit',
      minute: '2-digit',
    });
  }
}
