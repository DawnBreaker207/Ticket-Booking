import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { NzModalService } from 'ng-zorro-antd/modal';
import { Store } from '@ngrx/store';
import { headerColumns } from '@/app/core/constants/column';
import { Showtime, Theater } from '@/app/core/models/theater.model';
import { AsyncPipe, DatePipe } from '@angular/common';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzOptionComponent, NzSelectComponent } from 'ng-zorro-antd/select';
import { NzSpaceComponent } from 'ng-zorro-antd/space';
import {
  NzTableCellDirective,
  NzTableComponent,
  NzTbodyComponent,
  NzTheadComponent,
  NzThMeasureDirective,
  NzTrDirective,
} from 'ng-zorro-antd/table';
import { NzWaveDirective } from 'ng-zorro-antd/core/wave';
import { selectAllShowtimes } from '@/app/core/store/state/showtime/showtime.selectors';
import { FormShowtimeComponent } from '@/app/modules/admin/components/showtime/form/form.component';
import { ShowtimeActions } from '@/app/core/store/state/showtime/showtime.actions';
import {
  selectMovieLoading,
  selectMoviesError,
} from '@/app/core/store/state/movie/movie.selectors';
import { NzSpinComponent } from 'ng-zorro-antd/spin';
import { NzAlertComponent } from 'ng-zorro-antd/alert';
import { debounceTime, filter, Subject, take, takeUntil } from 'rxjs';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import dayjs from 'dayjs';
import { TheaterActions } from '@/app/core/store/state/theater/theater.actions';
import { selectAllTheaters } from '@/app/core/store/state/theater/theater.selectors';
import { NzColDirective, NzRowDirective } from 'ng-zorro-antd/grid';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';

@Component({
  selector: 'app-showtime',
  imports: [
    DatePipe,
    NzButtonComponent,
    NzIconDirective,
    NzOptionComponent,
    NzSelectComponent,
    NzSpaceComponent,
    NzTableCellDirective,
    NzTableComponent,
    NzTbodyComponent,
    NzThMeasureDirective,
    NzTheadComponent,
    NzTrDirective,
    NzWaveDirective,
    AsyncPipe,
    NzSpinComponent,
    NzAlertComponent,
    ReactiveFormsModule,
    NzRowDirective,
    NzColDirective,
    NzFormLabelComponent,
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

  form!: FormGroup;
  headerColumn = headerColumns.showtime;
  showtimes: readonly Showtime[] = [];
  theaters: Theater[] = [];
  loading$ = this.store.select(selectMovieLoading);
  error$ = this.store.select(selectMoviesError);

  ngOnInit() {
    this.form = this.fb.group({
      theaterId: [null],
      date: [dayjs().toDate()],
    });

    this.store.dispatch(TheaterActions.loadTheaters());
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
    const { theaterId, date } = this.form.value;
    if (!theaterId || !date) return;

    this.showtimes = [];
    this.store.dispatch(
      ShowtimeActions.loadShowtimesByTheaterId({ theaterId: theaterId }),
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
