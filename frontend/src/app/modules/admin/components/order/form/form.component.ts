import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NzModalRef} from 'ng-zorro-antd/modal';
import {Subject} from 'rxjs';
import {Order} from '@/app/core/models/order.model';
import {OrderService} from '@/app/core/services/order/order.service';
import {CurrencyPipe, DatePipe, JsonPipe} from '@angular/common';
import {NzTagComponent} from 'ng-zorro-antd/tag';
import {StatusTagsPipe} from '@/app/core/pipes/status-tags.pipe';

@Component({
  selector: 'app-form',
  imports: [
    DatePipe,
    CurrencyPipe,
    NzTagComponent,
    StatusTagsPipe
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css',
})
export class FormOrderComponent implements OnInit, OnDestroy {
  private modelRef = inject(NzModalRef);
  private reservationService = inject(OrderService);
  destroy$: Subject<void> = new Subject();
  order!: Order;

  ngOnInit() {
    const {orderId} = this.modelRef.getConfig().nzData;
    this.reservationService.getOrder(orderId).subscribe(order => {
      this.order = order;
    })
  }
  getSeatNumbers(order: Order): string {
    return order.seats.map(s => s.seat.seatNumber).join(', ');
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
