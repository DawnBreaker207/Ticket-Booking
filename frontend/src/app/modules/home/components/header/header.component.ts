import { Component } from '@angular/core';
import { UpperCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzHeaderComponent } from 'ng-zorro-antd/layout';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ProfileMenuComponent } from '@/app/shared/components/profile-menu/profile-menu.component';

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
  headers = [
    { name: 'Phim', path: '/home' },
    { name: ' Rạp', path: '' },
    { name: 'Tin mới và ưu đãi', path: '' },
  ];
}
