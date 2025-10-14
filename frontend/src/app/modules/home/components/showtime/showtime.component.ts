import { Component, inject, OnInit, signal } from '@angular/core';
import { Showtime } from '@/app/core/models/theater.model';
import { NzModalRef } from 'ng-zorro-antd/modal';
import dayjs from 'dayjs';

@Component({
  selector: 'app-theater',
  imports: [],
  templateUrl: './showtime.component.html',
  styleUrl: './showtime.component.css',
})
export class ShowtimeComponent implements OnInit {
  private modalRef = inject(NzModalRef);
  showtime = signal<Showtime[]>([]);

  ngOnInit() {
    const { showtimes } = this.modalRef.getConfig().nzData;
    this.showtime.set(showtimes);
  }

  formatShowtime(date: Date | string | null) {
    return dayjs(date).format('DD/MM/YYYY');
  }

  formatTime(time: Date | string | null) {
    if (!time) return '-';
    const today = dayjs().format('YYYY-MM-DD');
    const dateTime = dayjs(`${today} ${time}`, 'YYYY-MM-DD HH:mm:ss');
    if (!dateTime.isValid()) return '-';
    return dateTime.format('HH:mm');
  }
}
