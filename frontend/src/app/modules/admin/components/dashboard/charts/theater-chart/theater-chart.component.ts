import { Component, inject, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { TopTheater } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';
import { CurrencyFormatPipe } from '@/app/core/pipes/currency-format-pipe';

@Component({
  selector: 'app-theater-chart',
  imports: [NgxEchartsDirective],
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
    const colors = ['#5470C6', '#91CC75', '#FAC858', '#EE6666', '#73C0DE'];
    this.options = {
      tooltip: {
        trigger: 'item',
        formatter: (params: any) => {
          const value = params.value ?? 0;
          return `${params.name}:${this.currency.transform(value)}`;
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
