import { Component, inject, OnInit, signal } from '@angular/core';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSwitchModule } from 'ng-zorro-antd/switch';
import { NzColDirective, NzRowDirective } from 'ng-zorro-antd/grid';
import { NzCardComponent } from 'ng-zorro-antd/card';
import { RevenueChartComponent } from '@/app/modules/admin/components/dashboard/charts/revenue-chart/revenue-chart.component';
import { MovieChartComponent } from '@/app/modules/admin/components/dashboard/charts/movie-chart/movie-chart.component';
import { TheaterChartComponent } from '@/app/modules/admin/components/dashboard/charts/theater-chart/theater-chart.component';
import { PaymentChartComponent } from '@/app/modules/admin/components/dashboard/charts/payment-chart/payment-chart.component';
import { NzIconModule } from 'ng-zorro-antd/icon';
import {
  Armchair,
  DollarSign,
  LucideAngularModule,
  Theater,
  Tickets,
} from 'lucide-angular';
import { TheaterTableComponent } from '@/app/modules/admin/components/dashboard/tables/theater-table/theater-table.component';
import { MovieTableComponent } from '@/app/modules/admin/components/dashboard/tables/movie-table/movie-table.component';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';
import { DashboardMetrics } from '@/app/core/models/dashboard.model';
import { CurrencyFormatPipe } from '@/app/core/pipes/currency-format-pipe';
import { FormsModule } from '@angular/forms';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzDatePickerComponent } from 'ng-zorro-antd/date-picker';
import { NzSpinModule } from 'ng-zorro-antd/spin';

@Component({
  selector: 'app-dashboard',
  imports: [
    NzLayoutModule,
    NzSelectModule,
    NzSwitchModule,
    NzRowDirective,
    NzColDirective,
    NzCardComponent,
    RevenueChartComponent,
    MovieChartComponent,
    TheaterChartComponent,
    PaymentChartComponent,
    NzIconModule,
    LucideAngularModule,
    TheaterTableComponent,
    MovieTableComponent,
    CurrencyFormatPipe,
    FormsModule,
    NzButtonComponent,
    NzDatePickerComponent,
    NzSpinModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  dashboardService = inject(DashboardService);
  readonly DollarSign = DollarSign;
  readonly Tickets = Tickets;
  readonly Theater = Theater;
  readonly Armchair = Armchair;
  metrics!: DashboardMetrics;
  isLoading = signal<boolean>(true);
  filterType: 'day' | 'month' | 'quarter' | 'year' = 'day';

  selectedDate: Date = new Date();
  selectedQuarterYear: number = new Date().getFullYear();
  selectedQuarter: number = Math.floor((new Date().getMonth() + 3) / 3);

  quarters = [
    { label: 'Quý 1 (Q1)', value: 1 },
    { label: 'Quý 2 (Q2)', value: 2 },
    { label: 'Quý 3 (Q3)', value: 3 },
    { label: 'Quý 4 (Q4)', value: 4 },
  ];

  years = Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - i);

  ngOnInit() {
    this.dashboardService.getMetrics().subscribe({
      next: (data) => {
        if (data) {
          this.isLoading.set(false);
          this.metrics = data;
        }
      },
      error: () => {
        this.isLoading.set(true);
      },
    });
  }

  onFilterChange() {
    let payload = {};

    switch (this.filterType) {
      case 'day':
        payload = { type: 'day', date: this.selectedDate };
        break;
      case 'month':
        payload = { type: 'month', date: this.selectedDate };
        break;
      case 'quarter':
        payload = {
          type: 'quarter',
          year: this.selectedQuarterYear,
          quarter: this.selectedQuarter,
        };
        break;
      case 'year':
        payload = { type: 'year', year: this.selectedDate.getFullYear() };
        break;
    }
  }
}
