import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import {
  CircleUserRound,
  LogOut,
  LucideAngularModule,
  Settings,
} from 'lucide-angular';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { Store } from '@ngrx/store';
import { selectJwt } from '@/app/core/store/state/auth/auth.selectors';
import { AsyncPipe } from '@angular/common';
import { AuthActions } from '@/app/core/store/state/auth/auth.actions';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';

interface MenuItem {
  title: string;
  icon: any;
  link?: string;
  action?: () => void;
  isDanger?: boolean; // Thêm option cho nút Logout màu đỏ
}

@Component({
  selector: 'app-profile-menu',
  imports: [LucideAngularModule, NzDropDownModule, AsyncPipe, NzAvatarModule],
  templateUrl: './profile-menu.component.html',
  styleUrl: './profile-menu.component.css',
})
export class ProfileMenuComponent {
  private store = inject(Store);
  router = inject(Router);
  user$ = this.store.select(selectJwt);
  readonly CircleUserRound = CircleUserRound;
  readonly Settings = Settings;
  readonly LogOut = LogOut;

  public profileMenu: MenuItem[] = [
    {
      title: 'Your Profile',
      icon: this.CircleUserRound,
      link: '/profile',
    },
    {
      title: 'Settings',
      icon: this.Settings,
      link: '/settings',
    },
    {
      title: 'Log out',
      icon: this.LogOut,
      action: () => this.logout(),
      isDanger: true,
    },
  ];

  handleItemClick(item: MenuItem) {
    if (item.action) {
      item.action();
    } else if (item.link) {
      this.router.navigate([item.link]);
    }
  }

  logout() {
    this.store.dispatch(AuthActions.loadLogout());
  }
}
