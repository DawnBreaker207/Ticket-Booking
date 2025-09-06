import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from '@/app/modules/home/home.component';
import {AuthComponent} from '@/app/modules/home/pages/auth/auth';
import {ReservationComponent} from '@/app/modules/home/pages/reservation/reservation';
import {Home} from '@/app/modules/home/pages/home/home';
import {
  PaymentResultComponent
} from '@/app/modules/home/components/reservation/payment-result/payment-result.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {path: 'home', component: Home},
      {path: 'login', component: AuthComponent},
      {path: 'reservation/:orderId', component: ReservationComponent},
      {path: 'paymentResult', component: PaymentResultComponent}
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
