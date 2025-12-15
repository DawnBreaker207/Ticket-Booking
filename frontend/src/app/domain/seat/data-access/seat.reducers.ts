import { createReducer, on } from '@ngrx/store';
import { Seat } from '@domain/seat/models/seat.model';
import { SeatActions } from '@domain/seat/data-access/seat.actions';
import { SeatStatus } from '@core/constants/enum';

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
  on(SeatActions.selectSeat, (state, { seat }) => {
    const updatedSeats = state.seats.map((s) =>
      s.id === seat.id ? { ...s, status: seat.status } : s,
    );

    const updatedSelectedSeat =
      seat.status === 'SELECTED'
        ? [...state.selectedSeat.filter((s) => s.id !== seat.id), seat]
        : state.selectedSeat.filter((s) => s.id !== seat.id);

    return {
      ...state,
      seats: updatedSeats,
      selectedSeat: updatedSelectedSeat,
      error: null,
    };
  }),
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
