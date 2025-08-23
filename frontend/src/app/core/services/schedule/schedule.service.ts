import {inject, Injectable} from '@angular/core';
import {environment} from '@/environments/environment';
import {HttpClient} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {CinemaHall} from '@/app/core/models/cinemaHall.model';
import {ApiRes} from '@/app/core/models/common.model';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  URL = `${environment.apiUrl}/cinema`;
  private http = inject(HttpClient);

  getSchedules() {
    return this.http.get<ApiRes<CinemaHall[]>>(`${this.URL}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<CinemaHall[]>('cinema hall'))
    )
  }

  getSchedule(id: number) {
    return this.http.get<ApiRes<CinemaHall>>(`${this.URL}/${id}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<CinemaHall>('cinema hall'))
    )
  }

  createSchedule(schedule: CinemaHall) {
    return this.http.post<ApiRes<CinemaHall>>(`${this.URL}`, schedule).pipe(
      map((res) => res.data),
      catchError(this.handleError<CinemaHall>('cinema hall'))
    )
  }

  updateSchedule(schedule: CinemaHall) {
    return this.http.put<ApiRes<CinemaHall>>(`${this.URL}/${schedule.id}`, schedule).pipe(
      map((res) => res.data),
      catchError(this.handleError<CinemaHall>('cinema hall'))
    )
  }

  deleteSchedule(id: number) {
    return this.http.delete<void>(`${this.URL}/${id}`).pipe(
      catchError(this.handleError<CinemaHall>('cinema hall'))
    )
  }

  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    }
  }
}
