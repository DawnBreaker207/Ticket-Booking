import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HomeRoutingModule} from './home-routing.module';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HomeRoutingModule
  ],
  providers: [provideHttpClient(withInterceptorsFromDi())]
})
export class HomeModule {
}
