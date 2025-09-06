import {createFeatureSelector, createSelector} from '@ngrx/store';
import {reservationFeatureKey, ReservationState} from '@/app/core/store/state/reservation/reservation.reducers';

export const selectReservationState = createFeatureSelector<ReservationState>(reservationFeatureKey);


export const selectedAllSeats = createSelector(
  selectReservationState,
  (state) => state.seats
)


export const selectedSeats = createSelector(
  selectReservationState,
  (state) => state.selectedSeats
)

export const selectedTotalPrice = createSelector(
  selectReservationState,
  (state) => state.totalPrice
)


export const selectedOrder = createSelector(
  selectReservationState,
  (state) => ({
    orderId: state.orderId,
    orderStatus: state.orderStatus,
    paymentMethod: state.paymentMethod,
    paymentStatus: state.paymentStatus,
    cinemaHallId: state.cinemaHallId,
  })
)
