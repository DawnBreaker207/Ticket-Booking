import { Component, input, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { PaymentDistribution } from '@domain/dashboard/models/dashboard.model';

@Component({
  selector: 'app-payment-chart',
  imports: [NgxEchartsDirective, NzIconModule, NzButtonModule],
  templateUrl: './payment-chart.component.html',
  styleUrl: './payment-chart.component.css',
})
export class PaymentChartComponent implements OnInit {
  payments = input<PaymentDistribution[]>([]);
  options!: EChartsCoreOption;

  ngOnInit() {
    const chartData = this.payments().map((p) => ({
      name: p.method,
      value: p.amount,
    }));
    this.loadChartData(chartData);
  }

  loadChartData(data: { name: string; value: number }[]) {
    this.options = {
      tooltip: {
        trigger: 'item',
      },
      legend: { bottom: '0%', left: 'center', icon: 'circle' },
      series: [
        {
          name: 'Payment Type',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '45%'],
          itemStyle: { borderRadius: 2, borderColor: '#fff', borderWidth: 2 },
          label: { show: false },
          data: data,
        },
      ],
      emphasis: {
        label: { show: true, fontSize: 12, fontWeight: 'bold' },
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
    };
  }
}
