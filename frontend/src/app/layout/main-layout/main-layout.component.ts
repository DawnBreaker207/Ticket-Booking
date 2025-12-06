import { Component, inject, signal } from '@angular/core';
import { NzContentComponent, NzLayoutComponent } from 'ng-zorro-antd/layout';
import { NzSpinComponent } from 'ng-zorro-antd/spin';
import {
  RouteConfigLoadEnd,
  RouteConfigLoadStart,
  Router,
  RouterOutlet,
} from '@angular/router';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-main-layout',
  imports: [
    FooterComponent,
    HeaderComponent,
    RouterOutlet,
    NzContentComponent,
    NzLayoutComponent,
    NzMenuModule,
    NzSpinComponent,
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css',
})
export class MainLayoutComponent {
  private router = inject(Router);
  loading = signal(false);

  constructor() {
    this.router.events.pipe(takeUntilDestroyed()).subscribe((event) => {
      if (event instanceof RouteConfigLoadStart) {
        this.loading.set(true);
      } else if (event instanceof RouteConfigLoadEnd) {
        this.loading.set(false);
      }
    });
  }
}
