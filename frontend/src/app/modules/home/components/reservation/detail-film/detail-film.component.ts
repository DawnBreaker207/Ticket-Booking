import {Component, inject, input, output} from '@angular/core';
import {Movie} from '@/app/core/models/movie.model';
import {CinemaHall} from '@/app/core/models/cinemaHall.model';
import {NzImageModule} from 'ng-zorro-antd/image';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzImageViewComponent} from 'ng-zorro-antd/experimental/image';
import {AsyncPipe, DatePipe} from '@angular/common';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzWaveDirective} from 'ng-zorro-antd/core/wave';
import {Store} from '@ngrx/store';
import {selectedSeats} from '@/app/core/store/state/selectors/reservation.selectors';
import {map} from 'rxjs';

@Component({
  selector: 'app-detail-film',
  imports: [NzImageModule, NzIconModule, NzImageViewComponent, DatePipe, NzButtonComponent, NzWaveDirective, AsyncPipe],
  templateUrl: './detail-film.component.html',
  styleUrl: './detail-film.component.css'
})
export class DetailFilmComponent {
  filmDetail = input<Movie | null>(null);
  cinemaHall = input<CinemaHall | null>(null);
  index = input<number>(0);
  steps = input<number[]>([]);
  store = inject(Store);
  selectedSeats$ = this.store.select(selectedSeats).pipe(
    map(seats => seats.map(s => s.seatNumber).join(", "))
  );
  stepChange = output<number>();

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
}
