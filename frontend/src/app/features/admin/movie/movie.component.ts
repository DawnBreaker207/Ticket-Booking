import { Component, inject, OnInit, signal } from '@angular/core';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzTagComponent } from 'ng-zorro-antd/tag';
import { AsyncPipe, DatePipe, NgClass } from '@angular/common';
import { NzSpaceComponent } from 'ng-zorro-antd/space';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { NzAvatarComponent } from 'ng-zorro-antd/avatar';
import {
  NzListComponent,
  NzListItemComponent,
  NzListItemMetaComponent,
} from 'ng-zorro-antd/list';
import { debounceTime, distinctUntilChanged, map } from 'rxjs';
import { Store } from '@ngrx/store';
import { NzAlertComponent } from 'ng-zorro-antd/alert';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { IsDeletedPipe } from '@core/pipes/is-deleted.pipe';
import {
  selectAllMovies,
  selectMovieLoading,
  selectMoviesError,
  selectPaginationMovie,
  selectSelectedMovie,
} from '@domain/movie/data-access/movie.selectors';
import { headerColumns } from '@core/constants/column';
import { Movie } from '@domain/movie/models/movie.model';
import { Pagination } from '@core/models/common.model';
import { MovieActions } from '@domain/movie/data-access/movie.actions';
import { FormMovieComponent } from '@features/admin/movie/form/movie/movie-form.component';

@Component({
  selector: 'app-movie',
  imports: [
    NzTableModule,
    NzInputModule,
    NzSelectModule,
    NzButtonModule,
    NzIconModule,
    NzTagComponent,
    DatePipe,
    NzSpaceComponent,
    NzAvatarComponent,
    NzListComponent,
    NzListItemComponent,
    NzListItemMetaComponent,
    NgClass,
    ReactiveFormsModule,
    AsyncPipe,
    NzAlertComponent,
    NzSpinModule,
    IsDeletedPipe,
  ],
  templateUrl: './movie.component.html',
  styleUrl: './movie.component.css',
  providers: [NzModalService],
})
export class MovieComponent implements OnInit {
  private fb = inject(FormBuilder);
  private store = inject(Store);
  private modalService = inject(NzModalService);

  // NgRx Selectors
  movies$ = this.store.select(selectAllMovies);
  pagination$ = this.store.select(selectPaginationMovie);
  loading$ = this.store.select(selectMovieLoading);
  error$ = this.store.select(selectMoviesError);

  // Local State
  headerColumn = headerColumns.movie;
  searchCtrl = this.fb.control('');
  searchResults = signal<any | null>(null);
  selectedMovie = signal<any>(null);

  movieList: readonly Movie[] = [];
  pagination: Pagination | null = null;

  pageIndex = 1;
  pageSize = 10;

  ngOnInit() {
    this.store.dispatch(MovieActions.loadMovies({ page: 0, size: 10 }));

    this.movies$.subscribe((movies) => (this.movieList = movies));
    this.pagination$.subscribe((p) => {
      this.pagination = p;
      if (p) {
        this.pageIndex = p.pageNumber + 1;
        this.pageSize = p.pageSize;
      }
    });

    this.searchCtrl.valueChanges
      .pipe(
        debounceTime(1000),
        map((v) => v?.trim() || ''),
        distinctUntilChanged(),
      )
      .subscribe((search) => {
        if (search) {
          this.store.dispatch(MovieActions.searchMovies({ search: search }));
          this.movies$.subscribe((result) => this.searchResults.set(result));
        } else {
          this.store.dispatch(MovieActions.loadMovies({ page: 0, size: 10 }));
          this.searchResults.set([]);
        }
      });
  }

  selectMovie(id: number) {
    const movie = this.movieList.find((m) => m.id === id);
    if (movie) {
      this.store.dispatch(MovieActions.selectedMovie({ movie }));
      this.selectedMovie.set(movie);
    }

    this.openMovieModal('view', id);
    this.searchResults.set([]);
    this.searchCtrl.setValue('', { emitEvent: false });
  }

  onPageChange(page: number) {
    this.pageIndex = page;
    this.store.dispatch(
      MovieActions.loadMovies({
        page: page - 1,
        size: this.pageSize,
      }),
    );
  }

  onSizeChange(size: number) {
    this.pageSize = size;
    this.store.dispatch(
      MovieActions.loadMovies({
        page: 0,
        size,
      }),
    );
  }

  openMovieModal(mode: 'add' | 'edit' | 'view', id?: string | number) {
    const modal = this.modalService.create({
      nzContent: FormMovieComponent,
      nzTitle: mode,
      nzClosable: true,
      nzData: {
        mode: mode,
        movieId: id,
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
                    modal.getContentComponent() as FormMovieComponent;
                  if (form.form.valid) {
                    form.submit();
                    modal.close();
                  } else {
                    console.log('Form Invalid!');
                  }
                },
              },
            ]
          : null,
      nzOnCancel: () => {
        const formComponent = modal.getContentComponent();
        if (!formComponent.hasData()) {
          return true;
        } else {
          return new Promise((resolve) => {
            this.modalService.confirm({
              nzTitle: 'Are u sure',
              nzOnOk: () => resolve(true),
              nzOnCancel: () => resolve(false),
            });
          });
        }
      },
    });
  }

  onDelete(id: number) {
    this.store.dispatch(MovieActions.deleteMovie({ id }));
    this.store.select(selectSelectedMovie);
  }
}
