import {inject, Injectable} from '@angular/core';
import {environment} from '@/environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {ApiRes} from '@/app/core/models/common.model';
import {Order, OrderFilter} from '@/app/core/models/order.model';
import {formatTime} from '@/app/shared/utils/formatDate';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  URL = `${environment.apiUrl}/order`;
  private http = inject(HttpClient);

  getOrders(filter?: Partial<OrderFilter>, page: number = 0, size: number = 0) {
    console.log(filter)
    let params = new HttpParams();

    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    const startDate = formatTime(filter?.dateFrom);
    const endDate = formatTime(filter?.dateTo);
    filter = {...filter, dateFrom: startDate, dateTo: endDate};

    if (filter) {
      Object.keys(filter).forEach((key) => {
        const value = filter[key as keyof OrderFilter];
        if (value !== null && value !== undefined && value !== '') {
          params = params.set(key, value.toString());
        }
      })
    }

    return this.http.get<ApiRes<Order[]>>(`${this.URL}`, {params}).pipe(
      map((res: any) => res.data),
      catchError(this.handleError<Order[]>('Get orders'))
    )
  }

  getOrder(id: number) {
    return this.http.get<ApiRes<Order>>(`${this.URL}/${id}`, {}).pipe(
      map((res: any) => res.data),
      catchError(this.handleError<Order>('Get order'))
    )
  }

  initOrder(order: Partial<Order>) {
    return this.http.post<ApiRes<string>>(`${this.URL}/init`, order).pipe(
      map((res: any) => res.data),
      catchError(this.handleError<string>('Init order'))
    )
  }

  holdSeat(order: Partial<Order>) {
    return this.http.post<ApiRes<Order>>(`${this.URL}/seatHold`, order).pipe(
      map((res: any) => res.data),
      catchError(this.handleError<Order>('hold seat order'))
    )
  }

  confirm(order: Partial<Order>) {
    return this.http.post<ApiRes<Order>>(`${this.URL}/confirm`, order).pipe(
      map((res: any) => res.data),
      catchError(this.handleError<Order>('Confirm order'))
    )
  }

  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    }
  }

}
