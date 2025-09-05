import {Component, input} from '@angular/core';
import {NzIconModule} from 'ng-zorro-antd/icon';

@Component({
  selector: 'app-summary',
  imports: [
    NzIconModule
  ],
  templateUrl: './summary.component.html',
  styleUrl: './summary.component.css'
})
export class SummaryComponent {
  user = input<any>();
}
