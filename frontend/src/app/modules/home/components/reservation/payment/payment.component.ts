import {Component, inject} from '@angular/core';
import {NzRadioModule} from 'ng-zorro-antd/radio';
import {FormsModule} from '@angular/forms';
import {CountdownComponent} from '@/app/modules/home/components/reservation/countdown/countdown.component';
import {NzImageViewComponent} from 'ng-zorro-antd/experimental/image';
import {Store} from '@ngrx/store';
import {AsyncPipe, CurrencyPipe} from '@angular/common';
import {selectedTotalPrice} from '@/app/core/store/state/schedule/schedule.selectors';

@Component({
  selector: 'app-payment',
  imports: [NzRadioModule, FormsModule, CountdownComponent, NzImageViewComponent, AsyncPipe, CurrencyPipe],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent {
  radioValue = "A";
  store = inject(Store);
  totalPrice$ = this.store.select(selectedTotalPrice);
}
