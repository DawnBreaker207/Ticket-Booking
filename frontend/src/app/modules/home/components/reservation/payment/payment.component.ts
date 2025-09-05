import {Component} from '@angular/core';
import {NzRadioModule} from 'ng-zorro-antd/radio';
import {FormsModule} from '@angular/forms';
import {CountdownComponent} from '@/app/modules/home/components/reservation/countdown/countdown.component';
import {NzImageViewComponent} from 'ng-zorro-antd/experimental/image';

@Component({
  selector: 'app-payment',
  imports: [NzRadioModule, FormsModule, CountdownComponent, NzImageViewComponent],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent {
  radioValue = "A";
}
