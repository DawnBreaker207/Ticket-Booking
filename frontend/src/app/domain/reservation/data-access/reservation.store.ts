import {
  Reservation,
  ReservationFilter,
  ReservationInitRequest,
  ReservationProfile,
  ReservationRequest,
} from '@domain/reservation/models/reservation.model';
import { Pagination } from '@core/models/common.model';
import {
  patchState,
  signalStore,
  withComputed,
  withHooks,
  withMethods,
  withState,
} from '@ngrx/signals';
import { computed, inject } from '@angular/core';
import { ReservationService } from '@domain/reservation/data-access/reservation.service';
import { ShowtimeStore } from '@domain/showtime/data-access/showtime.store';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { concatMap, pipe, switchMap, tap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';

export interface ReservationState {
  reservations: Reservation[];
  reservation: Reservation | null;
  pagination: Pagination | null;
  reservationProfile: ReservationProfile[];
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
  reservationProfile: [],
  reservationId: null,
  userId: null,
  currentTTL: null,
  loading: false,
  saving: false,
  error: null,
};

export const ReservationStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withComputed((store) => ({
    remainingTime: computed(() => store.currentTTL()?.ttl),

    formattedTTL: computed(() => {
      const ttl = store.currentTTL()?.ttl;
      if (!ttl) return '00:00';
      const m = Math.floor(ttl / 60);
      const s = ttl % 60;
      return `${m}:${s.toString().padStart(2, '0')}`;
    }),
  })),
  withMethods(
    (
      store,
      reservationService = inject(ReservationService),
      showtimeStore = inject(ShowtimeStore),
    ) => {
      let timerIdx: any = null;

      const stopCountdown = () => {
        if (timerIdx) {
          clearInterval(timerIdx);
          timerIdx = null;
        }
      };

      const startCountdown = (ttl: number) => {
        stopCountdown();

        const current = store.currentTTL();
        if (current) patchState(store, { currentTTL: { ...current, ttl } });

        timerIdx = setInterval(() => {
          const current = store.currentTTL();
          if (current && current.ttl > 0) {
            patchState(store, (s) => ({
              currentTTL: s.currentTTL
                ? { ...current, ttl: current.ttl - 1 }
                : null,
            }));
          } else {
            stopCountdown();
          }
        }, 1000);
      };

      return {
        loadReservations: rxMethod<{ filter: Partial<ReservationFilter> }>(
          pipe(
            tap(() => patchState(store, { loading: true, error: null })),
            switchMap(({ filter }) =>
              reservationService.getReservations(filter).pipe(
                tapResponse({
                  next: ({ content, pagination }) =>
                    patchState(store, {
                      reservations: content,
                      pagination: pagination,
                      loading: false,
                    }),
                  error: (err: any) =>
                    patchState(store, { error: err.message, loading: false }),
                }),
              ),
            ),
          ),
        ),
        //
        loadReservation: rxMethod<{ id: string }>(
          pipe(
            tap(() => patchState(store, { loading: true, error: null })),
            switchMap(({ id }) =>
              reservationService.getReservation(id).pipe(
                tapResponse({
                  next: (reservation) =>
                    patchState(store, {
                      reservation: reservation,
                      loading: false,
                    }),
                  error: (err: any) =>
                    patchState(store, { error: err.message, loading: false }),
                }),
              ),
            ),
          ),
        ),
        loadReservationProfile: rxMethod<number>(
          pipe(
            tap(() => patchState(store, { loading: true, error: null })),
            switchMap((id) =>
              reservationService.getUserReservation(id).pipe(
                tapResponse({
                  next: ({ content, pagination }) =>
                    patchState(store, {
                      reservationProfile: content,
                      pagination: pagination,
                      loading: false,
                    }),
                  error: (err: any) =>
                    patchState(store, { error: err.message, loading: false }),
                }),
              ),
            ),
          ),
        ),
        //
        initReservation: rxMethod<ReservationInitRequest>(
          pipe(
            tap(() =>
              patchState(store, { loading: true, saving: true, error: null }),
            ),
            switchMap((req) =>
              reservationService.initReservation(req).pipe(
                tapResponse({
                  next: ({ reservationId, ttl, expiredAt }) => {
                    patchState(store, {
                      reservationId: reservationId,
                      currentTTL: {
                        reservationId: reservationId,
                        ttl: ttl,
                        expiredAt: new Date(expiredAt),
                      },
                      saving: false,
                      loading: false,
                    });
                    startCountdown(ttl);
                  },
                  error: (err: any) =>
                    patchState(store, { error: err.message, loading: false }),
                }),
              ),
            ),
          ),
        ),
        //
        holdSeat: rxMethod<{ reservation: ReservationRequest }>(
          pipe(
            tap(() =>
              patchState(store, { loading: true, saving: true, error: null }),
            ),
            switchMap(({ reservation }) => {
              return reservationService.holdReservationSeat(reservation).pipe(
                tapResponse({
                  next: () =>
                    patchState(store, {
                      loading: false,
                      saving: false,
                    }),
                  error: (err: any) =>
                    patchState(store, {
                      error: err.message,
                      loading: false,
                      saving: false,
                    }),
                }),
              );
            }),
          ),
        ),
        //
        confirm: rxMethod<{ reservation: ReservationRequest }>(
          pipe(
            tap(() =>
              patchState(store, { loading: true, saving: true, error: null }),
            ),
            concatMap(({ reservation }) => {
              return reservationService.confirmReservation(reservation).pipe(
                tapResponse({
                  next: ({ reservation }) =>
                    patchState(store, {
                      reservation: reservation,
                      loading: false,
                      saving: false,
                    }),
                  error: (err: any) =>
                    patchState(store, {
                      error: err.message,
                      loading: false,
                      saving: false,
                    }),
                }),
              );
            }),
          ),
        ),
        //
        cancel: rxMethod<{ reservationId: string; userId: number }>(
          pipe(
            tap(() => patchState(store, { loading: true, saving: true })),
            switchMap(({ reservationId, userId }) =>
              reservationService.cancelReservation(reservationId, userId).pipe(
                tapResponse({
                  next: () => {
                    stopCountdown();
                    patchState(store, {
                      reservationId: null,
                      currentTTL: null,
                      saving: false,
                      loading: false,
                    });
                  },
                  error: (err: any) =>
                    patchState(store, { error: err.message, saving: false }),
                }),
              ),
            ),
          ),
        ),
        //
        startCountdown,
        //
        stopCountdown,
        //
        _cleanup: () => stopCountdown(),
        //
        updateTTL: (ttl: number) => {
          const current = store.currentTTL();
          if (current) patchState(store, { currentTTL: { ...current, ttl } });
        },
        //
        clearError: () => patchState(store, { error: null }),
      };
    },
  ),
  withHooks({
    // onInit(store) {},
    onDestroy(store) {
      store._cleanup();
    },
  }),
);
