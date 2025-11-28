import { Component, inject, OnInit } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { PaymentDistribution } from '@/app/core/models/dashboard.model';
import { DashboardService } from '@/app/core/services/dashboard/dashboard.service';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';

@Component({
  selector: 'app-payment-chart',
  imports: [NgxEchartsDirective, NzIconModule, NzButtonModule],
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
