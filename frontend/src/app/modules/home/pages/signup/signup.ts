import {Component, inject, OnInit} from '@angular/core';
import {NzFormControlComponent, NzFormDirective, NzFormItemComponent} from 'ng-zorro-antd/form';
import {NzInputDirective, NzInputGroupComponent} from 'ng-zorro-antd/input';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '@/app/core/services/auth/auth.service';

@Component({
  selector: 'app-signup',
  imports: [
    NzFormDirective,
    NzFormItemComponent,
    NzInputGroupComponent,
    NzInputDirective,
    NzFormControlComponent,
    NzButtonComponent,
    ReactiveFormsModule
  ],
  templateUrl: './signup.html',
  styleUrl: './signup.css'
})
export class SignUpComponent implements OnInit {
  form!: FormGroup;
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);

  ngOnInit() {
    this.initializeForm();
  }

  initializeForm() {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required]
    })
  }

  onSubmit() {
    if (!this.form.valid) return;
    const username = this.form.get('username')?.value ?? '';
    const email = this.form.get('email')?.value ?? '';
    const password = this.form.get('password')?.value ?? '';
    this.authService.register(
      username,
      email,
      password
    ).subscribe((data) => {
      console.log(data)
    })
  }

}
