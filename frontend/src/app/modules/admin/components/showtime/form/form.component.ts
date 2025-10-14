import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import {
  NzFormControlComponent,
  NzFormDirective,
  NzFormLabelComponent,
} from 'ng-zorro-antd/form';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { NzColDirective, NzRowDirective } from 'ng-zorro-antd/grid';
import { DatePipe, NgClass } from '@angular/common';
import { NzAvatarComponent } from 'ng-zorro-antd/avatar';
import { NzDatePickerComponent } from 'ng-zorro-antd/date-picker';
import { NzInputDirective, NzInputGroupComponent } from 'ng-zorro-antd/input';
import {
  NzListComponent,
  NzListItemComponent,
  NzListItemMetaComponent,
} from 'ng-zorro-antd/list';
import { NzModalRef } from 'ng-zorro-antd/modal';
import { Store } from '@ngrx/store';
import { Actions, ofType } from '@ngrx/effects';
import { MovieService } from '@/app/core/services/movie/movie.service';
import {
  debounceTime,
  filter,
  map,
  of,
  Subject,
  switchMap,
  take,
  takeUntil,
} from 'rxjs';
import { Movie } from '@/app/core/models/movie.model';
import { Showtime } from '@/app/core/models/theater.model';
import { ShowtimeActions } from '@/app/core/store/state/showtime/showtime.actions';
import { selectSelectedShowtime } from '@/app/core/store/state/showtime/showtime.selectors';
import { selectAllTheaters } from '@/app/core/store/state/theater/theater.selectors';
import { selectSearchQuery } from '@/app/core/store/state/movie/movie.selectors';

@Component({
  selector: 'app-form',
  imports: [
    NzFormDirective,
    ReactiveFormsModule,
    NzRowDirective,
    DatePipe,
    FormsModule,
    NzAvatarComponent,
    NzColDirective,
    NzDatePickerComponent,
    NzFormControlComponent,
    NzFormLabelComponent,
    NzInputDirective,
    NzInputGroupComponent,
    NzListComponent,
    NzListItemComponent,
    NzListItemMetaComponent,
    NgClass,
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css',
})
export class FormShowtimeComponent implements OnInit, OnDestroy {
  mode: 'add' | 'edit' | 'view' = 'add';
  showtimeId!: number;
  form!: FormGroup;
  private fb = inject(FormBuilder);
  private modelRef = inject(NzModalRef);
  private store = inject(Store);
  private actions$ = inject(Actions);

  theaters$ = this.store.select(selectAllTheaters);
  searchMovie$ = this.store.select(selectSearchQuery);
  private movieService = inject(MovieService);

  searchCtrl = this.fb.control('');
  searchResults = signal<any | null>(null);
  selectedMovie = signal<any>(null);
  private initialForm: any = null;
  destroy$: Subject<void> = new Subject<void>();

  ngOnInit() {
    this.initForm();

    const { mode, id } = this.modelRef.getConfig().nzData;
    this.mode = mode;
    this.showtimeId = id;
    if (this.mode !== 'add' && this.showtimeId) {
      this.store.dispatch(
        ShowtimeActions.loadShowtime({ id: this.showtimeId as number }),
      );
      this.store
        .select(selectSelectedShowtime)
        .pipe(
          filter(
            (showtime) => showtime !== null && showtime.id === this.showtimeId,
          ),
          take(1),
          takeUntil(this.destroy$),
        )
        .subscribe((theater) => {
          if (theater) {
            this.patchFormValue(theater);
            this.initialForm = this.form.value;
            if (this.mode === 'view') {
              this.disableForm();
            }
          } else {
            this.initialForm = this.form.value;
          }
        });
    }

    this.searchMovie();
  }

  searchMovie() {
    this.searchCtrl.valueChanges
      .pipe(
        debounceTime(1000),
        map((v) => v?.trim()),
        switchMap((query) => {
          if (!query) return of([]);
          return this.movieService.getMovieLists(query);
        }),
      )
      .subscribe((res) => this.searchResults.set(res));
  }

  private disableForm() {
    Object.keys(this.form.controls).forEach((key) => {
      this.form.get(key)?.disable();
    });
  }

  selectMovie(movie: Movie) {
    console.log(movie);
    this.form.patchValue({
      movie: {
        id: movie.id,
        title: movie.title,
        poster: movie.poster,
        duration: movie.duration,
        overview: movie.overview,
        releaseDate: movie.releaseDate,
      },
    });
    this.searchResults.set(null);
    this.searchCtrl.setValue('');
  }

  initForm() {
    this.form = this.fb.group({
      id: [''],
      movieId: [''],
      movieTitle: [''],
      moviePosterUrl: [''],

      theaterId: [''],
      theaterName: [''],
      theaterLocation: [''],

      showDate: [''],
      showTime: [''],

      price: [''],

      totalSeats: [''],
      availableSeats: [''],
    });
  }

  private patchFormValue(showtime: Showtime) {
    this.form.patchValue({
      // Theater
      theaterName: showtime.theaterName,
      theaterId: showtime.theaterId,
      theaterLocation: showtime.theaterLocation,
      // Movie
      movieId: showtime.movieId,
      movieTitle: showtime.movieId,
      moviePosterUrl: showtime.moviePosterUrl,
      // Showtime
      price: showtime.price,
      totalSeats: showtime.totalSeats,
      // Showtime Date
      showDate: showtime.showDate,
      showTime: showtime.showTime,
    });
  }

  hasData(): boolean {
    return Object.values(this.form.value).some(
      (v) => v !== null && v !== '' && !(Array.isArray(v) && v.length === 0),
    );
  }

  submit() {
    if (this.form.valid) {
      Object.keys(this.form.value).forEach((key) => {
        this.form.get(key)?.markAllAsTouched();
      });
      return;
    }

    const showtimeData = this.form.value;

    if (this.mode === 'edit') {
      this.store.dispatch(
        ShowtimeActions.updateShowtime({
          id: this.showtimeId as number,
          showtime: showtimeData,
        }),
      );
    } else if (this.mode === 'add') {
      this.store.dispatch(
        ShowtimeActions.createShowtime({ showtime: showtimeData }),
      );
    }

    this.actions$
      .pipe(
        ofType(
          ShowtimeActions.createShowtimeSuccess,
          ShowtimeActions.updateShowtimeSuccess,
        ),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe(() => {
        this.modelRef.close();
      });

    this.actions$
      .pipe(
        ofType(
          ShowtimeActions.createShowtimeFailed,
          ShowtimeActions.updateShowtimeFailed,
        ),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe(({ error }) => {
        console.error('Save showtime failed', error);
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
