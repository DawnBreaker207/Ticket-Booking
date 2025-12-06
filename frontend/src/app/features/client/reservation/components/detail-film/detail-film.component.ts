import { Component, inject, input, OnInit, output } from '@angular/core';
import { NzImageModule } from 'ng-zorro-antd/image';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzWaveDirective } from 'ng-zorro-antd/core/wave';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { map } from 'rxjs';
import { CountdownComponent } from '@features/client/reservation/components/countdown/countdown.component';
import { selectSelectedMovie } from '@domain/movie/data-access/movie.selectors';
import { selectSelectedTheater } from '@domain/theater/data-access/theater.selectors';
import {
  selectSelectedShowtime,
  selectTotalPrice,
} from '@domain/showtime/data-access/showtime.selectors';
import { selectSelectedSeats } from '@domain/seat/data-access/seat.selectors';
import { MovieActions } from '@domain/movie/data-access/movie.actions';
import { timeFormat } from '@shared/utils/date.helper';

@Component({
  selector: 'app-detail-film',
  imports: [
    NzImageModule,
    NzIconModule,
    NzButtonComponent,
    NzWaveDirective,
    CommonModule,
    CountdownComponent,
  ],
  templateUrl: './detail-film.component.html',
  styleUrl: './detail-film.component.css',
})
export class DetailFilmComponent implements OnInit {
  private store = inject(Store);
  filmDetail$ = this.store.select(selectSelectedMovie);
  theater$ = this.store.select(selectSelectedTheater);
  showtime$ = this.store.select(selectSelectedShowtime);
  totalPrice$ = this.store.select(selectTotalPrice);

  index = input<number>(0);
  steps = input<number[]>([]);
  selectedSeats$ = this.store
    .select(selectSelectedSeats)
    .pipe(
      map((seats) =>
        seats
          .filter((s) => s.status === 'SELECTED')
          .map((s) => s.seatNumber.replace(/\s+/g, '')),
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
