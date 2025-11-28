import { Component, inject, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { TopTheater } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';
import { CurrencyFormatPipe } from '@/app/core/pipes/currency-format-pipe';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzButtonModule } from 'ng-zorro-antd/button';

@Component({
  selector: 'app-theater-chart',
  imports: [NgxEchartsDirective, NzIconModule, NzButtonModule],
  templateUrl: './theater-chart.component.html',
  styleUrl: './theater-chart.component.css',
})
export class TheaterChartComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  private currency = inject(CurrencyFormatPipe);
  options!: EChartsCoreOption;
  topTheaters: TopTheater[] = [];

  ngOnInit() {
    this.dashboardService.getTopTheater().subscribe((data) => {
      if (data) this.topTheaters = data;

      const theaterNames = this.topTheaters.map((t) => t.theaterName);
      const totalRevenue = this.topTheaters.map((t) => t.totalRevenue);

      this.loadChartData(theaterNames, totalRevenue);
    });
  }

  loadChartData(name: string[], data: any[]) {
    this.options = {
      tooltip: {
        trigger: 'item',
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
        axisLabel: {
          interval: 0,
          rotate: 0,
          color: '#666',
        },
        axisTick: {
          show: false,
        },
        axisLine: {
          lineStyle: {
            color: '#e0e0e0',
          },
        },
      },
      yAxis: {
        type: 'value',
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
          type: 'bar',
          barWidth: '40%',
          data: data,
          itemStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                { offset: 0, color: '#ff7a45' },
                { offset: 1, color: '#ffc069' },
              ],
            },
            borderRadius: [4, 4, 0, 0],
          },
        },
      ],
    };
  }
}
