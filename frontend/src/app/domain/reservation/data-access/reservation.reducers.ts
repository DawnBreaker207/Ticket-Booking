import { createReducer, on } from '@ngrx/store';
import { Reservation } from '@domain/reservation/models/reservation.model';
import { Pagination } from '@core/models/common.model';
import { ReservationActions } from '@domain/reservation/data-access/reservation.actions';

export const reservationFeatureKey = 'reservationKey';

export interface ReservationState {
  reservations: Reservation[];
  reservation: Reservation | null;
  pagination: Pagination | null;
  reservationId: string | null;
  userId: number | null;
  loading: boolean;
  currentTTL: {
    reservationId: string;
    ttl: number;
    expiredAt: Date;
  } | null;
  saving: boolean;
  error: string | null;
}

export const initialState: ReservationState = {
  reservations: [],
  reservation: null,
  pagination: null,
  reservationId: null,
  userId: null,
  currentTTL: null,
  loading: false,
  saving: false,
  error: null,
};

export const reservationReducer = createReducer(
  initialState,
  on(ReservationActions.createReservationInit, (state) => {
    return {
      ...state,
      loading: true,
      saving: true,
      error: null,
    };
  }),

  on(
    ReservationActions.createReservationInitSuccess,
    (state, { reservationId }) => ({
      ...state,
      reservationId: reservationId,
      currentTTL: {
        reservationId: reservationId,
        ttl: 0,
        expiredAt: new Date(),
      },
      saving: false,
      loading: false,
    }),
  ),

  on(ReservationActions.createReservationInitFailure, (state, { error }) => ({
    ...state,
    saving: false,
    error: error,
  })),

  on(ReservationActions.createReservationHoldSeat, (state) => {
    return {
      ...state,
      loading: true,
      saving: true,
      error: null,
    };
  }),

  on(ReservationActions.createReservationHoldSeatSuccess, (state) => ({
    ...state,
    saving: false,
    loading: false,
  })),

  on(
    ReservationActions.createReservationHoldSeatFailure,
    (state, { error }) => ({
      ...state,
      saving: false,
      error: error,
    }),
  ),

  on(ReservationActions.createReservation, (state) => {
    return {
      ...state,
      loading: true,
      saving: true,
      error: null,
    };
  }),

  on(ReservationActions.createReservationSuccess, (state, { reservation }) => ({
    ...state,
    reservation: reservation,
    saving: false,
    loading: false,
  })),

  on(ReservationActions.createReservationFailure, (state, { error }) => ({
    ...state,
    saving: false,
    error: error,
  })),

  on(
    ReservationActions.cancelReservation,
    (state, { reservationId, userId }) => {
      return {
        ...state,
        reservationId: reservationId,
        userId: userId,
        loading: true,
        saving: true,
        error: null,
      };
    },
  ),

  on(ReservationActions.cancelReservationSuccess, (state) => ({
    ...state,
    saving: false,
    loading: false,
  })),

  on(ReservationActions.cancelReservationFailure, (state, { error }) => ({
    ...state,
    saving: false,
    error: error,
  })),

  on(ReservationActions.loadReservations, (state) => {
    return {
      ...state,
      loading: true,
      error: null,
    };
  }),

  on(
    ReservationActions.loadReservationsSuccess,
    (state, { reservations, pagination }) => {
      return {
        ...state,
        reservations: reservations,
        pagination: pagination,
        loading: false,
      };
    },
  ),

  on(ReservationActions.loadReservationsFailure, (state, { error }) => {
    return {
      ...state,
      loading: false,
      error: error,
    };
  }),
  on(ReservationActions.loadReservation, (state) => {
    return {
      ...state,
      loading: true,
      error: null,
    };
  }),

  on(ReservationActions.loadReservationSuccess, (state, { reservation }) => {
    return {
      ...state,
      reservation: reservation,
      loading: false,
    };
  }),

  on(ReservationActions.loadReservationFailure, (state, { error }) => {
    return {
      ...state,
      loading: false,
      error: error,
    };
  }),

  on(
    ReservationActions.updateReservationTTL,
    (state, { reservationId, ttl, expiredAt }) => {
      return {
        ...state,
        loading: false,
        currentTTL:
          !state.currentTTL || state.currentTTL?.reservationId !== reservationId
            ? {
                reservationId: reservationId,
                ttl: ttl,
                expiredAt: new Date(expiredAt),
              }
            : { ...state.currentTTL, ttl: ttl, expiredAt: new Date(expiredAt) },
        error: null,
      };
    },
  ),

  on(ReservationActions.updateReservationCountdownTTL, (state, { ttl }) => ({
    ...state,
    currentTTL: state.currentTTL
      ? {
          ...state.currentTTL,
          ttl: ttl,
        }
      : null,
    error: null,
  })),
);
