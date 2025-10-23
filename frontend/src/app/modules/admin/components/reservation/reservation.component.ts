import { Component, inject, OnInit } from '@angular/core';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { headerColumns } from '@/app/core/constants/column';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { CommonModule } from '@angular/common';
import {
  PaymentMethod,
  PaymentStatus,
  ReservationStatus,
} from '@/app/core/constants/enum';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { StatusTagsPipe } from '@/app/core/pipes/status-tags.pipe';
import { formatDate } from '@/app/shared/utils/formatDate';
import { NzModalService } from 'ng-zorro-antd/modal';
import { ReportService } from '@/app/core/services/report/report.service';
import { saveAs } from 'file-saver';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import {
  Reservation,
  ReservationFilter,
} from '@/app/core/models/reservation.model';
import { Store } from '@ngrx/store';
import { selectReservations } from '@/app/core/store/state/reservation/reservation.selectors';
import { ReservationActions } from '@/app/core/store/state/reservation/reservation.actions';
import { FormReservationComponent } from '@/app/modules/admin/components/reservation/form/form.component';
import { NzSpinComponent } from 'ng-zorro-antd/spin';
import {
  selectMovieLoading,
  selectMoviesError,
} from '@/app/core/store/state/movie/movie.selectors';
import { NzAlertComponent } from 'ng-zorro-antd/alert';

@Component({
  selector: 'app-reservation',
  imports: [
    NzTableModule,
    NzButtonModule,
    NzInputModule,
    NzSelectModule,
    NzSpaceModule,
    NzIconModule,
    CommonModule,
    NzDatePickerModule,
    ReactiveFormsModule,
    NzTagModule,
    StatusTagsPipe,
    NzDropDownModule,
    NzSpinComponent,
    NzAlertComponent,
  ],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.css',
  providers: [NzModalService],
})
export class ReservationComponent implements OnInit {
  form!: FormGroup;
  private fb = inject(FormBuilder);
  private store = inject(Store);
  private modalService = inject(NzModalService);
  private reportService = inject(ReportService);
  headerColumn = headerColumns.reservation;
  reservationList: readonly Reservation[] = [];
  reservationStatus: ReservationStatus[] = ['CREATED', 'CONFIRMED', 'CANCELED'];
  paymentMethod: PaymentMethod[] = ['MOMO', 'VNPAY', 'ZALOPAY'];
  paymentStatus: PaymentStatus[] = ['PENDING', 'PAID', 'CANCELED'];
  reservation$ = this.store.select(selectReservations);
  loading$ = this.store.select(selectMovieLoading);
  error$ = this.store.select(selectMoviesError);

  ngOnInit() {
    this.initialForm();
    this.loadData();
  }

  loadData() {
    this.store.dispatch(
      ReservationActions.loadReservations({ filter: this.form.value }),
    );
    this.reservation$.subscribe((data) => {
      this.reservationList = data;
    });
  }

  initialForm() {
    this.form = this.fb.group({
      query: [''],
      userId: [''],
      reservationStatus: [null],
      dateRange: [null],
      totalAmount: [null],
      sortBy: ['newest'],
    });
  }

  openModal(reservationId: string) {
    this.modalService.create({
      nzContent: FormReservationComponent,
      nzTitle: undefined,
      nzClosable: true,
      nzData: {
        reservationId: reservationId,
      },
      nzWidth: 900,
      nzKeyboard: true,
      nzFooter: null,
    });
  }

  exportReport(type: string) {
    this.reportService.downloadReport(type).subscribe((res) => {
      const ext = type === 'excel' ? 'xlsx' : type;
      saveAs(res, `report.${ext}`);
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
      userId: formValue.userId,
      reservationStatus: formValue.reservationStatus,
      dateFrom: formValue.dateRange ? formatDate(formValue.dateRange[0]) : null,
      dateTo: formValue.dateRange ? formatDate(formValue.dateRange[1]) : null,
      totalAmount: formValue.totalAmount,
      sortBy: formValue.sortBy,
    };

    this.store.dispatch(
      ReservationActions.loadReservations({
        filter: filter as ReservationFilter,
      }),
    );
    this.store.select(selectReservations).subscribe((data) => {
      this.reservationList = data;
    });
  }
}
