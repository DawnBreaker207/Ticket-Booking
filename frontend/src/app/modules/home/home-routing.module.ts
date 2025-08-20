import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from '@/app/modules/home/home.component';
import {LoginComponent} from '@/app/modules/home/pages/login/login';
import {SignUpComponent} from '@/app/modules/home/pages/signup/signup';

const routes: Routes = [
  {
    path: '', component: HomeComponent
    , children: [
      {path: 'login', component: LoginComponent},
      {path: 'signup', component: SignUpComponent}
    ]
  },
  {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
  {path: '**', redirectTo: 'errors/404'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
