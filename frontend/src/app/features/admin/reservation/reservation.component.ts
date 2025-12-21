import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { AsyncPipe, CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzModalService } from 'ng-zorro-antd/modal';
import { saveAs } from 'file-saver';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { Store } from '@ngrx/store';
import { NzAlertComponent } from 'ng-zorro-antd/alert';
import { Subject } from 'rxjs';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { StatusTagsPipe } from '@shared/pipes/status-tags.pipe';
import { ReportService } from '@core/services/report/report.service';
import {
  selectPaginationReservation,
  selectReservations,
} from '@domain/reservation/data-access/reservation.selectors';
import {
  selectMovieLoading,
  selectMoviesError,
} from '@domain/movie/data-access/movie.selectors';
import { ReservationStatus } from '@core/constants/enum';
import { Reservation } from '@domain/reservation/models/reservation.model';
import { Pagination } from '@core/models/common.model';
import { headerColumns } from '@core/constants/column';
import { ReservationActions } from '@domain/reservation/data-access/reservation.actions';
import { formatDate } from '@shared/utils/date.helper';
import { FormReservationComponent } from '@features/admin/reservation/form/reservation-form.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';

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
    NzAlertComponent,
    AsyncPipe,
    NzFormLabelComponent,
    NzTooltipDirective,
    LoadingComponent,
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
