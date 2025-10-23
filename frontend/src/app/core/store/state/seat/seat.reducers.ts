import { createReducer, on } from '@ngrx/store';
import { Seat } from '@/app/core/models/theater.model';
import { SeatActions } from '@/app/core/store/state/seat/seat.actions';
import { SeatStatus } from '@/app/core/constants/enum';

export const seatFeatureKey = 'seatKey';

export interface SeatState {
  seats: Seat[];
  selectedSeat: Seat[];
  loading: boolean;
  error: string | null;
}

export const initialState: SeatState = {
  seats: [],
  selectedSeat: [],
  loading: false,
  error: null,
};

export const seatReducer = createReducer(
  initialState,
  on(SeatActions.loadAllSeats, (state) => {
    return {
      ...state,
      loading: true,
      error: null,
    };
  }),

  on(SeatActions.loadAllSeatsSuccess, (state, { seats }) => ({
    ...state,
    seats: seats,
    loading: false,
  })),
  on(SeatActions.loadAllSeatsFailed, (state, { error }) => ({
    ...state,
    error: error,
  })),
  on(SeatActions.selectSeat, (state, { seat }) => ({
    ...state,
    seats: state.seats.map((s) =>
      s.id === seat.id ? { ...s, status: seat.status } : s,
    ),
    selectedSeat:
      seat.status === ('SELECTED' as SeatStatus)
        ? [...state.selectedSeat, seat]
        : state.selectedSeat,
    error: null,
  })),
  on(SeatActions.deselectSeat, (state, { seatId }) => ({
    ...state,
    seats: state.seats.map((s) =>
      s.id === seatId ? { ...s, status: 'AVAILABLE' as SeatStatus } : s,
    ),
    selectedSeat: state.selectedSeat.filter((s) => s.id !== seatId),
    error: null,
  })),
  on(SeatActions.clearSelectedSeat, (state) => ({
    ...state,
    selectedSeat: [],
    error: null,
  })),
);
