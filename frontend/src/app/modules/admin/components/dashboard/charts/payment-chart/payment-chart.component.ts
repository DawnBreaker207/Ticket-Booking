import { Component, inject, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { PaymentDistribution } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';

@Component({
  selector: 'app-payment-chart',
  imports: [NgxEchartsDirective],
  templateUrl: './payment-chart.component.html',
  styleUrl: './payment-chart.component.css',
})
export class PaymentChartComponent implements OnInit {
  dashboardService = inject(DashboardService);
  options!: EChartsCoreOption;
  paymentDistribution: PaymentDistribution[] = [];

  ngOnInit() {
    this.dashboardService.getPaymentDistribution().subscribe((data) => {
      if (data) this.paymentDistribution = data;

      const chartData = this.paymentDistribution.map((p) => ({
        name: p.method,
        value: p.amount,
      }));

      this.loadChartData(chartData);
    });
  }

  loadChartData(data: any[]) {
    this.options = {
      tooltip: {
        trigger: 'item',
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          data: data,
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
