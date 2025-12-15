import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Seat } from '@domain/seat/models/seat.model';

export const SeatActions = createActionGroup({
  source: 'Seat',
  events: {
    //   Count down
    'Load All Seats': props<{ showtimeId: number }>(),
    'Load All Seats Success': props<{ seats: Seat[] }>(),
    'Load All Seats Failed': props<{ error: any }>(),

    'Select Seat': props<{ seat: Seat }>(),
    'Deselect Seat': props<{ seatId: number }>(),
    'Clear Selected Seat': emptyProps(),
  },
});
