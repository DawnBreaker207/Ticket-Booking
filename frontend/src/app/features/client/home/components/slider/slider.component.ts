import { Component } from '@angular/core';
import { NzCarouselModule } from 'ng-zorro-antd/carousel';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';

@Component({
  selector: 'app-slider',
  imports: [NzCarouselModule, NzButtonModule, NzIconModule],
  templateUrl: './slider.component.html',
  styleUrl: './slider.component.css',
})
export class SliderComponent {
  banners = [
    {
      title: 'Dune: Part Two',
      desc: 'The saga continues as Paul Atreides unites with Chani and the Fremen while on a warpath of revenge.',
      image:
        'https://image.tmdb.org/t/p/original/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg',
    },
    {
      title: 'Kung Fu Panda 4',
      desc: 'Po is gearing up to become the spiritual leader of his Valley of Peace.',
      image:
        'https://image.tmdb.org/t/p/original/kYgQzzjNis5jJalYtIHgrom0gOx.jpg',
    },
  ];
}
