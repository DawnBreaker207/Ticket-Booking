import {Component, inject, OnInit, signal} from '@angular/core';
import {NzStepsModule} from 'ng-zorro-antd/steps';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {DetailFilmComponent} from '@/app/modules/home/components/reservation/detail-film/detail-film.component';
import {ScheduleService} from '@/app/core/services/schedule/schedule.service';
import {CinemaHall, CinemaSeats} from '@/app/core/models/cinemaHall.model';
import {SeatComponent} from '@/app/modules/home/components/reservation/seat/seat.component';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {SummaryComponent} from '@/app/modules/home/components/reservation/summary/summary.component';
import {PaymentComponent} from '@/app/modules/home/components/reservation/payment/payment.component';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {selectUser} from '@/app/core/store/state/selectors/auth.selectors';
import {AsyncPipe} from '@angular/common';
import {Order} from '@/app/core/models/order.model';
import {OrderService} from '@/app/core/services/order/order.service';


@Component({
  selector: 'app-reservation',
  imports: [NzStepsModule, NzIconModule, NzButtonModule, DetailFilmComponent, SeatComponent, NzLayoutModule, SummaryComponent, PaymentComponent, AsyncPipe],
  templateUrl: './reservation.html',
  styleUrl: './reservation.css'
})
export class ReservationComponent implements OnInit {
  scheduleService = inject(ScheduleService);
  reservationService = inject(OrderService);
  private store = inject(Store);
  route = inject(ActivatedRoute);
  selectedSeats: CinemaSeats[] = []

  schedule!: CinemaHall
  orderId!: string
  order!: Order

  user$ = this.store.select(selectUser);

  index = signal(0);
  steps = signal([0, 1, 2]);

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.orderId = params.get('orderId')!;
    })
    this.scheduleService.getSchedule(7).subscribe(data => {
        this.schedule = data
        console.log(data)
      }
    );
  }

  onStepChange(newIndex: number) {
    if (this.index() === 0 && newIndex === 1) {
      const payload: Partial<Order> = {
        userId: 8,
        cinemaHallId: this.schedule.id,
        seats: this.selectedSeats.map(s => ({
          seatId: s.id,
          price: s.price
        }))

      }
      this.reservationService.holdSeat(payload).subscribe(res => {
        console.log(res)
        this.index.set(newIndex)
      })
      console.log(this.index())
      console.log(newIndex)
      return
    }

    if (this.index() === 1 && newIndex === 2) {
      // this.reservationService.confirm().subscribe(res => {
      //   this.index.set(newIndex);
      // })
      console.log(this.index())
      console.log(newIndex)
      return
    }
    this.index.set(newIndex);
  }

}
