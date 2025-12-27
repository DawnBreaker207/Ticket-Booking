import { Component, computed, inject, signal } from '@angular/core';
import { NzTableModule, NzTableQueryParams } from 'ng-zorro-antd/table';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzTagComponent } from 'ng-zorro-antd/tag';
import { DatePipe, NgClass } from '@angular/common';
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
} from '@domain/movie/data-access/movie.selectors';
import { headerColumns } from '@core/constants/column';
import { MovieActions } from '@domain/movie/data-access/movie.actions';
import { FormMovieComponent } from '@features/admin/movie/form/movie/movie-form.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

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
    NzAlertComponent,
    NzSpinModule,
    IsDeletedPipe,
    LoadingComponent,
  ],
  templateUrl: './movie.component.html',
  styleUrl: './movie.component.css',
  providers: [NzModalService],
})
export class MovieComponent {
  private fb = inject(FormBuilder);
  private store = inject(Store);
  private modalService = inject(NzModalService);

  // NgRx Selectors
  readonly movies = this.store.selectSignal(selectAllMovies);
  readonly pagination = this.store.selectSignal(selectPaginationMovie);
  readonly loading = this.store.selectSignal(selectMovieLoading);
  readonly error = this.store.selectSignal(selectMoviesError);

  // Local State
  readonly headerColumn = headerColumns.movie;
  readonly searchCtrl = this.fb.control('');

  isSearching = signal(false);
  selectedMovie = signal<any>(null);
  searchResults = computed(() => {
    return this.isSearching() && this.searchCtrl.value ? this.movies() : [];
  });

  constructor() {
    this.searchCtrl.valueChanges
      .pipe(
        debounceTime(500),
        map((v) => v?.trim() || ''),
        distinctUntilChanged(),
        takeUntilDestroyed(),
      )
      .subscribe((search) => {
        if (search) {
          this.isSearching.set(true);
          this.store.dispatch(MovieActions.searchMovies({ search: search }));
        } else {
          this.isSearching.set(false);
        }
      });
  }

  selectMovie(id: number) {
    const movie = this.movies().find((m) => m.id === id);
    if (movie) {
      this.store.dispatch(MovieActions.selectedMovie({ movie }));
      this.selectedMovie.set(movie);
      this.openMovieModal('view', id);
    }
    this.isSearching.set(false);
    this.searchCtrl.setValue('', { emitEvent: false });
  }

  onQueryParamsChange(params: NzTableQueryParams) {
    const { pageIndex, pageSize } = params;
    this.store.dispatch(
      MovieActions.loadMovies({ page: pageIndex - 1, size: pageSize }),
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
        mode === 'view'
          ? null
          : [
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
            ],
      nzOnCancel: (instance) => this.handleCancel(instance),
    });
  }

  private handleCancel(instance: FormMovieComponent) {
    if (!instance || !instance.hasData()) return true;

    return new Promise((resolve) => {
      this.modalService.confirm({
        nzTitle: 'Are u sure',
        nzOnOk: () => resolve(true),
        nzOnCancel: () => resolve(false),
      });
    });
  }

  onDelete(id: number) {
    this.store.dispatch(MovieActions.deleteMovie({ id }));
  }
}
