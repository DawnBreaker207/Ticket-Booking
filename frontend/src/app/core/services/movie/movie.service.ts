import {inject, Injectable} from '@angular/core';
import {environment} from '@/environments/environment';
import {HttpClient, HttpContext, HttpHeaders} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {SKIP_AUTH, USE_HEADER} from '@/app/core/constants/context-token.model';
import {Movie} from '@/app/core/models/movie.model';
import {ApiRes} from '@/app/core/models/common.model';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  URL = `${environment.apiUrl}/movie`;
  private apiMovie = environment.tmbd.apiUrl;
  private imageBase = environment.tmbd.imageUrl;
  private headers = new HttpHeaders({Authorization: `Bearer ${environment.tmbd.token}`});
  private http = inject(HttpClient);


  getMovieLists() {
    return this.http.get<ApiRes<Movie[]>>(`${environment.apiUrl}/movie`).pipe(
      map((res) => res.data),
      catchError(this.handleError<Movie[]>('movie'))
    );
  }

  saveMovie(movie: Movie) {
    return this.http.post<ApiRes<Movie>>(`${this.URL}`, movie).pipe(
      map((res) => res.data),
      catchError(this.handleError<Movie>('movie')));
  }

  searchMovies(query: string, language = 'vi-VN') {
    return this.http.get(`${this.apiMovie}/search/movie?query=${encodeURIComponent(query)}&language=${language}&page=1`,
      {
        headers: this.headers,
        context: new HttpContext()
          .set(SKIP_AUTH, true)
          .set(USE_HEADER, false)
      }
    ).pipe(map((res: any) => res.results || []));
  }

  getMovieDetails(id: number, language = 'vi-VN') {
    return this.http.get(`${this.apiMovie}/movie/${id}?language=${language}`, {
      headers: this.headers, context: new HttpContext()
        .set(SKIP_AUTH, true)
        .set(USE_HEADER, false)
    })
  }

  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    }
  }

}
