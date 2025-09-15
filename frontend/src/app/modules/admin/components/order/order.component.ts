import {Component, inject, OnInit} from '@angular/core';
import {NzTableModule} from 'ng-zorro-antd/table';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {OrderService} from '@/app/core/services/order/order.service';
import {headerColumns} from '@/app/core/constants/column';
import {Order} from '@/app/core/models/order.model';
import {NzSpaceModule} from 'ng-zorro-antd/space';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {CommonModule} from '@angular/common';
import {OrderStatus, PaymentMethod, PaymentStatus} from '@/app/core/constants/enum';
import {NzDatePickerModule} from 'ng-zorro-antd/date-picker';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {NzTagModule} from 'ng-zorro-antd/tag';
import {StatusTagsPipe} from '@/app/core/pipes/status-tags.pipe';
import {formatTime} from '@/app/shared/utils/formatDate';

@Component({
  selector: 'app-order',
  imports: [NzTableModule, NzButtonModule, NzInputModule, NzSelectModule, NzSpaceModule, NzIconModule, CommonModule, NzDatePickerModule, ReactiveFormsModule, NzTagModule, StatusTagsPipe],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent implements OnInit {
  form!: FormGroup;
  private fb = inject(FormBuilder);
  private reservationService = inject(OrderService);
  headerColumn = headerColumns.order;
  reservationList: readonly Order[] = []
  orderStatus: OrderStatus[] = ['CREATED', 'CONFIRMED', 'CANCELLED']
  paymentMethod: PaymentMethod[] = ['CASH', 'MOMO', 'VNPAY', 'ZALOPAY']
  paymentStatus: PaymentStatus[] = ['PENDING', 'PAID', 'CANCELLED']

  ngOnInit() {
    this.loadData()
    this.initialForm();

  }

  loadData() {
    this.reservationService.getOrders().subscribe(data => this.reservationList = data);
  }

  initialForm() {
    this.form = this.fb.group({
      query: [''],
      orderStatus: [null],
      paymentMethod: [null],
      paymentStatus: [null],
      dateRange: [null],
      totalAmount: [0],
      sortBy: ['newest']
    });
  }

  clearFilter() {
    this.form.reset();
    this.loadData();
  }

  onSubmit() {
    // if (!this.form.invalid) return;
    const formValue = this.form.value;
    const filter = {
      query: formValue.query,
      orderStatus: formValue.orderStatus,
      paymentMethod: formValue.paymentMethod,
      paymentStatus: formValue.paymentStatus,
      startDate: formValue.dateRange ? formatTime(formValue.dateRange[0]) : null,
      endDate: formValue.dateRange ? formatTime(formValue.dateRange[1]) : null,
      totalAmount: formValue.totalAmount,
      sortBy: formValue.sortBy,
    }

    this.reservationService.getOrders(filter).subscribe(data => {
      this.reservationList = data;
    })
  }
}
