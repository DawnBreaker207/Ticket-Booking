import { createActionGroup, emptyProps, props } from '@ngrx/store';
import {
  Reservation,
  ReservationFilter,
  ReservationInitRequest,
  ReservationRequest,
} from '@/app/core/models/reservation.model';
import { Pagination } from '@/app/core/models/common.model';

export const ReservationActions = createActionGroup({
  source: 'Reservation',
  events: {
    // // Reservation
    'Load Reservations': props<{
      filter: Partial<ReservationFilter>;
    }>(),
    'Load Reservations Success': props<{
      reservations: Reservation[];
      pagination: Pagination;
    }>(),
    'Load Reservations Failure': props<{ error: any }>(),

    'Load Reservation': props<{ id: string }>(),
    'Load Reservation Success': props<{ reservation: Reservation }>(),
    'Load Reservation Failure': props<{ error: any }>(),

    'Create Reservation Init': props<{ reservation: ReservationInitRequest }>(),
    'Create Reservation Init Success': props<{ reservationId: string }>(),
    'Create Reservation Init Failure': props<{ error: any }>(),

    'Create Reservation Hold Seat': props<{
      reservation: ReservationRequest;
    }>(),
    'Create Reservation Hold Seat Success': emptyProps(),
    'Create Reservation Hold Seat Failure': props<{ error: any }>(),

    'Create Reservation': props<{ reservation: ReservationRequest }>(),
    'Create Reservation Success': props<{ reservation: Reservation }>(),
    'Create Reservation Failure': props<{ error: any }>(),

    'Cancel Reservation': props<{ reservationId: string; userId: number }>(),
    'Cancel Reservation Success': emptyProps(),
    'Cancel Reservation Failure': props<{ error: any }>(),

    //   Count down
    'Update Reservation TTL': props<{
      reservationId: string;
      ttl: number;
      expiredAt: Date;
    }>(),
    'Update Reservation Countdown TTL': props<{ ttl: number }>(),
  },
});
