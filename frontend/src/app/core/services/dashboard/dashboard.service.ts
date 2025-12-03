import { inject, Injectable } from '@angular/core';
import { environment } from '@/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import {
  DashboardMetrics,
  DashboardQuery,
  PaymentDistribution,
  RevenuePoint,
  TopMovie,
  TopTheater,
} from '@/app/core/models/dashboard.model';
import { ApiRes } from '@/app/core/models/common.model';
import { catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  URL = `${environment.apiUrl}/dashboard`;
  private http = inject(HttpClient);

  getMetrics(query?: Partial<DashboardQuery>) {
    let params = new HttpParams();
    if (query) {
      Object.entries(query).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          params = params.set(key, value.toString());
        }
      });
    }
    return this.http
      .get<ApiRes<DashboardMetrics>>(`${this.URL}/metrics`, { params })
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<DashboardMetrics>('Dashboard metrics')),
      );
  }

  getRevenue(query?: Partial<DashboardQuery>) {
    let params = new HttpParams();

    if (query) {
      Object.entries(query).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          params = params.set(key, value.toString());
        }
      });
    }
    return this.http
      .get<ApiRes<RevenuePoint[]>>(`${this.URL}/revenue`, { params })
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<RevenuePoint[]>('Dashboard revenue')),
      );
  }

  getTopMovie(query?: Partial<DashboardQuery>) {
    let params = new HttpParams();

    if (query) {
      Object.entries(query).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          params = params.set(key, value.toString());
        }
      });
    }
    return this.http
      .get<ApiRes<TopMovie[]>>(`${this.URL}/top-movies`, { params })
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<TopMovie[]>('Dashboard top movies')),
      );
  }

  getTopTheater(query?: Partial<DashboardQuery>) {
    let params = new HttpParams();

    if (query) {
      Object.entries(query).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          params = params.set(key, value.toString());
        }
      });
    }
    return this.http
      .get<ApiRes<TopTheater[]>>(`${this.URL}/top-theaters`, { params })
      .pipe(
        map((res) => res.data),
        catchError(this.handleError<TopTheater[]>('Dashboard top theater')),
      );
  }

  getPaymentDistribution(query?: Partial<DashboardQuery>) {
    let params = new HttpParams();

    if (query) {
      Object.entries(query).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          params = params.set(key, value.toString());
        }
      });
    }

    return this.http
      .get<
        ApiRes<PaymentDistribution[]>
      >(`${this.URL}/payment-distribution`, { params })
      .pipe(
        map((res) => res.data),
        catchError(
          this.handleError<PaymentDistribution[]>(
            'Dashboard payment distribution',
          ),
        ),
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    };
  }
}
