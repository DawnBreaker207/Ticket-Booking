import { inject, Injectable } from '@angular/core';
import { environment } from '@/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import { ApiRes } from '@/app/core/models/common.model';
import { Payment, PaymentRequest } from '@/app/core/models/payment.model';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  URL = `${environment.apiUrl}/payment`;
  private http = inject(HttpClient);

  createPayment(query: PaymentRequest) {
    let params = new HttpParams();
    params = params.set('reservationId', query.reservationId);
    params = params.set('amount', query.amount);
    params = params.set('paymentType', query.paymentType);
    return this.http
      .get<ApiRes<Payment>>(`${this.URL}/create`, { params })
      .pipe(
        map((res) => res.data.paymentUrl),
        catchError(this.handleError<any>('Payment')),
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    };
  }
}
