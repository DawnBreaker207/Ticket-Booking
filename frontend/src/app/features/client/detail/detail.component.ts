import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { DatePipe } from '@angular/common';
import { map, switchMap, take } from 'rxjs';
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
  ],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.css',
})
export class DetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private store = inject(Store);

  private params$ = this.route.params.pipe(map((p) => +p['id']));

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
  movie = toSignal(
    this.params$.pipe(
      switchMap((id) => this.store.select(selectMovieById(id))),
    ),
  );

  ngOnInit() {
    this.params$
      .pipe(
        take(1),
        switchMap((id) =>
          this.store.select(selectMovieById(id)).pipe(
            take(1),
            map((m) => ({ id, movie: m })),
          ),
        ),
      )
      .subscribe(({ id }) => {
        this.store.dispatch(MovieActions.loadMovie({ id }));
      });
  }
}
