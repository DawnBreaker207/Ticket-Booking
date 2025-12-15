import { Component, input, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { TopTheater } from '@domain/dashboard/models/dashboard.model';

@Component({
  selector: 'app-theater-chart',
  imports: [NgxEchartsDirective, NzIconModule, NzButtonModule],
  templateUrl: './theater-chart.component.html',
  styleUrl: './theater-chart.component.css',
})
export class TheaterChartComponent implements OnInit {
  theaters = input<TopTheater[]>([]);
  options!: EChartsCoreOption;

  ngOnInit() {
    const { theaterName, totalRevenue } = this.theaters().reduce(
      (acc, t) => {
        acc.theaterName.push(t.theaterName);
        acc.totalRevenue.push(t.totalRevenue);
        return acc;
      },
      {
        theaterName: [] as string[],
        totalRevenue: [] as number[],
      },
    );

    this.loadChartData(theaterName, totalRevenue);
  }

  loadChartData(name: string[], data: number[]) {
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
