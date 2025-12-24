import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { NzModalRef } from 'ng-zorro-antd/modal';
import dayjs from 'dayjs';
import { NgClass } from '@angular/common';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { Showtime } from '@domain/showtime/models/showtime.model';
import { formatDate } from '@shared/utils/date.helper';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'app-theater',
  imports: [NgClass, NzButtonComponent, NzIconDirective],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css',
})
export class ShowtimeModalComponent implements OnInit {
  private modalRef = inject(NzModalRef);

  showtimes = signal<Showtime[]>([]);
  selectedShowtimeId = signal<number | null>(null);

  groupShowtimes = computed(() => {
    const listShowtimes = this.showtimes();
    const groupsDate: { date: string; items: Showtime[] }[] = [];

    listShowtimes.forEach((st) => {
      const date = formatDate(st.showDate);
      let groupDay = groupsDate.find((g) => g.date === date);
      if (!groupDay) {
        groupDay = { date: date, items: [] };
        groupsDate.push(groupDay);
      }
      groupDay?.items.push(st);
    });

    return groupsDate.sort((a, b) => a.date.localeCompare(b.date));
  });

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
}
