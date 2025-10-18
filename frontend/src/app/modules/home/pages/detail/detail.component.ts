import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import {
  selectMovieById,
  selectSelectedMovie,
} from '@/app/core/store/state/movie/movie.selectors';
import { Movie } from '@/app/core/models/movie.model';
import { AsyncPipe, JsonPipe } from '@angular/common';
import { MovieActions } from '@/app/core/store/state/movie/movie.actions';
import { filter, map, switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-detail',
  imports: [JsonPipe, AsyncPipe],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.css',
})
export class DetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private store = inject(Store);
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
