import { Component, input } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';
import { NzIconModule } from 'ng-zorro-antd/icon';

@Component({
  selector: 'app-dashboard-item',
  imports: [LucideAngularModule, NzIconModule],
  templateUrl: './dashboard-item.component.html',
  styleUrl: './dashboard-item.component.css',
})
export class DashboardItemComponent {
  title = input<string>('');
  value = input<string>('');
  icon = input<string>('');
}
