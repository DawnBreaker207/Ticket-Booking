import {createFeatureSelector, createSelector} from '@ngrx/store';
import {scheduleFeatureKey, ScheduleState} from '@/app/core/store/state/schedule/schedule.reducers';
import {CinemaHall, CinemaSeats} from '@/app/core/models/cinemaHall.model';

export const selectScheduleState = createFeatureSelector<ScheduleState>(scheduleFeatureKey);


export const selectedAllSeats = createSelector(
  selectScheduleState,
  (state): CinemaSeats[] => {
    return state.selectedSchedule?.seats ?? [];
  }
)

export const selectedSeats = createSelector(
  selectScheduleState,
  (state): CinemaSeats[] => {
    return state.selectedSchedule?.seats.filter(s => s.status === 'SELECTED') ?? [];
  }
)

export const selectedTotalPrice = createSelector(
  selectedSeats,
  (state) => {
    return state.reduce((sum, s) => sum + s.price, 0)
  }
)

export const selectedSchedules = createSelector(
  selectScheduleState,
  (state) => state.schedules.map(hall => ({
    movie: hall.movie,
    cinemaHallId: hall.id
  }))
);

export const selectedSchedule = createSelector(
  selectScheduleState,
  (state): CinemaHall | null => {
    if (!state.selectedSchedule?.movie || !state.selectedSchedule?.id) {
      return null;
    }
    return {
      id: state.selectedSchedule.id,
      movie: state.selectedSchedule.movie,
      seats: state.selectedSchedule.seats,
      movieSession: state.selectedSchedule.movieSession
    };
  }
);

