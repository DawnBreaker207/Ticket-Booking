import { Routes } from '@angular/router';
import { MainLayoutComponent } from '../../layout/main-layout/main-layout.component';
import { HomeComponent } from '@features/client/home/home.component';
import { DetailComponent } from '@features/client/detail/detail.component';
import { AuthComponent } from '@features/auth/auth';
import { AuthGuard } from '@core/guards/auth.guard';
import { ReservationComponent } from '@features/client/reservation/reservation.component';
import { PaymentResultComponent } from '@features/client/reservation/components/payment-result/payment-result.component';
import { ProfileComponent } from '@features/client/profile/profile.component';

export const CLIENT_ROUTES: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: 'client',
        component: HomeComponent,
      },
      { path: 'profile', component: ProfileComponent },
      { path: 'movie/detail/:id', component: DetailComponent },
      { path: 'login', component: AuthComponent },
      {
        canActivate: [AuthGuard],
        path: 'reservation/:reservationId/:showtimeId',
        component: ReservationComponent,
      },
      { path: 'paymentResult', component: PaymentResultComponent },
    ],
  },
  { path: '', redirectTo: '', pathMatch: 'full' },
  { path: '**', redirectTo: 'errors/404' },
];
