import { Component, inject, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { TopTheater } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';

@Component({
  selector: 'app-theater-chart',
  imports: [NgxEchartsDirective],
  templateUrl: './theater-chart.component.html',
  styleUrl: './theater-chart.component.css',
})
export class TheaterChartComponent implements OnInit {
  options!: EChartsCoreOption;
  topTheaters: TopTheater[] = [];
  dashboardService = inject(DashboardService);

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
