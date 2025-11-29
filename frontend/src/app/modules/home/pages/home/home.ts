import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzTypographyComponent } from 'ng-zorro-antd/typography';
import {
  NzColDirective,
  NzGridModule,
  NzRowDirective,
} from 'ng-zorro-antd/grid';

import { NzImageService } from 'ng-zorro-antd/image';
import { Store } from '@ngrx/store';
import {
  combineLatest,
  distinctUntilChanged,
  filter,
  map,
  shareReplay,
  Subject,
  take,
  takeUntil,
} from 'rxjs';
import { TheaterActions } from '@/app/core/store/state/theater/theater.actions';
import { NzModalModule, NzModalService } from 'ng-zorro-antd/modal';
import { selectAllShowtimes } from '@/app/core/store/state/showtime/showtime.selectors';
import { selectJwt } from '@/app/core/store/state/auth/auth.selectors';
import {
  selectAllTheaters,
  selectSelectedTheaterId,
} from '@/app/core/store/state/theater/theater.selectors';
import { ShowtimeActions } from '@/app/core/store/state/showtime/showtime.actions';
import { Movie } from '@/app/core/models/movie.model';
import { selectAllMovies } from '@/app/core/store/state/movie/movie.selectors';
import { MovieActions } from '@/app/core/store/state/movie/movie.actions';
import { ReservationInitRequest } from '@/app/core/models/reservation.model';
import { ReservationActions } from '@/app/core/store/state/reservation/reservation.actions';
import { Actions, ofType } from '@ngrx/effects';
import { StorageService } from '@/app/shared/services/storage/storage.service';
import { AsyncPipe } from '@angular/common';
import { Theater } from '@/app/core/models/theater.model';
import { NzEmptyComponent } from 'ng-zorro-antd/empty';
import { ShowtimeModalComponent } from '@/app/modules/home/components/showtime/modal/modal.component';
import { TheaterModalComponent } from '@/app/modules/home/components/theater/modal/theater.component';
import { SliderComponent } from '@/app/modules/home/components/slider/slider.component';
import { Router } from '@angular/router';
import { MovieItemComponent } from '@/app/shared/components/movie-item/movie-item.component';

