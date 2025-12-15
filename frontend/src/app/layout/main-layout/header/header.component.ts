import { Component, inject } from '@angular/core';
import { UpperCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzHeaderComponent } from 'ng-zorro-antd/layout';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { AuthService } from '@core/auth/auth.service';
import { ProfileMenuComponent } from '@shared/components/profile-menu/profile-menu.component';
import { SelectShowtimeComponent } from '@features/client/home/components/select/select.component';

@Component({
  selector: 'app-header',
  imports: [
    UpperCasePipe,
    RouterLink,
    NzButtonComponent,
    NzHeaderComponent,
    NzMenuDirective,
    ProfileMenuComponent,
    SelectShowtimeComponent,
    NzIconModule,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  authService = inject(AuthService);
  headers = [
    { name: 'Phim', path: '/client' },
    { name: ' Rạp', path: '' },
    { name: 'Tin mới và ưu đãi', path: '' },
  ];
}
