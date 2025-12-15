import { Component, inject } from '@angular/core';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { FormsModule } from '@angular/forms';
import { NzImageViewComponent } from 'ng-zorro-antd/experimental/image';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { CountdownComponent } from '@features/client/reservation/components/countdown/countdown.component';
import { selectTotalPrice } from '@domain/showtime/data-access/showtime.selectors';

@Component({
  selector: 'app-payment',
  imports: [
    NzRadioModule,
    FormsModule,
    CountdownComponent,
    NzImageViewComponent,
    CommonModule,
  ],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css',
})
export class PaymentComponent {
  private store = inject(Store);
  radioValue = 'A';
  totalPrice$ = this.store.select(selectTotalPrice);
}
