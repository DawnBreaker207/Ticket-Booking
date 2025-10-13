import {createActionGroup, props} from '@ngrx/store';
import {Reservation, ReservationFilter, ReservationRequest} from '@/app/core/models/reservation.model';


export const ReservationActions = createActionGroup({
  source: 'Reservation',
  events: {

    // // Reservation
    'Load Reservations': props<{ filter?: ReservationFilter, page?: number, size?: number }>(),
    'Load Reservations Success': props<{ reservations: Reservation[] }>(),
    'Load Reservations Failure': props<{ error: any }>(),

    'Load Reservation': props<{ id: string }>(),
    'Load Reservation Success': props<{ reservation: Reservation }>(),
    'Load Reservation Failure': props<{ error: any }>(),

    'Create Reservation': props<{ reservation: ReservationRequest }>(),
    'Create Reservation Success': props<{ reservation: Reservation }>(),
    'Create Reservation Failure': props<{ error: any }>(),

    //   Count down
    'Update Reservation TTL': props<{ reservationId: string, ttl: number }>()
  }
});
