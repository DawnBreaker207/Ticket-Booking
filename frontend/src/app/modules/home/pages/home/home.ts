import {Component, inject, OnInit, signal} from '@angular/core';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzTypographyComponent} from 'ng-zorro-antd/typography';
import {NzColDirective, NzGridModule, NzRowDirective} from 'ng-zorro-antd/grid';
import {NzCardComponent, NzCardModule} from 'ng-zorro-antd/card';
import {NzImageViewComponent} from 'ng-zorro-antd/experimental/image';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzImageService} from 'ng-zorro-antd/image';
import {ScheduleService} from '@/app/core/services/schedule/schedule.service';
import {Movie} from '@/app/core/models/movie.model';

@Component({
  selector: 'app-home',
  imports: [
    NzLayoutModule,
    NzTypographyComponent,
    NzRowDirective,
    NzColDirective,
    NzCardComponent,
    NzImageViewComponent,
    NzButtonComponent,
    NzCardModule,
    NzGridModule
  ],
  providers: [NzImageService],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  private scheduleService = inject(ScheduleService);
  movies = signal<Movie[]>([]);


  ngOnInit() {
    this.scheduleService.getSchedules().subscribe(data => {
      this.movies.set(data.map(hall => hall.movie));
    })
  }

}
