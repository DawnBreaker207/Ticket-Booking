import { Component, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';

@Component({
  selector: 'app-theater-chart',
  imports: [NgxEchartsDirective],
  templateUrl: './theater-chart.component.html',
  styleUrl: './theater-chart.component.css',
})
export class TheaterChartComponent implements OnInit {
  options!: EChartsCoreOption;

  ngOnInit() {
    this.options = {
      tooltip: {
        trigger: 'item',
      },
      xAxis: {
        type: 'value',
      },
      yAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      },
      series: [
        {
          type: 'bar',
          data: [820, 932, 901, 934, 1290, 1330, 1320],
        },
      ],
    };
  }
}
