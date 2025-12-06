import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSwitchModule } from 'ng-zorro-antd/switch';
import { NzCardComponent } from 'ng-zorro-antd/card';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { FormsModule } from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { DecimalPipe } from '@angular/common';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import {
  BehaviorSubject,
  catchError,
  combineLatest,
  finalize,
  of,
  switchMap,
  tap,
} from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { DashboardService } from '@domain/dashboard/data-access/dashboard.service';
import {
  DashboardMetrics,
  PaymentDistribution,
  RevenuePoint,
  TopMovie,
  TopTheater,
} from '@domain/dashboard/models/dashboard.model';
import { formatDate } from '@shared/utils/date.helper';
import { DashboardItemComponent } from '@features/admin/dashboard/components/dashboard-item/dashboard-item.component';
import { CurrencyFormatPipe } from '@shared/pipes/currency-format.pipe';
import { MovieTableComponent } from '@features/admin/dashboard/components/movie-table/movie-table.component';
import { TheaterTableComponent } from '@features/admin/dashboard/components/theater-table/theater-table.component';
import { PaymentChartComponent } from '@features/admin/dashboard/components/payment-chart/payment-chart.component';
import { TheaterChartComponent } from '@features/admin/dashboard/components/theater-chart/theater-chart.component';
import { MovieChartComponent } from '@features/admin/dashboard/components/movie-chart/movie-chart.component';
import { RevenueChartComponent } from '@features/admin/dashboard/components/revenue-chart/revenue-chart.component';

@Component({
  selector: 'app-dashboard',
  imports: [
    NzLayoutModule,
    NzSelectModule,
    NzSwitchModule,
    NzCardComponent,
    NzGridModule,
    NzIconModule,
    NzButtonModule,
    NzDatePickerModule,
    NzSpinModule,
    RevenueChartComponent,
    MovieChartComponent,
    TheaterChartComponent,
    PaymentChartComponent,
    TheaterTableComponent,
    MovieTableComponent,
    CurrencyFormatPipe,
    FormsModule,
    DecimalPipe,
    NzDropDownModule,
    NzMenuModule,
    NzDropDownModule,
    DashboardItemComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  private destroyRef = inject(DestroyRef);

  isLoading = signal<boolean>(false);
  filterType: 'week' | 'month' | 'quarter' | 'year' = 'week';
  private filterSubject = new BehaviorSubject<any>(this.getFilterPayload());
  selectedDate: Date = new Date();
  selectedQuarterYear: number = new Date().getFullYear();
  selectedQuarter: number = Math.floor((new Date().getMonth() + 3) / 3);

  metrics = signal<DashboardMetrics | null>(null);
  revenues = signal<RevenuePoint[]>([]);
  payments = signal<PaymentDistribution[]>([]);
  theaters = signal<TopTheater[]>([]);
  movies = signal<TopMovie[]>([]);
  quarters = [
    { label: 'Quý 1 (Q1)', value: 1 },
    { label: 'Quý 2 (Q2)', value: 2 },
    { label: 'Quý 3 (Q3)', value: 3 },
    { label: 'Quý 4 (Q4)', value: 4 },
  ];

  years = Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - i);

  ngOnInit() {
    this.filterSubject
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        tap(() => this.isLoading.set(true)),

        switchMap((payload) => {
          return combineLatest({
            metrics: this.dashboardService.getMetrics(payload),
            revenues: this.dashboardService.getRevenue(payload),
            payment: this.dashboardService.getPaymentDistribution(payload),
            theaters: this.dashboardService.getTopTheater(payload),
            movies: this.dashboardService.getTopMovie(payload),
          }).pipe(
            catchError(() => {
              return of(null);
            }),
            finalize(() => this.isLoading.set(false)),
          );
        }),
      )
      .subscribe({
        next: (res) => {
          if (res) {
            const { metrics, revenues, payment, theaters, movies } = res;
            this.metrics.set(metrics);
            this.revenues.set(revenues);
            this.payments.set(payment);
            this.theaters.set(theaters);
            this.movies.set(movies);
          }
        },
        error: () => {
          this.isLoading.set(false);
        },
      });
  }

  getFilterPayload() {
    let start: Date;
    let end: Date;
    const currentDate = this.selectedDate || new Date();
    const startMonth = (this.selectedQuarter - 1) * 3;
    const month = currentDate.getMonth();
    const year = currentDate.getFullYear();
    const currentDay = currentDate.getDay();
    const diff = currentDay === 0 ? -6 : 1 - currentDay;
    switch (this.filterType) {
      case 'week':
        start = new Date(currentDate);
        start.setDate(currentDate.getDate() + diff);
        end = new Date(start);
        end.setDate(start.getDate() + 6);
        break;
      case 'month':
        start = new Date(year, month, 1);
        end = new Date(year, month + 1);
        break;
      case 'quarter':
        start = new Date(this.selectedQuarterYear, startMonth, 1);
        end = new Date(this.selectedQuarterYear, startMonth + 3, 0);
        break;
      case 'year':
        start = new Date(year, 0, 1);
        end = new Date(year, 11, 31);
        break;
    }
    return {
      startDate: formatDate(start),
      endDate: formatDate(end),
    };
  }

  onFilterChange() {
    const payload = this.getFilterPayload();
    this.filterSubject.next(payload);
  }
}
