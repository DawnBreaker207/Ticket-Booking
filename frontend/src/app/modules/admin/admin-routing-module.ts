import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from '@/app/modules/admin/components/dashboard/dashboard';
import {MovieComponent} from '@/app/modules/admin/components/movie/movie';
import {OrderComponent} from '@/app/modules/admin/components/order/order';
import {AdminComponent} from '@/app/modules/admin/admin.component';

const routes: Routes = [
    {
      path: '', component: AdminComponent, children: [
        {path: 'dashboard', component: DashboardComponent},
        {path: 'movie', component: MovieComponent},
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
