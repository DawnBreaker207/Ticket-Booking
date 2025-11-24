import { Component, inject, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { TopMovie } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';

@Component({
  selector: 'app-movie-chart',
  imports: [NgxEchartsDirective],
  templateUrl: './movie-chart.component.html',
  styleUrl: './movie-chart.component.css',
})
export class MovieChartComponent implements OnInit {
  options!: EChartsCoreOption;
  topMovies: TopMovie[] = [];
  dashboardService = inject(DashboardService);

  ngOnInit() {
    this.dashboardService.getTopMovie().subscribe((data) => {
      if (data) this.topMovies = data;

      const movieName = this.topMovies.map((m) => m.movieName);
      const totalRevenue = this.topMovies.map((m) => m.revenue);
      this.loadChartData(movieName, totalRevenue);
    });
  }

  loadChartData(name: string[], data: any[]) {
    this.options = {
      tooltip: {
        trigger: 'item',
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
          data: data,
        },
      ],
    };
  }
}
