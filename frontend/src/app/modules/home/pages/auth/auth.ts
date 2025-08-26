import {Component, inject, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '@/app/core/services/auth/auth.service';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzIconDirective} from 'ng-zorro-antd/icon';
import {NzInputDirective, NzInputGroupComponent} from 'ng-zorro-antd/input';
import {NzFormControlComponent, NzFormDirective, NzFormItemComponent, NzFormLabelComponent} from 'ng-zorro-antd/form';
import {NgClass} from '@angular/common';
import {NzDatePickerComponent} from 'ng-zorro-antd/date-picker';
import {NzOptionComponent, NzSelectComponent} from 'ng-zorro-antd/select';

@Component({
  selector: 'app-auth',
  imports: [
    FormsModule,
    NzButtonComponent,
    NzFormControlComponent,
    NzFormDirective,
    NzFormItemComponent,
    NzInputDirective,
    NzInputGroupComponent,
    ReactiveFormsModule,
    NgClass,
    NzIconDirective,
    NzFormLabelComponent,
    NzDatePickerComponent,
    NzSelectComponent,
    NzOptionComponent
  ],
  templateUrl: './auth.html',
  styleUrl: './auth.css'
})
export class AuthComponent implements OnInit {
  activeTab = signal<'login' | 'register'>('login');
  form!: FormGroup;
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);

  ngOnInit() {
    this.initializeForm();
  }

  initializeForm() {
    this.form = this.fb.group({
      username: ['',],
      email: ['',],
      password: ['',]
    })
  }

  onSubmit() {
    console.log(this.form.value);
    if (!this.form.valid) return;
    const username = this.form.get('username')?.value ?? '';

    const password = this.form.get('password')?.value ?? '';
    this.authService.login(
      username,
      password
    ).subscribe((data) => {
      console.log(data)
    })
  }

}
