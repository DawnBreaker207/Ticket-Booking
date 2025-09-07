import {createActionGroup, emptyProps, props} from '@ngrx/store';
import {CinemaHall, CinemaSeats} from '@/app/core/models/cinemaHall.model';

export const ScheduleActions = createActionGroup({
  source: 'Schedule',
  events: {
    // Seats
    'Toggle Seats': props<{ seat: CinemaSeats }>(),

    'Load Schedules': emptyProps(),
    'Load Schedules Success': props<{ schedules: CinemaHall[] }>(),
    'Load Schedules Failed': props<{ error: any }>(),

    'Load Schedule': props<{ scheduleId: number }>(),
    'Load Schedule Success': props<{ schedule: CinemaHall }>(),
    'Load Schedule Failed': props<{ error: any }>(),
  }
});
