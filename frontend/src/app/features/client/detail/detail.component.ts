import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { DatePipe, NgClass } from '@angular/common';
import { map } from 'rxjs';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzRateModule } from 'ng-zorro-antd/rate';
import { FormsModule } from '@angular/forms';
import { selectMovieById } from '@domain/movie/data-access/movie.selectors';
import { MovieActions } from '@domain/movie/data-access/movie.actions';
import { toSignal } from '@angular/core/rxjs-interop';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { selectAllShowtimes } from '@domain/showtime/data-access/showtime.selectors';
import {
  selectAllTheaters,
  selectSelectedTheaterId,
} from '@domain/theater/data-access/theater.selectors';
import { ShowtimeActions } from '@domain/showtime/data-access/showtime.actions';
import { TheaterActions } from '@domain/theater/data-access/theater.actions';
import dayjs from 'dayjs';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-detail',
  imports: [
    DatePipe,
    NzTagModule,
    NzButtonModule,
    NzIconModule,
    NzTabsModule,
    NzAvatarModule,
    NzRateModule,
    FormsModule,
    LoadingComponent,
    NgClass,
    TranslatePipe,
  ],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.css',
})
export class DetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private store = inject(Store);
  private router = inject(Router);

  private movieId = toSignal(this.route.params.pipe(map((p) => +p['id'])));
  selectedDate = signal<Date>(dayjs().startOf('day').toDate());

  movie = computed(() => {
    const id = this.movieId();
    return id ? this.store.selectSignal(selectMovieById(id))() : null;
  });

  allShowtimes = this.store.selectSignal(selectAllShowtimes);
  allTheaters = this.store.selectSignal(selectAllTheaters);
  selectedTheaterId = this.store.selectSignal(selectSelectedTheaterId);

  dates = computed(() => {
    return Array.from({ length: 7 }).map((_, i) =>
      dayjs().add(i, 'day').toDate(),
    );
  });

  showtimes = computed(() => {
    const movie = this.movie();
    const showtimes = this.allShowtimes();
    const theaters = this.allTheaters();
    const currentTheaterId = this.selectedTheaterId();
    const date = this.selectedDate();

    if (!movie || !showtimes.length || !currentTheaterId) return [];

    const filtered = showtimes.filter(
      (st) =>
        st.movieId === movie.id &&
        dayjs(st.showDate).isSame(date, 'day') &&
        st.theaterId === currentTheaterId,
    );
    if (filtered.length === 0) return [];

    const groupTheater = new Map<number, any>();

    filtered.forEach((st) => {
      const theater = theaters.find((t) => t.id === st.theaterId);
      if (theater) {
        if (!groupTheater.has(theater.id)) {
          groupTheater.set(theater.id, {
            id: theater.id,
            name: theater.name,
            showtimes: [],
          });
        }
        groupTheater.get(theater.id).showtimes.push({
          id: st.id,
          time: st.showTime.toString().substring(0, 5),
          availableSeats: st.availableSeats,
          totalSeats: st.totalSeats || 100,
        });
      }
    });
    return Array.from(groupTheater.values());
  });

  ngOnInit() {
    const id = this.movieId();
    if (id) {
      this.store.dispatch(MovieActions.loadMovie({ id: id }));
      this.store.dispatch(
        ShowtimeActions.loadShowtimesByMovieId({
          movieId: id,
          page: 0,
          size: 50,
        }),
      );
      if (this.allTheaters().length === 0) {
        this.store.dispatch(
          TheaterActions.loadTheaters({ page: 0, size: 100 }),
        );
      }
    }
  }

  selectDate(date: Date) {
    this.selectedDate.set(date);
  }

  reviews = [
    {
      user: 'Minh Tuấn',
      avatar: 'https://i.pravatar.cc/150?u=99',
      rating: 5,
      comment: 'Phim đỉnh cao, hình ảnh và âm thanh quá tuyệt vời!',
      date: '02/03/2025',
    },
    {
      user: 'Lan Ngọc',
      avatar: 'https://i.pravatar.cc/150?u=88',
      rating: 4,
      comment: 'Hơi dài nhưng xứng đáng.',
      date: '03/03/2025',
    },
  ];
}
