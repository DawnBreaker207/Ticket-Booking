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
import {Router} from '@angular/router';
import {on, Store} from '@ngrx/store';
import {CinemaHall} from '@/app/core/models/cinemaHall.model';
import {OrderService} from '@/app/core/services/order/order.service';
import {selectUser} from '@/app/core/store/state/auth/auth.selectors';
import {filter, switchMap, take} from 'rxjs';
import {Jwt} from '@/app/core/models/jwt.model';

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
  private orderService = inject(OrderService);
  private store = inject(Store);
  router = inject(Router);
  movies = signal<Movie[]>([]);
  schedules = signal<CinemaHall[]>([]);
  user$ = this.store.select(selectUser);

  // modalService = inject(NzModalService);


  ngOnInit() {
    this.scheduleService.getSchedules().subscribe(data => {
      this.movies.set(data.map(hall => hall.movie));
      this.schedules.set(data);
    })
  }

  // onShowSchedule() {
  //   this.modalService.create({
  //     nzContent: ScheduleComponent,
  //     nzFooter: null,
  //   })
  // }

  onSelect() {
    this.user$.pipe(
      take(1),
      filter((jwt): jwt is Jwt => jwt !== undefined),
      switchMap(jwt => this.orderService.initOrder({orderStatus: 'CREATED', userId: jwt.userId, cinemaHallId: 7})))
      .subscribe((res) => {
        this.router.navigateByUrl(`/reservation/${res}`);
      })
  }

  protected readonly on = on;
}
