import { Component, inject } from '@angular/core';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { FormsModule } from '@angular/forms';
import { CountdownComponent } from '@/app/modules/home/components/reservation/countdown/countdown.component';
import { NzImageViewComponent } from 'ng-zorro-antd/experimental/image';
import { Store } from '@ngrx/store';
import { selectTotalPrice } from '@/app/core/store/state/showtime/showtime.selectors';
import { CommonModule } from '@angular/common';

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
