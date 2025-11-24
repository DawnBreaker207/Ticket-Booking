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
  options!: EChartsCoreOption;
  paymentDistribution: PaymentDistribution[] = [];
  dashboardService = inject(DashboardService);

  ngOnInit() {
    this.dashboardService.getPaymentDistribution().subscribe((data) => {
      if (data) this.paymentDistribution = data;
      const methodName = this.paymentDistribution.map((p) => p.method);
      const totalAmount = this.paymentDistribution.map((p) => p.amount);
      this.loadChartData(methodName, totalAmount);
    });
  }

  loadChartData(name: string[], data: any[]) {
    this.options = {
      tooltip: {
        trigger: 'item',
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          data: [{ value: data, name: name }],
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
