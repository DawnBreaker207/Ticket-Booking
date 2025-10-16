import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzTypographyComponent} from 'ng-zorro-antd/typography';
import {NzColDirective, NzGridModule, NzRowDirective,} from 'ng-zorro-antd/grid';
import {NzCardComponent, NzCardModule} from 'ng-zorro-antd/card';
import {NzImageViewComponent} from 'ng-zorro-antd/experimental/image';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzImageService} from 'ng-zorro-antd/image';
import {Router, RouterLink} from '@angular/router';
import {Store} from '@ngrx/store';
import {filter, forkJoin, map, Subject, switchMap, take, takeUntil} from 'rxjs';
import {Jwt} from '@/app/core/models/jwt.model';
import {TheaterActions} from '@/app/core/store/state/theater/theater.actions';
import {NzModalService} from 'ng-zorro-antd/modal';
import {TheaterComponent} from '@/app/modules/home/components/theater/theater.component';
import {selectAllShowtimes} from '@/app/core/store/state/showtime/showtime.selectors';
import {selectJwt,} from '@/app/core/store/state/auth/auth.selectors';
import {selectAllTheaters} from '@/app/core/store/state/theater/theater.selectors';
import {Showtime} from '@/app/core/models/theater.model';
import {ShowtimeComponent} from '@/app/modules/home/components/showtime/showtime.component';
import {ShowtimeActions} from '@/app/core/store/state/showtime/showtime.actions';
import {Movie} from '@/app/core/models/movie.model';
import {selectMovieById} from '@/app/core/store/state/movie/movie.selectors';
import {MovieActions} from '@/app/core/store/state/movie/movie.actions';
import {ReservationInitRequest,} from '@/app/core/models/reservation.model';
import {UserService} from '@/app/core/services/user/user.service';
import {ReservationActions} from '@/app/core/store/state/reservation/reservation.actions';
import {Actions, ofType} from '@ngrx/effects';
import {User} from '@/app/core/models/user.model';

@Component({
  selector: 'app-home',
  imports: [
    NzLayoutModule,
    NzTypographyComponent,
    NzRowDirective,
    NzColDirective,
    NzCardComponent,
    NzImageViewComponent,
    NzButtonComponent,
    NzCardModule,
    NzGridModule,
    RouterLink,
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
  private userService = inject(UserService);
  private destroy$ = new Subject<void>();

  showtimes$ = this.store.select(selectAllShowtimes);
  theaters$ = this.store.select(selectAllTheaters);
  user$ = this.store.select(selectJwt);
  selectedTheaterId: number | null = null;
  showTimesForTheater: Showtime[] = [];
  movies: Movie[] = [];

  ngOnInit() {
    this.selectTheaterModal();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private selectTheaterModal() {
    this.store.dispatch(TheaterActions.loadTheaters());
    this.store.dispatch(MovieActions.loadMovies());
    this.theaters$
      .pipe(
        filter((t) => t.length > 0),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe((theaters) => {
        const defaultTheater = theaters[0];
        this.selectedTheaterId = defaultTheater.id;
        this.store.dispatch(
          ShowtimeActions.loadShowtimesByTheaterId({
            theaterId: defaultTheater.id as number,
          }),
        );

        this.filterShowtimeByTheater(defaultTheater.id);
        const modal = this.modalService.create({
          nzTitle: 'Chọn rạp chiếu',
          nzContent: TheaterComponent,
          nzData: {
            theaters: theaters,
            selectedTheaterId: this.selectedTheaterId,
          },
          nzFooter: null,
        });
        modal.afterClose.subscribe((selectedId: number | null) => {
          if (!selectedId) return;

          this.selectedTheaterId = selectedId;
          this.store.dispatch(
            ShowtimeActions.loadShowtimesByTheaterId({theaterId: selectedId}),
          );
          this.filterShowtimeByTheater(selectedId);
          this.updateMovieForHome();
        });
      });
  }

  private updateMovieForHome() {
    if (!this.selectedTheaterId) return;

    this.showtimes$.pipe(take(1)).subscribe((showtimes) => {
      const showtimeByTheater = showtimes.filter(
        (st) => st.theaterId === this.selectedTheaterId,
      );

      const movieSelectors$ = showtimeByTheater.map((st) =>
        this.store.select(selectMovieById(st.movieId)).pipe(take(1)),
      );

      forkJoin(movieSelectors$).subscribe((movieStores) => {
        const movie: Record<number, Movie> = {};
        showtimeByTheater.forEach((st, i) => {
          const movieStore = movieStores[i];
          if (movieStore) movie[st.movieId] = movieStore;
          else
            movie[st.movieId] = {
              id: st.movieId,
              title: st.movieTitle,
              poster: st.moviePosterUrl,
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
        });
        this.movies = Object.values(movie);
      });
    });
  }

  private filterShowtimeByTheater(theaterId: number) {
    this.showtimes$
      .pipe(
        filter((st) => st.length > 0),
        take(1),
        takeUntil(this.destroy$),
      )
      .subscribe((showtimes) => {
        this.showTimesForTheater = showtimes.filter(
          (st) => st.theaterId === theaterId,
        );
        this.updateMovieForHome();
      });
  }

  onSelect(movieId: number) {
    if (!this.selectedTheaterId) return;
    this.showtimes$.pipe(take(1)).subscribe((showtimes) => {
      const showtimeForMovies = showtimes.filter(
        (st) =>
          st.movieId === movieId && st.theaterId === this.selectedTheaterId,
      );

      if (!showtimeForMovies.length) return;
      this.modalService
        .create({
          nzTitle: `Chọn khung giờ`,
          nzContent: ShowtimeComponent,
          nzData: {
            showtimes: showtimeForMovies,
          },
          nzFooter: null,
        })
        .afterClose.subscribe((selectShowtimeId: number | null) => {
        if (!selectShowtimeId) return;
        this.proceedToReservation(selectShowtimeId);
      });
    });
  }

  private proceedToReservation(showtimeId: number) {
    this.user$
      .pipe(
        take(1),
        filter((jwt): jwt is Jwt => jwt !== undefined),
        switchMap((res) => this.userService.getByEmail(res.email)),
        filter((user): user is User => !!user),
        switchMap((user) => {
          console.log(user)
          const reservation: ReservationInitRequest = {
            reservationId: '',
            userId: user.userId.toString(),
            showtimeId: showtimeId,
            theaterId: this.selectedTheaterId!,
          };
          console.log('Init Reservation', reservation)

          this.store.dispatch(
            ReservationActions.createReservationInit({reservation: reservation}),
          );

          return this.actions$
            .pipe(
              ofType(ReservationActions.createReservationSuccessInit),
              take(1),
              map(({reservationId}) => reservationId)
            )
        }),
        takeUntil(this.destroy$),
      )
      .subscribe((reservationId) => {
        this.router.navigateByUrl(`/reservation/${reservationId}`);
      });
  }
}
