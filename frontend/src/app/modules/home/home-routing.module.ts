import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from '@/app/modules/home/home.component';
import { AuthComponent } from '@/app/modules/home/pages/auth/auth';
import { ReservationComponent } from '@/app/modules/home/pages/reservation/reservation';
import { Home } from '@/app/modules/home/pages/home/home';
import { PaymentResultComponent } from '@/app/modules/home/components/reservation/payment-result/payment-result.component';
import { DetailComponent } from '@/app/modules/home/pages/detail/detail.component';
import { AuthGuard } from '@/app/core/guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: 'home',
        component: Home,
      },
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

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule {}
