import { Component, inject, OnInit, signal } from '@angular/core';
import { NzModalRef } from 'ng-zorro-antd/modal';
import { Theater } from '@domain/theater/models/theater.model';

@Component({
  selector: 'app-theater',
  imports: [],
  templateUrl: './theater.component.html',
  styleUrl: './theater.component.css',
})
export class TheaterModalComponent implements OnInit {
  private modelRef = inject(NzModalRef);
  theaters = signal<Theater[]>([]);
  selectedTheaterId = signal<number | null>(null);

  ngOnInit() {
    const { theaters, selectedTheaterId } = this.modelRef.getConfig().nzData;
    this.theaters.set(theaters);
    this.selectedTheaterId.set(selectedTheaterId);
  }

  onSelect(event: Event) {
    const target = event.target as HTMLSelectElement;
    const theaterId = Number(target.value);
    this.selectedTheaterId.set(theaterId);
    this.modelRef.close(theaterId);
  }
}
