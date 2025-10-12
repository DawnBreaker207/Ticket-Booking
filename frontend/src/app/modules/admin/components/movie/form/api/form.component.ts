import {Component, inject, signal} from '@angular/core';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {MovieService} from '@/app/core/services/movie/movie.service';
import {DatePipe, NgClass} from '@angular/common';
import {NzModalModule} from 'ng-zorro-antd/modal';
import {NzListModule} from 'ng-zorro-antd/list';
import {NzAvatarModule} from 'ng-zorro-antd/avatar';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzButtonModule} from 'ng-zorro-antd/button';

@Component({
  selector: 'movie-form-api',
  imports: [
    ReactiveFormsModule,
    DatePipe,
    NzModalModule,
    NzListModule,
    NzAvatarModule,
    NzInputModule,
    NzButtonModule,
    NgClass
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css'
})
export class FormMovieAPIComponent {
  private fb = inject(FormBuilder);
  private movieService = inject(MovieService);

  searchCtrl = this.fb.control('');
  searchResults = signal<any[]>([]);
  selectedMovie = signal<any | null>(null)

  onSearch() {
    const query = this.searchCtrl.value?.trim();
    if (!query) {
      this.searchResults.set([]);
      return
    }
    this.movieService.searchMovies(query).subscribe(res => {
      this.searchResults.set(res);
    })

  }

  selectMovie(movie: any) {
    this.selectedMovie.set(movie);
  }
}
