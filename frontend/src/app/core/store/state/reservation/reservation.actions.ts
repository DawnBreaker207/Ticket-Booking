import {createActionGroup, emptyProps, props} from '@ngrx/store';
import {CinemaSeats} from '@/app/core/models/cinemaHall.model';
import {Order} from '@/app/core/models/order.model';

export const ReservationActions = createActionGroup({
  source: 'Reservation',
  events: {
    // Seats
    'Toggle Seats': props<{ seat: CinemaSeats }>(),
    // Order
    'Load Order': props<{ order: Order }>(),
    'Create Order': props<{ order: Partial<Order> }>(),
    'Confirm Order': props<{ order: Partial<Order> }>(),
    'Confirm Order Success': props<{ order: Order }>(),
    'Confirm Order Failure': props<{ error: any }>(),

    //   Count down
    'Update Order TTL': props<{ orderId: string, ttl: number }>()
  }
});
