import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'home', loadChildren: () => import('./modules/home/home.module').then(m => m.HomeModule)},
  {path: 'admin', loadChildren: () => import('./modules/admin/admin-module').then(m => m.AdminModule)},
  {path: 'errors', loadChildren: () => import('./modules/error/error.module').then(m => m.ErrorModule)},
  {path: '', redirectTo: 'errors/404'}
];
