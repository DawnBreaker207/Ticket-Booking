import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {NzModalModule, NzModalRef, NzModalService} from 'ng-zorro-antd/modal';
import {MovieService} from '@/app/core/services/movie/movie.service';
import {map, Subject} from 'rxjs';
import {NzInputDirective} from 'ng-zorro-antd/input';
import {NzFormControlComponent, NzFormDirective, NzFormLabelComponent} from 'ng-zorro-antd/form';
import {FormMovieAPIComponent} from '@/app/modules/admin/components/movie/form/api/form.component';
import {NzGridModule} from 'ng-zorro-antd/grid';
import {NzOptionComponent, NzSelectComponent} from 'ng-zorro-antd/select';
import {NzDatePickerComponent} from 'ng-zorro-antd/date-picker';
import {environment} from '@/environments/environment';
import {NzButtonComponent} from 'ng-zorro-antd/button';

@Component({
  selector: 'movie-form',
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
    NzButtonComponent
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css'
})
export class FormMovieComponent implements OnInit, OnDestroy {
  movieForm!: FormGroup;
  private fb = inject(FormBuilder);
  private modelRef = inject(NzModalRef);
  private movieService = inject(MovieService);
  private modalService = inject(NzModalService);
  imageBase = environment.tmbd.imageUrl;
  mode: 'add' | 'edit' | 'view' = 'add';
  destroy$: Subject<void> = new Subject<void>();

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    this.movieForm = this.fb.group({
      poster: [''],
      title: [''],
      overview: [''],
      duration: [null],
      language: [''],
      genres: [[]],
      releaseDate: [null],
      imdbId: [''],
      filmId: [null],
    })
  }


  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  submit() {
    if (this.movieForm.valid) {
      this.movieService.saveMovie(this.movieForm.value).subscribe((data) => {
        console.log(data);
      })
    }
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
            const selected = modal.getContentComponent().selectedMovie();
            if (!selected) return;
            this.movieService.getMovieDetails(selected.id).pipe(
              map((movie: any) => ({
                  ...movie,
                  poster_path: `${this.imageBase}/${movie.poster_path}`,
                  genre: movie.genres.map((g: any) => g.name.replace(/^Phim\s/i, ''))
                })
              )).subscribe({
              next: (result: any) => {
                console.log(result)
                this.movieForm.patchValue({
                  title: result.title,
                  poster: result.poster_path,
                  overview: result.overview,
                  duration: result.runtime,
                  language: result.original_language,
                  genres: result.genre,
                  releaseDate: result.release_date,
                  imdbId: result.imdb_id,
                  filmId: result.id,
                })
                modal.close();
              },
            })

          }
        }
      ],


    });

  }
}
