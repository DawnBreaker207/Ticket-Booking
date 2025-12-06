import { Component } from '@angular/core';
import { NzTableModule } from 'ng-zorro-antd/table';

@Component({
  selector: 'app-theater-table',
  imports: [NzTableModule],
  templateUrl: './theater-table.component.html',
  styleUrl: './theater-table.component.css',
})
export class TheaterTableComponent {
  data: any[] = [
    {
      movie: 'Movie A',
      showtime: 3499,
      ticket: 9394,
    },
    {
      movie: 'Movie B',
      showtime: 3499,
      ticket: 9394,
    },
    {
      movie: 'Movie C',
      showtime: 3499,
      ticket: 9394,
    },
    {
      movie: 'Movie D',
      showtime: 3499,
      ticket: 9394,
    },
    {
      movie: 'Movie E',
      showtime: 3499,
      ticket: 9394,
    },
  ];
}
