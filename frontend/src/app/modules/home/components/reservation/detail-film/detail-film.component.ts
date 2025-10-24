import {Component, inject, input, OnInit, output} from '@angular/core';
import {NzImageModule} from 'ng-zorro-antd/image';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzImageViewComponent} from 'ng-zorro-antd/experimental/image';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzWaveDirective} from 'ng-zorro-antd/core/wave';
import {Store} from '@ngrx/store';
import {selectSelectedMovie} from '@/app/core/store/state/movie/movie.selectors';
import {CommonModule} from '@angular/common';
import {selectSelectedTheater} from '@/app/core/store/state/theater/theater.selectors';
import {selectSelectedSeats} from '@/app/core/store/state/seat/seat.selectors';
import {map} from 'rxjs';
import {selectSelectedShowtime} from '@/app/core/store/state/showtime/showtime.selectors';
import {timeFormat} from '@/app/shared/utils/formatDate';
import {MovieActions} from '@/app/core/store/state/movie/movie.actions';

@Component({
  selector: 'app-detail-film',
  imports: [
    NzImageModule,
    NzIconModule,
    NzImageViewComponent,
    NzButtonComponent,
    NzWaveDirective,
    CommonModule,
  ],
  templateUrl: './detail-film.component.html',
  styleUrl: './detail-film.component.css',
})
export class DetailFilmComponent implements OnInit {
  private store = inject(Store);
  filmDetail$ = this.store.select(selectSelectedMovie);
  theater$ = this.store.select(selectSelectedTheater);
  showtime$ = this.store.select(selectSelectedShowtime);
  index = input<number>(0);
  steps = input<number[]>([]);
  selectedSeats$ = this.store.select(selectSelectedSeats).pipe(
    map((seats) =>
      seats
        .filter((s) => s.status === 'SELECTED')
        .map((s) => s.seatNumber)
        .join(', '),
    ),
  );

  stepChange = output<number>();

  ngOnInit() {
    this.showtime$.subscribe((data) =>
      this.store.dispatch(
        MovieActions.loadMovie({ id: data?.movieId as number }),
      ),
    );
  }

  nextStep() {
    if (this.index() < this.steps().length - 1) {
      this.stepChange.emit(this.index() + 1);
    }
  }

  prevStep() {
    if (this.index() > 0) {
      this.stepChange.emit(this.index() - 1);
    }
  }

  saveSeats() {
    this.stepChange.emit(this.index() + 1);
  }

  protected readonly timeFormat = timeFormat;
}
