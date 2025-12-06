import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { NzModalService } from 'ng-zorro-antd/modal';
import { Store } from '@ngrx/store';
import { AsyncPipe, DatePipe } from '@angular/common';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import {
  NzTableModule,
  NzTheadComponent,
  NzThMeasureDirective,
} from 'ng-zorro-antd/table';
import { NzWaveModule } from 'ng-zorro-antd/core/wave';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzAlertModule } from 'ng-zorro-antd/alert';
import { debounceTime, filter, Subject, take, takeUntil } from 'rxjs';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import dayjs from 'dayjs';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzTooltipModule } from 'ng-zorro-antd/tooltip';
import { NzProgressModule } from 'ng-zorro-antd/progress';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import {
  selectAllShowtimes,
  selectShowtimeError,
  selectShowtimeLoading,
} from '@domain/showtime/data-access/showtime.selectors';
import { headerColumns } from '@core/constants/column';
import { Showtime } from '@domain/showtime/models/showtime.model';
import { Pagination } from '@core/models/common.model';
import { Theater } from '@domain/theater/models/theater.model';
import { TheaterActions } from '@domain/theater/data-access/theater.actions';
import { selectAllTheaters } from '@domain/theater/data-access/theater.selectors';
import { ShowtimeActions } from '@domain/showtime/data-access/showtime.actions';
import { formatDate } from '@shared/utils/date.helper';
import { FormShowtimeComponent } from '@features/admin/showtime/form/showtime-form.component';

@Component({
  selector: 'app-showtime',
  imports: [
    DatePipe,
    NzButtonModule,
    NzIconModule,
    NzSelectModule,
    NzSpaceModule,
    NzThMeasureDirective,
    NzTheadComponent,
    NzTableModule,
    NzWaveModule,
    AsyncPipe,
    NzSpinModule,
    NzAlertModule,
    ReactiveFormsModule,
    NzFormModule,
    FormsModule,
    NzDatePickerModule,
    NzTagModule,
    NzTooltipModule,
    NzProgressModule,
    NzDropDownModule,
  ],
  templateUrl: './showtime.component.html',
  styleUrl: './showtime.component.css',
  providers: [NzModalService],
})
export class ShowtimeComponent implements OnInit, OnDestroy {
  private modalService = inject(NzModalService);
  private store = inject(Store);
  private destroy$ = new Subject<void>();
  private fb = inject(FormBuilder);

  loading$ = this.store.select(selectShowtimeLoading);
  error$ = this.store.select(selectShowtimeError);

  headerColumn = headerColumns.showtime;
  form!: FormGroup;
  showtimes: readonly Showtime[] = [];
  pagination: Pagination | null = null;
  theaters: Theater[] = [];

  pageIndex = 1;
  pageSize = 10;

  ngOnInit() {
    this.form = this.fb.group({
      theaterId: [null],
      dateRange: [null],
      date: [dayjs().toDate()],
    });

    this.store.dispatch(TheaterActions.loadTheaters({ page: 0, size: 10 }));
    this.store
      .select(selectAllTheaters)
      .pipe(
        filter((t) => t.length > 0),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe((theaters) => {
        this.theaters = theaters;

        if (!this.form.value.theaterId) {
          this.form.patchValue({ theaterId: theaters[0].id });
        }

        this.loadData();
      });
    this.form.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.loadData());
  }

  loadData() {
    const { theaterId, date, dateRange } = this.form.value;
    if (!theaterId || !date) return;

    this.showtimes = [];
    this.store.dispatch(
      ShowtimeActions.loadShowtimesByTheaterId({
        theaterId: theaterId,
        dateRange: {
          dateFrom: dateRange ? formatDate(dateRange[0]) : undefined,
          dateTo: dateRange ? formatDate(dateRange[1]) : undefined,
        },
        page: this.pageIndex,
        size: this.pageSize,
      }),
    );

    this.store
      .select(selectAllShowtimes)
      .pipe(
        filter((showtimes) => !!showtimes && showtimes.length >= 0),
        debounceTime(100),
        takeUntil(this.destroy$),
      )
      .subscribe((showtimes) => {
        this.showtimes = showtimes;
      });
  }

  openModal(mode: 'add' | 'edit' | 'view', id?: number) {
    const modal = this.modalService.create({
      nzContent: FormShowtimeComponent,
      nzTitle: undefined,
      nzClosable: true,
      nzData: {
        mode: mode,
        id: id,
      },
      nzWidth: 900,
      nzKeyboard: true,
      nzFooter:
        mode !== 'view'
          ? [
              {
                label: 'Confirm',
                type: 'primary',
                onClick: () => {
                  const form =
                    modal.getContentComponent() as FormShowtimeComponent;
                  if (form.form.valid) {
                    form.submit();
                    modal.close();
                  }
                },
              },
            ]
          : null,
    });
  }

  onDelete(id: number) {
    this.store.dispatch(ShowtimeActions.deleteShowtime({ id }));
    this.store.select(selectAllShowtimes).subscribe((data) => {
      this.showtimes = data;
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
