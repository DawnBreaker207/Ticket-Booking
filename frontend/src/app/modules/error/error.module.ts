import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ErrorRoutingModule} from './error-routing.module';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    ErrorRoutingModule
  ],
  providers: [provideHttpClient(withInterceptorsFromDi())]
})
export class ErrorModule {
}
