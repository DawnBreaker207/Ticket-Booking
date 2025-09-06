import {Component, inject, input, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {NzModalModule, NzModalRef, NzModalService} from 'ng-zorro-antd/modal';
import {MovieService} from '@/app/core/services/movie/movie.service';
import {map, Subject} from 'rxjs';
import {NzAutosizeDirective, NzInputDirective} from 'ng-zorro-antd/input';
import {NzFormControlComponent, NzFormDirective, NzFormLabelComponent} from 'ng-zorro-antd/form';
import {FormMovieAPIComponent} from '@/app/modules/admin/components/movie/form/api/form.component';
import {NzGridModule} from 'ng-zorro-antd/grid';
import {NzOptionComponent, NzSelectComponent} from 'ng-zorro-antd/select';
import {NzDatePickerComponent} from 'ng-zorro-antd/date-picker';
import {environment} from '@/environments/environment';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzSpaceModule} from 'ng-zorro-antd/space';

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
    NzButtonComponent,
    NzSpaceModule,
    NzAutosizeDirective
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css'
})
export class FormMovieComponent implements OnInit, OnDestroy {
  mode: 'add' | 'edit' | 'view' = 'add';
  movieId!: number;
  movieForm!: FormGroup;
  private fb = inject(FormBuilder);
  private modelRef = inject(NzModalRef);
  private movieService = inject(MovieService);
  private modalService = inject(NzModalService);
  imageBase = environment.tmbd.imageUrl;
  destroy$: Subject<void> = new Subject<void>();

  ngOnInit() {
    this.initForm();
    const {mode, movieId} = this.modelRef.getConfig().nzData;
    this.mode = mode;
    this.movieId = movieId;
    if (this.mode !== 'add' && this.movieId) {
      this.movieService.findOneMovie(this.movieId as number).subscribe(data => {
        this.movieForm.patchValue(data);
        if (this.mode === 'view') {
          Object.keys(this.movieForm.controls).forEach(key => {
            this.movieForm.get(key)?.disable();
          })
        }

      })
    }
  }

  hasData(): boolean {
    return Object.values(this.movieForm.value).some(v => v !== null && v !== '' && !(Array.isArray(v) && v.length === 0));
  }

  initForm() {
    this.movieForm = this.fb.group({
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
    })
  }


  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  submit() {
    if (this.movieForm.valid) {
      console.log(this.movieForm.controls['id'].value);
      const submit = this.mode === 'add' ? this.movieService.saveMovie(this.movieForm.value) : this.movieService.updateMovie(this.movieForm.value);
      submit.subscribe((data) => {
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
                  genres: movie.genres.map((g: any) => g.name.replace(/^Phim\s/i, ''))
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
                  genres: result.genres,
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
