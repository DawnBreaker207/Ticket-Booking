import { Component, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { CurrencyPipe } from '@angular/common';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { FormsModule } from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { selectJwt } from '@core/auth/auth.selectors';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-confirm',
  imports: [
    NzIconModule,
    FormsModule,
    NzRadioModule,
    NzButtonModule,
    CurrencyPipe,
    NzInputModule,
    TranslatePipe,
  ],
  templateUrl: './confirm.component.html',
  styleUrl: './confirm.component.css',
})
export class ConfirmComponent {
  private store = inject(Store);
  user = this.store.selectSignal(selectJwt);
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
