import {CinemaSeats} from '@/app/core/models/cinemaHall.model';
import {createReducer, on} from '@ngrx/store';
import {ScheduleActions} from '@/app/core/store/state/schedule/schedule.actions';
import {Movie} from '@/app/core/models/movie.model';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';
import {SeatStatus} from '@/app/core/constants/enum';

export const scheduleFeatureKey = 'scheduleKey';

export interface ScheduleState {
  cinemaHallId: number,
  seats: CinemaSeats[],
  movie: Movie | null
  movieSession: string
}

export const initialState: ScheduleState = {
  seats: [],
  cinemaHallId: 0,
  movie: null,
  movieSession: ''
}

export const scheduleReducer = createReducer(
  initialState,
  on(ScheduleActions.loadScheduleSuccess, (state, {schedule}) => {
    return {
      ...state,
      cinemaHallId: schedule.id,
      movie: schedule.movie,
      seats: schedule.seats,
      movieSession: schedule.movieSession
    }
  }),

  on(ReservationActions.toggleSeats, (state, {seat}) => {

    const newSeats = state.seats.map(s =>
      s.id === seat.id ? {...s, status: s.status === 'SELECTED' ? 'AVAILABLE' as SeatStatus : 'SELECTED'} : s);
    return {...state, seats: newSeats};
  })
)