@Component({
  selector: 'app-home',
  imports: [
    NzLayoutModule,
    NzTypographyComponent,
    NzRowDirective,
    NzColDirective,
    NzGridModule,
    AsyncPipe,
    NzEmptyComponent,
    NzModalModule,
    SliderComponent,
    MovieItemComponent,
  ],
  providers: [NzImageService, NzModalService],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit, OnDestroy {
  private store = inject(Store);
  private router = inject(Router);
  private actions$ = inject(Actions);
  private modalService = inject(NzModalService);
  private storageService = inject(StorageService);

  showtimes$ = this.store.select(selectAllShowtimes);
  theaters$ = this.store.select(selectAllTheaters);
  user$ = this.store.select(selectJwt);
  allMovies$ = this.store.select(selectAllMovies);
  selectedTheaterId$ = this.store.select(selectSelectedTheaterId);

  private destroy$ = new Subject<void>();
  showTimesForTheater$ = combineLatest([
    this.showtimes$,
    this.selectedTheaterId$,
  ]).pipe(
    map(([showtimes, theaterId]) =>
      theaterId ? showtimes.filter((st) => st.theaterId === theaterId) : [],
    ),
  );

  movies$ = combineLatest([this.showTimesForTheater$, this.allMovies$]).pipe(
    map(([showtimes, allMovies]) => {
      if (!showtimes.length) return [] as Movie[];

      const uniqueMovieIds = [...new Set(showtimes.map((st) => st.movieId))];

      return uniqueMovieIds
        .map((movieId) => {
          const movieState = allMovies.find((m) => m.id === movieId);
          if (movieState) return movieState;

          const showtime = showtimes.find((st) => st.movieId === movieId);
          if (!showtime) return null;

          return {
            id: showtime.movieId as number,
            title: showtime.movieTitle || 'Unknown',
            poster: showtime.moviePosterUrl || '',
            overview: '',
            duration: 0,
            genres: [],
            releaseDate: new Date(),
            language: '',
            imdbId: '',
            filmId: '',
            createdAt: new Date(),
            updatedAt: new Date(),
            isDeleted: false,
          } as Movie;
        })
        .filter((m): m is Movie => m !== null);
    }),
    shareReplay(1),
  );

  ngOnInit() {
    this.selectTheaterModal();
    this.watchTheaterChanges();
  }

  private selectTheaterModal() {
    this.store.dispatch(TheaterActions.loadTheaters({ page: 0, size: 10 }));
    this.store.dispatch(MovieActions.loadMovies({ page: 0, size: 100 }));

    const savedTheaterId = this.storageService.getSelectedTheaterId();

    this.theaters$
      .pipe(
        filter((t) => t.length > 0),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe((theaters) => {
        if (savedTheaterId && theaters.find((t) => t.id === savedTheaterId)) {
          this.store.dispatch(
            TheaterActions.setSelectedTheaterId({ theaterId: savedTheaterId }),
          );
        } else {
          const defaultTheater = theaters[0].id;
          this.store.dispatch(
            TheaterActions.setSelectedTheaterId({ theaterId: defaultTheater }),
          );
          this.openTheaterModal(theaters, defaultTheater);
        }
      });
  }

  private watchTheaterChanges() {
    this.selectedTheaterId$
      .pipe(
        filter((id): id is number => id !== null),
        distinctUntilChanged(),
        takeUntil(this.destroy$),
      )
      .subscribe((id) => {
        this.store.dispatch(
          ShowtimeActions.loadShowtimesByTheaterId({
            theaterId: id,
            page: 0,
            size: 100,
          }),
        );
      });
  }

  openTheaterSelection() {
    combineLatest([this.theaters$, this.selectedTheaterId$])
      .pipe(take(1))
      .subscribe(([theaters, selectedId]) => {
        if (theaters.length > 0) {
          this.openTheaterModal(theaters, selectedId || theaters[0].id);
        }
      });
  }

  private openTheaterModal(theaters: Theater[], defaultId: number) {
    const modal = this.modalService.create({
      nzTitle: 'Chọn rạp chiếu',
      nzContent: TheaterModalComponent,
      nzData: {
        theaters: theaters,
        selectedTheaterId: defaultId,
      },
      nzFooter: null,
      nzClosable: true,
    });

    modal.afterClose.pipe(take(1)).subscribe((selectedId: number | null) => {
      if (selectedId) {
        this.store.dispatch(
          TheaterActions.setSelectedTheaterId({ theaterId: selectedId }),
        );
      }
    });
  }

  onSelectMovie(movieId: number) {
    this.showTimesForTheater$
      .pipe(take(1), takeUntil(this.destroy$))
      .subscribe((showtimes) => {
        const movieShowtimes = showtimes.filter((st) => st.movieId === movieId);
        if (!movieShowtimes.length) return;

        const modal = this.modalService.create({
          nzTitle: `Chọn khung giờ`,
          nzContent: ShowtimeModalComponent,
          nzData: {
            showtimes: movieShowtimes,
          },
          nzFooter: null,
        });

        modal.afterClose
          .pipe(take(1))
          .subscribe((selectShowtimeId: number | null) => {
            if (!selectShowtimeId) return;
            this.proceedToReservation(selectShowtimeId);
          });
      });
  }

  private proceedToReservation(showtimeId: number) {
    combineLatest([this.user$, this.selectedTheaterId$])
      .pipe(
        filter(([user, theaterId]) => !!user && theaterId !== null),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe(([user, theaterId]) => {
        const reservation: ReservationInitRequest = {
          reservationId: '',
          userId: user!.userId,
          showtimeId: showtimeId,
          theaterId: theaterId!,
        };

        // Dispatch action
        this.store.dispatch(
          ReservationActions.createReservationInit({
            reservation: reservation,
          }),
        );

        // ✅ Lắng nghe success action riêng biệt
        this.actions$
          .pipe(
            ofType(ReservationActions.createReservationInitSuccess),
            take(1),
            takeUntil(this.destroy$),
          )
          .subscribe(({ reservationId }) => {
            const state = {
              reservationId,
              userId: user!.userId,
              showtimeId,
              theaterId: theaterId,
            };
            this.storageService.setItem('reservationState', state);
            this.router.navigate(
              [`/reservation/${reservationId}/${showtimeId}`],
              { state: state },
            );
          });
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
