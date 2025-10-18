import {Component, inject} from '@angular/core';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {Store} from '@ngrx/store';
import {selectSelectedSeats} from '@/app/core/store/state/seat/seat.selectors';
import {selectPrice, selectTotalPrice,} from '@/app/core/store/state/showtime/showtime.selectors';
import {CommonModule} from '@angular/common';
import {selectJwt} from '@/app/core/store/state/auth/auth.selectors';

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
