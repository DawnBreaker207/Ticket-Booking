import { Component, inject, OnInit } from '@angular/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import type { EChartsCoreOption } from 'echarts/core';
import { RevenuePoint } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';

@Component({
  selector: 'app-revenue-chart',
  imports: [NgxEchartsDirective],
  templateUrl: './revenue-chart.component.html',
  styleUrl: './revenue-chart.component.css',
})
export class RevenueChartComponent implements OnInit {
  options!: EChartsCoreOption;
  revenue: RevenuePoint[] = [];
  dashboardService = inject(DashboardService);

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
      },
      xAxis: {
        type: 'category',
        data: name,
      },
      yAxis: { type: 'value' },
      series: [
        {
          data: data,
          type: 'line',
        },
      ],
    };
  }
}
