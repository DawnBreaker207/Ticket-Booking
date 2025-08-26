import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from '@/app/modules/home/home.component';
import {Home} from '@/app/modules/home/pages/home/home';
import {AuthComponent} from '@/app/modules/home/pages/auth/auth';

const routes: Routes = [
  {
    path: '', component: HomeComponent
    , children: [
      {path: '', component: Home},
      {path: 'login', component: AuthComponent},
    ]
  },
  {path: '', redirectTo: '', pathMatch: 'full'},
  {path: '**', redirectTo: 'errors/404'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
