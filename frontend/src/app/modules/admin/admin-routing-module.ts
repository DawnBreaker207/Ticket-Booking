import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from '@/app/modules/admin/admin.component';
import { DashboardComponent } from '@/app/modules/admin/components/dashboard/dashboard.component';
import { MovieComponent } from '@/app/modules/admin/components/movie/movie.component';
import { ReservationComponent } from '@/app/modules/admin/components/reservation/reservation.component';
import { TheaterComponent } from '@/app/modules/admin/components/theater/theater.component';
import { ShowtimeComponent } from '@/app/modules/admin/components/showtime/showtime.component';

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
        data: { breadcrumb: 'Dashboard' },
      },
      {
        path: 'movie',
        component: MovieComponent,
        data: { breadcrumb: 'Movie Manager' },
      },
      {
        path: 'theater',
        component: TheaterComponent,
        data: { breadcrumb: 'Theater Manager' },
      },
      {
        path: 'showtime',
        component: ShowtimeComponent,
        data: { breadcrumb: 'Showtime Manager' },
      },
      {
        path: 'reservation',
        component: ReservationComponent,
        data: { breadcrumb: 'Reservation Manager' },
      },
    ],
  },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'errors/404' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
