import { inject, Injectable } from '@angular/core';
import { environment } from '@/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import {
  Seat,
  Showtime,
  ShowtimeRequest,
  Theater,
  TheaterRequest,
} from '@/app/core/models/theater.model';
import { ApiRes } from '@/app/core/models/common.model';

@Injectable({
  providedIn: 'root',
})
export class TheaterService {
  URL_THEATER = `${environment.apiUrl}/theater`;
  URL_SHOWTIME = `${environment.apiUrl}/showtime`;
  URL_SEAT = `${environment.apiUrl}/seats`;
  private http = inject(HttpClient);

  // Theater
  getTheaters() {
    return this.http.get<ApiRes<Theater[]>>(`${this.URL_THEATER}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<Theater[]>('theater')),
    );
  }

  getTheater(id: number) {
    return this.http.get<ApiRes<Theater>>(`${this.URL_THEATER}/${id}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<Theater>('theater')),
    );
  }

  getTheaterByLocation(location: string) {
    const params = new HttpParams().set('location', location);
    return this.http
      .get<ApiRes<Theater>>(`${this.URL_THEATER}/search`, { params })
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Theater>('theater')),
      );
  }

  createTheater(theater: TheaterRequest) {
    return this.http.post<ApiRes<Theater>>(`${this.URL_THEATER}`, theater).pipe(
      map((res) => res.data),
      catchError(this.handleError<Theater>('theater')),
    );
  }

  updateTheater(id: number, schedule: TheaterRequest) {
    return this.http
      .put<ApiRes<Theater>>(`${this.URL_THEATER}/${id}`, schedule)
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Theater>('theater')),
      );
  }

  deleteTheater(id: number) {
    return this.http
      .delete<void>(`${this.URL_THEATER}/${id}`)
      .pipe(catchError(this.handleError<void>('theater')));
  }

  // Showtime

  getShowtimeByDate(date: number) {
    const params = new HttpParams().set('date', date);
    return this.http
      .get<ApiRes<Showtime[]>>(`${this.URL_SHOWTIME}`, { params })
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Showtime[]>('showtime')),
      );
  }

  getShowtimeByMovie(movieId: number) {
    return this.http
      .get<ApiRes<Showtime[]>>(`${this.URL_SHOWTIME}/movies/${movieId}`)
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Showtime[]>('showtime')),
      );
  }

  getShowtimeByTheater(theaterId: number) {
    return this.http
      .get<ApiRes<Showtime[]>>(`${this.URL_SHOWTIME}/theaters/${theaterId}`)
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Showtime[]>('showtime')),
      );
  }

  getAvailableShowtime(date: Date) {
    const params = new HttpParams().set('date', date.toDateString());
    return this.http
      .get<ApiRes<Showtime[]>>(`${this.URL_SHOWTIME}/available`, { params })
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Showtime[]>('showtime')),
      );
  }

  getAvailableShowtimeByMovie(movieId: number) {
    return this.http
      .get<
        ApiRes<Showtime[]>
      >(`${this.URL_SHOWTIME}/available/movies/${movieId}`)
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Showtime[]>('showtime')),
      );
  }

  getShowtime(id: number) {
    return this.http.get<ApiRes<Showtime>>(`${this.URL_SHOWTIME}/${id}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<Showtime>('showtime')),
    );
  }

  createShowtime(showtime: ShowtimeRequest) {
    return this.http
      .post<ApiRes<Showtime>>(`${this.URL_SHOWTIME}`, showtime)
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Showtime>('showtime')),
      );
  }

  updateShowtime(id: number, showtime: ShowtimeRequest) {
    return this.http
      .put<ApiRes<Showtime>>(`${this.URL_SHOWTIME}/${id}`, showtime)
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Showtime>('showtime')),
      );
  }

  deleteShowtime(id: number) {
    return this.http
      .delete<void>(`${this.URL_SHOWTIME}/${id}`)
      .pipe(catchError(this.handleError<void>('showtime')));
  }

  // Seat
  getSeatByShowtime(showtimeId: number) {
    return this.http
      .get<ApiRes<Seat[]>>(`${this.URL_SEAT}/showtime/${showtimeId}`)
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Seat[]>('showtime')),
      );
  }

  getAvailableSeatByShowtime(showtimeId: number) {
    return this.http
      .get<ApiRes<Seat[]>>(`${this.URL_SEAT}/showtime/${showtimeId}/available`)
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<Seat[]>('showtime')),
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    };
  }
}
