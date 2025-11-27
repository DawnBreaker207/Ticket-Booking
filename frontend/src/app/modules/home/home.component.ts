import { Component, inject, signal } from '@angular/core';
import { FooterComponent } from '@/app/modules/home/components/footer/footer.component';
import { HeaderComponent } from '@/app/modules/home/components/header/header.component';
import {
  NzContentComponent,
  NzFooterComponent,
  NzLayoutComponent,
} from 'ng-zorro-antd/layout';
import {
  RouteConfigLoadEnd,
  RouteConfigLoadStart,
  Router,
  RouterOutlet,
} from '@angular/router';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NzSpinComponent } from 'ng-zorro-antd/spin';

@Component({
  selector: 'app-home',
  imports: [
    FooterComponent,
    HeaderComponent,
    RouterOutlet,
    NzContentComponent,
    NzFooterComponent,
    NzLayoutComponent,
    NzMenuModule,
    NzSpinComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
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
