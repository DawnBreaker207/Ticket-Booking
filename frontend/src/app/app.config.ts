import {
  ApplicationConfig,
  importProvidersFrom,
  isDevMode,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideStore } from '@ngrx/store';
import { icons } from './icons-provider';
import { provideNzIcons } from 'ng-zorro-antd/icon';
import { en_US, provideNzI18n } from 'ng-zorro-antd/i18n';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideEffects } from '@ngrx/effects';
import { AuthInterceptor } from './core/interceptor/auth.interceptor';
import { CredentialInterceptor } from './core/interceptor/credential.interceptor';
import { ErrorInterceptor } from '@/app/core/interceptor/error.interceptor';
import {
  authFeatureKey,
  authReducer,
} from '@/app/core/store/state/auth/auth.reducers';
import { AuthEffects } from '@/app/core/store/state/auth/auth.effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import {
  reservationFeatureKey,
  reservationReducer,
} from '@/app/core/store/state/reservation/reservation.reducers';
import { ReservationEffects } from '@/app/core/store/state/reservation/reservation.effects';
import {
  theaterFeatureKey,
  theaterReducer,
} from '@/app/core/store/state/theater/theater.reducers';
import { TheaterEffects } from '@/app/core/store/state/theater/theater.effects';
import {
  movieFeatureKey,
  movieReducer,
} from '@/app/core/store/state/movie/movie.reducers';
import {
  showtimeFeatureKey,
  showtimeReducer,
} from '@/app/core/store/state/showtime/showtime.reducers';
import { MovieEffects } from '@/app/core/store/state/movie/movie.effects';
import { ShowtimeEffects } from '@/app/core/store/state/showtime/showtime.effects';
import {
  seatFeatureKey,
  seatReducer,
} from '@/app/core/store/state/seat/seat.reducers';
import { SeatEffects } from '@/app/core/store/state/seat/seat.effects';
import { NgxEchartsModule } from 'ngx-echarts';
import { CurrencyFormatPipe } from '@/app/core/pipes/currency-format-pipe';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    CurrencyPipe,
    CurrencyFormatPipe,
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient(
      withInterceptors([
        AuthInterceptor,
        CredentialInterceptor,
        ErrorInterceptor,
      ]),
    ),
    provideRouter(routes),
    provideNzIcons(icons),
    provideNzI18n(en_US),
    provideStore({
      [authFeatureKey]: authReducer,
      [movieFeatureKey]: movieReducer,
      [theaterFeatureKey]: theaterReducer,
      [showtimeFeatureKey]: showtimeReducer,
      [reservationFeatureKey]: reservationReducer,
      [seatFeatureKey]: seatReducer,
    }),
    provideEffects([
      AuthEffects,
      MovieEffects,
      TheaterEffects,
      ShowtimeEffects,
      ReservationEffects,
      SeatEffects,
    ]),
    importProvidersFrom(
      NgxEchartsModule.forRoot({
        echarts: () => import('echarts'),
      }),
    ),
    importProvidersFrom(CommonModule, FormsModule, ReactiveFormsModule),
    provideAnimationsAsync(),
    provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() }),
  ],
};
