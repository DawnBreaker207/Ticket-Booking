import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { selectMovieById } from '@/app/core/store/state/movie/movie.selectors';
import { AsyncPipe, DatePipe } from '@angular/common';
import { MovieActions } from '@/app/core/store/state/movie/movie.actions';
import { map, switchMap } from 'rxjs';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzRateModule } from 'ng-zorro-antd/rate';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-detail',
  imports: [
    AsyncPipe,
    DatePipe,
    NzTagModule,
    NzButtonModule,
    NzIconModule,
    NzTabsModule,
    NzAvatarModule,
    NzRateModule,
    FormsModule,
  ],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.css',
})
export class DetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private store = inject(Store);

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
  movieId$ = this.route.params.pipe(map((params) => +params['id']));
  movie$ = this.movieId$.pipe(
    switchMap((id) => this.store.select(selectMovieById(id))),
  );

  ngOnInit() {
    this.movieId$.subscribe((id) => {
      this.store.dispatch(MovieActions.loadMovie({ id }));
    });
  }
}
