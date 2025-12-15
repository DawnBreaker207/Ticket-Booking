import { Component, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { AsyncPipe, CurrencyPipe } from '@angular/common';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { FormsModule } from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { selectJwt } from '@core/auth/auth.selectors';
import { selectSelectedSeats } from '@domain/seat/data-access/seat.selectors';
import {
  selectPrice,
  selectTotalPrice,
} from '@domain/showtime/data-access/showtime.selectors';

@Component({
  selector: 'app-confirm',
  imports: [
    NzIconModule,
    AsyncPipe,
    FormsModule,
    NzRadioModule,
    NzButtonModule,
    CurrencyPipe,
    NzInputModule,
  ],
  templateUrl: './confirm.component.html',
  styleUrl: './confirm.component.css',
})
export class ConfirmComponent {
  private store = inject(Store);
  user$ = this.store.select(selectJwt);
  selectedSeats$ = this.store.select(selectSelectedSeats);
  totalPrice$ = this.store.select(selectTotalPrice);
  price$ = this.store.select(selectPrice);
  radioValue = 'A';

  voucherCode: string = '';
  isValidatingVoucher: boolean = false;
  appliedVoucher: any = null;

  applyVoucher() {
    if (!this.voucherCode.trim()) return;

    this.isValidatingVoucher = true;

    setTimeout(() => {
      this.isValidatingVoucher = false;

      if (this.voucherCode.toUpperCase() === 'WELCOME10') {
        this.appliedVoucher = {
          code: 'WELCOME10',
          discountAmount: 50000,
        };
      }
    }, 1000);
  }

  removeVoucher() {
    this.appliedVoucher = null;
    this.voucherCode = '';
  }
}
