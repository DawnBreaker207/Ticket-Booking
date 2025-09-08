import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd/modal';
import {ScheduleService} from '@/app/core/services/schedule/schedule.service';
import {debounceTime, map, of, Subject, switchMap} from 'rxjs';
import {NzInputDirective, NzInputGroupComponent} from 'ng-zorro-antd/input';
import {NzColDirective, NzRowDirective} from 'ng-zorro-antd/grid';
import {NzDatePickerComponent} from 'ng-zorro-antd/date-picker';
import {NzFormControlComponent, NzFormDirective, NzFormLabelComponent} from 'ng-zorro-antd/form';
import dayjs from 'dayjs';
import {MovieService} from '@/app/core/services/movie/movie.service';
import {Movie} from '@/app/core/models/movie.model';
import {DatePipe, NgClass} from '@angular/common';
import {NzAvatarComponent} from 'ng-zorro-antd/avatar';
import {NzListComponent, NzListItemComponent, NzListItemMetaComponent} from 'ng-zorro-antd/list';

@Component({
  selector: 'app-schedule-form',
  imports: [
    FormsModule,
    NzColDirective,
    NzDatePickerComponent,
    NzFormControlComponent,
    NzFormDirective,
    NzFormLabelComponent,
    NzInputDirective,
    NzRowDirective,
    ReactiveFormsModule,
    NzInputGroupComponent,
    DatePipe,
    NzAvatarComponent,
    NzListComponent,
    NzListItemComponent,
    NzListItemMetaComponent,
    NgClass,

  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css'
})
export class FormScheduleComponent implements OnInit, OnDestroy {
  mode: 'add' | 'edit' | 'view' = 'add';
  scheduleId!: number;
  form!: FormGroup;
  private fb = inject(FormBuilder);
  private modelRef = inject(NzModalRef);
  private scheduleService = inject(ScheduleService);
  private movieService = inject(MovieService);
  searchCtrl = this.fb.control('');
  searchResults = signal<any | null>(null);
  selectedMovie = signal<any>(null);
  destroy$: Subject<void> = new Subject<void>();

  ngOnInit() {
    this.initForm();

    const {mode, id} = this.modelRef.getConfig().nzData
    console.log(mode, id);
    this.mode = mode;
    this.scheduleId = id;
    if (this.mode !== 'add' && this.scheduleId) {
      this.scheduleService.getSchedule(this.scheduleId as number).subscribe(data => {
        console.log(data);
        this.form.patchValue({
          id: data.id,
          movieSession: data.movieSession && dayjs(data.movieSession).format('YYYY-MM-DD HH:mm:ss').toString(),
          movie: {
            id: data.movie.id,
            title: data.movie.title,
            poster: data.movie.poster,
            duration: data.movie.duration,
            overview: data.movie.overview,
          }
        });
        if (this.mode === 'view') {
          Object.keys(this.form.controls).forEach(key => {
            this.form.get(key)?.disable();
          })
        }
      })
    }

    this.searchCtrl.valueChanges.pipe(
      debounceTime(1000),
      map(v => v?.trim()),
      switchMap(query => {
        if (!query) return of([]);
        return this.movieService.getMovieLists(query);
      })
    ).subscribe(res => this.searchResults.set(res))
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
        releaseDate: movie.releaseDate
      }
    })
    console.log(this.form.value)
    this.searchResults.set(null);
    this.searchCtrl.setValue("")
  }

  initForm() {
    this.form = this.fb.group({
      id: [''],
      movie: this.fb.group({
        id: [''],
        title: [''],
        poster: [''],
        duration: [''],
        overview: [''],
        releaseDate: ['']
      }),
      movieSession: [''],
    })
  }

  hasData(): boolean {
    return Object.values(this.form.value).some(v => v !== null && v !== '' && !(Array.isArray(v) && v.length === 0));
  }

  submit() {
    if (this.form.valid) {
      const submit = this.mode === 'add' ? this.scheduleService.createSchedule(this.form.value) : this.scheduleService.updateSchedule(this.form.value);
      submit.subscribe((data) => {
        console.log(data)
      })
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
