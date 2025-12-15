import {
  ApplicationConfig,
  importProvidersFrom,
  isDevMode,
  LOCALE_ID,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideStore } from '@ngrx/store';
import { nzIcons } from './icons-provider';
import { provideNzIcons } from 'ng-zorro-antd/icon';
import { en_US, provideNzI18n } from 'ng-zorro-antd/i18n';
import {
  CommonModule,
  CurrencyPipe,
  registerLocaleData,
} from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideEffects } from '@ngrx/effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import { NgxEchartsModule } from 'ngx-echarts';
import { icons, LucideAngularModule } from 'lucide-angular';
import localVi from '@angular/common/locales/vi';
import { CurrencyFormatPipe } from '@shared/pipes/currency-format.pipe';
import { AuthInterceptor } from '@core/interceptor/auth.interceptor';
import { CredentialInterceptor } from '@core/interceptor/credential.interceptor';
import { ErrorInterceptor } from '@core/interceptor/error.interceptor';
import { authFeatureKey, authReducer } from '@core/auth/auth.reducers';
import {
  movieFeatureKey,
  movieReducer,
} from '@domain/movie/data-access/movie.reducers';
import {
  theaterFeatureKey,
  theaterReducer,
} from '@domain/theater/data-access/theater.reducers';
import {
  showtimeFeatureKey,
  showtimeReducer,
} from '@domain/showtime/data-access/showtime.reducers';
import {
  reservationFeatureKey,
  reservationReducer,
} from '@domain/reservation/data-access/reservation.reducers';
import {
  seatFeatureKey,
  seatReducer,
} from '@domain/seat/data-access/seat.reducers';
import { AuthEffects } from '@core/auth/auth.effects';
import { MovieEffects } from '@domain/movie/data-access/movie.effects';
import { TheaterEffects } from '@domain/theater/data-access/theater.effects';
import { ShowtimeEffects } from '@domain/showtime/data-access/showtime.effects';
import { ReservationEffects } from '@domain/reservation/data-access/reservation.effects';
import { SeatEffects } from '@domain/seat/data-access/seat.effects';

registerLocaleData(localVi);
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
    provideNzIcons(nzIcons),
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
    importProvidersFrom(
      CommonModule,
      FormsModule,
      ReactiveFormsModule,
      LucideAngularModule.pick(icons),
    ),
    { provide: LOCALE_ID, useValue: 'vi' },
    provideAnimationsAsync(),
    provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() }),
  ],
};
