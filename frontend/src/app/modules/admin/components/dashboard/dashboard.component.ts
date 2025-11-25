import { Component, inject, OnInit } from '@angular/core';
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

  ngOnInit() {
    this.dashboardService.getMetrics().subscribe((data) => {
      if (data) {
        this.metrics = data;
      }
    });
  }
}
