import {inject, Injectable} from '@angular/core';
import {environment} from '@/environments/environment';
import {HttpClient, HttpContext, HttpHeaders, HttpParams} from '@angular/common/http';
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
  private headers = new HttpHeaders({Authorization: `Bearer ${environment.tmbd.token}`});
  private http = inject(HttpClient);


  getMovieLists(query?: string) {
    let params = query ? new HttpParams().set('title', query?.toString()) : undefined;

    return this.http.get<ApiRes<Movie[]>>(`${this.URL}`, {params}).pipe(
      map((res) => res.data),
      catchError(this.handleError<Movie[]>('movie'))
    );
  }

  saveMovie(movie: Movie) {
    return this.http.post<ApiRes<Movie>>(`${this.URL}`, movie).pipe(
      map((res) => res.data),
      catchError(this.handleError<Movie>('movie')));
  }

  findOneMovie(id: number) {
    return this.http.get<ApiRes<Movie>>(`${this.URL}/${id}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<Movie>('movie'))
    )
  }

  updateMovie(movie: Movie) {
    return this.http.put<ApiRes<Movie>>(`${this.URL}/${movie.id}`, movie).pipe(
      map((res) => res.data),
      catchError(this.handleError<Movie>('movie'))
    )
  }


  removeMovie(id: number) {
    return this.http.delete<void>(`${this.URL}/${id}`).pipe(
      map((res) => console.log(res)),
      catchError(this.handleError<void>('movie'))
    )
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
