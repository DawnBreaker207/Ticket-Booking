import { Component, inject, OnInit } from '@angular/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import type { EChartsCoreOption } from 'echarts/core';
import { RevenuePoint } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';
import { CurrencyFormatPipe } from '@/app/core/pipes/currency-format-pipe';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';

@Component({
  selector: 'app-revenue-chart',
  imports: [NgxEchartsDirective, NzIconModule, NzButtonModule],
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
        trigger: 'axis',
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
        top: '10%',
      },
      xAxis: {
        type: 'category',
        data: name,
        axisLine: {
          lineStyle: {
            color: '#e0e0e0',
          },
        },
        axisLabel: {
          color: '#666',
        },
      },
      yAxis: {
        type: 'value',
        axisLine: {
          show: false,
        },
        splitLine: {
          lineStyle: {
            type: 'dashed',
            color: '#f0f0f0',
          },
        },
      },
      series: [
        {
          name: 'Revenue',
          type: 'line',
          data: data,
          label: {
            formatter: (params: any) =>
              this.currency.transform(params.value ?? 0),
          },
          smooth: true,
          showSymbol: false,
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: 'rgba(24,144,255,0.2)',
                },
                {
                  offset: 1,
                  color: 'rgba(24,144,255,0)',
                },
              ],
            },
          },
          itemStyle: {
            color: '#1890ff',
          },
          lineStyle: {
            width: 3,
          },
        },
      ],
    };
  }
}
