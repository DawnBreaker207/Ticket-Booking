import {Component} from '@angular/core';
import {RouterLink} from '@angular/router';
import {CircleUserRound, LogOut, LucideAngularModule, Settings} from 'lucide-angular';
import {NzDropDownModule} from 'ng-zorro-antd/dropdown';

@Component({
  selector: 'app-profile-menu',
  imports: [RouterLink, LucideAngularModule, NzDropDownModule],
  templateUrl: './profile-menu.component.html',
  styleUrl: './profile-menu.component.css',
})
export class ProfileMenuComponent {
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
      link: '/auth',
    },
  ];
}
