import {Component, inject, OnInit} from '@angular/core';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzTypographyComponent} from 'ng-zorro-antd/typography';
import {NzColDirective, NzGridModule, NzRowDirective} from 'ng-zorro-antd/grid';
import {NzCardComponent, NzCardModule} from 'ng-zorro-antd/card';
import {NzImageViewComponent} from 'ng-zorro-antd/experimental/image';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzImageService} from 'ng-zorro-antd/image';
import {Router} from '@angular/router';
import {on, Store} from '@ngrx/store';
import {OrderService} from '@/app/core/services/order/order.service';
import {selectUser} from '@/app/core/store/state/auth/auth.selectors';
import {filter, switchMap, take} from 'rxjs';
import {Jwt} from '@/app/core/models/jwt.model';
import {AsyncPipe} from '@angular/common';
import {selectedSchedules} from '@/app/core/store/state/schedule/schedule.selectors';
import {ScheduleActions} from '@/app/core/store/state/schedule/schedule.actions';

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
    NzGridModule,
    AsyncPipe
  ],
  providers: [NzImageService],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  private orderService = inject(OrderService);
  private store = inject(Store);
  router = inject(Router);
  schedules$ = this.store.select(selectedSchedules);
  user$ = this.store.select(selectUser);

  // modalService = inject(NzModalService);


  ngOnInit() {
    this.store.dispatch(ScheduleActions.loadSchedules());
  }

  // onShowSchedule() {
  //   this.modalService.create({
  //     nzContent: ScheduleComponent,
  //     nzFooter: null,
  //   })
  // }

  onSelect(cinemaHallId: number) {
    this.store.dispatch(ScheduleActions.loadSchedule({scheduleId: cinemaHallId}))
    this.user$.pipe(
      take(1),
      filter((jwt): jwt is Jwt => jwt !== undefined),
      switchMap(jwt => this.orderService.initOrder({orderStatus: 'CREATED', userId: jwt.userId, cinemaHallId: cinemaHallId})))
      .subscribe((res) => {
        this.router.navigateByUrl(`/reservation/${res}`);
      })
  }

  protected readonly on = on;
}
