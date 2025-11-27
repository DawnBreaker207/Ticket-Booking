import { inject, Injectable } from '@angular/core';
import { environment } from '@/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import { ApiRes, ResponsePage } from '@/app/core/models/common.model';
import {
  Reservation,
  ReservationFilter,
  ReservationInitRequest,
  ReservationInitResponse,
  ReservationRequest,
} from '@/app/core/models/reservation.model';
import { formatDate, formatTime } from '@/app/shared/utils/formatDate';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  URL = `${environment.apiUrl}/reservation`;
  private http = inject(HttpClient);

  getReservations(
    filter?: Partial<ReservationFilter>,
    page: number = 0,
    size: number = 0,
  ) {
    console.log(filter);
    let params = new HttpParams();

    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    const startDate = formatDate(filter?.dateFrom);
    const endDate = formatTime(filter?.dateTo);
    filter = { ...filter, dateFrom: startDate, dateTo: endDate };

    if (filter) {
      Object.keys(filter).forEach((key) => {
        const value = filter[key as keyof ReservationFilter];
        if (value !== null && value !== undefined && value !== '') {
          params = params.set(key, value.toString());
        }
      });
    }

    return this.http
      .get<ApiRes<ResponsePage<Reservation[]>>>(`${this.URL}`, { params })
      .pipe(
        map((res) => res.data),
        catchError(
          this.handleError<ResponsePage<Reservation[]>>('Get reservations'),
        ),
      );
  }

  getReservation(id: string) {
    return this.http.get<ApiRes<Reservation>>(`${this.URL}/${id}`, {}).pipe(
      map((res: any) => res.data),
      catchError(this.handleError<Reservation>('Get reservation')),
    );
  }

  initReservation(reservation: ReservationInitRequest) {
    return this.http
      .post<ApiRes<ReservationInitResponse>>(`${this.URL}/init`, reservation)
      .pipe(
        map((res: any) => res.data),
        catchError(
          this.handleError<ReservationInitResponse>('Init reservation'),
        ),
      );
  }

  holdReservationSeat(reservation: ReservationRequest) {
    return this.http
      .post<ApiRes<void>>(`${this.URL}/seatHold`, reservation)
      .pipe(
        map((res: any) => res.data),
        catchError(this.handleError<void>('hold seat reservation')),
      );
  }

  confirmReservation(reservation: ReservationRequest) {
    return this.http
      .post<ApiRes<Reservation>>(`${this.URL}/confirm`, reservation)
      .pipe(
        map((res: any) => res.data),
        catchError(this.handleError<Reservation>('Confirm reservation')),
      );
  }

  cancelReservation(reservationId: string, userId: number) {
    return this.http
      .post<ApiRes<void>>(`${this.URL}/${reservationId}/cancel`, userId)
      .pipe(
        map((res: any) => res.data),
        catchError(this.handleError<void>('Cancel reservation')),
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    };
  }
}
