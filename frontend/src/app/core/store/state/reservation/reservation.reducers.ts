import {CinemaSeats} from '@/app/core/models/cinemaHall.model';
import {createReducer, on} from '@ngrx/store';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';
import {OrderStatus, PaymentMethod, PaymentStatus, SeatStatus} from '@/app/core/constants/enum';


export const reservationFeatureKey = 'reservationKey';

export interface ReservationState {
  orderId: string,
  orderStatus: OrderStatus,
  paymentMethod: PaymentMethod,
  paymentStatus: PaymentStatus,
  cinemaHallId: number
}

export const initialState: ReservationState = {
  orderId: '',
  orderStatus: JSON.parse(localStorage.getItem('orderStatus') || 'null') || 'CREATED',
  paymentMethod: JSON.parse(localStorage.getItem('paymentMethod') || 'null') || 'CASH',
  paymentStatus: JSON.parse(localStorage.getItem('paymentStatus') || 'null') || 'PENDING',
  cinemaHallId: JSON.parse(localStorage.getItem('cinemaHallId') || 'null') || 7,
}

export const reservationReducer = createReducer(
  initialState,
  on(ReservationActions.loadOrder, (state, {order}) => {
    return {...state, order}
  }),

  on(ReservationActions.createOrder, (state, {order}) => {
    return {
      ...state,
      orderId: order.orderId ?? state.orderId,
      orderStatus: order.orderStatus ?? state.orderStatus,
      paymentMethod: order.paymentMethod ?? state.paymentMethod,
      paymentStatus: order.paymentStatus ?? state.paymentStatus,
      cinemaHallId: order.cinemaHallId ?? state.cinemaHallId
    }
  }),

  on(ReservationActions.confirmOrder, (state, {order}) => {
    return {
      ...state,
      orderId: order.orderId ?? state.orderId,
      orderStatus: order.orderStatus ?? state.orderStatus,
      paymentMethod: order.paymentMethod ?? state.paymentMethod,
      paymentStatus: order.paymentStatus ?? state.paymentStatus,
      cinemaHallId: order.cinemaHallId ?? state.cinemaHallId
    }
  }),
  on(ReservationActions.confirmOrderSuccess, () => {
    localStorage.removeItem('cinemaHallId');
    localStorage.removeItem('orderStatus');
    localStorage.removeItem('paymentMethod');
    localStorage.removeItem('paymentStatus');
    return initialState;
  })
)
