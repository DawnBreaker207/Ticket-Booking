import { Component, computed, inject, OnInit } from '@angular/core';
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
  icon: string;
  link?: string;
  action?: () => void;
  role: 'admin' | 'user' | 'both';
  isDanger?: boolean;
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

    if (userId) {
      this.userStore.loadUser(userId);
    }
  }

  public readonly profileMenu: MenuItem[] = [
    {
      title: 'Admin',
      icon: 'Gauge',
      link: '/admin',
      role: 'admin',
    },
    {
      title: 'Your Profile',
      icon: 'CircleUserRound',
      link: '/profile',
      role: 'user',
    },
    {
      title: 'Settings',
      icon: 'Settings',
      link: '/settings',
      role: 'user',
    },
    {
      title: 'Log out',
      icon: 'LogOut',
      action: () => this.logout(),
      isDanger: true,
      role: 'both',
    },
  ];

  filterMenu = computed(() => {
    const user = this.userStore.selectedUser();
    const role = user?.role[0]?.toLowerCase();

    return this.profileMenu.filter((item) => {
      if (item.role === 'both') return true;

      return item.role === role;
    });
  });

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
