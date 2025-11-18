import { Component, inject } from '@angular/core';
import { UpperCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzHeaderComponent } from 'ng-zorro-antd/layout';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ProfileMenuComponent } from '@/app/shared/components/profile-menu/profile-menu.component';
import { AuthService } from '@/app/core/services/auth/auth.service';

@Component({
  selector: 'app-header',
  imports: [
    UpperCasePipe,
    RouterLink,
    NzButtonComponent,
    NzHeaderComponent,
    NzMenuDirective,
    ProfileMenuComponent,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  authService = inject(AuthService);
  headers = [
    { name: 'Phim', path: '/home' },
    { name: ' Rạp', path: '' },
    { name: 'Tin mới và ưu đãi', path: '' },
  ];
}
