import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AdminComponent} from '@/app/modules/admin/admin.component';
import {DashboardComponent} from '@/app/modules/admin/components/dashboard/dashboard.component';
import {MovieComponent} from '@/app/modules/admin/components/movie/movie.component';
import {OrderComponent} from '@/app/modules/admin/components/order/order.component';
import {ScheduleComponent} from '@/app/modules/admin/components/schedule/schedule.component';
import {FormMovieAPIComponent} from '@/app/modules/admin/components/movie/form/api/form.component';

const routes: Routes = [
    {
      path: '', component: AdminComponent, children: [
        {path: 'dashboard', component: DashboardComponent},
        {path: 'movie', component: MovieComponent},
        {path: 'schedule', component: ScheduleComponent},
        {path: 'order', component: OrderComponent},
      ]
    }, {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
    {path: '**', redirectTo: 'errors/404'}
  ]
;

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {
}
