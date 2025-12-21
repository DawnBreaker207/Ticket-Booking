import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { Store } from '@ngrx/store';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { selectUserId } from '@core/auth/auth.selectors';
import { AuthActions } from '@core/auth/auth.actions';
import { UserStore } from '@domain/user/data-access/user.store';

interface MenuItem {
  title: string;
  icon: any;
  link?: string;
  action?: () => void;
  isDanger?: boolean; // Thêm option cho nút Logout màu đỏ
}

@Component({
  selector: 'app-profile-menu',
  imports: [LucideAngularModule, NzDropDownModule, NzAvatarModule],
  templateUrl: './profile-menu.component.html',
  styleUrl: './profile-menu.component.css',
})
export class ProfileMenuComponent implements OnInit {
  private store = inject(Store);
  router = inject(Router);
  readonly userStore = inject(UserStore);

  ngOnInit() {
    const userId = this.store.selectSignal(selectUserId)();

    if (userId !== undefined && userId !== null) {
      this.userStore.loadUser(userId);
    }
  }

  public profileMenu: MenuItem[] = [
    {
      title: 'Your Profile',
      icon: 'CircleUserRound',
      link: '/profile',
    },
    {
      title: 'Settings',
      icon: 'Settings',
      link: '/settings',
    },
    {
      title: 'Log out',
      icon: 'LogOut',
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
