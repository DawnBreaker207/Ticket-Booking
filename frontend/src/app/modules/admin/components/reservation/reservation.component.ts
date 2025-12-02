import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { headerColumns } from '@/app/core/constants/column';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { AsyncPipe, CommonModule } from '@angular/common';
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
import { Reservation } from '@/app/core/models/reservation.model';
import { Store } from '@ngrx/store';
import {
  selectPaginationReservation,
  selectReservations,
} from '@/app/core/store/state/reservation/reservation.selectors';
import { ReservationActions } from '@/app/core/store/state/reservation/reservation.actions';
import { FormReservationComponent } from '@/app/modules/admin/components/reservation/form/form.component';
import { NzSpinComponent } from 'ng-zorro-antd/spin';
import {
  selectMovieLoading,
  selectMoviesError,
} from '@/app/core/store/state/movie/movie.selectors';
import { NzAlertComponent } from 'ng-zorro-antd/alert';
import { Pagination } from '@/app/core/models/common.model';
import { Subject } from 'rxjs';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

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
    AsyncPipe,
    NzFormLabelComponent,
    NzTooltipDirective,
  ],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.css',
  providers: [NzModalService],
})
export class ReservationComponent implements OnInit, OnDestroy {
  form!: FormGroup;

  private fb = inject(FormBuilder);
  private store = inject(Store);
  private modalService = inject(NzModalService);
  private reportService = inject(ReportService);

  reservation$ = this.store.select(selectReservations);
  pagination$ = this.store.select(selectPaginationReservation);
  loading$ = this.store.select(selectMovieLoading);
  error$ = this.store.select(selectMoviesError);

  private destroy$ = new Subject<void>();
  reservationStatus: ReservationStatus[] = ['CREATED', 'CONFIRMED', 'CANCELED'];
  paymentMethod: PaymentMethod[] = ['MOMO', 'VNPAY', 'ZALOPAY'];
  paymentStatus: PaymentStatus[] = ['PENDING', 'PAID', 'CANCELED'];

  pageIndex = 1;
  pageSize = 10;

  reservationList: readonly Reservation[] = [];
  pagination: Pagination | null = null;

  headerColumn = headerColumns.reservation;

  ngOnInit() {
    this.initialForm();
    this.loadData();
  }

  loadData() {
    this.store.dispatch(
      ReservationActions.loadReservations({ filter: this.getFilter() }),
    );
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

  private getFilter() {
    const formValue = this.form.value;
    return {
      query: formValue.query || undefined,
      userId: formValue.userId || undefined,
      reservationStatus: formValue.reservationStatus || undefined,
      dateFrom: formValue.dateRange
        ? formatDate(formValue.dateRange[0])
        : undefined,
      dateTo: formValue.dateRange
        ? formatDate(formValue.dateRange[1])
        : undefined,
      totalAmount: formValue.totalAmount || undefined,
      sortBy: formValue.sortBy,
      page: this.pageIndex - 1,
      size: this.pageSize,
    };
  }

  onPageChange(page: number) {
    this.pageIndex = page;
    this.loadData();
  }

  onSubmit() {
    this.pageIndex = 1;
    this.loadData();
  }

  onSizeChange(size: number) {
    this.pageSize = size;
    this.pageIndex = 1;
    this.loadData();
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

  clearFilter() {
    this.form.reset();
    this.pageIndex = 1;
    this.pageSize = 10;
    this.loadData();
  }

  exportReport(type: string) {
    this.reportService.downloadReport(type).subscribe((res) => {
      const ext = type === 'excel' ? 'xlsx' : type;
      saveAs(res, `report.${ext}`);
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
