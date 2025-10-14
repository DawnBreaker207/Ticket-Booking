import { Component, input } from '@angular/core';
import { Theater } from '@/app/core/models/theater.model';

@Component({
  selector: 'app-theater',
  imports: [],
  templateUrl: './theater.component.html',
  styleUrl: './theater.component.css',
})
export class TheaterComponent {
  theaters = input<Theater[]>([]);
  selectedTheater = input<number | null>(null);
}
