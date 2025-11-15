import { Component, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';

@Component({
  selector: 'app-payment-chart',
  imports: [NgxEchartsDirective],
  templateUrl: './payment-chart.component.html',
  styleUrl: './payment-chart.component.css',
})
export class PaymentChartComponent implements OnInit {
  options!: EChartsCoreOption;

  ngOnInit() {
    this.options = {
      tooltip: {
        trigger: 'item',
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          data: [
            { value: 820, name: 'Momo' },
            { value: 932, name: 'VNPay' },
            { value: 901, name: 'ZaloPay' },
          ],
        },
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
    };
  }
}
