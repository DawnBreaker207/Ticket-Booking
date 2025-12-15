import { Component, input, output } from '@angular/core';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { RouterLink } from '@angular/router';
import { NzTypographyModule } from 'ng-zorro-antd/typography';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { Movie } from '@domain/movie/models/movie.model';

@Component({
  selector: 'app-movie-item',
  imports: [RouterLink, NzIconModule, NzButtonComponent, NzTypographyModule],
  templateUrl: './movie-item.component.html',
  styleUrl: './movie-item.component.css',
})
export class MovieItemComponent {
  movie = input.required<Movie>();
  selectMovie = output<number>();

  onSelectMovie(id: number) {
    this.selectMovie.emit(id);
  }
}
