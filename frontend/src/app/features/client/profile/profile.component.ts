import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzTabPosition, NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzUploadModule } from 'ng-zorro-antd/upload';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzQRCodeModule } from 'ng-zorro-antd/qr-code';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzTooltipModule } from 'ng-zorro-antd/tooltip';
import { Observable, of } from 'rxjs';
import { BookingHistoryComponent } from '@features/client/profile/booking-history/booking-history.component';
import { InfoComponent } from '@features/client/profile/info/info.component';
import { UserProfile } from '@domain/user/models/user.model';
import { ReservationProfile } from '@domain/reservation/models/reservation.model';

@Component({
  selector: 'app-profile',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NzButtonModule,
    NzInputModule,
    NzFormModule,
    NzTabsModule,
    NzAvatarModule,
    NzIconModule,
    NzDropDownModule,
    NzUploadModule,
    NzTagModule,
    NzBadgeModule,
    NzTabsModule,
    NzUploadModule,
    NzModalModule,
    NzQRCodeModule,
    NzEmptyModule,
    NzTooltipModule,
    BookingHistoryComponent,
    InfoComponent,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  isSaving: boolean = false;
  user$: Observable<UserProfile> = of({
    avatar: 'https://i.pravatar.cc/300?img=12',
    username: 'Nguyễn Văn A',
    email: 'nguyenvana@gmail.com',
    role: 'Member',
  });
  tickets$: Observable<ReservationProfile[]> = of([
    {
      reservationId: 'ORD-6A0A71A0D0EF',
      movieTitle: 'Dune: Hành Tinh Cát 2',
      moviePoster:
        'https://image.tmdb.org/t/p/w200/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg',
      theater: 'CGV Vincom',
      room: 'IMAX 01',
      date: '2025-11-28',
      time: '19:30:00',
      seats: ['F12', 'F13'],
      amount: 320000,
    },
    {
      reservationId: 'ORD-6A0A71A0D0EF',
      movieTitle: 'Kung Fu Panda 4',
      moviePoster:
        'https://image.tmdb.org/t/p/w200/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg',
      theater: 'Lotte Cinema',
      room: 'Std 05',
      date: '2025-10-15',
      time: '14:00:00',
      seats: ['A1'],
      amount: 150000,
    },
    {
      reservationId: 'ORD-6A0A71A0D0EF',
      movieTitle: 'Kung Fu Panda 4',
      moviePoster:
        'https://image.tmdb.org/t/p/w200/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg',
      theater: 'Lotte Cinema',
      room: 'Std 05',
      date: '2025-10-15',
      time: '14:00:00',
      seats: ['A1'],
      amount: 150000,
    },
    {
      reservationId: 'ORD-6A0A71A0D0EF',
      movieTitle: 'Kung Fu Panda 4',
      moviePoster:
        'https://image.tmdb.org/t/p/w200/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg',
      theater: 'Lotte Cinema',
      room: 'Std 05',
      date: '2025-10-15',
      time: '14:00:00',
      seats: ['A1'],
      amount: 150000,
    },
    {
      reservationId: 'ORD-6A0A71A0D0EF',
      movieTitle: 'Kung Fu Panda 4',
      moviePoster:
        'https://image.tmdb.org/t/p/w200/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg',
      theater: 'Lotte Cinema',
      room: 'Std 05',
      date: '2025-10-15',
      time: '14:00:00',
      seats: ['A1'],
      amount: 150000,
    },
  ]);
  tabPosition: NzTabPosition = 'left';

  ngOnInit() {
    this.checkScreenSize();
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.tabPosition = window.innerWidth < 768 ? 'top' : 'left';
  }
}
