import { Component, inject } from '@angular/core';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { selectJwt } from '@core/auth/auth.selectors';
import { selectSelectedSeats } from '@domain/seat/data-access/seat.selectors';
import {
  selectPrice,
  selectTotalPrice,
} from '@domain/showtime/data-access/showtime.selectors';

@Component({
  selector: 'app-summary',
  imports: [NzIconModule, CommonModule],
  templateUrl: './summary.component.html',
  styleUrl: './summary.component.css',
})
export class SummaryComponent {
  private store = inject(Store);
  user$ = this.store.select(selectJwt);
  selectedSeats$ = this.store.select(selectSelectedSeats);
  totalPrice$ = this.store.select(selectTotalPrice);
  price$ = this.store.select(selectPrice);
}
