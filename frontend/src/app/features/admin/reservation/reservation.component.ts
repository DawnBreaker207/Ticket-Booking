import { Component, inject, OnInit } from '@angular/core';
import { NzTableModule, NzTableQueryParams } from 'ng-zorro-antd/table';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { Store } from '@ngrx/store';
import { NzAlertComponent } from 'ng-zorro-antd/alert';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { StatusTagsPipe } from '@shared/pipes/status-tags.pipe';
import {
  selectPaginationReservation,
  selectReservationError,
  selectReservationLoading,
  selectReservations,
} from '@domain/reservation/data-access/reservation.selectors';
import { ReservationStatus } from '@core/constants/enum';
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
    NzFormLabelComponent,
    NzTooltipDirective,
    LoadingComponent,
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

  reservations = this.store.selectSignal(selectReservations);
  pagination = this.store.selectSignal(selectPaginationReservation);
  loading = this.store.selectSignal(selectReservationLoading);
  error = this.store.selectSignal(selectReservationError);

  headerColumn = headerColumns.reservation;
  reservationStatus: ReservationStatus[] = ['CONFIRMED', 'CANCELED'];

  ngOnInit() {
    this.initialForm();
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

  private getFilter(pageIndex: number, pageSize: number) {
    const { query, userId, reservationStatus, dateRange, totalAmount, sortBy } =
      this.form.value;
    console.log(dateRange);
    return {
      query: query.trim() || undefined,
      userId: userId || undefined,
      reservationStatus: reservationStatus || undefined,
      startDate: dateRange?.[0] ? formatDate(dateRange[0]) : undefined,
      endDate: dateRange?.[1] ? formatDate(dateRange[1]) : undefined,
      totalAmount: totalAmount || undefined,
      sortBy: sortBy,
      page: pageIndex - 1,
      size: pageSize,
    };
  }

  onSubmit() {
    const currentSize = this.pagination()?.pageSize ?? 10;
    this.loadData(1, currentSize);
  }

  private loadData(page: number, size: number) {
    const filter = this.getFilter(page, size);
    this.store.dispatch(
      ReservationActions.loadReservations({ filter: filter }),
    );
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
    this.form.reset({ sortBy: 'newest' });
    this.onSubmit();
  }

  onQueryParamsChange(params: NzTableQueryParams) {
    const { pageIndex, pageSize } = params;
    this.loadData(pageIndex, pageSize);
  }
}
