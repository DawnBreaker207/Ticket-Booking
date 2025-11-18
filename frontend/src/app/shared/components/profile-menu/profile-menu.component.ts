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

@Component({
  selector: 'app-profile-menu',
  imports: [LucideAngularModule, NzDropDownModule, AsyncPipe],
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

  public profileMenu = [
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
    },
  ];

  logout() {
    this.store.dispatch(AuthActions.loadLogout());
  }
}
