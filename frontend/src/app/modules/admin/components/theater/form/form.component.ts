import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule,} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd/modal';
import {filter, Subject, take, takeUntil} from 'rxjs';
import {NzInputDirective} from 'ng-zorro-antd/input';
import {NzColDirective, NzRowDirective} from 'ng-zorro-antd/grid';
import {NzFormControlComponent, NzFormDirective, NzFormLabelComponent,} from 'ng-zorro-antd/form';
import {Store} from '@ngrx/store';
import {TheaterActions} from '@/app/core/store/state/theater/theater.actions';
import {selectTheaterById,} from '@/app/core/store/state/theater/theater.selectors';
import {Actions, ofType} from '@ngrx/effects';
import {Theater} from '@/app/core/models/theater.model';
import {NzOptionComponent, NzSelectComponent} from 'ng-zorro-antd/select';

@Component({
  selector: 'app-theater-form',
  imports: [
    FormsModule,
    NzColDirective,
    NzFormControlComponent,
    NzFormDirective,
    NzFormLabelComponent,
    NzInputDirective,
    NzRowDirective,
    ReactiveFormsModule,
    NzOptionComponent,
    NzSelectComponent,
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css',
})
export class FormTheaterComponent implements OnInit, OnDestroy {
  mode: 'add' | 'edit' | 'view' = 'add';
  theaterId!: number;
  form!: FormGroup;
  private fb = inject(FormBuilder);
  private modelRef = inject(NzModalRef);
  private store = inject(Store);
  private actions$ = inject(Actions);

  private initialForm: any = null;
  destroy$: Subject<void> = new Subject<void>();

  ngOnInit() {
    this.initForm();

    const { mode, id } = this.modelRef.getConfig().nzData;

    this.mode = mode;
    this.theaterId = id;
    if (this.mode !== 'add' && this.theaterId) {
      this.store.dispatch(
        TheaterActions.loadTheater({ id: this.theaterId as number }),
      );
      this.store
        .select(selectTheaterById(this.theaterId))
        .pipe(
          filter(
            (theater) => theater !== null && theater.id === this.theaterId,
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
  }

  private disableForm() {
    Object.keys(this.form.controls).forEach((key) => {
      this.form.get(key)?.disable();
    });
  }

  initForm() {
    this.form = this.fb.group({
      id: [''],
      name: [''],
      location: [''],
      capacity: [''],
    });
  }

  private patchFormValue(theater: Theater) {
    this.form.patchValue({
      name: theater.name,
      location: theater.name,
      capacity: theater.capacity,
    });
  }

  hasData(): boolean {
    return Object.values(this.form.value).some(
      (v) => v !== null && v !== '' && !(Array.isArray(v) && v.length === 0),
    );
  }

  submit() {
    if (this.form.valid) {
      const theaterData = this.form.value;
      if (this.mode === 'edit') {
        this.store.dispatch(
          TheaterActions.updateTheater({
            id: this.theaterId as number,
            theater: theaterData,
          }),
        );
      } else if (this.mode === 'add') {
        this.store.dispatch(
          TheaterActions.createTheater({ theater: theaterData }),
        );
      }

      this.actions$
        .pipe(
          ofType(
            TheaterActions.createTheaterSuccess,
            TheaterActions.updateTheaterSuccess,
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
            TheaterActions.createTheaterFailed,
            TheaterActions.updateTheaterFailed,
          ),
          take(1),
          takeUntil(this.destroy$),
        )
        .subscribe(({ error }) => {
          console.error('Save theater failed', error);
        });
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
