import {
  ChangeDetectorRef,
  Component,
  inject,
  input,
  OnInit,
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzTooltipModule } from 'ng-zorro-antd/tooltip';
import { NzWaveModule } from 'ng-zorro-antd/core/wave';
import { NzMessageService } from 'ng-zorro-antd/message';
import { MovieTicket } from '@/app/modules/home/components/profile/booking-history/booking-history.component';
import { UserProfile } from '@/app/core/models/user.model';

@Component({
  selector: 'app-info',
  imports: [
    FormsModule,
    NzButtonModule,
    NzFormModule,
    NzIconModule,
    NzInputModule,
    NzGridModule,
    NzTagModule,
    NzTooltipModule,
    NzWaveModule,
    ReactiveFormsModule,
  ],
  templateUrl: './info.component.html',
  styleUrl: './info.component.css',
})
export class InfoComponent implements OnInit {
  user = input.required<UserProfile>();

  private fb = inject(FormBuilder);
  private msg = inject(NzMessageService);
  private cdr = inject(ChangeDetectorRef);
  selectedFile: File | null = null;
  isSaving: boolean = false;
  selectedTicket: MovieTicket | null = null;
  profileForm!: FormGroup;
  avatarPreview: string = '';

  ngOnInit() {
    this.initializeForm();
    this.avatarPreview = this.user().avatar;
  }

  initializeForm() {
    this.profileForm = this.fb.group({
      username: [this.user().username, [Validators.required]],
      email: [{ value: this.user().email, disabled: true }],
      phone: [
        '0912345678',
        [Validators.required, Validators.pattern(/^[0-9]{10}$/)],
      ],
      address: ['Hà Nội, Việt Nam'],
    });
  }

  onFileSelected(event: Event) {
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
    this.avatarPreview = this.user().avatar;
    this.selectedFile = null;
    inputElement.value = '';
    this.msg.info('Đã hủy thay đổi ảnh');
  }

  submitProfile() {
    if (this.profileForm.valid) {
      this.isSaving = true;

      setTimeout(() => {
        this.isSaving = false;

        this.user().username = this.profileForm.get('username')?.value;

        if (this.selectedFile) {
          this.user().avatar = this.avatarPreview;
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

  private getBase64(img: File, callback: (img: string) => void) {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result!.toString()));
    reader.readAsDataURL(img);
  }
}
