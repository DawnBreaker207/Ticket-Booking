import {createFeatureSelector, createSelector} from '@ngrx/store';
import {scheduleFeatureKey, ScheduleState} from '@/app/core/store/state/schedule/schedule.reducers';
import {CinemaHall} from '@/app/core/models/cinemaHall.model';

export const selectScheduleState = createFeatureSelector<ScheduleState>(scheduleFeatureKey);


export const selectedAllSeats = createSelector(
  selectScheduleState,
  (state) => state.seats
)

export const selectedSeats = createSelector(
  selectScheduleState,
  (state) => state.seats.filter(s => s.status === 'SELECTED')
)

export const selectedTotalPrice = createSelector(
  selectedSeats,
  (state) => state.reduce((sum, s) => sum + s.price, 0)
)


export const selectedSchedule = createSelector(
  selectScheduleState,
  (state): CinemaHall | null => {
    if (!state.movie || !state.cinemaHallId) {
      return null;
    }
    return {
      id: state.cinemaHallId,
      movie: state.movie,
      seats: state.seats,
      movieSession: state.movieSession
    };
  }
);

