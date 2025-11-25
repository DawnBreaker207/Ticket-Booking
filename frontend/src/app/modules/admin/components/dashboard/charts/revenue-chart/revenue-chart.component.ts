import { Component, inject, OnInit } from '@angular/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import type { EChartsCoreOption } from 'echarts/core';
import { RevenuePoint } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';
import { CurrencyFormatPipe } from '@/app/core/pipes/currency-format-pipe';

@Component({
  selector: 'app-revenue-chart',
  imports: [NgxEchartsDirective],
  templateUrl: './revenue-chart.component.html',
  styleUrl: './revenue-chart.component.css',
})
export class RevenueChartComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  private currency = inject(CurrencyFormatPipe);

  options!: EChartsCoreOption;
  revenue: RevenuePoint[] = [];

  ngOnInit() {
    this.dashboardService.getRevenue().subscribe((data) => {
      if (data) this.revenue = data;

      const date = this.revenue.map((r) => r.date.toString());
      const revenue = this.revenue.map((r) => r.revenue);
      this.loadChartData(date, revenue);
    });
  }

  loadChartData(name: string[], data: any[]) {
    this.options = {
      tooltip: {
        trigger: 'item',
        formatter: (params: any) => {
          const value = params.value ?? 0;
          return `${this.currency.transform(value)}`;
        },
      },
      xAxis: {
        type: 'category',
        data: name,
      },
      yAxis: {
        type: 'value',
      },
      series: [
        {
          type: 'line',
          data: data,
          label: {
            formatter: (params: any) =>
              this.currency.transform(params.value ?? 0),
          },
        },
      ],
    };
  }
}
