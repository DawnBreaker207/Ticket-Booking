import { Component, input, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { TopMovie } from '@/app/core/models/dashboard.model';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';

@Component({
  selector: 'app-movie-chart',
  imports: [NgxEchartsDirective, NzIconModule, NzButtonModule],
  templateUrl: './movie-chart.component.html',
  styleUrl: './movie-chart.component.css',
})
export class MovieChartComponent implements OnInit {
  movies = input<TopMovie[]>([]);
  options!: EChartsCoreOption;

  ngOnInit() {
    const { movieName, totalRevenue } = this.movies().reduce(
      (acc, m) => {
        acc.movieName.push(m.movieName);
        acc.totalRevenue.push(m.revenue);
        return acc;
      },
      {
        movieName: [] as string[],
        totalRevenue: [] as number[],
      },
    );

    this.loadChartData(movieName, totalRevenue);
  }

  loadChartData(name: string[], data: number[]) {
    this.options = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
      },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: {
        type: 'value',
        boundaryGap: [0, 0.01],
        splitLine: { lineStyle: { type: 'dashes', color: '#f0f0f0' } },
      },
      yAxis: {
        type: 'category',
        data: name,
        axisLine: { show: false },
        axisTick: { show: false },
      },
      series: [
        {
          name: 'Revenue',
          type: 'bar',
          data: data,
          itemStyle: {
            color: '#1890ff',
            borderRadius: [0, 4, 4, 0],
          },
          barWidth: 20,
        },
      ],
    };
  }
}
