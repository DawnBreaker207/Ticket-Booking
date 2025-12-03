import {ChangeDetectorRef, Component, HostListener, inject, OnInit,} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule,} from '@angular/forms';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzTabPosition, NzTabsModule} from 'ng-zorro-antd/tabs';
import {NzAvatarModule} from 'ng-zorro-antd/avatar';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzDropDownModule} from 'ng-zorro-antd/dropdown';
import {NzUploadModule} from 'ng-zorro-antd/upload';
import {NzTagModule} from 'ng-zorro-antd/tag';
import {NzBadgeModule} from 'ng-zorro-antd/badge';
import {NzModalModule} from 'ng-zorro-antd/modal';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzQRCodeModule} from 'ng-zorro-antd/qr-code';
import {NzEmptyModule} from 'ng-zorro-antd/empty';
import {NzTooltipModule} from 'ng-zorro-antd/tooltip';
import {Store} from '@ngrx/store';
import {selectJwt} from '@/app/core/store/state/auth/auth.selectors';
import {
  BookingHistoryComponent,
  MovieTicket
} from '@/app/modules/home/components/profile/booking-history/booking-history.component';
import {InfoComponent} from '@/app/modules/home/components/profile/info/info.component';


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
  private fb = inject(FormBuilder);
  private msg = inject(NzMessageService);
  private cdr = inject(ChangeDetectorRef);
  private store = inject(Store);
  user$ = this.store.select(selectJwt);
  isSaving: boolean = false;
  user = {
    avatar: 'https://i.pravatar.cc/300?img=12', // Ảnh gốc (Server)
    name: 'Nguyễn Văn A',
    email: 'nguyenvana@gmail.com',
    memberLevel: 'Member',
  };
  tickets: MovieTicket[] = [
    {
      id: 'TK-9921',
      title: 'Dune: Hành Tinh Cát 2',
      poster: 'https://image.tmdb.org/t/p/w200/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg',
      cinema: 'CGV Vincom',
      room: 'IMAX 01',
      date: '28/11/2025',
      time: '19:30',
      seats: ['F12', 'F13'],
      price: 320000,
      status: 'Upcoming',
    },
    {
      id: 'TK-8820',
      title: 'Kung Fu Panda 4',
      poster: 'https://image.tmdb.org/t/p/w200/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg',
      cinema: 'Lotte Cinema',
      room: 'Std 05',
      date: '15/10/2025',
      time: '14:00',
      seats: ['A1'],
      price: 150000,
      status: 'Completed',
    },
  ];
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
