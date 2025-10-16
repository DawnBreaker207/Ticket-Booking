import { createActionGroup, emptyProps, props } from '@ngrx/store';
import {
  Seat,
  Showtime,
  ShowtimeRequest,
} from '@/app/core/models/theater.model';

export const ShowtimeActions = createActionGroup({
  source: 'Showtime',
  events: {
    // Load all
    'Load Showtimes By Movie Id': props<{ movieId: number }>(),
    'Load Showtimes By Movie Id Success': props<{ showtimes: Showtime[] }>(),
    'Load Showtimes By Movie Id Failed': props<{ error: any }>(),

    'Load Showtimes By Theater Id': props<{ theaterId: number }>(),
    'Load Showtimes By Theater Id Success': props<{ showtimes: Showtime[] }>(),
    'Load Showtimes By Theater Id Failed': props<{ error: any }>(),

    // Load simple
    'Load Showtime': props<{ id: number }>(),
    'Load Showtime Success': props<{ showtime: Showtime }>(),
    'Load Showtime Failed': props<{ error: any }>(),

    // Create
    'Create Showtime': props<{ showtime: ShowtimeRequest }>(),
    'Create Showtime Success': props<{ showtime: Showtime }>(),
    'Create Showtime Failed': props<{ error: any }>(),

    // Update
    'Update Showtime': props<{ id: number; showtime: ShowtimeRequest }>(),
    'Update Showtime Success': props<{ showtime: Showtime }>(),
    'Update Showtime Failed': props<{ error: any }>(),

    // Delete
    'Delete Showtime': props<{ id: number }>(),
    'Delete Showtime Success': props<{ id: number }>(),
    'Delete Showtime Failed': props<{ error: any }>(),

    //   UI Actions
    'Selected Showtime': props<{ showtime: Showtime | null }>(),
    'Clear Error': emptyProps(),

    // Seats
    'Load Seats': props<{ showtimeId: number }>(),
    'Load Seats Success': props<{ seats: Seat[] }>(),
    'Load Seats Failed': props<{ error: any }>(),

    'Clear Showtime': emptyProps(),
    'Clear Seats': emptyProps(),
  },
});
