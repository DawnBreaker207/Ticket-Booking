import { Theater } from '@domain/theater/models/theater.model';
import { Movie } from '@domain/movie/models/movie.model';
import { signalStore, withMethods } from '@ngrx/signals';
import { computed, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { NzModalService } from 'ng-zorro-antd/modal';
import { StorageService } from '@core/services/storage/storage.service';
import {
  selectAllTheaters,
  selectSelectedTheaterId,
} from '@domain/theater/data-access/theater.selectors';
import { selectAllShowtimes } from '@domain/showtime/data-access/showtime.selectors';
import { selectAllMovies } from '@domain/movie/data-access/movie.selectors';
import { selectJwt } from '@core/auth/auth.selectors';
import { TheaterActions } from '@domain/theater/data-access/theater.actions';
import { MovieActions } from '@domain/movie/data-access/movie.actions';
import { ShowtimeActions } from '@domain/showtime/data-access/showtime.actions';
import { TheaterModalComponent } from '@features/client/home/components/theater-modal/theater.component';
import { ShowtimeModalComponent } from '@features/client/home/components/showtime-modal/modal.component';
import { ReservationStore } from '@features/client/reservation/reservation.store';

export const HomeStore = signalStore(
  withMethods(
    (
      store,
      ngrxStore = inject(Store),
      modalService = inject(NzModalService),
      storageService = inject(StorageService),
      reservationStore = inject(ReservationStore),
    ) => {
      const theaters = ngrxStore.selectSignal(selectAllTheaters);
      const showtimes = ngrxStore.selectSignal(selectAllShowtimes);
      const allMovies = ngrxStore.selectSignal(selectAllMovies);
      const selectedTheaterId = ngrxStore.selectSignal(selectSelectedTheaterId);
      const user = ngrxStore.selectSignal(selectJwt);

      const showtimesForTheater = computed(() => {
        const id = selectedTheaterId();
        return id ? showtimes().filter((st) => st.theaterId === id) : [];
      });

      const movies = computed(() => {
        const showtimes = showtimesForTheater();
        const movies = allMovies();
        if (!showtimes.length || !movies.length) return [];

        const movieMap = new Map(movies.map((m) => [m.id, m]));
        const uniqueIds = [...new Set(showtimes.map((st) => st.movieId))];
        return uniqueIds
          .map((id) => movieMap.get(id))
          .filter((m): m is Movie => !!m);
      });

      return {
        theaters,
        selectedTheaterId,
        movies,
        showtimesForTheater,
        user,
        initData() {
          const currentTheaters = theaters();
          const currentMovies = allMovies();

          if (currentTheaters.length === 0) {
            ngrxStore.dispatch(
              TheaterActions.loadTheaters({ page: 0, size: 10 }),
            );
          }
          if (currentMovies.length === 0) {
            ngrxStore.dispatch(MovieActions.loadMovies({ page: 0, size: 100 }));
          }

          if (currentTheaters.length > 0 && !selectedTheaterId()) {
            const savedTheater = storageService.getSelectedTheaterId();
            const foundTheater = currentTheaters.find(
              (t) => t.id === savedTheater,
            );

            if (savedTheater && foundTheater) {
              ngrxStore.dispatch(
                TheaterActions.setSelectedTheaterId({
                  theaterId: savedTheater,
                }),
              );
            } else {
              const defaultTheater = currentTheaters[0].id;
              ngrxStore.dispatch(
                TheaterActions.setSelectedTheaterId({
                  theaterId: defaultTheater,
                }),
              );
              this.openTheaterModal(currentTheaters, defaultTheater as number);
            }
          }
        },

        loadShowtimes(theaterId: number) {
          ngrxStore.dispatch(
            ShowtimeActions.loadShowtimesByTheaterId({
              theaterId: theaterId,
              page: 0,
              size: 100,
            }),
          );
        },

        openTheaterModal(theatersList: Theater[], defaultTheater: number) {
          const modal = modalService.create({
            nzTitle: 'Chọn rạp chiếu',
            nzContent: TheaterModalComponent,
            nzData: {
              theaters: theatersList,
              selectedTheaterId: defaultTheater,
            },
            nzFooter: null,
          });
          modal.afterClose.subscribe((id) => {
            if (id)
              ngrxStore.dispatch(
                TheaterActions.setSelectedTheaterId({ theaterId: id }),
              );
          });
        },
        handleMovieSelection(movieId: number) {
          const movieShowtimes = showtimesForTheater().filter(
            (st) => st.movieId === movieId,
          );
          const modal = modalService.create({
            nzTitle: undefined,
            nzContent: ShowtimeModalComponent,
            nzData: { showtimes: movieShowtimes },
            nzFooter: null,
          });
          modal.afterClose.subscribe((stId) => {
            if (stId) this.proceedToReservation(stId);
          });
        },

        proceedToReservation(showtimeId: number) {
          const currentUser = user();
          const theaterId = selectedTheaterId();

          if (!currentUser || !theaterId) return;
          console.log(
            `userId: ${currentUser.userId}, theaterId: ${theaterId}, showtimeId: ${showtimeId}`,
          );
          reservationStore.initReservation({
            reservationId: '',
            userId: currentUser.userId,
            showtimeId: showtimeId,
            theaterId: theaterId,
          });
        },
      };
    },
  ),
);
