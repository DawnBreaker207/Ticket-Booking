import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NzResultModule} from 'ng-zorro-antd/result'
import {NzButtonModule} from 'ng-zorro-antd/button';
import {ActivatedRoute, Router} from '@angular/router';
import {NzSpinModule} from 'ng-zorro-antd/spin';
import {combineLatest, take} from 'rxjs';
import {Store} from '@ngrx/store';
import {selectedOrder} from '@/app/core/store/state/reservation/reservation.selectors';
import {selectUser} from '@/app/core/store/state/auth/auth.selectors';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';

@Component({
  selector: 'app-payment-result',
  imports: [NzResultModule, NzButtonModule, NzSpinModule],
  templateUrl: './payment-result.component.html',
  styleUrl: './payment-result.component.css'
})
export class PaymentResultComponent implements OnInit, OnDestroy {
  private store = inject(Store);
  route = inject(ActivatedRoute);
  router = inject(Router);
  vnpTxnRef: any = 4;
  vnpResponseCode: any;
  vnpAmount: any;
  vnpOrderInfo: any
  status: 'success' | 'error' | 'info' = 'info';
  errorMessage: string = ''
  countdown = 10;
  private intervalId: any;


  constructor() {
    this.route.queryParams.subscribe(params => {
      this.vnpTxnRef = params['vnp_TxnRef'];
      this.vnpResponseCode = params['vnp_ResponseCode'];
      this.vnpAmount = params['vnp_Amount'];
      this.vnpOrderInfo = params['vnp_OrderInfo'];

    })
  }

  ngOnInit() {
    if (this.vnpTxnRef && this.vnpResponseCode === '00') {
      console.log(`This is success`)
      this.status = 'success';
      this.saveOrder();
      this.startCountDown();
    } else {
      this.status = 'error';
      this.errorMessage = `Thanh toán thất bại. Mã lỗi: ${this.vnpResponseCode}`;
      this.startCountDown();
    }
  }


  saveOrder() {
    combineLatest([
      this.store.select(selectedOrder),
      this.store.select(selectUser),
    ]).pipe(take(1))
      .subscribe(([order, user]) => {
        console.log(order, user, this.vnpTxnRef, this.vnpAmount)
        this.store.dispatch(ReservationActions.confirmOrder({
          order: {
            ...order,
            orderId: this.vnpTxnRef,
            userId: user?.userId,
            totalAmount: this.vnpAmount
          }
        }))
      })
  }


  goHome() {
    this.router.navigate(['/home']);
  }

  startCountDown() {
    this.intervalId = setInterval(() => {
      this.countdown--;
      if (this.countdown === 0) {
        clearInterval(this.intervalId);
        this.goHome();
      }
    }, 1000)
  }

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }
}
