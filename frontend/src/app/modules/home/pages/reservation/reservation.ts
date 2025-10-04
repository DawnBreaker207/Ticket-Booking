import {Component, inject, OnInit, signal} from '@angular/core';
import {NzStepsModule} from 'ng-zorro-antd/steps';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {DetailFilmComponent} from '@/app/modules/home/components/reservation/detail-film/detail-film.component';
import {SeatComponent} from '@/app/modules/home/components/reservation/seat/seat.component';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {SummaryComponent} from '@/app/modules/home/components/reservation/summary/summary.component';
import {PaymentComponent} from '@/app/modules/home/components/reservation/payment/payment.component';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {selectUser} from '@/app/core/store/state/auth/auth.selectors';
import {AsyncPipe} from '@angular/common';
import {Order} from '@/app/core/models/order.model';
import {OrderService} from '@/app/core/services/order/order.service';
import {combineLatest, filter, map, take, tap} from 'rxjs';
import {PaymentService} from '@/app/core/services/payment/payment.service';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';
import {selectedSchedule, selectedSeats, selectedTotalPrice} from '@/app/core/store/state/schedule/schedule.selectors';
import {CinemaHall} from '@/app/core/models/cinemaHall.model';
import {OrderStatus, PaymentMethod, PaymentStatus} from '@/app/core/constants/enum';


@Component({
  selector: 'app-reservation',
  imports: [NzStepsModule, NzIconModule, NzButtonModule, DetailFilmComponent, SeatComponent, NzLayoutModule, SummaryComponent, PaymentComponent, AsyncPipe],
  templateUrl: './reservation.html',
  styleUrl: './reservation.css'
})
export class ReservationComponent implements OnInit {
  reservationService = inject(OrderService);
  paymentService = inject(PaymentService);
  private store = inject(Store);
  route = inject(ActivatedRoute);
  router = inject(Router);
  selectedSeats$ = this.store.select(selectedSeats);
  totalPrice$ = this.store.select(selectedTotalPrice);
  totalPrice: number = 0;
  schedule$ = this.store.select(selectedSchedule);
  orderId!: string
  order!: Order

  user$ = this.store.select(selectUser);

  index = signal(0);
  steps = signal([0, 1, 2]);

  ngOnInit() {
    combineLatest([
      this.route.paramMap.pipe(
        map(params => params.get('orderId') as string),
        tap(orderId => this.orderId = orderId)
      ),
      this.user$.pipe(
        take(1)),
      this.schedule$.pipe(
        filter((s): s is CinemaHall => s != undefined),
        take(1)
      )
    ]).subscribe(([orderId, user, schedule]) => {
      console.log(orderId, user, schedule)
      this.store.dispatch(ReservationActions.createOrder({
          order: {
            orderId: orderId,
            userId: user?.userId,
            cinemaHallId: schedule.id,
            orderStatus: 'CREATED' as OrderStatus,
            paymentMethod: 'CASH' as PaymentMethod,
            paymentStatus: 'PENDING' as PaymentStatus
          }
        })
      )
      // this.store.dispatch(ScheduleActions.loadSchedule({scheduleId: schedule.id}))
    })
  }

  onStepChange(newIndex: number) {
    console.log(newIndex, this.index());
    if (newIndex === 1 && this.index() === 0) {
      console.log("Hold seat")
      let payload: any;
      combineLatest([this.selectedSeats$, this.schedule$]).pipe(
        take(1),
        map(([seats, schedule]) =>
          ({
            schedule: schedule,
            seats: seats.map(seat => ({
              seat: {
                id: seat.id
              },
              price: seat.price
            }))
          })
        )).subscribe(({schedule, seats}) => {
          if (!schedule) return;

          payload = {
            orderId: this.orderId,
            userId: 8,
            cinemaHallId: schedule.id,
            seats: seats
          }

          console.log(payload)
        }
      )
      this.reservationService.holdSeat(payload).subscribe(res => {
        console.log(res)
        this.index.set(newIndex)
      })
      console.log(this.index())
      console.log(newIndex)
      return
    }
    if (newIndex === 3 && this.index() === 2) {
      console.log("Payment")
      this.totalPrice$.subscribe(data => this.totalPrice = data);
      this.paymentService.createPayment({orderId: this.orderId, totalPrice: this.totalPrice}).subscribe(res => {
        console.log(res)
        window.open(res.paymentUrl, '_self');
        this.index.set(newIndex);
      })
    } else {
      this.index.set(newIndex);
    }
  }

}
