import {
  ChangeDetectorRef,
  Component,
  HostListener,
  inject,
  OnInit,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
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
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzQRCodeModule } from 'ng-zorro-antd/qr-code';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzTooltipModule } from 'ng-zorro-antd/tooltip';

interface MovieTicket {
  id: string;
  title: string;
  poster: string;
  cinema: string;
  room: string;
  date: string;
  time: string;
  seats: string[];
  price: number;
  status: 'Upcoming' | 'Completed' | 'Cancelled';
}

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
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private msg = inject(NzMessageService);
  private cdr = inject(ChangeDetectorRef);

  profileForm!: FormGroup;
  isSaving: boolean = false;

  tabPosition: NzTabPosition = 'left';

  user = {
    avatar: 'https://i.pravatar.cc/300?img=12', // Ảnh gốc (Server)
    name: 'Nguyễn Văn A',
    email: 'nguyenvana@gmail.com',
    memberLevel: 'Member',
  };

  avatarPreview: string = '';
  selectedFile: File | null = null;

  isDetailVisible: boolean = false;
  selectedTicket: MovieTicket | null = null;

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

  ngOnInit() {
    this.avatarPreview = this.user.avatar;
    this.checkScreenSize();

    this.profileForm = this.fb.group({
      fullname: [this.user.name, [Validators.required]],
      email: [{ value: this.user.email, disabled: true }],
      phone: [
        '0912345678',
        [Validators.required, Validators.pattern(/^[0-9]{10}$/)],
      ],
      address: ['Hà Nội, Việt Nam'],
    });
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.tabPosition = window.innerWidth < 768 ? 'top' : 'left';
  }

  onFileSelected(event: any) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      const isImg = file.type === 'image/png' || file.type === 'image/jpeg';
      if (!isImg) {
        this.msg.error('Chỉ hỗ trợ file JPG/PNG');
        return;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.msg.error('Dung lượng ảnh phải dưới 2MB!');
        return;
      }

      this.getBase64(file, (img: string) => {
        this.avatarPreview = img;
        this.selectedFile = file;

        this.msg.info('Đã chọn ảnh. Bấm "Lưu thay đổi" để hoàn tất.');
        this.cdr.markForCheck();
      });
    }
  }

  resetAvatar(inputElement: HTMLInputElement) {
    this.avatarPreview = this.user.avatar;
    this.selectedFile = null;
    inputElement.value = '';
    this.msg.info('Đã hủy thay đổi ảnh');
  }

  submitProfile() {
    if (this.profileForm.valid) {
      this.isSaving = true;

      setTimeout(() => {
        this.isSaving = false;

        this.user.name = this.profileForm.get('fullname')?.value;

        if (this.selectedFile) {
          this.user.avatar = this.avatarPreview;
          this.selectedTicket = null;
        }

        this.msg.success('Lưu thành công');
        this.cdr.markForCheck();
      }, 1000);
    } else {
      Object.values(this.profileForm.controls).forEach((control) => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({ onlySelf: true });
        }
      });
    }
  }

  openTicketDetail(ticket: MovieTicket) {
    this.selectedTicket = ticket;
    this.isDetailVisible = true;
  }

  closeTicketDetail() {
    this.isDetailVisible = false;
  }

  private getBase64(img: File, callback: (img: string) => void) {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result!.toString()));
    reader.readAsDataURL(img);
  }
}
