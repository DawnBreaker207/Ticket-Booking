import { Component } from '@angular/core';
import {
  NzTableCellDirective,
  NzTableComponent,
  NzTbodyComponent,
  NzTheadComponent,
  NzThMeasureDirective,
  NzTrDirective,
} from 'ng-zorro-antd/table';

@Component({
  selector: 'app-movie-table',
  imports: [
    NzTableCellDirective,
    NzTableComponent,
    NzTbodyComponent,
    NzThMeasureDirective,
    NzTheadComponent,
    NzTrDirective,
  ],
  templateUrl: './movie-table.component.html',
  styleUrl: './movie-table.component.css',
})
export class MovieTableComponent {
  data: any[] = [
    {
      theater: 'Theater A',
      tickets: 9394,
      revenue: 3499,
      seat: 65.4,
      cancel: 39.4,
    },
    {
      theater: 'Theater B',
      tickets: 9394,
      revenue: 3499,
      seat: 65.4,
      cancel: 39.4,
    },
    {
      theater: 'Theater C',
      tickets: 9394,
      revenue: 3499,
      seat: 65.4,
      cancel: 39.4,
    },
    {
      theater: 'Theater D',
      tickets: 9394,
      revenue: 3499,
      seat: 65.4,
      cancel: 39.4,
    },
    {
      theater: 'Theater E',
      tickets: 9394,
      revenue: 3499,
      seat: 65.4,
      cancel: 39.4,
    },
  ];
}
