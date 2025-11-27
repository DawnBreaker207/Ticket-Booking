import { Component, inject, OnInit, signal } from '@angular/core';
import { Showtime } from '@/app/core/models/theater.model';
import { NzModalRef } from 'ng-zorro-antd/modal';
import dayjs from 'dayjs';
import { NgClass } from '@angular/common';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { timeFormat } from '@/app/shared/utils/formatDate';

@Component({
  selector: 'app-theater',
  imports: [NgClass, NzButtonComponent],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css',
})
export class ShowtimeModalComponent implements OnInit {
  private modalRef = inject(NzModalRef);
  showtimes = signal<Showtime[]>([]);
  selectedShowtimeId = signal<number | null>(null);

  ngOnInit() {
    const { showtimes } = this.modalRef.getConfig().nzData;
    this.showtimes.set(showtimes);
  }

  formatShowtime(date: Date | string | null) {
    return dayjs(date).format('DD/MM/YYYY');
  }

  onSelect(showtimeId: number) {
    this.selectedShowtimeId.set(showtimeId);
  }

  confirmSelection() {
    if (this.selectedShowtimeId()) {
      this.modalRef.close(this.selectedShowtimeId());
    }
  }

  protected readonly timeFormat = timeFormat;
}
