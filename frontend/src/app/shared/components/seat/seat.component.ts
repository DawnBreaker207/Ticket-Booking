import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'seat-icon',
  imports: [CommonModule],
  templateUrl: './seat.component.html',
  styleUrl: './seat.component.css',
})
export class SeatIconComponent {
  size = input<number | string>(24);

  className = input<string>('');
}
