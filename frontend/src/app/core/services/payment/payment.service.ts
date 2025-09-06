import {inject, Injectable} from '@angular/core';
import {environment} from '@/environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {ApiRes} from '@/app/core/models/common.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  URL = `${environment.apiUrl}/payment`;
  private http = inject(HttpClient);

  createPayment(query?: any) {
    console.log(query)
    let params = new HttpParams();
    params = params.set('amount', query.totalPrice);
    params = params.set('vnp_TxnRef', query.orderId);
    params = params.set('bankCode', 'NCB');
    return this.http.get<ApiRes<any>>(`${this.URL}/vnpay`, {params}).pipe(
      map(res => res.data),
      catchError(this.handleError<any>('Payment'))
    )
  }

  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    }
  }
}
