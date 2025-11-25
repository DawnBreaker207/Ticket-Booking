import { Component, inject, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { TopMovie } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';
import { CurrencyFormatPipe } from '@/app/core/pipes/currency-format-pipe';

@Component({
  selector: 'app-movie-chart',
  imports: [NgxEchartsDirective],
  templateUrl: './movie-chart.component.html',
  styleUrl: './movie-chart.component.css',
})
export class MovieChartComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  private currency = inject(CurrencyFormatPipe);
  options!: EChartsCoreOption;
  topMovies: TopMovie[] = [];

  ngOnInit() {
    this.dashboardService.getTopMovie().subscribe((data) => {
      if (data) this.topMovies = data;

      const movieName = this.topMovies.map((m) => m.movieName);
      const totalRevenue = this.topMovies.map((m) => m.revenue);
      this.loadChartData(movieName, totalRevenue);
    });
  }

  loadChartData(name: string[], data: any[]) {
    const colors = ['#5470C6', '#91CC75', '#FAC858', '#EE6666', '#73C0DE'];
    this.options = {
      tooltip: {
        trigger: 'item',
        formatter: (params: any) => {
          const value = params.value ?? 0;
          return `${this.currency.transform(value)}`;
        },
      },
      xAxis: {
        type: 'value',
      },
      yAxis: {
        type: 'category',
        data: name,
      },
      series: [
        {
          type: 'bar',
          data: data.map((val, idx) => ({
            value: val,
            itemStyle: {
              color: colors[idx % colors.length],
            },
          })),
        },
      ],
    };
  }
}
