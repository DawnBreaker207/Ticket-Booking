import {Component, inject, input} from '@angular/core';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {Store} from '@ngrx/store';
import {AsyncPipe, CurrencyPipe} from '@angular/common';
import {selectedSeats, selectedTotalPrice} from '@/app/core/store/state/schedule/schedule.selectors';

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
