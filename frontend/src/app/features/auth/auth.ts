import { Component, inject, OnInit, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzInputDirective, NzInputGroupComponent } from 'ng-zorro-antd/input';
import {
  NzFormControlComponent,
  NzFormDirective,
  NzFormItemComponent,
  NzFormLabelComponent,
} from 'ng-zorro-antd/form';
import { Store } from '@ngrx/store';
import { Router, RouterLink } from '@angular/router';
import { Actions, ofType } from '@ngrx/effects';
import { take } from 'rxjs';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { NzStringTemplateOutletDirective } from 'ng-zorro-antd/core/outlet';
import { AuthActions } from '@core/auth/auth.actions';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-auth',
  imports: [
    FormsModule,
    NzButtonComponent,
    NzFormControlComponent,
    NzFormItemComponent,
    NzInputDirective,
    NzInputGroupComponent,
    ReactiveFormsModule,
    NzIconDirective,
    NzFormLabelComponent,
    RouterLink,
    NzFormDirective,
    NzCheckboxComponent,
    NzStringTemplateOutletDirective,
    TranslatePipe,
  ],
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})
export class AuthComponent implements OnInit {
  activeTab = signal<'login' | 'register'>('login');
  private fb = inject(FormBuilder);
  private store = inject(Store);
  private router = inject(Router);
  private actions$ = inject(Actions);

  formLogin!: FormGroup;
  formRegister!: FormGroup;
  isRegisterLoading = false;
  isLoginLoading = false;
  passwordVisible = false;

  ngOnInit() {
    this.initializeRegisterForm();
    this.initializeLoginForm();
    if (this.router.url.includes('register')) {
      this.activeTab.set('register');
    } else {
      this.activeTab.set('login');
    }

    this.formRegister.get('password')?.valueChanges.subscribe(() => {
      this.formRegister.get('confirmPassword')?.updateValueAndValidity();
    });
  }

  toggleMode() {
    this.activeTab.update((tab) => (tab === 'login' ? 'register' : 'login'));
    if (this.activeTab() === 'login') {
      this.formLogin.reset({ remember: true });
    } else {
      this.formRegister.reset();
    }
  }

  initializeLoginForm() {
    this.formLogin = this.fb.group({
      identifier: [''],
      password: [''],
      remember: [false],
    });
  }

  initializeRegisterForm() {
    this.formRegister = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
    });
  }

  submitLogin() {
    if (this.formLogin.valid) {
      this.isLoginLoading = true;
      this.activeTab.set('login');
      const { identifier, password } = this.formLogin.value;
      this.store.dispatch(
        AuthActions.loadLogin({ user: { identifier, password } }),
      );
      this.isLoginLoading = false;
    } else {
      this.updateValidity(this.formLogin);
    }
  }

  submitRegister() {
    if (this.formRegister.valid) {
      this.activeTab.set('register');
      this.isRegisterLoading = true;
      const { username, email, password } = this.formRegister.value;
      this.store.dispatch(
        AuthActions.loadRegister({
          user: { username, email, password },
        }),
      );

      this.actions$
        .pipe(ofType(AuthActions.loadRegisterSuccess), take(1))
        .subscribe(() => {
          this.isRegisterLoading = false;
          this.activeTab.set('login');
          this.formLogin.patchValue({ email: this.formRegister.value.email });
        });
    } else {
      this.updateValidity(this.formRegister);
    }
  }

  //   Validator
  confirmValidator(control: AbstractControl) {
    if (!control.value) return { required: true };
    if (control.valid !== this.formRegister.get('password')?.value) {
      return { confirm: true, error: true };
    }
    return null;
  }

  updateValidity(form: FormGroup) {
    Object.values(form.controls).forEach((control) => {
      if (control.invalid) {
        control.markAsDirty();
        control.updateValueAndValidity({ onlySelf: true });
      }
    });
  }

  protected readonly toolbar = toolbar;
}
