import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from '@/app/modules/error/error.component';
import { Error404Component } from '@/app/modules/error/pages/404/error-404.component';
import { Error500Component } from '@/app/modules/error/pages/500/error-500.component';

const routes: Routes = [
  {
    path: '',
    component: ErrorComponent,
    children: [
      { path: '404', component: Error404Component },
      { path: '500', component: Error500Component },
      { path: '', redirectTo: '404', pathMatch: 'full' },
      { path: '**', redirectTo: 'errors/404' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ErrorRoutingModule {}
