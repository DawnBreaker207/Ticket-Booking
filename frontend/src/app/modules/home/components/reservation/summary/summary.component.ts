import {Component, inject, input} from '@angular/core';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {Store} from '@ngrx/store';
import {selectedSeats, selectedTotalPrice} from '@/app/core/store/state/reservation/reservation.selectors';
import {AsyncPipe, CurrencyPipe} from '@angular/common';

@Component({
  selector: 'app-summary',
  imports: [
    NzIconModule,
    AsyncPipe,
    CurrencyPipe
  ],
  templateUrl: './summary.component.html',
  styleUrl: './summary.component.css'
})
export class SummaryComponent {
  user = input<any>();
  store = inject(Store);
  selectedSeats$ = this.store.select(selectedSeats);
  totalPrice$ = this.store.select(selectedTotalPrice);

}
