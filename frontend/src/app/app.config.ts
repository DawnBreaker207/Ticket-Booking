import {
  ApplicationConfig,
  importProvidersFrom,
  isDevMode,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideStore} from '@ngrx/store';
import {icons} from './icons-provider';
import {provideNzIcons} from 'ng-zorro-antd/icon';
import {en_US, provideNzI18n} from 'ng-zorro-antd/i18n';
import {CommonModule, registerLocaleData} from '@angular/common';
import en from '@angular/common/locales/en';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {provideEffects} from '@ngrx/effects';
import {AuthInterceptor} from './core/interceptor/auth.interceptor';
import {CredentialInterceptor} from './core/interceptor/credential.interceptor';
import {ErrorInterceptor} from '@/app/core/interceptor/error.interceptor';
import {authFeatureKey, authReducer} from '@/app/core/store/state/auth/auth.reducers';
import {AuthEffects} from '@/app/core/store/state/auth/auth.effects';
import {provideStoreDevtools} from '@ngrx/store-devtools';
import {reservationFeatureKey, reservationReducer} from '@/app/core/store/state/reservation/reservation.reducers';
import {ReservationEffects} from '@/app/core/store/state/reservation/reservation.effects';
import {countdownFeatureKey, countdownReducer} from '@/app/core/store/state/countdown/countdown.reducers';
import {theaterFeatureKey, theaterReducer} from '@/app/core/store/state/theater/theater.reducers';
import {TheaterEffects} from '@/app/core/store/state/theater/theater.effects';
import {movieFeatureKey, movieReducer} from '@/app/core/store/state/movie/movie.reducers';
import {showtimeFeatureKey, showtimeReducer} from '@/app/core/store/state/showtime/showtime.reducers';

registerLocaleData(en);

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideHttpClient(withInterceptors([AuthInterceptor, CredentialInterceptor, ErrorInterceptor])),
    provideRouter(routes),
    provideStore({
      [authFeatureKey]: authReducer,
      [movieFeatureKey]: movieReducer,
      [theaterFeatureKey]: theaterReducer,
      [showtimeFeatureKey]: showtimeReducer,
      [reservationFeatureKey]: reservationReducer,
      [countdownFeatureKey]: countdownReducer,

    }),
    provideEffects([AuthEffects, ReservationEffects, TheaterEffects]),
    provideNzIcons(icons),
    provideNzI18n(en_US),
    importProvidersFrom(
      CommonModule,
      FormsModule,
      ReactiveFormsModule
    ),
    provideAnimationsAsync(),
    provideStoreDevtools({maxAge: 25, logOnly: !isDevMode()})
  ]
};
