import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NzModalModule, NzModalRef, NzModalService } from 'ng-zorro-antd/modal';
import { MovieService } from '@/app/core/services/movie/movie.service';
import { filter, map, Subject, take, takeUntil } from 'rxjs';
import { NzAutosizeDirective, NzInputDirective } from 'ng-zorro-antd/input';
import {
  NzFormControlComponent,
  NzFormDirective,
  NzFormLabelComponent,
} from 'ng-zorro-antd/form';
import { FormMovieAPIComponent } from '@/app/modules/admin/components/movie/form/api/form.component';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzOptionComponent, NzSelectComponent } from 'ng-zorro-antd/select';
import { NzDatePickerComponent } from 'ng-zorro-antd/date-picker';
import { environment } from '@/environments/environment';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import { Store } from '@ngrx/store';
import { Actions, ofType } from '@ngrx/effects';
import {
  selectMovieById,
  selectMovieLoadingDetails,
  selectMoviesError,
  selectMoviesSaving,
} from '@/app/core/store/state/movie/movie.selectors';
import { MovieActions } from '@/app/core/store/state/movie/movie.actions';
import { Movie } from '@/app/core/models/movie.model';
import { NzSpinComponent } from 'ng-zorro-antd/spin';
import { AsyncPipe } from '@angular/common';
import { NzAlertComponent } from 'ng-zorro-antd/alert';

@Component({
  selector: 'app-movie-form',
  imports: [
    NzModalModule,
    NzInputDirective,
    ReactiveFormsModule,
    NzFormDirective,
    NzGridModule,
    NzFormControlComponent,
    NzSelectComponent,
    NzOptionComponent,
    NzDatePickerComponent,
    NzFormLabelComponent,
    NzButtonComponent,
    NzSpaceModule,
    NzAutosizeDirective,
    NzSpinComponent,
    AsyncPipe,
    NzAlertComponent,
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css',
})
export class FormMovieComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private store = inject(Store);
  private actions$ = inject(Actions);
  private modelRef = inject(NzModalRef);
  private movieService = inject(MovieService);
  private modalService = inject(NzModalService);

  // Props
  mode: 'add' | 'edit' | 'view' = 'add';
  movieId!: number;
  form!: FormGroup;
  imageBase = environment.tmbd.imageUrl;

  // NgRx State
  loading$ = this.store.select(selectMovieLoadingDetails);
  saving$ = this.store.select(selectMoviesSaving);
  error$ = this.store.select(selectMoviesError);

  private initialFormValue: any = null;
  private destroy$: Subject<void> = new Subject<void>();

  ngOnInit() {
    this.initForm();
    const { mode, movieId } = this.modelRef.getConfig().nzData;
    this.mode = mode;
    this.movieId = movieId;

    if (this.mode !== 'add' && this.movieId) {
      this.store.dispatch(MovieActions.loadMovie({ id: this.movieId }));
      this.store
        .select(selectMovieById(this.movieId))
        .pipe(
          filter((movie) => movie !== null && movie.id === this.movieId),
          take(1),
          takeUntil(this.destroy$),
        )
        .subscribe((movie) => {
          if (movie) {
            this.patchFormValue(movie);
            this.initialFormValue = this.form.value;
            if (this.mode === 'view') {
              this.disableForm();
            }
          } else {
            this.initialFormValue = this.form.value;
          }
        });
    }
  }

  hasData(): boolean {
    if (this.mode === 'view') return false;

    if (!this.initialFormValue) return false;

    return (
      JSON.stringify(this.initialFormValue) !== JSON.stringify(this.form.value)
    );
  }

  initForm() {
    this.form = this.fb.group({
      id: [''],
      poster: [''],
      title: [''],
      overview: [''],
      duration: [null],
      language: [''],
      genres: [[]],
      releaseDate: [null],
      imdbId: [''],
      filmId: [null],
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private disableForm() {
    Object.keys(this.form.controls).forEach((key) => {
      this.form.get(key)?.disable();
    });
  }

  private patchFormValue(movie: Movie) {
    this.form.patchValue({
      title: movie.title,
      poster: movie.poster,
      overview: movie.overview,
      duration: movie.duration,
      language: movie.language,
      genres: movie.genres,
      releaseDate: movie.releaseDate,
      imdbId: movie.imdbId,
      filmId: movie.filmId,
    });
  }

  submit() {
    if (!this.form.valid) {
      Object.keys(this.form.controls).forEach((key) => {
        this.form.get(key)?.markAllAsTouched();
      });
      return;
    }

    const movieData = this.form.value;

    if (this.mode === 'edit') {
      this.store.dispatch(
        MovieActions.updateMovie({ id: this.movieId, movie: movieData }),
      );
    } else if (this.mode === 'add') {
      this.store.dispatch(MovieActions.createMovie({ movie: movieData }));
    }

    this.actions$
      .pipe(
        ofType(
          MovieActions.createMovieSuccess,
          MovieActions.updateMovieSuccess,
        ),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe(() => {
        this.modelRef.close();
      });

    this.actions$
      .pipe(
        ofType(MovieActions.createMovieFailed, MovieActions.updateMovieFailed),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe(({ error }) => {
        console.error('Save movie failed: ', error);
      });
  }

  openMovieModal() {
    const modal = this.modalService.create({
      nzContent: FormMovieAPIComponent,
      nzTitle: undefined,
      nzClosable: true,
      nzWidth: 800,
      nzKeyboard: true,
      nzFooter: [
        {
          label: 'Confirm',
          type: 'primary',
          onClick: () => {
            const formComponent = modal.getContentComponent();
            const selected = formComponent.selectedMovie();

            if (!selected) return;

            this.movieService
              .getMovieDetails(selected.id)
              .pipe(
                map((movie: any) => ({
                  ...movie,
                  poster_path: `${this.imageBase}/${movie.poster_path}`,
                  genres: movie.genres.map((g: any) =>
                    g.name.replace(/^Phim\s/i, ''),
                  ),
                })),
                take(1),
                takeUntil(this.destroy$),
              )
              .subscribe({
                next: (result: any) => {
                  this.form.patchValue({
                    title: result.title,
                    poster: result.poster_path,
                    overview: result.overview,
                    duration: result.runtime,
                    language: result.original_language,
                    genres: result.genres,
                    releaseDate: result.release_date,
                    imdbId: result.imdb_id,
                    filmId: result.id,
                  });
                  this.form.markAsDirty();
                  modal.close();
                },
                error: (error) => {
                  console.error('Failed to fetch movie details', error);
                },
              });
          },
        },
      ],
    });
  }
}
