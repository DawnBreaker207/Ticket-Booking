import { Component, input, OnInit } from '@angular/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import type { EChartsCoreOption } from 'echarts/core';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { RevenuePoint } from '@domain/dashboard/models/dashboard.model';

@Component({
  selector: 'app-revenue-chart',
  imports: [NgxEchartsDirective, NzIconModule, NzButtonModule],
  templateUrl: './revenue-chart.component.html',
  styleUrl: './revenue-chart.component.css',
})
export class RevenueChartComponent implements OnInit {
  revenues = input<RevenuePoint[]>([]);
  options!: EChartsCoreOption;

  ngOnInit() {
    const { date, revenue } = this.revenues().reduce(
      (acc, r) => {
        acc.date.push(r.date.toString());
        acc.revenue.push(r.revenue);
        return acc;
      },
      {
        date: [] as string[],
        revenue: [] as number[],
      },
    );
    this.loadChartData(date, revenue);
  }

  loadChartData(name: string[], data: number[]) {
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
