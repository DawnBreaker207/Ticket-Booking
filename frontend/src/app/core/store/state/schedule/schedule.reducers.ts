import {CinemaHall, CinemaSeats} from '@/app/core/models/cinemaHall.model';
import {createReducer, on} from '@ngrx/store';
import {ScheduleActions} from '@/app/core/store/state/schedule/schedule.actions';
import {Movie} from '@/app/core/models/movie.model';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';
import {SeatStatus} from '@/app/core/constants/enum';

export const scheduleFeatureKey = 'scheduleKey';

export interface ScheduleState {
  schedules: CinemaHall[]
  selectedSchedule: CinemaHall | null
}

export const initialState: ScheduleState = {
  schedules: [],
  selectedSchedule: null
}

export const scheduleReducer = createReducer(
  initialState,
  on(ScheduleActions.loadSchedulesSuccess, (state, {schedules}) => {
    return {
      ...state,
      schedules
    }
  }),
  on(ScheduleActions.loadScheduleSuccess, (state, {schedule}) => {
    return {
      ...state,
      selectedSchedule: schedule
    }
  }),

  on(ReservationActions.toggleSeats, (state, {seat}) => {
    if (!state.selectedSchedule) return state;
    const newSeats = state.selectedSchedule.seats.map(s =>
      s.id === seat.id ? {...s, status: s.status === 'SELECTED' ? 'AVAILABLE' as SeatStatus : 'SELECTED'} : s);
    return {
      ...state, selectedSchedule: {
        ...state.selectedSchedule,
        seats: newSeats
      }
    };
  })
)
