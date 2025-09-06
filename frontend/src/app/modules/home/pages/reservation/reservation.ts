import {Component, inject, OnInit, signal} from '@angular/core';
import {NzStepsModule} from 'ng-zorro-antd/steps';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {DetailFilmComponent} from '@/app/modules/home/components/reservation/detail-film/detail-film.component';
import {ScheduleService} from '@/app/core/services/schedule/schedule.service';
import {CinemaHall} from '@/app/core/models/cinemaHall.model';
import {SeatComponent} from '@/app/modules/home/components/reservation/seat/seat.component';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {SummaryComponent} from '@/app/modules/home/components/reservation/summary/summary.component';
import {PaymentComponent} from '@/app/modules/home/components/reservation/payment/payment.component';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {selectUser} from '@/app/core/store/state/selectors/auth.selectors';
import {AsyncPipe} from '@angular/common';
import {Order} from '@/app/core/models/order.model';
import {OrderService} from '@/app/core/services/order/order.service';
import {selectedSeats, selectedTotalPrice} from '@/app/core/store/state/selectors/reservation.selectors';
import {map} from 'rxjs';
import {PaymentService} from '@/app/core/services/payment/payment.service';
import {ReservationActions} from '@/app/core/store/state/actions/reservation.actions';


@Component({
  selector: 'app-reservation',
  imports: [NzStepsModule, NzIconModule, NzButtonModule, DetailFilmComponent, SeatComponent, NzLayoutModule, SummaryComponent, PaymentComponent, AsyncPipe],
  templateUrl: './reservation.html',
  styleUrl: './reservation.css'
})
export class ReservationComponent implements OnInit {
  scheduleService = inject(ScheduleService);
  reservationService = inject(OrderService);
  paymentService = inject(PaymentService);
  private store = inject(Store);
  route = inject(ActivatedRoute);
  router = inject(Router);
  selectedSeats$ = this.store.select(selectedSeats);
  totalPrice$ = this.store.select(selectedTotalPrice);
  totalPrice: number = 0;
  schedule: CinemaHall | null = null;
  orderId!: string
  order!: Order

  user$ = this.store.select(selectUser);

  index = signal(0);
  steps = signal([0, 1, 2]);

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.orderId = params.get('orderId')!;
      this.store.dispatch(ReservationActions.createOrder({
        order: {
          orderId: this.orderId,
          cinemaHallId: 7,
          orderStatus: 'CREATED',
          paymentMethod: 'CASH',
          paymentStatus: 'PENDING'
        }
      }))
    })
    this.scheduleService.getSchedule(7).subscribe(data => {
        this.schedule = data
      }
    );
  }

  onStepChange(newIndex: number) {
    if (newIndex == 1) {
      let payload: any;
      this.selectedSeats$.pipe(
        map((seats) => seats.map(seat => ({
          ...seat,
          seat: {
            id: seat.id
          },
          price: seat.price
        })))
      ).subscribe(data => {
          payload = {
            orderId: this.orderId,
            userId: 8,
            cinemaHallId: this.schedule?.id,
            seats: data
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
    } else if (newIndex == 2) {
      console.log(this.index())
      console.log(newIndex)
      this.index.set(newIndex)
    } else {
      this.totalPrice$.subscribe(data => this.totalPrice = data);
      this.paymentService.createPayment({orderId: this.orderId, totalPrice: this.totalPrice}).subscribe(res => {
        console.log(res)
        window.open(res.paymentUrl, '_self');
        this.index.set(newIndex);
      })
    }
  }

}
